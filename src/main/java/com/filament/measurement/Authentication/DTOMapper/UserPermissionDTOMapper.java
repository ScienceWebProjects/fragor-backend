package com.filament.measurement.Authentication.DTOMapper;

import com.filament.measurement.Authentication.DTO.UserPermissionDTO;
import com.filament.measurement.Authentication.Model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class UserPermissionDTOMapper implements Function<User, UserPermissionDTO> {
    @Override
    public UserPermissionDTO apply(User user) {
        boolean changer = !user.getRole().name().equals("COMMON_USER");
        return new UserPermissionDTO(
                user.getEmail(),
                changer
        );
    }
}
