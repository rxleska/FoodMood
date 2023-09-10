[[_TOC_]]

## LOGIN REQUESTS

### LOGIN

[PUT] /login

> no parameters
>
> >
>
> expects
>
> > ```
> > {
> >     "username":"someUser",
> >     "password":"somePassword"
> > }
> > ```
>
> returns
>
> > ```
> > {
> > "loggedIn": true,
> > "status": "current status",
> > "token": -1
> > }
> > ```
>
> Functional Description
>
> > accepts login info and checks it against current db of users
> > if login is succsesful returns a temporay login token for authenticating users for other api calls
> > if token is negative then user was unable to login
> > if user doesn't exists returns -1
> > username doesn't match any in db -2
> > already logged in -3
> > invalid body -4
 
### LOGOUT

[PUT] /logout

> no parameters
>
> >
>
> expects
>
> > ```
> > {
> >     "token":<Usertoken>
> > }
> > ```
>
> returns
> 
> > ```
> > {
> >     "loggedIn": Boolean if logged in,
> >     "status": "current status"
> > }
> > ```
>
> Functional Description
>
> > matches token against current list of all logged in users and removes the current login token from use if matched



### CHECK TOKEN STATUS

[GET] /status

> no parameters
>
> expects
>
> > ```
> > {
> >     "token":<UserToken>    
> > }
> > ```
>
> returns
> 
> > ```
> > {
> >     "loggedIn": Boolean if logged in,
> >     "status": "current status"
> > }
> > ```
>
> Functional Description
>
> > matches token against current list of all logged in users and returns true if it exists

### Get Current USER DATA FROM TOKEN

[GET] /status/info

> parameters:
> 
> > ?token=Usertoken
>
> expects Nothing
>
>
> >
>
> Returns 
>
> > ```
> > {
> >     "userId":1,
> >     "userName":"exampleUsername",
> >     "retaurants":[
> >         1,
> >         5,
> >         ...
> >     ],
> >     "foods":[
> >         23,
> >         45,
> >         ...
> >     ],
> >     "lastLogInDate":{
> >         "month":12,
> >         "day":23,
> >         "year":1999
> >     },
> >     "banStatus":false
> > }
> > ```
>
> Functional Description
> > Gets all public user information for a given user token


### REGISTER ACCOUNT

[POST] /registration

> no parameters
>
> >
>
> expects
>
> > ```
> > {
> >     "username":"someUser",
> >     "password":"somePassword"
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     "loggedIn": true,
> >     "status": "current status",
> >     "token": -1
> > }
> > ```
>
> Functional Description
>
> > accepts registration info and checks it against current db of users
> > if user is already registered returns token -1
> > Invalid json request body -2
> 

## GET ALL DATA

### GET ALL DATA

[GET] /data

> no parameters
>
> >
>
> expects
>
> must provide json body in request, doesn't need anything in it.
>
> returns
>
> > ```
> > {
> >     Restaraunts:[
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         ...
> >     ],
> >     Foods:[
> >         {
> >             id:0,  //these will be almost certainly sequential -Parker 
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "", //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         {
> >             id:1,
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "",  //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         ...
> >     ]
> >}
> >```
>
> Functional Description
> > Gets all information avalible to all users 

### GET ALL NEW DATA

[GET] /data

> no parameters
>
> >
>
> >```
> > {
> >     Restaraunts:[
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         ...
> >     ],
> >     Foods:[
> >         {
> >             id:0,  //these will be almost certainly sequential -Parker 
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "", //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         {
> >             id:1,
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "",  //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         ...
> >     ]
> >}
> >```
>
> Functional Description
> > Gets all information avalible to all users 

### GET RECOMMENDED ITEMS

[GET] /recon

> no parameters
>
> >
>
> expects
>
> no json body in request
>
> returns
>
> > ```
> > {
> >     foods: [
> >         1,
> >         2,
> >         5,
> >         6,
> >         7,
> >         ...
> >     ]
> > }
> >```
>
> Functional Description
> > Gets an array of food ids that are recommended right now

### GET CURRENT FOODS

 [GET] /cfoods

> no parameters
>
> >
>
> expects
>
> > ```
> > {
> >     restaurantId:-1,
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     Foods:[
> >         1,
> >         2,
> >         3,
> >         ...
> >     ]
> >}
> >```
>
> Functional Description
> > Gets a array of food ids representing the currently availiable foods at a given restaurant

### GET ALL RESTAURANTS

[GET] /restaurants

> no parameters
>
> >
>
> expects
>
> no json body in request
>
> returns
>
> > ```
> > {
> >     Restaraunts:[
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         {
> >             id: 0,
> >             lat: 42.02730363101929, 
> >             lng: -93.65128447236182,
> >             name: "",
> >             description: "", //will probably be null for demo 3 because there is nothing provided in the ISU api -Parker
> >             type: "",
> >             address: "",
> >             slug: "",  //you can ignore this, it is a consequence of me taking short cuts -Parker
> >             sub: [
> >                 "Sub Restaraunt 1",
> >                 "Sub Restaraunt 2",
> >                 ...//other sub restaraunts
> >             ],         //working on this part not currently deployed -Parker
> >             food: [
> >                 0,
> >                 1,
> >                 42,
> >                 13,
> >                 ...//other food ids
> >             ],
> >             lastUpdated: 2023-04-01
> >         },
> >         ...
> >     ]
> >```
>
> Functional Description
> > Gets all Restaurant information avalible to all users

### GET ALL FOODS

[GET] /foods

> no parameters
>
> >
>
> expects
>
> no json body in request
>
> returns
>
> > ```
> > {
> >         Foods:[
> >         {
> >             id:0,  //these will be almost certainly sequential -Parker 
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "", //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         {
> >             id:1,
> >             dateAdded: 2023-04-1,
> >             avgStars: 4.425,
> >             numReviews: 86,
> >             name: "food title",
> >             calories: 135,
> >             category: "",  //will be something like entree or drink, however the dining api is incosistent so expect weirdness or don't use -Parker
> >             type:[
> >                 "HALAL","VEGETARIAN","VEGAN" //these are the only things that it can be will probably be more in future -Parker
> >             ],
> >             isServed: true,
> >             reviews: [],  //will be list of review id's once again a side effect of laziness -Parker
> >             restaurant:319,  //matching restaurant id
> >             subrestaurant:"subrestaurant"
> >         },
> >         ...
> >     ]
> >}
> >```
>
> Functional Description
> > Gets all Food information avalible to all users

## REVIEWS

### MAKE REVIEW

[POST] /review

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     score:4.5,
> >     body:"examplebody",
> >     food:0,
> >     token:-1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"review added"
> > }
> > ```
>
> Functional Description
> > Adds a Review to the database

### EDIT REVIEW

[PUT] /review

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     reviewID:1,
> >     score:4.5,
> >     body:"examplebody",
> >     food:0,
> >     token:-1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"review modified"
> > }
> > ```
>
> Functional Description
> > Modifies a Review in the database

### DELETE REVIEW

[DELETE] /review

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     reviewID:1,
> >     token:-1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"review deleted"
> > }
> > ```
>
> Functional Description
> > Deletes a Review in the database

### GET REVIEWS FROM FOOD ID

[GET] /review/food

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     food:1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     reviews:[
> >         {
> >             reviewId:1,
> >             userId:4,
> >             userName:"name4",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:2,
> >             userId:5,
> >             userName:"name5",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:3,
> >             userId:6,
> >             userName:"name6",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:4,    
> >             userId:7,
> >             userName:"name7",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a list of the reviews for a given food


### GET REVIEWS FROM FOOD ID partitioned

[GET] /review/food

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     food:1,
> >     index:0,
> >     size:20
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     reviews:[
> >         {
> >             reviewId:1,
> >             userId:4,
> >             userName:"name4",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:2,
> >             userId:5,
> >             userName:"name5",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:3,
> >             userId:6,
> >             userName:"name6",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         {
> >             reviewId:4,
> >             userId:7,
> >             userName:"name7",
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             }
> >         },
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a subsegment of the reviews for a given food

### GET REVIEWS FROM USER ID

[GET] /review/user

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     userId:1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     reviews:[
> >         {
> >             reviewId:1,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:1
> >         },
> >         {
> >             reviewId:2,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:2
> >         },
> >         {
> >             reviewId:3,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:3
> >         },
> >         {
> >             reviewId:4,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:4
> >         },
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a list of the reviews for a given user ID

### GET REVIEWS FROM USER ID Partitioned

[GET] /review/user

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     userId:1,
> >     index:0,
> >     size:20
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     reviews:[
> >         {
> >             reviewId:1,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:1
> >         },
> >         {
> >             reviewId:2,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:2
> >         },
> >         {
> >             reviewId:3,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:3
> >         },
> >         {
> >             reviewId:4,
> >             rating:4.5,
> >             body:"examplebody",
> >             date:{
> >                 month:11,
> >                 day:11,
> >                 year:1111
> >             },
> >             food:4
> >         },
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a subsegment of the reviews for a given user ID

## GET OPEN INFORMATION

### GET CURRENTLY OPEN

[GET] /restaurants/open

> no parameters
>
> >
>
> expects
>
> no json body in request
>
> returns
>
> > ```
> > {
> >     restaraunts:[
> >         1,
> >         4,
> >         7,
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a list of restaraunt ids of restaraunts that are currently open
    

### GET TODAYS HOURS  

[GET] /restaurants/today

> no parameters
>
> >
>
> expects
>
> no json body in request
>
> returns
>
> > ```
> > {
> >     restarauntHours:[
> >         {
> >             id:1,
> >             hours:[
> >                 {
> >                     section:breakfast,
> >                     from:"6:30",
> >                     to:"9:30"
> >                 },
> >                 {
> >                     section:lunch,
> >                     from:"10:30",
> >                     to:"14:30"
> >                 },
> >                 {
> >                     section:dinner,
> >                     from:"16:30",
> >                     to:"21:30"
> >                 },
> >                 ...
> >             ]
> >         },
> >         {
> >             id:2,
> >             hours:[
> >                 {
> >                     section:breakfast,
> >                     from:"6:30",
> >                     to:"9:30"
> >                 },
> >                 {
> >                     section:lunch,
> >                     from:"10:30",
> >                     to:"14:30"
> >                 },
> >                 {
> >                     section:dinner,
> >                     from:"16:30",
> >                     to:"21:30"
> >                 },
> >                 ...
> >             ]
> >         },
> >         ...
> >     ]
> > }
> > ```
>
> Functional Description
> > Gets a list of restaraunt ids of restaraunts that are currently open

## ADMIN COMMAND

###  REVIEW DELETE

[DELETE] /del/review

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     token:0,
> >     reviewId:1
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"deleted"
> > }
> > ```
>
> Functional Description
> > Deletes a review from any person

### USER BLACKLIST

[PUT] /bl/user

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     token:0,
> >     userId:1,
> >     isBlackListed:true
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"blacklisted"
> > }
> > ```
>
> Functional Description
> > Blacklist or UnBlacklists a user

### USER DELETE

[DELETE] /del/user

> no parameter
>
> >
>
> expects
>
> > ```
> > {
> >     token:0,
> >     userId:1,
> > }
> > ```
>
> returns
>
> > ```
> > {
> >     success:true,
> >     status:"deleted"
> > }
> > ```
>
> Functional Description
> > Deletes a user account
