package com.socs.workersManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    private static final AtomicInteger managerCounter = new AtomicInteger(0);
    private static final AtomicInteger userCounter = new AtomicInteger(0);

    @jakarta.annotation.PostConstruct
    public void initializeCounters() {
        List<User> allUsers = repository.findAll();
        int totalUsers = repository.findAll().size();
        System.out.println("Total Users: " + totalUsers);
        int maxManagerId = getMaxId(allUsers, "mr") + totalUsers;
        int maxUserId = getMaxId(allUsers, "ur") + totalUsers;

        managerCounter.set(maxManagerId);
        userCounter.set(maxUserId);
    }

    private int getMaxId(List<User> users, String rolePrefix) {
        return users.stream()
                .map(User::getId)
                .filter(id -> id.startsWith(rolePrefix))
                .mapToInt(id -> Integer.parseInt(id.substring(rolePrefix.length(), rolePrefix.length() + 3)))
                .max().orElse(0);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    // Add new user using UserRequest DTO
    public User addUser(UserRequest userRequest) {
        repository.findByEmail(userRequest.getEmail()).ifPresent(existingUser -> {
            throw new IllegalStateException("User with this email already exists");
        });

        String rolePrefix = userRequest.getRole().name().endsWith("M") ? "mr" : "ur";
        String academicYear = String.valueOf(Year.now().getValue()).substring(2);

        int idNumber = rolePrefix.equals("mr")
                ? managerCounter.incrementAndGet()
                : userCounter.incrementAndGet();

        String customId = String.format("socs/%s%03d/%s", rolePrefix, idNumber, academicYear);

        User user = User.builder()
                .id(customId)
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .picture("C:\\Users\\HENOK\\Documents\\pic\\SOCSPIC\\" + userRequest.getPicture())
                .build();

        return repository.save(user);
    }

    // Update user by ID using UserRequest DTO
    public User updateUser(String userId, UserRequest updatedUser) {
        User existingUser = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());

        if (updatedUser.getPicture() != null && !updatedUser.getPicture().isEmpty()) {
            existingUser.setPicture("C:\\Users\\HENOK\\Documents\\pic\\SOCSPIC\\" + updatedUser.getPicture());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return repository.save(existingUser);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void deleteUserById(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        repository.delete(user);
    }

    // get user by ID
    public User getUserById(String userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    // get user by email
    public User getUserByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("User not found with email: " + email);
    }

}
