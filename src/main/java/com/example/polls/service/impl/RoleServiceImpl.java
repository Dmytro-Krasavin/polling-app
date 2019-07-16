package com.example.polls.service.impl;

import com.example.polls.exception.response.AppException;
import com.example.polls.model.Role;
import com.example.polls.model.RoleType;
import com.example.polls.repository.RoleRepository;
import com.example.polls.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByType(RoleType roleType) {
        return roleRepository.findByType(roleType)
                .orElseThrow(() -> new AppException("User Role not set"));
    }
}
