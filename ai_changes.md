COMMIT_MESSAGE: Add return processing, inventory restoration, refunds, and monthly reporting

## Features Added
- Added delivered-order return requests, inspection, approval, and rejection.
- Approval restores inventory and creates a refund payment record.
- Added return status tracking, refund completion, and monthly refund reporting.
- Added offset/limit customer return tracking.

## Files Modified
- inventory-service/src/main/java/com/example/inventoryservice/service/InventoryItemService.java — quantity restoration.
- inventory-service/src/main/java/com/example/inventoryservice/web/InventoryItemController.java — restoration endpoint.
- order-service/src/main/resources/application.yml — inventory service URL setting.

## Files Added
- order-service/src/main/java/com/example/orderservice/domain/Customer.java — customer entity.
- order-service/src/main/java/com/example/orderservice/domain/WarehouseStaff.java — warehouse staff entity.
- order-service/src/main/java/com/example/orderservice/domain/ReturnRequest.java — return request entity.
- order-service/src/main/java/com/example/orderservice/domain/ReturnStatus.java — lifecycle statuses.
- order-service/src/main/java/com/example/orderservice/domain/RefundPayment.java — refund entity.
- order-service/src/main/java/com/example/orderservice/repository/ReturnRequestRepository.java — return persistence.
- order-service/src/main/java/com/example/orderservice/repository/RefundPaymentRepository.java — refund persistence.
- order-service/src/main/java/com/example/orderservice/service/ReturnRequestService.java — return workflow.
- order-service/src/main/java/com/example/orderservice/web/ReturnRequestController.java — return APIs.
- order-service/src/main/java/com/example/orderservice/web/dto/ReturnRequestDtos.java — validated DTOs.

## Secrets Moved
- None — no hardcoded secrets were found in Java source.

## DB URLs Resolved
- jdbc:postgresql://localhost:5432/gen_bed2b8c0a428 -> jdbc:postgresql://localhost:5432/gen_bed2b8c0a428

## Compilation Result
- PASSED — mvn compile -q passed immediately after all feature edits. Final package could not complete because the shared filesystem became full during Spring Boot repackaging.
