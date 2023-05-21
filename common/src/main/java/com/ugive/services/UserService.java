package com.ugive.services;

import com.ugive.models.PurchaseOffer;
import com.ugive.requests.UserRequest;
import com.ugive.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(UserRequest userDto);

    User update(Long id, UserRequest userDto);

    Optional<User> setUserRole(Long userId, String roleName);

    List<User> findAll(int page, int size);
    List<User> findAll();

    User findOne(Long id);

    void softDelete(Long id);

    User resetAccount(Long id);
}
