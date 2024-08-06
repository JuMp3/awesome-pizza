# Usa un'immagine base distroless
FROM gcr.io/distroless/java17

# Imposta la directory di lavoro
WORKDIR /app

# Copia il file JAR generato nel container
COPY target/awesome-pizza.jar app.jar

# Espone la porta su cui l'applicazione sarà in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]

