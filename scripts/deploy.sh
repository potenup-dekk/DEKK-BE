#!/bin/bash

PROJECT_ROOT="/home/ec2-user/dekk-was"
JAR_NAME=$(ls -tr $PROJECT_ROOT/*.jar | grep -v plain | tail -n 1)

echo "> 발견된 JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"
chmod +x $JAR_NAME

echo "> 애플리케이션 실행"

nohup java -jar \
  -Duser.timezone=Asia/Seoul \
  -Dspring.profiles.active=prod \
  $JAR_NAME > $PROJECT_ROOT/nohup.out 2>&1 < /dev/null &
