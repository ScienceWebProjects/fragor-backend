package com.filament.measurement.Authentication.Service;

import com.filament.measurement.Authentication.DTO.AuthenticationTokenDTO;
import com.filament.measurement.Authentication.DTO.UserPermissionDTO;
import com.filament.measurement.Authentication.DTOMapper.AuthenticationTokenDTOMapper;
import com.filament.measurement.Authentication.DTOMapper.UserPermissionDTOMapper;
import com.filament.measurement.Authentication.Request.*;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Model.Token;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Permission.Role;
import com.filament.measurement.Authentication.Permission.TokenType;
import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Authentication.Repository.TokenRepository;
import com.filament.measurement.Authentication.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final CompanyRepository companyRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final AuthenticationTokenDTOMapper authenticationTokenDTOMapper;
    @Autowired
    private final UserPermissionDTOMapper userPermissionDTOMapper;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final TokenRepository tokenRepository;

    public void userRegistration(UserRegistrationRequest form){
        Company company = companyRepository.findByToken(form.getToken()).orElseThrow();
        Role role = Role.COMMON_USER;

        if(company.getName().equals("FraGorrrrrrrrr")) role = Role.OWNER;

        User user = User.builder()
                .email(form.getEmail())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(passwordEncoder.encode(form.getPassword()))
                .company(company)
                .role(role)
                .build();
        userRepository.save(user);
    }
    public AuthenticationTokenDTO userLogin (UserLoginRequest form){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(),
                        form.getPassword()
                )
        );
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow();
        String jwt = jwtService.generateToken(user);
        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .revoked(false)
                .build();
        revokeAllUserTokens(user);
        tokenRepository.save(token);
        return authenticationTokenDTOMapper.apply(jwt,user.getRole());
    }
    public void deleteUserByMaster(String email, HttpServletRequest request){
        User masterUser = jwtService.extractUser(request);
        User deleteUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFound404Exception("No found user by email"));
        isUserInCompany(masterUser,deleteUser);
        userRepository.deleteById(deleteUser.getId());
    }
    public List<UserPermissionDTO> getUsersPermissions(HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        List<User> companyUsers = masterUser.getCompany().getUsers();
        return companyUsers.stream()
                .map(userPermissionDTOMapper)
                .collect(Collectors.toList());
    }
    public void changeUserPermissionsByMaster(ChangeUserPermissionsRequest form, HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() ->new NotFound404Exception("No found user"));
        isUserInCompany(masterUser,user);
        if (form.getChanger()) user.setRole(Role.CHANGER_USER);
        else user.setRole(Role.COMMON_USER);
        userRepository.save(user);
    }
    public void changeUserEmail(ChangeUserEmailRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        user.setEmail(form.getEmail());
        userRepository.save(user);
    }
    public void changeUserPassword(ChangeUserPasswordRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);

        if (!passwordEncoder.matches(form.getOldPassword(),user.getPassword()))
            throw new CustomValidationException("Wrong old password");

        if (!form.getPassword().equals(form.getPassword2()))
            throw new CustomValidationException("Password don't match");

        user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user);
    }
    private void isUserInCompany(User masterUser,User user){
        if (!masterUser.getCompany().equals(user.getCompany()))
            throw new NotFound404Exception("Big brother watching you");
    }
    private void revokeAllUserTokens(User user){
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validTokens.isEmpty()) return;
        validTokens.forEach( token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }



}
