package com.example.polls.service.impl;

import com.example.polls.exception.AppException;
import com.example.polls.model.Role;
import com.example.polls.model.RoleType;
import com.example.polls.repository.RoleRepository;
import com.example.polls.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByType(RoleType roleType) {
        return fetchByType(roleType)
                .orElseThrow(() -> new AppException("User Role not set"));
    }

    @Override
    public Optional<Role> fetchByType(RoleType roleType) {
        return roleRepository.findByType(roleType);
    }
}
