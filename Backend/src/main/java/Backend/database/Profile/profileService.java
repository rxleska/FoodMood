package Backend.database.Profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Backend.database.Review.Review;
import Backend.database.Review.ReviewRepository;

@Service
public class profileService
{
    @Autowired
	private ProfileRepository profileRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List getAll()
    {
        return profileRepository.findAll();
    }

    public int length()
    {
        return (int)profileRepository.count();
    }

    public Profile getProfile(String name){return profileRepository.findByUsername(name);}

    public Profile getProfileId(int id){return profileRepository.findById(id);}

    public boolean usernameMatch(String username)
    {
        return profileRepository.existsByUsername(username);
    }

    public boolean passwordCheck(String username,String password)
    {
        Profile temp = profileRepository.findByUsername(username);
        return password.equals(temp.getPassword());
    }

    public void add(String username,String password,boolean isAdmin)
    {
        profileRepository.save(new Profile(username,password,isAdmin));
    }

    public void deleteUser(String username)
    {
        profileRepository.deleteByUsername(username);
    }

    public int banUser(long id)//bans a user by id
    {
        if(!profileRepository.existsById(id))//checks if they exist
        {
            return -1;
        }
        Profile temp = profileRepository.findById(id);
        if(temp.getIsAdmin())//makes usre that admins do not get banned
        {
            return -2;
        }
        temp.setBanStatus(true);//bans them lmao
        deleteReviews(temp);
        profileRepository.save(temp);//saves the bans status
        return 1;
    }

    public void deleteReviews(Profile profile)//deletes all reviews from a profile, potentially useful for banning
    {
        for(Review review: profile.getReviews())
        {
            reviewRepository.delete(review);
        }
    }
}
