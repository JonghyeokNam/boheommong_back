## 1단계: Gradle 빌드(Stage 이름: build)
## gradle 버전은 원하는 버전으로 조정 가능
#FROM openjdk:21
#
## 빌드 산출물(jar)을 복사
#COPY build/libs/boheommong.jar app.jar
#
## 컨테이너에서 노출할 포트 설정(예: 8080)
#EXPOSE 8080
#
## Spring Boot 애플리케이션 실행
#ENTRYPOINT ["java", "-jar", "/app.jar"]
