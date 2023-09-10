package Backend;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Backend.database.Profile.*;

@Service
public class loginManager 
{
    @Autowired
    private profileService userList;
    private static HashMap<Integer,String> tokens = new HashMap<Integer,String>();//tracks currently logged in users

    public int login(String username,String password)
    {
        if(!tokens.containsValue(username))
        {
                if(userList.usernameMatch(username))
                {
                    if(userList.passwordCheck(username, password))
                    {
                        return generateToken(username);
                    }else
                    {
                        return -1;//wrong password
                    }
                }
                return -2;//username doesn't match any in database
        }
        return -3;//already logged in
    }

    public int register(String username,String password,boolean isAdmin)
    {
		if(userList.usernameMatch(username))
		{
			return -1;//invalid username
		}
		userList.add(username, password,isAdmin);
		return generateToken(username);
    }

    public String checkToken(int token)
    {
        return tokens.get(Integer.valueOf(token));//returns username else null REMEMBER TO HANDLE NULL
    }
    
    public void clearToken(int token)
    {    
        tokens.remove(token);
    }

    public int deleteUser(String username,String password,int token)
    {
        String temp = checkToken(token);
        if(temp==null)
        {
            return -1;//invalid token
        }
        else if(!temp.equals(username))
        {
            return -2;//username token mismatch
        }
        else if(!userList.passwordCheck(username, password))
        {
            return -3;//username password mismatch
        }
        else
        {
            userList.deleteUser(username);
            clearToken(token);
            return 0;
        }
    }

    public int checkAdmin(int token)//checks if user is admin and if the token exists in one method
    {
        String temp = checkToken(token);
        if(temp==null)
        {
            return -2;//invalid token
        }
        return userList.getProfile(temp).getIsAdmin()?1:-3;//TURING OPERATOR WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    }

    private int generateToken(String username)//generates token and adds it to log in array
    {
        int tempToken=(int)(Math.random()*Integer.MAX_VALUE);//generates a random token to be used for persistant login
        while(tokens.containsKey(tempToken))
        {
            tempToken=(int)(Math.random()*Integer.MAX_VALUE);
        }
        tokens.put(Integer.valueOf(tempToken),username);
        return tempToken;
    }

}
