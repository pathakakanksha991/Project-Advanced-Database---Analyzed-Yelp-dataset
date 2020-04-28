This project works with Yelp dataset (https://www.kaggle.com/shikhar42/yelps-dataset) to create a data driven information system.
It uses Spring-boot along along with MariaDB Cluster for the phase 1.
For phase 2, MongoDB is used.

The aim is to build a system that remains DB agnostic except for the data layer, and work effectively for both relational and non-relational database cluster.


To run this application, go to the application folder (yelp) and run the following without quotes:
"mvnw spring-boot:run"

or

You can build the application jar by going to the application folder (yelp) and using the following without quotes:
"mvnw clean install"