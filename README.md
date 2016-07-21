# Genderize Scalatra API #

## Build & Run ##

```sh
$ cd genderize-scalatra
$ sbt compile
$ sbt
> container:start
```

REST API - Hit [http://localhost:8080/genderize/{name}] in your browser and you will get back the gender for that name. 
The gender can be Male/Female/Unknown. The API is not case sensitive.
You will get back a `Name not found` response in case the name cannot be found in the database which is sourced from 
US census data.

If you want to run the docker container.

```sh
$ sbt docker
$ docker run -itd -p 8080:8080 genderize_scalatra:0.1.0
```
      
Hit [http://localhost:8080/genderize/{name}] in your browser if you have done port forwarding and localhost mapping
otherwise do [http://<DockerIP>:8080/genderize/{name}].