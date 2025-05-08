# Amazon Corretto 17 이미지를 베이스로 사용
FROM amazoncorretto:21

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# Gradle Wrapper를 사용하여 애플리케이션 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test -x checkstyleMain -x checkstyleTest

# 80 포트 노출
EXPOSE 80

# 프로젝트 정보를 ENV로 설정
ENV PROJECT_NAME=deokhugam_team10
ENV PROJECT_VERSION=0.0.1-SNAPSHOT

# JVM 옵션을 ENV로 설정
ENV JVM_OPTS=""

# 빌드된 jar 파일 실행
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]

시크릿매니저 env 상쇄 aws 시크릿매니저
 ecs definition
