# SigestickBackend

Este proyecto es un backend desarrollado en Spring Boot para la gestión de tickets. Asegúrate de seguir estos pasos antes de ejecutar el proyecto.

## Requisitos previos

1. **Lombok**:  
   Es necesario tener instalado y habilitado el complemento de Lombok en tu IDE.  
   Puedes encontrar más información aquí: [Lombok](https://projectlombok.org/).

2. **Base de datos**:
    - Crea una base de datos llamada `ticket_db` en tu sistema de gestión de bases de datos.
    - Modifica las credenciales de la base de datos en el archivo de configuración (`application.properties` o `application.yml`) para que coincidan con las credenciales de tu entorno.

## Configuración

En el archivo `application.properties` o `application.yml`, asegúrate de actualizar estas líneas con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
