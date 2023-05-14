package com.ugive.mappers;

import com.ugive.dto.UserBalanceDto;
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

    public UserBalance toEntity(UserBalanceDto userBalanceDto) {
        UserBalance userBalance = modelMapper.map(userBalanceDto, UserBalance.class);
        userBalance.setUser(userService.findOne(userBalanceDto.getPerson()));
        return userBalance;
    }

    public UserBalanceDto toDto(UserBalance userBalance) {
        UserBalanceDto userBalanceDto = new UserBalanceDto();
        userBalanceDto.setPerson(userBalance.getUser().getId());
        userBalanceDto.setMoney(userBalance.getBalance());
        return userBalanceDto;
    }

    public void updateEntityFromDto(UserBalanceDto userBalanceDto, UserBalance userBalance) {
        if (userBalanceDto.getPerson() != null) {
            userBalance.setUser(userService.findOne(userBalanceDto.getPerson()));
        }
        if (userBalanceDto.getMoney() != null) {
            userBalance.setBalance(userBalanceDto.getMoney());
        }
    }
}
