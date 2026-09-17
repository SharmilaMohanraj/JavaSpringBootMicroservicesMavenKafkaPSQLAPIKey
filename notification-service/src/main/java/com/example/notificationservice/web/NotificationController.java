package com.example.notificationservice.web;

import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.web.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@Tag(name = "Admin Notifications")
public class NotificationController {
  private final NotificationService service;

  public NotificationController(NotificationService service) {
    this.service = service;
  }

  @GetMapping("/unread")
  @Operation(summary = "List unread notifications")
  public List<NotificationResponse> unread() {
    return service.findUnread();
  }

  @PatchMapping("/{id}/read")
  @Operation(summary = "Mark a notification as read")
  public NotificationResponse markRead(@PathVariable UUID id) {
    return service.markRead(id);
  }
}
