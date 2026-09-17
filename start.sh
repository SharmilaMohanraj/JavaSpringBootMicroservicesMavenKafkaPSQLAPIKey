#!/bin/bash
cd "$(dirname "$0")"
export SERVER_FORWARD_HEADERS_STRATEGY=framework
export ADMIN_API_KEY="${ADMIN_API_KEY:-local-admin-key}"
RESOLVED_EUREKA_URL="http://localhost:8761/eureka"

restart_service() {
  local service="$1"
  local port="$2"
  local pid
  pid=$(pgrep -f -- "--server.port=$port" || true)
  if [ -n "$pid" ]; then
    echo "$service: killing existing PID $pid on port $port" >> "$service.log"
    kill $pid 2>/dev/null || true
    for i in 1 2 3 4 5 6 7 8 9 10; do
      bash -c "exec 3<>/dev/tcp/127.0.0.1/$port" 2>/dev/null || break
      sleep 1
    done
    if bash -c "exec 3<>/dev/tcp/127.0.0.1/$port" 2>/dev/null; then
      echo "$service: still bound after 10s, force killing" >> "$service.log"
      kill -9 $pid 2>/dev/null || true
      sleep 1
    fi
  fi
  nohup java -jar "$service"/target/*.jar --server.port="$port" --eureka.client.service-url.defaultZone="$RESOLVED_EUREKA_URL" >> "$service.log" 2>&1 &
  sleep 5
}

CATALOG_SERVICE_PORT="${CATALOG_SERVICE_PORT:-29838}"
restart_service "catalog-service" "$CATALOG_SERVICE_PORT"
INVENTORY_SERVICE_PORT="${INVENTORY_SERVICE_PORT:-22482}"
restart_service "inventory-service" "$INVENTORY_SERVICE_PORT"
CART_SERVICE_PORT="${CART_SERVICE_PORT:-21954}"
restart_service "cart-service" "$CART_SERVICE_PORT"
ORDER_SERVICE_PORT="${ORDER_SERVICE_PORT:-22354}"
restart_service "order-service" "$ORDER_SERVICE_PORT"
USER_SERVICE_PORT="${USER_SERVICE_PORT:-26642}"
restart_service "user-service" "$USER_SERVICE_PORT"

GATEWAY_PORT="${SERVER_PORT:-29875}"
EXISTING_PID=$(pgrep -f -- "--server.port=$GATEWAY_PORT" || true)
if [ -n "$EXISTING_PID" ]; then
  echo "gateway-service: killing existing PID $EXISTING_PID on port $GATEWAY_PORT" >> gateway-service.log
  kill $EXISTING_PID 2>/dev/null || true
  for i in 1 2 3 4 5 6 7 8 9 10; do
    bash -c "exec 3<>/dev/tcp/127.0.0.1/$GATEWAY_PORT" 2>/dev/null || break
    sleep 1
  done
  if bash -c "exec 3<>/dev/tcp/127.0.0.1/$GATEWAY_PORT" 2>/dev/null; then
    echo "gateway-service: still bound after 10s, force killing" >> gateway-service.log
    kill -9 $EXISTING_PID 2>/dev/null || true
    sleep 1
  fi
fi
exec java -jar gateway-service/target/*.jar --server.port=$GATEWAY_PORT --eureka.client.service-url.defaultZone=$RESOLVED_EUREKA_URL
