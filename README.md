# Description

App which blocks unused ip calls exceeding limit, which is set in application.properties file

# Build and deploy
docker build -t your-image-name .

docker run -p 8080:8080 my-app-image