import requests
import json
import time

# call the API and get the response
url = "http://10.90.74.141:8080"

# create json body to pass to the API # creates a new user that is unique every time and probably won't be used again
jsonBody = {
    "username": "testUserNameNumber" + str(time.time()),
    "password": "testPassWordNumber" + str(time.time())
}
# dict to json
jsonBody = json.dumps(jsonBody)

# send the request # set header to application/json
response = requests.post(url + "/registration", data=jsonBody,
                         headers={"Content-Type": "application/json"})


# get token int from response
token = response.json()["token"]

# print the response
print("token = " + str(token))

if token <= 0:
    print("token is invalid")
    exit()

# exit()
# get food data from the API
fresponse = requests.get(url + "/foods")

# parse out json array foods:
foods = fresponse.json()["Foods"]


foodList = []

for food in foods:
    foodList.append((food["name"], food["id"]))

# print(foodList)


for p1, p2 in foodList:
    jsonBodyGood = {
        "token": token,
        "food": p2,
        "score": 5,
        "body": p1 + " is a good food"
    }
    jsonBodyGood = json.dumps(jsonBodyGood)

    jsonBodyBad = {
        "token": token,
        "food": p2,
        "score": 1,
        "body": p1 + " is a bad food"
    }
    jsonBodyBad = json.dumps(jsonBodyBad)

    jsonBodyMid = {
        "token": token,
        "food": p2,
        "score": 3,
        "body": p1 + " is a mediocre food"
    }
    jsonBodyMid = json.dumps(jsonBodyMid)

    res1 = requests.post(url + "/review", data=jsonBodyGood,
                         headers={"Content-Type": "application/json"})
    res2 = requests.post(url + "/review", data=jsonBodyBad,
                         headers={"Content-Type": "application/json"})
    res3 = requests.post(url + "/review", data=jsonBodyMid,
                         headers={"Content-Type": "application/json"})

    # append the response to a file
    with open("testing.txt", "a") as f:
        f.write("foodid " + str(p2) + " \n" + res1.text + "\n" +
                res2.text + "\n" + res3.text + "\n")
