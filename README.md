# Spring MVC Application

This is a simple Spring MVC application that uses a MySQL database. The purpose of this application - practice creating MVC systems without using Spring Boot.The application and database are containerized using Docker.

## Prerequisites

Ensure that you have the following installed on your system:

1. [Java 11](https://adoptopenjdk.net/)
2. [Maven](https://maven.apache.org/download.cgi)
3. [Docker](https://docs.docker.com/get-docker/)

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

4. Build the Docker image for the Spring MVC application.
    ```bash
    docker build -t spring-mvc-app .
    ```

5. Run a MySQL Docker container with the database name, username, and password that your application expects. In this case, we'll use the database name `database`, the username `admin`, and the password `password`.
    ```bash
    docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=database -e MYSQL_USER=admin -e MYSQL_PASSWORD=password -p 3306:3306 -d mysql:8.0
    ```

6. Run the Spring MVC application Docker container, linking it to the MySQL container.
    ```bash
    docker run -p 8080:8080 --name spring-mvc-app --link mysql-db:mysql -d spring-mvc-app
    ```

7. Access the application by opening `http://localhost:8080/spring-mvc-app` in a web browser.

## Stopping the application

To stop the application and the database, run the following commands:

```bash
docker stop spring-mvc-app mysql-db
docker rm spring-mvc-app mysql-db