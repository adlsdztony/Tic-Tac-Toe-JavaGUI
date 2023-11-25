FROM openjdk:8-jdk-alpine AS build
# copy source code
COPY . /usr/src/app
# set working directory
WORKDIR /usr/src/app
# build jar
RUN javac -d classes ./src/ass4/*
RUN jar --create --file app.jar --manifest ./MANIFEST.MF -C classes .


# Path: Dockerfile
FROM openjdk:8-jre-alpine
# copy jar from build stage
COPY --from=build /usr/src/app/app.jar /usr/app/app.jar
# set working directory
WORKDIR /usr/app
# run jar
ENTRYPOINT ["java", "-jar", "app.jar"]



