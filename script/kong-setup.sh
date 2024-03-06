#!/bin/bash
kong migrations reset -y
kong migrations bootstrap

set -e

# apk --no-cache add curl
# apk add curl
# apk upgrade

apk add curl && apk add --upgrade curl

echo "waiting 30 sec for kong-gateway to start properly"
sleep 30

curl -X POST 'kong-gateway:8001/services' -F 'name="account-service"' -F 'url="http://account-service:8080/"'
curl -X POST 'kong-gateway:8001/services/account-service/routes' -F 'name="sys-account"' -F 'paths="/sys"' -F 'strip_path="false"'
curl -X POST 'kong-gateway:8001/services/account-service/routes' -F 'name="user-account"' -F 'paths="/"'

curl -X POST 'kong-gateway:8001/plugins' -F 'name="cors"' -F 'config.origins="http://localhost:3000"' -F 'config.methods[1]="GET"' -F 'config.methods[2]="POST"' -F 'config.methods[3]="PATCH"' -F 'config.methods[4]="DELETE"'

curl -X POST 'kong-gateway:8001/routes/sys-account/plugins' -F 'name="basic-auth"'
curl -X POST 'kong-gateway:8001/consumers' -F 'username="public"'
curl -X POST 'kong-gateway:8001/consumers/public/basic-auth' -F 'username="public"' -F 'password="publicPassword"'

echo "waiting another 30 sec for kong-gateway to load config"
sleep 30

token=$(curl -X GET 'kong-gateway:8000/token/generate' -H 'Authorization: Basic YWRtaW46YWRtaW4=' | grep -Eo '"token"[^,]*' | grep -Eo '[^:]*$' | grep -Eo '[^"]*')

curl -X POST 'kong-gateway:8001/routes/sys-account/plugins' -F 'name="request-transformer"' -F "config.add.headers=\"x-api-key: $token\""

echo "finish kong setup!"
