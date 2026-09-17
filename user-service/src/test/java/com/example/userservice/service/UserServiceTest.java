package com.example.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.userservice.domain.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.web.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UserServiceTest {
  @Test
  void createMapsRequestAndSavesUser() {
    UserRepository repository = mock(UserRepository.class);
    when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    var response =
        new UserService(repository).create(new UserRequest("person@example.test", "Person", true));
    ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
    verify(repository).save(user.capture());
    assertEquals("person@example.test", user.getValue().getEmail());
    assertEquals("Person", response.displayName());
  }
}
