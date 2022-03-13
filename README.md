
# An ecommerce Bookstore Application.

This repository deals with the backend side of things in terms of user-side functionalities.
Admin-side functionalities will be addressed in a separate project/repository.

# Short Description 

I have made this project using Java along with the Spring Framework and with the help of a few other dependencies, like JPA, Hibernate, Security, Spring Mail, Lombok.
The project is also making use of REST architectural style. 
As a relational database management system I am using MySQL. 

# Functionalities and structure 

The project is structured in packages - each package having an unique purpose, like: Entities package, Services package etc
The project follows the MVC pattern.

The user can: register, login, retrieve his lost password, update his personal details and update his shipping addresses.

# Register 
- the user provides an username, an email and a password (which is encrypted using BCryptPasswordEncoder). Following the registration, the user is prompted to check his email address (I am using the Mailtrap.io service as my fake email provider) where he will receive an activation link which is unique per request and that has an expiration time of 15 minutes. Upon clicking the activation link, the user account is enabled and he can proceed to login into his account having full rights.

# Login
- after activation, the user can use his email and password to login into the application. I am using JWT along with refresh tokens that will help reissue the JWT after expiration as means of communication between client and server, which was one of the most difficult things to implement in this project.

# Forgot Password 
- in case the user forgot his password, all he has to do is click the forgot password button and provide the email associated with his account. If the email is valid, an email will be sent to that address (the link is unique and has an expiration time of 30 minutes). All the user has to do is click on that link which will redirect him to a page where he can type a new password for his account.

# Personal Details and Shipping Address
- These two features are part of the personal page of the user. The user can update or delete some informations about himself and he can also set shipping addresses with the possibility of having only one unique shipping address as the default shipping address. 

# How to use (if you plan to install it locally)

To clone and run this application by yourself, make sure you have at least Java 11 and all JDK stuff (including JRE), Maven to build the dependencies,
IntelliJ IDEA (which I recommend), and Postman (it's not necessary, though it's really useful to handle the rest API. After that, do the following instructions: 

- Switch the branch to ```branch-name```

- Clone this repository
```bash
$ git clone https://github.com/MavrodinMC/BookstoreProjectBackend
```
- Open this project using IntelliJ (or any other IDE)

- Run ```BookstoreProject.java```
  > This is gonna be building the maven dependencies too
- The endpoints are located in 'http://localhost:8080' and config its port on ```src/main/resources/application.properties```
  > Use a software like Postman to do the requests. 
  
- Make sure to create a database called **bookstore** 
  > or create it with another name. However, you must to rename its name in ```src/main/resources/application.properties```
 **By the way, you can change the port (8080) to another one, just change the line in ```src/main/resources/application.properties```**

  **Now, you are able to run this Java application locally.** 




