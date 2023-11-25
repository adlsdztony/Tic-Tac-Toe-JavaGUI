FROM openjdk:8-jdk-alpine AS build
# copy source code
COPY . /usr/src/app
# set working directory
WORKDIR /usr/src/app
# build jar
RUN mkdir classes
RUN javac -d classes ./src/ass4/*
RUN jar cfe app.jar ass4.Main -C classes .


# Path: Dockerfile
FROM openjdk:8-jre-alpine
# copy jar from build stage
COPY --from=build /usr/src/app/app.jar /usr/app/app.jar
# set working directory
WORKDIR /usr/app
# turn on X11 display
ENV DISPLAY :0
RUN apk add --no-cache libxext libxrender libxtst
# install x11
RUN apk add --no-cache xorg-server
# run x11
CMD ["Xorg", "-ac", ":0", "-listen", "tcp"]

# run jar
ENTRYPOINT ["java", "-jar", "app.jar"]



