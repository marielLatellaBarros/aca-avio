# Run the application
After cloning the repository locally, go to the repository folder and execute:
```./mvnw spring-boot:run```

# Stop the application
Execute the command ```control + C```


# OpenAPI - Swagger UI
[Open API](http://localhost:8080/swagger-ui/index.html){:target="_blank"}


# Sample requests
## Create a scheduled flight
```bash
curl -X 'POST' \
  'http://localhost:8080/api/flight-scheduler/schedule' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "flightNumber": "AR1234",
  "departure": "SAEZ",
  "destination": "SAME",
  "departureTime": "2025-03-20T12:00:00Z",
  "arrivalTime": "2025-03-21T12:00:00Z"
}'
```
### Sample Response
Response code 201 Created
```bash
{
  "uuid": "b2d6876b-1ec8-43cb-a978-8150975b2088",
  "flightNumber": "AR1234",
  "departure": "SAEZ",
  "destination": "SAME",
  "departureTime": "2025-03-20T12:00:00Z",
  "arrivalTime": "2025-03-21T12:00:00Z"
}
```

## Retrieve a scheduled flight by uuid
```bash
curl -X 'GET' \
  'http://localhost:8080/api/flight-scheduler/schedule/{uuid}' \
  -H 'accept: */*'
```
### Sample Response
Response Code 200 Ok
```bash
{
  "uuid": "b2d6876b-1ec8-43cb-a978-8150975b2088",
  "flightNumber": "AR1234",
  "departure": "SAEZ",
  "destination": "SAME",
  "departureTime": "2025-03-20T12:00:00Z",
  "arrivalTime": "2025-03-21T12:00:00Z"
}
```

## Retrieve all scheduled flights
```bash
curl -X 'GET' \
  'http://localhost:8080/api/flight-scheduler/schedule' \
  -H 'accept: */*'
```
### Sample Response
Response Code 200 Ok
```bash
[
  {
    "uuid": "b2d6876b-1ec8-43cb-a978-8150975b2088",
    "flightNumber": "AR1234",
    "departure": "SAEZ",
    "destination": "SAME",
    "departureTime": "2025-03-20T12:00:00Z",
    "arrivalTime": "2025-03-21T12:00:00Z"
  },
  {
    "uuid": "70c05205-7e0f-4781-8a77-27d38ddc079b",
    "flightNumber": "AR1235",
    "departure": "SAEZ",
    "destination": "SAME",
    "departureTime": "2025-04-20T12:00:00Z",
    "arrivalTime": "2025-04-21T12:00:00Z"
  },
  {
    "uuid": "ac397417-e4ff-4ae2-936c-5c524b92aafe",
    "flightNumber": "AR1235",
    "departure": "EBAW",
    "destination": "EBBR",
    "departureTime": "2025-05-20T12:00:00Z",
    "arrivalTime": "2025-05-21T12:00:00Z"
  }
]
```
## Retrieve all scheduled flights for a specific flight number (AR1235)
```bash
curl -X 'GET' \
  'http://localhost:8080/api/flight-scheduler/schedule/search?flightNumber=AR1235' \
  -H 'accept: */*'
```
### Sample Response
Response Code 200 Ok
```bash
[
  {
    "uuid": "70c05205-7e0f-4781-8a77-27d38ddc079b",
    "flightNumber": "AR1235",
    "departure": "SAEZ",
    "destination": "SAME",
    "departureTime": "2025-04-20T12:00:00Z",
    "arrivalTime": "2025-04-21T12:00:00Z"
  },
  {
    "uuid": "ac397417-e4ff-4ae2-936c-5c524b92aafe",
    "flightNumber": "AR1235",
    "departure": "EBAW",
    "destination": "EBBR",
    "departureTime": "2025-05-20T12:00:00Z",
    "arrivalTime": "2025-05-21T12:00:00Z"
  }
]
```
## Retrieve all scheduled flights between two periods (departing after 2025-04-01 and 2025-04-30)
```bash
curl -X 'GET' \
  'http://localhost:8080/api/flight-scheduler/schedule/search?departureTimeAfter=2025-04-01T12%3A00%3A00Z&departureTimeBefore=2025-04-30T12%3A00%3A00Z' \
  -H 'accept: */*'
```
### Sample Response
Response Code 200 Ok
```bash
[
  {
    "uuid": "70c05205-7e0f-4781-8a77-27d38ddc079b",
    "flightNumber": "AR1235",
    "departure": "SAEZ",
    "destination": "SAME",
    "departureTime": "2025-04-20T12:00:00Z",
    "arrivalTime": "2025-04-21T12:00:00Z"
  }
]
```
Search criteria: flightNumber, departure, destination, departureTimeAfter, departureTimeBefore, arrivalTimeAfter, arrivalTimeBefore

## Delete a scheduled flight for a specific uuid
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/flight-scheduler/schedule/ac397417-e4ff-4ae2-936c-5c524b92aafe' \
  -H 'accept: */*'
```
### Sample Response
Response Code 204 No Content

