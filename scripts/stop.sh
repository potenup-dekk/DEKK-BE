#!/bin/bash

CURRENT_PID=$(pgrep -f "dekk.*\.jar")

if [ -z "$CURRENT_PID" ]; then
  echo "현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 10
fi
