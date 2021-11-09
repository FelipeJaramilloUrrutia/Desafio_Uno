# Instrucciones para compilar y ejecutar proyecto previred

# Especificaciones
* Se requiere Java 17.

# Pasos
* Situado en la carpeta \previred, ejecutar el comando mvn package para generar así el archivo previred-1.jar
* Ahora, en la carpeta previred\target ejecutar el comando java -jar previred-1.jar < request.json >response.txt, siendo el request.json el archivo que se debe modificar si se quieren cambiar los rangos de fechas. En response.json, se guardará la respuesta.

# Adicional
* Si se desea, dentro de la carpeta src\main\resources se dejó un archivo llamado request.json, el cual se puede ejecutar al realizar una llamada GET a http://localhost:8081/getFechas modificando la data que este posee.
* También se puede realizar una llamada POST, ingresando la data en formato JSON a la ruta http://localhost:8081/getFechas por Postman.

# Swagger
* Para ver la documentación de Swagger, ir a http://localhost:8081/v2/api-docs