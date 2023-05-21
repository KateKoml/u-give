package com.ugive.controllers;

import com.ugive.models.Role;
import com.ugive.models.User;
import com.ugive.requests.RoleRequest;
import com.ugive.services.RoleService;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin")
@RequiredArgsConstructor
public class AdminController {
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> findAll() {
        List<Role> roles = roleService.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
        Role role = roleService.findOne(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody RoleRequest roleRequest) {
        Role createdRole = roleService.create(roleRequest);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Integer id,
                                           @RequestBody RoleRequest roleRequest) {
        Role updatedRole = roleService.update(id, roleRequest);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Integer id) {
        roleService.delete(id);
        return new ResponseEntity<>("Role was deleted", HttpStatus.OK);
    }

    @PostMapping("/roles/set/users/{userId}")
    public ResponseEntity<User> setUserRole(@PathVariable Long userId, @RequestParam String roleName) {
        Optional<User> user = userService.setUserRole(userId, roleName);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
