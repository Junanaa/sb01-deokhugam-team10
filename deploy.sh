#!/bin/bash
set -e

BUILD_JAR=$(ls /home/ubuntu/app/build/libs/*.jar)
JAR_NAME=$(basename "$BUILD_JAR")
echo ">>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

echo ">>> build 파일 복사" >> /home/ubuntu/deploy.log
DEPLOY_PATH=/home/ubuntu/app/
cp "$BUILD_JAR" "$DEPLOY_PATH"

echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 종료" >> /home/ubuntu/deploy.log
pids=$(ps -ef | grep java | grep "$DEPLOY_PATH" | awk '{print $2}' || true)
if [ -n "$pids" ]; then
  echo "$pids" | xargs kill -15
fi

DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
echo ">>> 배포할 JAR: $DEPLOY_JAR" >> /home/ubuntu/deploy.log
nohup java -jar "$DEPLOY_JAR" >> /home/ubuntu/deploy.log 2>> /home/ubuntu/deploy_err.log &
