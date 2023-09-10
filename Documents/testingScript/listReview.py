
import requests
import json
import time

# call the API and get the response
url = "http://10.90.74.141:8080"
url += "/review/food"


reivews = requests.get(url + "?food=40&index=0&size=0")

print(reivews.text)
