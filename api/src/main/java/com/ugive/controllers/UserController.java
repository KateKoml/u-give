package com.ugive.controllers;

import com.ugive.exceptions.ValidationCheckException;
import com.ugive.models.User;
import com.ugive.models.enums.Gender;
import com.ugive.requests.UserRequest;
import com.ugive.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
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
import java.util.Objects;

@RestController
@RequestMapping("rest/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Spring Data User Find All Search",
            description = "Find All Users without limits sorted by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Users",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))

                    ),
                    @ApiResponse(responseCode = "INTERVAL_SERVER_ERROR", description = "Internal Server Error")
            }
    )
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data User Search with Pageable Params",
            description = "Load page with Pageable Params page and size",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Users",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PageImpl.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @GetMapping("/paging")
    public ResponseEntity<List<User>> findAllByPage(
            @Parameter(name = "page", example = "0", required = true) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "size", example = "10") @RequestParam(value = "size", defaultValue = "10") int size) {
        List<User> users = userService.findAll(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data User Search by user Id",
            description = "Load user by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findOne(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Create User",
            description = "Create User with request body",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Successfully created User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserRequest.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationCheckException(errorMessage);
        }

        User createdUser = userService.create(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Spring Data Update User",
            description = "Update User based on given id and request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully updated User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @Valid @RequestBody UserRequest userRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationCheckException(errorMessage);
        }
        User updatedUser = userService.update(id, userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Delete User",
            description = "Delete User with possibility to restore within 30 days",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully deleted User",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.softDelete(id);
        return new ResponseEntity<>("Your account is deleted. You can restore it within 30 days.", HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Restore User",
            description = "Restore User if User have this possibility",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully restored User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @PutMapping("/{id}/restore")
    public ResponseEntity<User> restoreAccount(@PathVariable("id") Long id) {
        User user = userService.resetAccount(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data User Search by given params",
            description = "Search users by name, surname, gender or phone",
            parameters = {
                    @Parameter(name = "name",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Kate", type = "string", description = "user name")),
                    @Parameter(name = "surname",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Komleva", type = "string", description = "user surname")),
                    @Parameter(name = "gender",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "FEMALE", type = "Gender", implementation = Gender.class, description = "user gender")),
                    @Parameter(name = "phone",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "375295905041", type = "string", description = "user phone"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded users",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "gender", required = false) Gender gender,
            @RequestParam(value = "phone", required = false) String phone
    ) {
        List<User> users = userService.searchByNameSurnameGenderPhone(name, surname, gender, phone);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
