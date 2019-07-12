FROM gradle:jdk11 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --info

FROM openjdk:11-jre
EXPOSE 443
COPY --from=builder /home/gradle/src/build/distributions/backend-challenge-2019-boot-1.0.tar /app/
WORKDIR /app
RUN tar -xvf backend-challenge-2019-boot-1.0.tar
WORKDIR /app/backend-challenge-2019-boot-1.0
COPY --from=builder /home/gradle/src/category.txt /app/backend-challenge-2019-boot-1.0
COPY --from=builder /home/gradle/src/keystore.p12 /app/backend-challenge-2019-boot-1.0
CMD bin/backend-challenge-2019
