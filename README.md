# Contact Book: A Spring MVC Application (Java / Kotlin)

![Application Screenshot](src%2Fmain%2Fwebapp%2Fstatic%2Fpng%2Fapplication_screenshot.png)

## Overview

The Contact Book is a small application using the Spring MVC framework. Designed as a practical exercise to explore the interaction of various technologies, the application uses a combination of Java, Kotlin, SQL, CSS, HTML, JavaScript, Spring 5.3.10, MySQL databases, Docker and Tomcat to provide comprehensive user interaction.
## Features

The application supports the following features:

1. Add a new user or generate 10 random profiles. Each user profile contains unique data such as first name, last name, email address and logo.
2. Unlimited user additions, with all data securely stored in a database.
3. Individual or bulk deletion of user profiles.
4. Ability to modify user data.
5. Access to individual user profiles via a comprehensive people list. For large user bases, a handy search bar enables swift profile search.

## Prerequisites

Before starting with the application, ensure you have the following tools installed on your system:

1. [Java 11](https://adoptopenjdk.net/)
2. [Kotlin](https://kotlinlang.org/docs/command-line.html)
3. [Maven](https://maven.apache.org/download.cgi)
4. [Docker](https://docs.docker.com/get-docker/)
5. [Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

To set up and run the application, follow these steps:

1. Clone the repository to your local machine.
    ```bash
    git clone https://github.com/mkrasikoff/contact-book.git
    ```

2. Change into the project directory.
    ```bash
    cd contact-book
    ```

3. Build the Maven project to generate the WAR file.
    ```bash
    mvn clean install
    ```

4. Build and run the Docker containers with Docker Compose.
    ```bash
    docker-compose up --build -d
    ```

5. Access the application by opening `http://localhost:8080/contact-book` in your web browser.

## Termination

To halt the application and the associated database, execute the following command:

```bash
docker-compose down
