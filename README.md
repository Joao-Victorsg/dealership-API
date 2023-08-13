<div style="text-align: Justify;">

# Dealership API
Doing some study on Rest APIs with Java and Spring Boot

# About the API
An API for managing a dealership company. You will be able to CRUD a Car, a Client and a Sale. The sale is composed by a Client and a Car.

To be able to CRUD, you have to be a valid user and get a Token in the auth endpoint. After that you have to use the bearer token 
in the requests.

The user can be created at the users endpoint in any environment different from production. 

It's built with Java and Spring Framework. It has the only purpose to study the technologies that are used to build an API with Java.

## Integrations with external services

Currently, the Dealership API is only integrated with a database and an external API.  

The external API is the VIA CEP, that receives a postcode and returns the address of this postcode. It's called by a Feign client.

### Circuit Breaker Pattern

As our API depends on an external service, it has to be prepared to deal with problems in this service. Because of that, the 
circuit breaker was implemented. It has a fallback method that is executed every time that the feign client throws an exception. 

The logical inside the fallback method is really simple. It only returns an address with the postcode that was passed to the feign client
and the isAddressSearched field as false. This field is used as a flag to verify if the client have a valid address or not. 

It is verified in the SalesController.It not permits a sale to be concluded if the client don't have a valid address. 

That was done because the address isn't crucial information at the time of registration.

The technology used for it was the Spring Cloud Circuit Breaker with Resilience4j.

## Tests inside the API

The API is tested with Unit Tests and Integrations tests. 

**For unit tests:**

JUnit5 and Mockito are used.

**For integration tests:**

Wiremock to mock and simulate the external API.
TestContainers to simulate a production environment, orchestrating a container for the database and another for the wiremock.
RestAssured to do the rest calls to the endpoints.

**For mutation tests:**

The objective behind this is to verify if the unit tests and the integrations tests 
are really testing all the available possibilities. The technology used for it was pitest.

## Data Generation

To promote the development and tests when using the API a data generation packet was created inside the configuration 
packet of the API. It aims to generate data for clients, cars, sales and users inside the database every time the application 
is turned on.

The database is dropped every time that the application is turned off.

The data generated in this process is different every time and to do that the datafaker and gson dependencies 
were used in this process.

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
* Open Feign
* Circuit Breaker
* Datafaker
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

There is a postman collection to do the requests at the collection folder. When the application initialize, it mock 100 rows of data in the database. You can see it by doing a GET.

</div>