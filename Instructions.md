# Popcorn Palace Application

A movie theater management system that allows for movie listings, showtime scheduling, and ticket booking.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
  - [Using Docker Compose](#using-docker-compose)
  - [Running Locally](#running-locally)
- [Testing the Application](#testing-the-application)
  - [Running Automated Tests](#running-automated-tests)
  - [Manual API Testing](#manual-api-testing)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

## Prerequisites

The following tools are required to build, run, and test this application:

- Java 17 or higher (If not using Docker)
- Maven 3.6.x or higher (Or use the included Maven wrapper)
- Docker and Docker Compose (for containerized deployment)
- An API testing tool like Postman or cURL (for manual API testing)
- Git (for cloning the repository)

## Building the Project

1. Clone or download the project to your local machine:
   ```
   git clone https://github.com/Ilai-Shohat/popcorn-palace
   cd popcorn-palace
   ```

2. Build the project using Maven:
   ```
   mvn clean package
   ```
   Or if you don't have Maven installed, use the Maven wrapper:
   ```
   # For Unix/Linux/macOS
   ./mvnw clean package
   
   # For Windows
   mvnw.cmd clean package
   ```
   This will compile the code, run tests, and create a JAR file in the `target` directory.

## Running the Application 

### Using Docker Compose

The easiest way to run the application is using Docker Compose, which will set up both the Spring Boot application and a PostgreSQL database.

1. Make sure Docker and Docker Compose are installed on your system.

2. Build the Docker image and start the containers:
   ```
   docker-compose up --build
   ```
   Or to run in detached mode:
   ```
   docker-compose up -d --build
   ```

3. The application will be available at http://localhost:8080

4. To stop the application and related containers:
   ```
   docker-compose down
   ```

### Running Locally

1. Make sure you have a PostgreSQL database running (or modify `application.properties` to use a different database).

2. Run the application from the command line:
   ```
   java -jar target/popcorn-palace-0.0.1-SNAPSHOT.jar
   ```
   
   Or using Spring Boot Maven plugin:
   ```
   mvn spring-boot:run
   ```

3. The application will be available at http://localhost:8080

## Testing the Application

### Running Automated Tests

1. To run all tests:
   ```
   mvn test
   ```

2. To run a specific test class:
   ```
   mvn test -Dtest=MovieServiceTest
   ```

### Manual API Testing

The application exposes several REST endpoints that can be tested using tools like Postman or cURL:

#### Movies API
- Get all movies: `GET /movies/all`
- Create movie: `POST /movies`
- Update movie: `POST /movies/update/{movieTitle}`
- Delete movie: `DELETE /movies/{movieTitle}`

#### Showtimes API
- Get showtime by ID: `GET /showtimes/{showtimeId}`
- Create showtime: `POST /showtimes`
- Update showtime: `POST /showtimes/update/{showtimeId}`
- Delete showtime: `DELETE /showtimes/{showtimeId}`

#### Ticket Booking API
- Book a ticket: `POST /bookings`

## Project Structure

The application follows a standard Spring Boot structure:

- `controller`: REST API controllers
- `service`: Business logic
- `repository`: Data access layer
- `model`: JPA entities
- `dto`: Data Transfer Objects
- `mapper`: Object mappers
- `exception`: Custom exceptions and error handling
- `config`: Configuration classes

Key architecture components:
- Spring Boot for application framework
- Spring Data JPA for database access
- PostgreSQL for data storage
- Maven for dependency management and build
- Docker for containerization