package com.filament.measurement.Authentication.DTOMapper;

import com.filament.measurement.Authentication.DTO.AuthenticationTokenDTO;
import com.filament.measurement.Authentication.Permission.Role;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;
@Service
public class AuthenticationTokenDTOMapper implements BiFunction<String, Role, AuthenticationTokenDTO> {

    @Override
    public AuthenticationTokenDTO apply(String token, Role role) {
        return new AuthenticationTokenDTO(
                token,
                role.name()
        );
    }
}
