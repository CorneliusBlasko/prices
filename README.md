# (EN) Price Service Technical Test

## Overview

This document provides a comprehensive guide to setting up, running, and understanding the design decisions behind the Price Service application.
## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.x or 8.x
- Docker
### Installation

1. Clone the repository:
``` shell
git clone https://github.com/CorneliusBlasko/prices.git
```

### Running the Application

First of all, compile the application to generate the `.jar` files:

```shell
# For Windows  
gradlew.bat build  
  
#For Linux/MacOS  
./gradlew build  
```

Then, build the images:

```` shell 
docker-compose build
````

And finally, deploy the images:

``` shell
docker-compose up -d
```
### Testing the Application

The unit and integration tests are executed during the build process, so there's no need to execute them separately. The manual testing can be done via `curl` commands:

```bash
curl "http://localhost:8080/prices?application_date=2020-06-14T10:00:00&product_id=35455&brand_id=1" -v
```

This command will retrieve the price for product 35455 on June 14, 2020, at 10:00 AM for brand 1 and show the headers.
## API Documentation

The API documentation is available at the following URL: http://localhost:8080/swagger-ui/index.html

The documentation is generated using OpenAPI and provides a user-friendly interface for exploring the API endpoints.

## Database Setup

The application uses an in-memory H2 database. The schema and data are initialized using the `schema.sql` and `data.sql` scripts in the `src/main/resources` directory. The database is automatically created and populated when the application starts.

## Assumptions

- The application assumes that the input parameters (application date, product ID, and brand ID) are valid. Error handling for invalid input is implemented, returning a 400 Bad Request.
- The application assumes that the database is available and accessible.
- The application uses a simplified data model for prices. In a real-world scenario, a more complex model might be needed.
## Business Requirements

The application is designed to meet the following business requirements:
- Provide an API endpoint to retrieve the applicable price for a product, given a date, product ID, and brand ID.
- Prioritise prices based on a priority field, returning the highest priority price.
## Technical Architecture

The application follows a Hexagonal Architecture. This design promotes separation of concerns and makes the application more maintainable and testable.

- **Domain Layer:** Contains the domain models (e.g., `Price`) and the ports that define the contract to retrieve data (such as `PriceRepositoryPort`)
- **Application Layer:** Contains the application services (`PriceService`) that orchestrate the use cases by interacting with the domain layer.
- **Infrastructure Layer:** Contains the adapters that interact with external systems such as databases (using `PricePersistenceAdapter`) and clients through REST APIs (`PriceController`).
## Design Decisions

### Business Decisions

#### Default price

Although it was not included in the task specification, it has been decided to add a new field to the service response. This field, called `defaultPrice`, is returned when the price of a product is requested for a date that does not exist in the `prices` table. We considered that, although the price may not be present in the table, it is necessary to always return a price for any `product_id` because, from a business logic perspective, a product cannot exist without a price.

Therefore, the following conditions apply to `defaultPrice`:

- If there is no price for the requested product on the designated date, the `price` field will be returned empty and the `defaultPrice` field will contain the product’s base price without any applied rate.
- If there is a price for the requested product on the designated date, the `price` field will hold the value found according to business rules, such as applying the disambiguator, and the `defaultPrice` field will be empty.

To implement this logic, a new table `PRODUCT_DEFAULT_PRICES` has been created in the database, which contains the base prices of the products. Internally, when a product does not have a price with an applied rate, its priority is set to -1 to avoid collisions with other legitimate values.
### Technical Decisions

- **Hexagonal Architecture:** This architecture was chosen to enforce a clear separation of concerns, making the application more maintainable, testable, and adaptable to future changes.
- **JPA and Hibernate:** JPA was used for object-relational mapping, and Hibernate is the chosen implementation.
- **OpenAPI (Swagger):** Used for API documentation.
- **Gradle:** Used as the build system.
- **JUnit and Spring Test:** Used for unit and integration testing.

-----

# (ES) # Prueba Técnica Price Service

## Descripción General

Este documento proporciona una guía completa para configurar, ejecutar y comprender las decisiones de diseño detrás de la aplicación Price Service.
## Primeros Pasos

### Requisitos Previos

- Java 17 o superior
- Gradle 7.x o 8.x
- Docker
### Instalación

1. Clonar el repositorio:

```shell
git clone https://github.com/CorneliusBlasko/prices.git`
```
### Ejecución de la Aplicación

En primer lugar es necesario compilar la aplicación para generar los archivos `.jar`:

``` shell
# Windows   
gradlew.bat build      

# Linux/MacOS   
./gradlew build`  
```

Posteriormente se construyen las imágenes:

```shell
docker-compose build`
```

Y finalmente se despliegan las imágenes:

``` shell
docker-compose up -d
```

### Pruebas de la Aplicación

Las pruebas unitarias y de integración se ejecutan durante el proceso de construcción, por lo que no es necesario ejecutarlas por separado. Las pruebas manuales se pueden realizar mediante comandos `curl`:

```bash 
curl "http://localhost:8080/prices?application_date=2020-06-14T10:00:00&product_id=35455&brand_id=1" -v
```

Este comando obtendrá el precio para el producto 35455 el 14 de junio de 2020 a las 10:00 AM para la marca 1 y mostrará las cabeceras de la respuesta.
## Documentación de la API

La documentación de la API está disponible en la siguiente URL:  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) La documentación se genera utilizando OpenAPI y proporciona una interfaz amigable para explorar los endpoints de la API.
## Configuración de la Base de Datos

La aplicación utiliza una base de datos en memoria usando `H2`. El esquema y los datos se inicializan utilizando los scripts `schema.sql` y `data.sql` que se encuentran en el directorio `src/main/resources`. La base de datos se crea y se carga automáticamente al iniciar la aplicación.
## Supuestos

- La aplicación asume que los parámetros de entrada (fecha de aplicación, ID de producto e ID de marca) son válidos. Se ha implementado gestión de errores para entradas no válidas, devolviendo un `400 Bad Request`.
- Se asume que la base de datos está disponible y accesible.
- La aplicación utiliza un modelo de datos simplificado para los precios. En un escenario real podría requerirse un modelo más complejo.

## Requisitos de Negocio

La aplicación está diseñada para cumplir con los siguientes requisitos de negocio:
- Proporcionar un endpoint de API para obtener el precio aplicable de un producto, dada una fecha, un ID de producto y un ID de marca.
- Priorizar los precios en función de un desambiguador, devolviendo el precio con mayor prioridad.
## Arquitectura Técnica

La aplicación sigue una Arquitectura Hexagonal. Este diseño promueve la separación de responsabilidades y hace que la aplicación sea más mantenible y testeable.

- **Capa de Dominio:** Contiene los modelos de dominio (por ejemplo, `Price`) y los puertos que definen el contrato para obtener datos (como `PriceRepositoryPort`).
- **Capa de Aplicación:** Contiene los servicios de aplicación (`PriceService`) que orquestan los casos de uso interactuando con la capa de dominio.
- **Capa de Infraestructura:** Contiene los adaptadores que interactúan con sistemas externos como bases de datos (usando `PricePersistenceAdapter`) y clientes a través de APIs REST (`PriceController`).
## Decisiones de Diseño

### Decisiones de Negocio

#### Precio por Defecto

Aunque no constaba en la especificación de la tarea se ha decidido añadir un nuevo campo a la respuesta del servicio. Este campo, llamado `defaultPrice`, es devuelto cuando se pide el precio de un producto para una fecha que no consta en la tabla `prices`. Hemos considerado que, si bien el precio puede no aparecer en la tabla, es necesario devolver siempre un precio para cualquier `product_id` porque, desde el punto de vista de lógica de negocio, no puede existir un producto sin precio.

Por tanto, las siguientes condiciones se aplican a `defaultPrice`:

- Si no existe un precio para el producto solicitado en la fecha designada se devuelve el campo `price` vacío y el campo `defaultPrice` contendrá el precio base del producto sin ninguna tarifa aplicada.
- Si existe un precio para el producto solicitado en la fecha designada el campo `price` contendrá el valor encontrado siguiendo las reglas de negocio, tales como la aplicación del desambiguador, y el campo `defaultPrice` vendrá vacío.

Para aplicar esta lógica se ha creado una nueva tabla `PRODUCT_DEFAULT_PRICES` en la base de datos que contiene el precio base de los productos. Internamente, cuando un producto no tiene precio con tarifa aplicada su prioridad se establece en -1 para evitar colisiones con otros valores legítimos.
### Decisiones Técnicas

- **Arquitectura Hexagonal:** Se eligió esta arquitectura para imponer una separación clara de responsabilidades, haciendo que la aplicación sea más mantenible, testeable y adaptable a futuros cambios.
- **JPA y Hibernate:** Se utilizó JPA para el mapeo objeto-relacional, siendo Hibernate la implementación seleccionada.
- **OpenAPI (Swagger):** Utilizado para la documentación de la API.
- **Gradle:** Utilizado como sistema de construcción.
- **JUnit y Spring Test:** Utilizados para pruebas unitarias y de integración.
