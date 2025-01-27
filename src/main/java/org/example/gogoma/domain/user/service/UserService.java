package org.example.gogoma.domain.user.service;

import org.example.gogoma.controller.dto.UserListResponse;
import org.example.gogoma.controller.dto.UserResponse;


public interface UserService {

    UserResponse getUserById(int id);

    UserListResponse getAllUsers();

    void deleteUserById(int id);
}
