# Genderize Scalatra Api #

## Build & Run ##

```sh
$ cd ScalatraTest
$ ./sbt
> container:start
> browse
```

REST API - Hit [http://localhost:8080/genderize/{name}]
(http://localhost:8080/genderize/{name}) in your browser and you will get back the gender for that name. 
The gender can be Male/Female/Unknown. The API is not case sensitive.
You will get back a `Name not found` response in case the name cannot be found in the database which is sourced from 
us census data.
