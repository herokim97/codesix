# 빌드 스테이지
FROM gradle:8.10.2-jdk17 AS builder

WORKDIR /apps

COPY . /apps
RUN gradle clean build --no-daemon --parallel


# 1. 기본 이미지로 OpenJDK를 사용
FROM openjdk:17-jdk-slim

# 이미지에 레이블 추가
LABEL type="application"

WORKDIR /apps

# 2. JAR 파일을 컨테이너로 복사
COPY --from=builder apps/build/libs/*-SNAPSHOT.jar /app/app.jar

ENV URL=jdbc:mysql://localhost:3306/codesix
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=gksl671456!
ENV ACCESS_KEY=AKIAXEVXYSHWDSPDZ4UA
ENV SECRET_KEY=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

# 3. 포트 설정 (필요에 따라 포트 번호는 변경 가능)
EXPOSE 8080

# 4. 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app/app.jar"]