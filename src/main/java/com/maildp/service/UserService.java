package com.maildp.service;

import com.maildp.dto.response.UserResponse;
import com.maildp.entity.Department;
import com.maildp.entity.User;
import com.maildp.enums.Role;
import com.maildp.repository.DepartmentRepository;
import com.maildp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found: " + id));
        return toResponse(user);
    }

    public UserResponse updateUser(Long id, UserResponse request) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found: " + id));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole()));
        }
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                    new RuntimeException("Department not found"));
            user.setDepartment(department);
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found: " + id));
        userRepository.delete(user);
    }

    public UserResponse toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found: " + id));
        user.setIsActive(!user.getIsActive());
        return toResponse(userRepository.save(user));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .departmentName(user.getDepartment() != null
                    ? user.getDepartment().getName() : null)
                .departmentId(user.getDepartment() != null
                    ? user.getDepartment().getId() : null)
                .isActive(user.getIsActive())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }
}