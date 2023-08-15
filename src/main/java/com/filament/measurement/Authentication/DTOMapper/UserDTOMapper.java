package com.filament.measurement.Authentication.DTOMapper;

import com.filament.measurement.Authentication.DTO.UserDTO;
import com.filament.measurement.Authentication.Model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getFirstName())
                .surname(user.getLastName())
                .permission(user.getRole().name())
                .email(user.getEmail())
                .build();
    }
}
