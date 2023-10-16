# java-template

Este proyecto incluye dos Dockerfiles para construir imágenes Docker: una para un servidor MySQL y otra para una aplicación Java basada en Maven. 

## Dockerfile para MySQL

El archivo `Dockerfile.mysql` está diseñado para crear una imagen de MySQL 5.7 con configuraciones predefinidas.

### Variables de Entorno

- `MYSQL_ROOT_PASSWORD`: Contraseña de root para MySQL (se establece en "root").
- `MYSQL_DATABASE`: Nombre de la base de datos a crear (se establece en "accwe-hospital").

### Puerto Expuesto

- Puerto 3306 se expone para acceder a MySQL.

### Comando de Inicio

El contenedor se inicia con el siguiente comando:

```bash
CMD ["mysqld", "--bind-address=172.17.0.2"]
```

## Dockerfile para la Aplicación Spring Boot
El archivo `Dockerfile.maven` está diseñado para compilar y ejecutar una aplicación Java basada en Maven.

### Etapa 1: Compilación y Pruebas
En esta etapa, se compila la aplicación Java y se ejecutan las pruebas.

### Etapa 2: Construcción de la Imagen Final
En esta etapa, se crea una imagen basada en OpenJDK 11 con la aplicación Java compilada.

### Puerto Expuesto
- Puerto 8080 se expone para acceder a la aplicación Java.

### Comando de Inicio
```bash
La aplicación se inicia con el siguiente comando:
CMD ["java", "-jar", "/app/app.war"]
```