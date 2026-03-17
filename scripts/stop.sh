#!/bin/bash
PROJECT_ROOT="/home/ec2-user/dekk-was"
JAR_FILE="$PROJECT_ROOT/build/libs/dekk-api-prod.jar" # 실제 빌드된 jar 파일 이름으로 변경하세요!

CURRENT_PID=$(pgrep -f "dekk-api-prod.jar")

if [ -z "$CURRENT_PID" ]; then
  echo "현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 10
fi
