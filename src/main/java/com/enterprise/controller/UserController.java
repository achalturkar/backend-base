package com.enterprise.controller;

import com.enterprise.dto.ApiResponse;
import com.enterprise.dto.PageResponse;
import com.enterprise.dto.UserDto;
import com.enterprise.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * User Management Controller
 * 
 * @author Enterprise Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User management endpoints")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @Operation(summary = "Create new user", description = "Create a new user in the system")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Creating new user with email: {}", userDto.getEmail());
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdUser, "User created successfully"));
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable String userId) {
        log.info("Fetching user with id: {}", userId);
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Get user by email", description = "Retrieve user details by email")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user with email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination")
    public ResponseEntity<ApiResponse<PageResponse<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Fetching all users - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResponse<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Search users", description = "Search users by username, email, or name")
    public ResponseEntity<ApiResponse<PageResponse<UserDto>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Searching users with query: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResponse<UserDto> users = userService.searchUsers(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Search completed successfully"));
    }
    
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Update user", description = "Update user details")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserDto userDto) {
        log.info("Updating user with id: {}", userId);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }
    
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @Operation(summary = "Delete user", description = "Delete a user from the system")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with id: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
    
    @PostMapping("/{userId}/assign-roles")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Assign roles to user", description = "Assign multiple roles to a user")
    public ResponseEntity<ApiResponse<Void>> assignRolesToUser(
            @PathVariable String userId,
            @RequestBody List<String> roleIds) {
        log.info("Assigning roles to user with id: {}", userId);
        userService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Roles assigned successfully"));
    }
    
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Get user roles", description = "Retrieve all roles assigned to a user")
    public ResponseEntity<ApiResponse<List<String>>> getUserRoles(@PathVariable String userId) {
        log.info("Fetching roles for user with id: {}", userId);
        List<String> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(roles, "User roles retrieved successfully"));
    }
    
    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasAuthority('USER_READ')")
    @Operation(summary = "Get user permissions", description = "Retrieve all permissions for a user")
    public ResponseEntity<ApiResponse<List<String>>> getUserPermissions(@PathVariable String userId) {
        log.info("Fetching permissions for user with id: {}", userId);
        List<String> permissions = userService.getUserPermissions(userId);
        return ResponseEntity.ok(ApiResponse.success(permissions, "User permissions retrieved successfully"));
    }
}
