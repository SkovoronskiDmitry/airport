![Java CI]
# dskovoronski-airport

## How build
Setup java 8 and Maven, see [setup_JDK8_maven.md](documentation/setup_JDK8_maven.md "click") 
  
      
## Build project 
1. Setup application using jetty server [using_jetty.md](documentation/using_jetty.md "click")
2. Setup application using jetty plugin  [using_jetty_plugin.md](documentation/using_jetty_plugin.md "click")


## FLights

### findAll

```
curl --location --request GET 'http://localhost:8081/flights'
```

#### findById

```
curl --request GET 'http://localhost:8081/flights/101' | json_pp
```
### create

```
curl --location --request POST 'http://localhost:8081/flights' \
--header 'Content-Type: application/json' \
--data-raw '{"direction":"Misk-Moscow","dateFlight":"2020-05-01"}'
```

### update

```
curl --location --request PUT 'http://localhost:8081/flights/101' \
--header 'Content-Type: application/json' \
--data-raw '{"direction":"Moscow=Minsk"}'
```

### delete

```
curl --location --request DELETE 'http://localhost:8081/flights/101'
```

## FlightsDto

### findWithDateFilter

```
curl --location --request GET 'http://localhost:8081/flightsDto?dateFrom=2020-04-01&dateTo=2020-04-10'
```

### findAllWithQuantityPassengers

```
curl --location --request GET 'http://localhost:8081/flightsDto/quantity'
```

## Passengers

### findAll

```
curl --location --request GET 'http://localhost:8081/passengers'
```

### findById

```
curl --location --request GET 'http://localhost:8081/passengers/2'
```

### create

```
curl --location --request POST 'http://localhost:8081/passengers' \
--header 'Content-Type: application/json' \
--data-raw '{"firstName":"Dmitry","lastName":"Skovoronski","flightId":"104"}'
```

### update

```
curl --location --request PUT 'http://localhost:8081/passengers/2' \
--header 'Content-Type: application/json' \
--data-raw '{"firstName":"Dmitry","lastName":"Skovoronski","flightId":"105"}'
```

### delete

```
curl --location --request DELETE 'http://localhost:8081/passengers/2'
```
