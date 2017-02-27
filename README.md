## retail-manager
This is server side restful API developed using Spring boot. It allows a retail manager to
1. Add a shop
    - The retail manager can post a JSON payload to a shop endppint containing shopName, shopAddress.number, shopAddress.postCode.
    - This API uses Google Maps Geocoding API tp retreive the longitude and latitude and store all information to a server side
    in-memory data store.
2. Find the nearest shop by giving cutomerLongitude and customerLatitude

## Dependencies

- Gradle 3.3
- Java 8
- Spring-boot
- Google Map Geocoding API
- Jacoco

## Build

It is a gradle based java application, use the following to package it in a jar.

``````````````````
gradle build
``````````````````
To build without running tests

`````````````````````
gradle build -x test
`````````````````````

On successful build the jar can be found at `build/libs/` path.

## Run

Once the application is built, you have the jar, it can be run with the following command -

```````````````````````````````````````````````````````````````
java -jar path/retail-manager/build/libs/retail-manager-1.0.jar
```````````````````````````````````````````````````````````````

Or, simply run the main() of ApplicationLauncher.java

Or, the below command also run the application -

``````````
gradle run
```````````

You will see the following message after a successful start up -
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.0.RELEASE)

Retail Manager is running!

## Test

The application has test cases as well. To run, use the below -

```````````
gradle test
```````````

It also has  "jacoco" plugin to see the code coverage -
````````````````````````````
gradle test jacocoTestReport
````````````````````````````
The coverage can be veiwed at -
src\retail-manager\build\reports\tests\test\index.html

## APIs -
There are two APIs-

### Add Shop
- This api adds a shop to the in-memory database.

`````````````````````````````````````
URI            - /shop/add
REQUEST BODY   - {"shopName" : "Hasan Test Shop","shopAddress" : {"number": "EON IT Park, Kharadi, Pune","postCode" : 411014}}
HTTP METHOD    - POST
HTTP RESPONSE  - 201 OK
RESPONSE BODY  - {"success": true}
`````````````````````````````````````
e.g - http://localhost:8080/shop/add

### Get Nearest Shop
- This api finds the nearest shop to the location (latitude, longtitude) passed in the request parameter.

```
URI            - /shop/find
REQUEST PARAMS - customerLatitude, customerLongitude
HTTP METHOD    - GET
HTTP RESPONSE  - 200 OK
RESPONSE BODY  - {"shopName" : "Test Shop","shopAddress" : {"number": "1234","postCode" : 411014}, "shopLatitude": "18.5515", "shopLongitude" : "73.9348"}

```

e.g - http://localhost:8080/shop/find?customerLatitude=18.5793&customerLongitude=73.9823




