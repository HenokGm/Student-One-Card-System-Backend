package com.socs.workersManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/socs/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // Change password endpoint
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request, Principal connectedUser) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    // Add a new user using UserRequest DTO
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest) {
        User savedUser = service.addUser(userRequest);
        return ResponseEntity.ok(savedUser);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update user by ID using UserRequest DTO
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestParam("id") String id, @RequestBody UserRequest updatedUser) {
        User user = service.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // Delete a user by ID
    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@RequestParam("id") String id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Get user by ID
    @GetMapping("/get-user-by-id")
    public ResponseEntity<User> getUserById(@RequestParam("id") String id) {
        User user = service.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // get user by email
    @GetMapping("/get-user")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        User user = service.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
