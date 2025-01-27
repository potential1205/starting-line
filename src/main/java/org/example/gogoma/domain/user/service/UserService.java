package org.example.gogoma.domain.user.service;

import org.example.gogoma.controller.response.UserListResponse;
import org.example.gogoma.controller.response.UserResponse;


public interface UserService {

    UserResponse getUserById(int id);

    UserListResponse getAllUsers();

    void deleteUserById(int id);
}
