package edu.vinu.service.user;

import edu.vinu.dto.UserDto;
import edu.vinu.model.User;
import edu.vinu.request.CreateUserRequest;
import edu.vinu.request.UpdateUserRequest;

public interface UserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToDto(User user);
}
