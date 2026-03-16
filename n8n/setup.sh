#!/bin/bash
# n8n EC2 초기 세팅 스크립트
# 사용법: EC2 접속 후 이 스크립트 실행

set -e

echo "=== 1. Docker 설치 ==="
sudo apt update
sudo apt install -y docker.io docker-compose-plugin
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER

echo "=== 2. n8n 실행 ==="
mkdir -p ~/dekk-n8n
cd ~/dekk-n8n

# docker-compose.yml은 scp로 미리 복사해두거나 여기서 생성
docker compose up -d

echo "=== 3. n8n 상태 확인 ==="
sleep 10
docker compose logs --tail 20

echo ""
echo "=== 세팅 완료 ==="
echo "n8n UI: http://$(curl -s ifconfig.me):5678"
echo ""
echo "다음 단계:"
echo "1. n8n UI 접속 후 초기 계정 생성"
echo "2. Google Gemini API 크레덴셜 등록"
echo "3. 워크플로우 import:"
echo "   curl -X POST http://localhost:5678/api/v1/workflows \\"
echo "     -H 'X-N8N-API-KEY: <API_KEY>' \\"
echo "     -H 'Content-Type: application/json' \\"
echo "     -d @outfit-filter-workflow.json"
echo "4. 워크플로우 활성화"
