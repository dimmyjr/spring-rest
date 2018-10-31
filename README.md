# SPRING REST

This project aims to become the base of reference architecture for the development of "services" and "microservices". In it is gathered ways of working with the [Spring](https://spring.io/) that serves as the main frameworks. SWAGGER is also used to catalog services.

The application is based on bankslip management. CREATING, LISTING, PAYING AND CANCELING.

### Frameworks:
- Spring Boot
- Spring Data 
- Spring Data REST
- Swagger
- Validation
- Lombok
- Jackson
- H2 Database

## Build

Use the package manager [maven](https://maven.apache.org/) to build.

```bash
mvn clean install
```

## Run

For run this project.

```bash
mvn spring-boot:run
```

## SHOW SERVICES CATALOG

```html
http://localhost:8080/swagger-ui.html#/
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
