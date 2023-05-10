package com.ugive.services.impl;

import com.ugive.dto.UserDto;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.exceptions.UserValidationException;
import com.ugive.mappers.UserMapper;
import com.ugive.models.Role;
import com.ugive.models.User;
import com.ugive.repositories.RoleRepository;
import com.ugive.repositories.UserRepository;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private Pattern pattern;
    private Matcher matcher;

    @Override
    @Transactional
    public Optional<User> create(UserDto userDto) {
        validationCheck(userDto);
        User user = userMapper.toEntity(userDto);
        Role userRole = roleRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist")); // получаем объект Role из БД
        user.getRoles().add(userRole);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> update(Long id, UserDto userDto) {
        validationCheck(userDto);
        User user = userCheck(id);
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new ForbiddenChangeException("User is deleted");
        }
        userMapper.updateEntityFromDto(userDto, user);
        return Optional.of(userRepository.save(user));
    }

    private void validationCheck(UserDto userDto) {
        if (!nameValidation(userDto.getUserName()) || !nameValidation(userDto.getSurname())) {
            throw new UserValidationException("Try another name. Example: Jack, Katerine.");
        } else if (!emailValidation(userDto.getEmail())) {
            throw new UserValidationException("This e-mail doesn't exist!");
        } else if (!phoneValidation(userDto.getPhone())) {
            throw new UserValidationException("This phone doesn't exist!");
        } else if (!passwordValidation(userDto.getPassword())) {
            throw new UserValidationException("The password must be at least 6 characters (maximum 20 characters)" +
                    " and contains numbers, lowercase and uppercase letters.");
        } else if (!loginValidation(userDto.getLogin())) {
            throw new UserValidationException("Try another login with numbers, letters, \"_\" and \"-\"");
        }
    }

    public Optional<User> setUserRole(Long userId, String roleName) {
        User user = userCheck(userId);
        Role userRole = roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        user.getRoles().add(userRole);
        return Optional.of(userRepository.save(user));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable("users")
    public List<User> findAll(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size));
        return usersPage.stream().toList();
    }

    public User findOne(Long id) {
        User user = userCheck(id);
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new ForbiddenChangeException("This account is deleted");
        }
        return user;
    }

    public void softDelete(Long id) {
        User user = userCheck(id);
        user.setIsDeleted(true);
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
    }

    public Optional<User> resetAccount(Long id) {
        User user = userCheck(id);
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            user.setIsDeleted(false);
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        }
        return Optional.of(userRepository.save(user));
    }

    @Scheduled(cron = "0 0 0 * * *") // Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteExpiredUsers() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        userRepository.deleteExpiredUsers(expirationDate);
    }

    private User userCheck(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));
    }

    public boolean nameValidation(String name) {
        final String NAME_PATTERN = "^[A-Z][a-z]*[A-Z]?[a-z]*$";
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public boolean emailValidation(String email) {
        final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean phoneValidation(String phone) {
        final String PHONE_PATTERN = "^(375|80)(29|25|33|44)(\\d{3})(\\d{2})(\\d{2})$";
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean passwordValidation(String password) {
        final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean loginValidation(String login) {
        final String LOGIN_VALIDATION = "^[a-zA-Z0-9_-]{3,16}$";
        pattern = Pattern.compile(LOGIN_VALIDATION);
        matcher = pattern.matcher(login);
        return matcher.matches();
    }
}
