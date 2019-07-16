package com.example.polls.service;

import com.example.polls.model.Role;
import com.example.polls.model.RoleType;

public interface RoleService {

    Role findByType(RoleType roleType);

}
