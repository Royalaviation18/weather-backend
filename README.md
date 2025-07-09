## 🌤️ Weather Information API

A Spring Boot-based REST API that provides **current weather information** for Indian pincodes using the OpenWeather API.  
The application stores pincode coordinates and weather data in a PostgreSQL database to minimize repeated API calls.



### 📌 Features

- Fetch weather data by pincode and date
- Stores geolocation and weather info in PostgreSQL
- Auto-fetches coordinates if not in DB
- Auto-fetches weather if not in DB
- Global error handling
- Swagger API documentation
- JUnit test cases (TDD-style)



### 🔗 API Endpoint

**POST** `/weather`

### Request Body
```json
{
  "pincode": "411014",
  "date": "2024-07-08"
}
```

Sample Successful Response
```bash
{
  "pincode": "411014",
  "date": "2024-07-08",
  "temperature": 29.5,
  "humidity": 72,
  "description": "clear sky"
}
```
## 📚 API Reference

####  🔄 Get weather

```http
  POST http://localhost:8080/weather
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `pincode` | `string` | **Required**. pincode of the place for which weather forecast is required|
| `date` | `string` | **Required**. date to be provided in the format 'yyyy-mm-dd' |


### ⚙️  Run Locally

✅ Prerequisites
- Java 17+

- Maven

- PostgreSQL running locally

Clone the project

```bash
  git clone https://github.com/Royalaviation18/weather-backend
```

Go to the project directory

```bash
  cd weather-backend
```

Set up PostgreSQL database

- Create a DB named weather_db and update credentials in:
```bash
  src/main/resources/application.properties
```

Run the app

```bash
  ./mvnw spring-boot:run
```
- Or from IntelliJ, run GithubApplication.java.


Access Swagger Docs:

Navigate to:
👉 http://localhost:8080/swagger-ui/index.html






## 🧪  Running Test Cases

This project uses JUnit 5 and Mockito for unit testing the service and controller layers.

#### Prerequisities
- Make sure you have
  
  i) JDK 17 or above
  
  ii) Maven 3.8+

  iii) PostgreSQL Running     

▶️ To run tests via IntelliJ IDEA
 
 #### Run all JUnit tests
  ```bash
  ./mvnw test
  ```

- Unit tests available for:

    i) WeatherServiceImpl

    ii) GeoServiceImpl

    iii) WeatherController (using @WebMvcTest)

- Navigate to src/test
- Right click on whichever you want to test individually and click run
-




📝 Sample Output
  ```bash
  [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```


