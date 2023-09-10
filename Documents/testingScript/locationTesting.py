import matplotlib.pyplot as plt

# This program plots all of the current restaurants on a graph using their longitude and latitude so that we can see were the splits of north, east, west and south campus are


locations = "E/HomeFragment: getData: -93.6456354 42.0235144\nE/HomeFragment: getData: -93.6515007 42.0249565\nE/HomeFragment: getData: -93.6484556 42.0272385\nE/HomeFragment: getData: -93.6403311 42.0251392\nE/HomeFragment: getData: -93.6515007 42.0249565\nE/HomeFragment: getData: -93.6456354 42.0235144\nE/HomeFragment: getData: -93.6382529 42.0238084\nE/HomeFragment: getData: -93.65045 42.0239234\nE/HomeFragment: getData: -93.6515007 42.0249565\nE/HomeFragment: getData: -93.6456354 42.0235144\nE/HomeFragment: getData: -93.6429145 42.0339213\nE/HomeFragment: getData: -93.6484556 42.0272385\nE/HomeFragment: getData: -93.6487947 42.0281648\nE/HomeFragment: getData: -93.6444688 42.0254011\nE/HomeFragment: getData: -93.6456391 42.0297367\nE/HomeFragment: getData: -93.6531506 42.0285641\nE/HomeFragment: getData: -93.6382529 42.0238084\nE/HomeFragment: getData: -93.6323694 42.0067073\nE/HomeFragment: getData: -93.6515007 42.0249565\nE/HomeFragment: getData: -93.6382529 42.0238084"

locations = locations.split("\n")

# create a map
plt.plot()


for location in locations:
    # remove the first 22 characters
    location = location[25:]
    print(location)
    # split the string into a list
    location = location.split(" ")
    print(location)
    # convert the strings to floats
    lat = float(location[1])
    lon = float(location[0])
    # plot the points
    plt.scatter(lon, lat, c='red', alpha=0.5)


plt.show()
