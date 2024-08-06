# Passaggi per Creare ed Eseguire il Container Docker

1. **Costruisci il file JAR**: Assicurati di avere il file JAR del tuo progetto. Puoi generarlo eseguendo il comando `mvn clean package` nella directory del tuo progetto.
2. **Costruisci l’immagine Docker**: Esegui il seguente comando nella directory dove si trova il tuo Dockerfile:
   `docker build -t awesome-pizza .`

3. **Esegui il container Docker**: Una volta costruita l’immagine, puoi eseguire il container con il seguente comando:
   `docker run -p 8080:8080 awesome-pizza`

Questo `Dockerfile` crea un’immagine Docker per il tuo progetto Spring Boot, espone la porta 8080 e avvia l’applicazione.