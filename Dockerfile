# OpenJDK 17 slim 기반 이미지 사용
#FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jdk-jammy

# 이미지에 레이블 추가
LABEL type="application"

# 작업 디렉토리 설정
WORKDIR /app

# 애플리케이션 jar 파일을 컨테이너로 복사
COPY build/libs/*-SNAPSHOT.jar /app/app.jar

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

ENV URL=jdbc:mysql://localhost:3306/codesix
# docker-compose 를 사용하여 관리하기 : 각각의 이름으로 통신 가능
# 52.79.142.162
# jdbc:mysql://52.79.142.162:3306/codesix


# docker start mysql-container
# docker start ~

# 애플리케이션을 실행하기 위한 엔트리포인트 정의
ENTRYPOINT ["java", "-jar", "/app/app.jar"]