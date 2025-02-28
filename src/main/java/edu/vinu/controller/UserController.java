package edu.vinu.controller;

import edu.vinu.dto.UserDto;
import edu.vinu.exception.AlreadyExistsException;
import edu.vinu.exception.ResourceNotFoundException;
import edu.vinu.model.User;
import edu.vinu.request.CreateUserRequest;
import edu.vinu.request.UpdateUserRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto=userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success!",userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto=userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!",userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request,userId);
            UserDto userDto=userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!",userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!",null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
