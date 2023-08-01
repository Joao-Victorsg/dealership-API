# Dealership API
Doing some study on Rest APIs with Java and Spring Boot

# About the API
An API for managing a dealership company. You will be able to CRUD a Car, a Client and a Sale. The sale is composed by a Client and a Car.

To be able to CRUD, you have to be a valid user and get a Token in the auth endpoint. After that you have to use the bearer token 
in the requests.

The user can be created at the users endpoint in any environment different from production. 

It's built with Java and Spring Framework. It has the only purpose to study the technologies that are used to build an API with Java.

# Features

This API provides HTTP endpoint's for the following:

### Cars
* Create a car: POST/cars
* Update a car: PUT/cars/{VIN}
* Delete a car (by VIN): DELETE/cars/{VIN}
* Get all cars: GET/cars
* Get a specific car (by VIN): Get/cars/{VIN}

### Clients

* Create a client: POST/clients
* Update a client: PUT/clients/{CPF}
* Delete a client (by CPF): DELETE/clients/{CPF}
* Get all clients: GET/clients
* Get a specific client (by CPF): Get/clients/{CPF}

### Sales

* Create a sale: POST/sales
* Delete a sale (by ID): DELETE/sales/{id}
* Get all sales: GET/sales
* Get a specific sale (by ID): Get/sales/{id}

### Users

* Create a user: POST/users

### Auths

* Authenticate a user: POST/auths

# This project was developed with: 

* Java 11
* Spring Framework
* Maven
* Postgres
* springdoc-openapi-ui
* Mapstruct
* Lombok
* Datafaker
* Rest Template
* Gson
* Test Containers
* Wiremock
* JUnit 5
* Mockito
* Rest Assured
* Pitest
 
# Compile and build

mvn clean install

# Run

You can run the application localy using:
mvn spring-boot:run

By default, the API will be avalaible at http://localhost:8080/v1/dealership/

# Documentation

You can find the documentation made by Spring-doc-openapi at http://localhost:8080/swagger-ui/index.html#/dealership-controller

# Collection

There is a postman collection to do the requests at the collection folder. When the application initialize, it mock 100 rows of data in the h2-database. You can see it by doing a GET.
