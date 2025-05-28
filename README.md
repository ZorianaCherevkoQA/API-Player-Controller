Description:
1. This application is about creating users and operations with them;
2. In the system already created 2 uses with roles: ‘supervisor’ (this user cannot be
   deleted, only he can edit the user) and ‘admin’;

   Basic application requirements:
1. User should be older than 16 and younger than 60 years old;
2. Only those with role ‘supervisor’ or ‘admin’ can create users;
3. User can be created only with one role from the list: ‘admin’ or ‘user’
4. ‘login’ field is unique for each user;
5. ‘screenName’ field is unique for each user;
6. ‘password’ must contain latin letters and numbers (min 7 max 15 characters);
7. Users ‘gender’ can only be: ‘male’ or ‘female’;


🐞 BUGS DISCOVERED

🟡 Create Bugs
1. Duplicate data in queries results in 200
Repeated POST requests with the same player data return 200 OK. (There are no duplicates actually created)

2. Gender field accepts invalid values
Gender can be set to unexpected values like "robot" without validation error.

3. Player creation works even without supplying a password 
(the field is not required in swagger but mentioned as required in requirements)

4. Login and screen name uniqueness not enforced
Players can be created using duplicate logins or screen names.

🔴 Delete Bugs
1. A player is able to delete their own account

🔵 Get Bugs
1. Retrieving a player after deletion returns 200 OK instead of 404 Not Found.

2. Retrieving a player with Invalid ID returns 200 instead of 400 or 404.

🟠 Update Bugs
1. Age validation not enforced
Players can be updated with an age below 17 or above 60.

2. Screen name duplication allowed
Players can be updated to have identical screen names as others without validation errors.

3. Gender field accepts invalid values
Gender can be set to unexpected values like "robot" without validation error.

4. It is possible to send updates to users after deletion and receive a 200 OK.

5. Role field accepts invalid values but not applied
Updating fields like "role" to "supervisor" returns 200 OK but does not actually change the role.

6. Password update accepts invalid values
Passwords made up of digits only (outside or within the accepted range) return 200 OK.

