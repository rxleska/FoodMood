import requests
import json

token = 296883283


# call the API and get the response
url = "http://10.90.74.141:8080"

# create json body to pass to the API
jsonBody = {
    "token": token
}

# dict to json
jsonBody = json.dumps(jsonBody)

# logout account
response = requests.put(url + "/logout", data=jsonBody)


# print the response
print(response.text)
