#!/bin/bash
echo "Health Check 시작"

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:8081/actuator/health)
  UP_COUNT=$(echo $RESPONSE | grep 'UP' | wc -l)

  if [ $UP_COUNT -ge 1 ]; then
      echo "Health Check 성공"
      exit 0
  fi

  echo "Health Check 실패. 10초 후 재시도... ($RETRY_COUNT/10)"
  sleep 10
done

echo "Health Check 최종 실패. 배포를 중단합니다."
exit 1
