FROM	maven:3.5-jdk-8

EXPOSE 8080

WORKDIR /usr/src/app

RUN apt-get update
RUN apt-get install -y graphviz

CMD mvn clean install && cd elmma-core && mvn spring-boot:run