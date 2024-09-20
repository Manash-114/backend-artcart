#bulid stage

FROM maven:3-openjdk-17 as bulid
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


#running stage

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=bulid /build/target/art-cart-backend-*.jar /app/art-cart-backend.jar

EXPOSE 9090

CMD java -jar art-cart-backend.jar

