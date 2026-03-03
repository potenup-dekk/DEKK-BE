#!/bin/bash

# 변수 설정
REPOSITORY="/home/ubuntu/dekk-was"
APP_NAME="dekk"

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl $APP_NAME | grep java | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포 시작"

# 새로 빌드된 JAR 파일 찾기
JAR_NAME=$(ls -tr $REPOSITORY/build/libs/*SNAPSHOT.jar | grep -v plain | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> 애플리케이션 실행"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=prod $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

echo "> 배포 완료! 로그는 $REPOSITORY/nohup.out 에서 확인할 수 있습니다."
