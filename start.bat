@echo off
setlocal
set ADMIN_API_KEY=%ADMIN_API_KEY%
if "%ADMIN_API_KEY%"=="" set ADMIN_API_KEY=local-admin-key
start /B java -jar catalog-service\target\catalog-service.jar --server.port=29838
start /B java -jar inventory-service\target\inventory-service.jar --server.port=22482
start /B java -jar cart-service\target\cart-service.jar --server.port=21954
start /B java -jar order-service\target\order-service.jar --server.port=22354
start /B java -jar user-service\target\user-service.jar --server.port=26642
java -jar gateway-service\target\gateway-service.jar --server.port=29875
