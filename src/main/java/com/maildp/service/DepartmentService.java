package com.maildp.service;

import com.maildp.dto.request.DepartmentRequest;
import com.maildp.dto.response.DepartmentResponse;
import com.maildp.entity.Department;
import com.maildp.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department already exists: "
                + request.getName());
        }
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(departmentRepository.save(department));
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + id));
        return toResponse(department);
    }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + id));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return toResponse(departmentRepository.save(department));
    }

    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + id));
        departmentRepository.delete(department);
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .build();
    }
}