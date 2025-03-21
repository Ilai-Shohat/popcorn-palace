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
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## Prerequisites

The following tools are required to build, run, and test this application:

- Java 17 or higher
- Maven 3.6.x or higher
- Docker and Docker Compose (for containerized deployment)
- Git (optional, for version control)
- An API testing tool like Postman or cURL (for manual API testing)

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

4. To stop the application:
   ```
   docker-compose down
   ```

## Testing the Application

### Running Automated Tests

1. To run all tests:
   ```
   mvn test
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

Example for creating a new movie using cURL:
```bash
curl -X POST http://localhost:8080/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"The Matrix", "genre":"Sci-Fi", "duration":136, "rating":8.7, "releaseYear":1999}'
```

## Project Structure

The application follows a standard Spring Boot structure:

- `controller`: REST API controllers
- `service`: Business logic
- `repository`: Data access layer
- `model`: JPA entities
- `dto`: Data Transfer Objects
- `mapper`: Object mappers
- `exception`: Custom exceptions

Key components:
- Movie: Represents a film with details like title, genre, and rating
- Showtime: Represents a scheduled movie showing in a specific theater
- Ticket: Represents a booking for a specific showtime and seat
