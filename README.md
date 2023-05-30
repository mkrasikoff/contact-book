# Spring MVC Application

This is a simple Spring MVC application that uses a MySQL database. The purpose of this application is to practice creating MVC systems without using Spring Boot. The application and database are containerized using Docker and managed with Docker Compose.

## Prerequisites

Ensure that you have the following installed on your system:

1. [Java 11](https://adoptopenjdk.net/)
2. [Maven](https://maven.apache.org/download.cgi)
3. [Docker](https://docs.docker.com/get-docker/)
4. [Docker Compose](https://docs.docker.com/compose/install/)

## Steps to run the application

1. Clone the repository to your local machine.
    ```bash
    git clone https://github.com/mkrasikoff/spring-mvc-app.git
    ```

2. Navigate into the project directory.
    ```bash
    cd spring-mvc-app
    ```

3. Build the Maven project to generate the WAR file.
    ```bash
    mvn clean install
    ```

4. Build and run the Docker containers using Docker Compose.
    ```bash
    docker-compose up --build -d
    ```

5. Access the application by opening `http://localhost:8080/spring-mvc-app` in a web browser.

## Stopping the application

To stop the application and the database, run the following command:

```bash
docker-compose down
