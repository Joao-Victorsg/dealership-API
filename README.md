# dealership-api
Doing some study on Rest APIs with Java and Spring Boot

# About the API
An API for managing a dealership company. You will be able to CRUD a Car, a Client and a sale that is composed by a Client and a Car.

It's built with Java and Spring Framework. It has the only porpose to study the tecnologies that are used to build an API with Java.

# Features
This API provides HTTP endpoint's for the following:
### Cars
* Create a car: POST/car
* Update a car: PUT/car/{VIN}
* Delete a car (by VIN): DELETE/car/{VIN}
* Get all cars: GET/car
* Get a specific car (by VIN): Get/car/{VIN}

### Clients

* Create a client: POST/client
* Update a client: PUT/client/{CPF}
* Delete a client (by CPF): DELETE/client/{CPF}
* Get all clients: GET/client
* Get a specific client (by CPF): Get/client/{CPF}

# This project was developed with: 

* Java 11
* Spring Framework
* Maven
* H2 - File 
* springdoc-openapi-ui
* Mapstruct 1.5.2
* Lombok
* Datafaker
* Gson
 
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
