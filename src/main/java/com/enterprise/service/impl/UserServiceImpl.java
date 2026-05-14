package com.enterprise.service.impl;

import com.enterprise.dto.UserDto;
import com.enterprise.dto.PageResponse;
import com.enterprise.entity.Permission;
import com.enterprise.entity.Role;
import com.enterprise.entity.User;
import com.enterprise.exception.DuplicateEntryException;
import com.enterprise.exception.ResourceNotFoundException;
import com.enterprise.mapper.UserMapper;
import com.enterprise.repository.RoleRepository;
import com.enterprise.repository.UserRepository;
import com.enterprise.service.UserService;
import com.enterprise.util.AuditLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User Service Implementation
 * 
 * @author Enterprise Team
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuditLogUtil auditLogUtil;
    
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto.getUsername());
        
        // Check if user already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntryException("email", userDto.getEmail());
        }
        
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateEntryException("username", userDto.getUsername());
        }
        
        // Create user
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIsActive(true);
        user.setIsVerified(false);
        user.setFailedLoginAttempts(0);
        
        user = userRepository.save(user);
        
        // Assign roles
        if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
            Set<Role> roles = userDto.getRoleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            user = userRepository.save(user);
        }
        
        auditLogUtil.logAuditEvent("CREATE", "User", user.getId(), "SUCCESS", 
                "User created: " + user.getUsername());
        
        log.info("User created successfully: {}", user.getId());
        return userMapper.toDto(user);
    }
    
    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        log.info("Updating user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Check email uniqueness
        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntryException("email", userDto.getEmail());
        }
        
        // Check username uniqueness
        if (!user.getUsername().equals(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateEntryException("username", userDto.getUsername());
        }
        
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setIsActive(userDto.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());
        
        user = userRepository.save(user);
        
        auditLogUtil.logAuditEvent("UPDATE", "User", user.getId(), "SUCCESS", 
                "User updated: " + user.getUsername());
        
        log.info("User updated successfully: {}", user.getId());
        return userMapper.toDto(user);
    }
    
    @Override
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Soft delete
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        
        auditLogUtil.logAuditEvent("DELETE", "User", user.getId(), "SUCCESS", 
                "User deleted: " + user.getUsername());
        
        log.info("User deleted successfully: {}", user.getId());
    }
    
    @Override
    public UserDto getUserById(String userId) {
        log.debug("Fetching user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return userMapper.toDto(user);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return userMapper.toDto(user);
    }
    
    @Override
    public PageResponse<UserDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users");
        
        Page<User> page = userRepository.findAllActiveAndVerified(pageable);
        
        return PageResponse.<UserDto>builder()
                .content(page.getContent().stream().map(userMapper::toDto).collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .isFirst(page.isFirst())
                .isEmpty(page.isEmpty())
                .build();
    }
    
    @Override
    public PageResponse<UserDto> searchUsers(String search, Pageable pageable) {
        log.debug("Searching users with query: {}", search);
        
        Page<User> page = userRepository.searchUsers(search, pageable);
        
        return PageResponse.<UserDto>builder()
                .content(page.getContent().stream().map(userMapper::toDto).collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .isFirst(page.isFirst())
                .isEmpty(page.isEmpty())
                .build();
    }
    
    @Override
    public void assignRolesToUser(String userId, List<String> roleIds) {
        log.info("Assigning roles to user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)))
                .collect(Collectors.toSet());
        
        user.setRoles(roles);
        userRepository.save(user);
        
        auditLogUtil.logAuditEvent("ASSIGN_ROLES", "User", user.getId(), "SUCCESS", 
                "Roles assigned: " + roleIds.size());
        
        log.info("Roles assigned successfully to user: {}", user.getId());
    }
    
    @Override
    public List<String> getUserRoles(String userId) {
        log.debug("Getting roles for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return user.getRoles().stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getUserPermissions(String userId) {
        log.debug("Getting permissions for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getPermissionCode)
                .distinct()
                .collect(Collectors.toList());
    }
}
