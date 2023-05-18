package com.ugive.mappers;

import com.ugive.dto.UserBalanceRequest;
import com.ugive.models.UserBalance;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserBalanceMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserBalance toEntity(UserBalanceRequest userBalanceRequest) {
        UserBalance userBalance = modelMapper.map(userBalanceRequest, UserBalance.class);
        userBalance.setUser(userService.findOne(userBalanceRequest.getPerson()));
        return userBalance;
    }

    public void updateEntityFromDto(UserBalanceRequest userBalanceRequest, UserBalance userBalance) {
        if (userBalanceRequest.getPerson() != null) {
            userBalance.setUser(userService.findOne(userBalanceRequest.getPerson()));
        }
        if (userBalanceRequest.getMoney() != null) {
            userBalance.setBalance(userBalanceRequest.getMoney());
        }
    }
}
