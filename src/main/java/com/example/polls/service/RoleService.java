package com.example.polls.service;

import com.example.polls.model.Role;
import com.example.polls.model.RoleType;

import java.util.Optional;

public interface RoleService {

    Role findByType(RoleType roleType);

    Optional<Role> fetchByType(RoleType roleType);
}
