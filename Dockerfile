FROM openjdk:8-jdk-alpine AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mkdir classes && \
    javac -d classes ./src/server/*.java ./src/game/*.java && \
    jar cfe server.jar server.Server -C classes .


FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/app/server.jar /usr/app/server.jar
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "server.jar"]



