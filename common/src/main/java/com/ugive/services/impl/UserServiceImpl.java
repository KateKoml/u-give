package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.UserMapper;
import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
import com.ugive.models.Role;
import com.ugive.models.User;
import com.ugive.models.UserBalance;
import com.ugive.models.enums.Gender;
import com.ugive.repositories.FavouriteRepository;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.RoleRepository;
import com.ugive.repositories.UserBalanceRepository;
import com.ugive.repositories.UserRepository;
import com.ugive.requests.UserRequest;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.MappingException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FavouriteRepository favouriteRepository;
    private final PurchaseOfferRepository offerRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User create(UserRequest userRequest) {
        User user;
        try {
            user = userMapper.toEntity(userRequest);
        } catch (ForbiddenChangeException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }

        Role userRole = null;
        try {
            userRole = roleRepository.findByRoleName("USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        } catch (EntityNotFoundException e) {
            logger.error("This role doesn't exist. " + e.getMessage());
        }
        if(userRole != null) {
            user.getRoles().add(userRole);
        }
        userRepository.save(user);

        try {
            UserBalance userBalance = new UserBalance();
            userBalance.setUser(user);
            userBalance.setBalance(BigDecimal.ZERO);
            userBalanceRepository.save(userBalance);
        } catch (EntityNotFoundException e) {
            logger.error("User balance wasn't added. " + e.getMessage());
            throw new EntityNotFoundException(e.getMessage());
        }
        return user;
    }

    @Override
    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = userCheck(id);
        try {
            userMapper.updateEntityFromRequest(userRequest, user);
        } catch (ForbiddenChangeException e) {
        logger.error("Error updating user request to entity. " + e.getMessage());
        throw new ForbiddenChangeException(e.getMessage());
    }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> setUserRole(Long userId, String roleName) {
        User user = userCheck(userId);
        try {
            Role userRole = roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
            user.getRoles().add(userRole);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new EntityNotFoundException(e.getMessage());
        }
        return Optional.of(userRepository.save(user));
    }

    @Cacheable("users")
    @Override
    public List<User> findAll() {
        return userRepository.findAllByIsDeletedFalseOrderById();
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
    public List<User> searchByNameSurnameGenderPhone(String name, String surname, Gender gender, String phone) {
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

        List<Favourite> favourites = favouriteRepository.findAllByUserId(id);
        for (Favourite favourite : favourites) {
            favourite.setDeleted(true);
            favourite.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            favouriteRepository.save(favourite);
        }

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
        try {
            Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
            userRepository.deleteExpiredUsers(expirationDate);
        } catch (SchedulingException e) {
            logger.error("Scheduling doesn't work correctly. " + e.getMessage());
        }
    }

    private User userCheck(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));
        if (user.isDeleted()) {
            logger.error("User is deleted (isDeleted = true)");
            throw new ForbiddenChangeException("User is deleted");
        }
        return user;
    }
}
