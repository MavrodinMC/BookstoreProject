# BookstoreProjectBackend
A project for managing a book store. 

This repository deals with the backend side of things in terms of user-side functionalities.
Admin-side functionalities will be addressed in a separate project/repository.

---- Short Description ----

I have made this project using Java along with the Spring Framework and with the help of a few other dependencies, like JPA, Hibernate, Security, Spring Mail, Lombok.
The project is also making use of REST architectural style. 
As a relational database management system I am using MySQL. 
If you want to clone this repository, I would recommend having IntelliJ IDEA and at least Java version 11 installed on your machine.

---- Functionalities and structure ----

The project is structured in packages - each package having an unique purpose, like: Entities package, Services package etc
The project follows the MVC pattern.

The user can: register, login, retrieve his lost password, update his personal details and update his shipping addresses.

Register - the user provides an username, an email and a password (which is encrypted using BCryptPasswordEncoder). Following the registration, the user is prompted to check his email address (I am using the Mailtrap.io service as my fake email provider) where he will receive an activation link which is unique per request and that has an expiration time of 15 minutes. Upon clicking the activation link, the user account is enabled and he can proceed to login into his account having full rights.

Login - after activation, the user can use his email and password to login into the application. I am using JWT along with refresh tokens that will help reissue the JWT after expiration as means of communication between client and server, which was one of the most difficult things to implement in this project.

Forgot Password - in case the user forgot his password, all he has to do is click the forgot password button and provide the email associated with his account. If the email is valid, an email will be sent to that address (the link is unique and has an expiration time of 30 minutes). All the user has to do is click on that link which will redirect him to a page where he can type a new password for his account.

Personal Details and Shipping Address - These two features are part of the personal page of the user. The user can update or delete some informations about himself and he can also set shipping addresses with the possibility of having only one unique shipping address as the default shipping address. 







