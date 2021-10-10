FROM openjdk:11 AS builder

ENV SBT_VERSION 1.5.5
RUN curl -L -o sbt-$SBT_VERSION.zip https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-$SBT_VERSION.zip
RUN unzip sbt-$SBT_VERSION.zip -d ops
WORKDIR /HelloWorld
COPY . /HelloWorld
RUN /ops/sbt/bin/sbt assembly


FROM openjdk:18-jdk-alpine3.13 
EXPOSE 3000
WORKDIR /app
COPY --from=builder /HelloWorld/target/scala-3.0.0/scala3-simple-assembly-0.1.0.jar .
CMD java -jar ./scala3-simple-assembly-0.1.0.jar --server.address=0.0.0.0 -Dhttp.port=3000