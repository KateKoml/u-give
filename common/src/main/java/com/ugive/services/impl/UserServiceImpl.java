package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.UserMapper;
import com.ugive.models.PurchaseOffer;
import com.ugive.models.Role;
import com.ugive.models.User;
import com.ugive.models.UserBalance;
import com.ugive.models.enums.Gender;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.RoleRepository;
import com.ugive.repositories.UserBalanceRepository;
import com.ugive.repositories.UserRepository;
import com.ugive.requests.UserRequest;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PurchaseOfferRepository offerRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User create(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        Role userRole = roleRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        UserBalance userBalance = new UserBalance();
        userBalance.setUser(user);
        userBalance.setBalance(BigDecimal.ZERO);
        userBalanceRepository.save(userBalance);
        return user;
    }

    @Override
    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = userCheck(id);
        userMapper.updateEntityFromRequest(userRequest, user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> setUserRole(Long userId, String roleName) {
        User user = userCheck(userId);
        Role userRole = roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        user.getRoles().add(userRole);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public List<User> findAll() {
        Sort sort = Sort.by("id").ascending();
        return userRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
        return usersPage.getContent()
                .stream()
                .filter(user -> !user.isDeleted())
                .toList();
    }

    @Override
    public User findOne(Long id) {
        return userCheck(id);
    }

    @Override
    public List<User> searchByNameSurnameGender(String name, String surname, Gender gender, String phone) {
        return userRepository.searchByNameSurnameGender(name, surname, gender, phone);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        User user = userCheck(id);
        user.setDeleted(true);
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);

        UserBalance userBalance = userBalanceRepository.findByUserId(id);
        userBalance.setDeleted(true);
        userBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        userBalanceRepository.save(userBalance);

        List<PurchaseOffer> offers = offerRepository.findBySellerId(id);
        for (PurchaseOffer offer : offers) {
            offer.setDeleted(true);
            offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            offerRepository.save(offer);
        }
    }

    @Override
    @Transactional
    public User resetAccount(Long id) {
        User user = userCheck(id);
        UserBalance userBalance = userBalanceRepository.findByUserId(id);

        if (user.isDeleted()) {
            user.setDeleted(false);
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));

            userBalance.setDeleted(false);
            userBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            userBalanceRepository.save(userBalance);

            List<PurchaseOffer> offers = offerRepository.findBySellerId(id);
            for (PurchaseOffer offer : offers) {
                offer.setDeleted(false);
                offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
                offerRepository.save(offer);
            }
        }
        return userRepository.save(user);
    }

    @Scheduled(cron = "0 0 0 * * *") // "0 0 0 * * *" Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteExpiredUsers() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        userRepository.deleteExpiredUsers(expirationDate);
    }

    private User userCheck(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));
        if (user.isDeleted()) {
            throw new ForbiddenChangeException("User is deleted");
        }
        return user;
    }
}
