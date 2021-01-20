# IDB 2020 Final Project
This repository contains the code for the sample application which interfaces to the database.
## Table of contents
* [Installation](#installation)
* [Additional Information](#additional-information)
### Installation
In order to compile this project you will need the following software installed on your machine:
* [JDK 10](https://www.oracle.com/it/java/technologies/java-archive-javase10-downloads.html)
* [Maven](https://maven.apache.org/download.cgi)

Moreover you will need to place a `.env` file in the root folder of your project with the db connection information:
```
DB_URL=jdbc:postgresql://localhost:dbPort/dbName
DB_USERNAME=username
DB_PASSWORD=password
```

Once the program is setup, you can download the file `res/db_script.sql` and import it into your PostgresSQL database to initialize
the schema with sample data.
### How to run
To run the project execute within the project root folder:
```bash
mvn clean package exec:exec
```
## Additional information
### Notion specifications
The Notion specifications can be reached by clicking [here](https://www.notion.so/riccardobusetti/IDB-Final-Project-2020-4b2213725f2c40bbae0f70e0786a1581).
### Authors
* Riccardo Busetti - rbusetti@unibz.it - **17692**
* Ramon Piha - rpiha@unibz.it - **17662**

