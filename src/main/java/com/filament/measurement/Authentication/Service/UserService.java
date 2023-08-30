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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final AuthenticationTokenDTOMapper authenticationTokenDTOMapper;
    private final UserPermissionDTOMapper userPermissionDTOMapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    @Autowired
    public UserService(
            JwtService jwtService,
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            CompanyRepository companyRepository,
            UserPermissionDTOMapper userPermissionDTOMapper,
            AuthenticationTokenDTOMapper authenticationTokenDTOMapper
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.userPermissionDTOMapper = userPermissionDTOMapper;
        this.authenticationTokenDTOMapper = authenticationTokenDTOMapper;
    }

    public void userRegistration(UserRegistrationRequest form){
        Company company = companyRepository.findByToken(form.getToken()).orElseThrow();
        Role role = Role.COMMON_USER;
        if(company.getName().equals("FraGor")) role = Role.OWNER;
        else if(company.getUsers().isEmpty()) role = Role.MASTER_USER;
        saveUserIntoDb(form, company, role);
    }

    public AuthenticationTokenDTO userLoginViaPassword(UserLoginRequest form){
        User user = authenticateUserViaPassword(form);
        String jwt = jwtService.generateToken(user);
        createUserToken(user,jwt);
        return authenticationTokenDTOMapper.apply(jwt,user.getRole());
    }

    public AuthenticationTokenDTO userLoginViaPin(UserLoginRequest form) {
        User user = authenticateUserViaPin(form);
        String jwt = jwtService.generateToken(user);
        createUserToken(user,jwt);
        return authenticationTokenDTOMapper.apply(jwt,user.getRole());
    }

    public void userLogout(HttpServletRequest request){
        String userToken = request.getHeader("Authorization").substring(7);
        Token token = tokenRepository.findByToken(userToken).get();
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
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
                .filter(user -> !user.getRole().equals(Role.MASTER_USER))
                .filter(user -> !user.getRole().equals(Role.OWNER))
                .map(userPermissionDTOMapper)
                .collect(Collectors.toList());
    }

    public void changeUserPermissionsByMaster(ChangeUserPermissionsRequest form, HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() ->new NotFound404Exception("User not found"));
        isUserInCompany(masterUser,user);
        String userRole = user.getRole().name();
        if (form.getChanger() && userRole.equals("COMMON_USER")) user.setRole(Role.CHANGER_USER);
        else if(!form.getChanger() && userRole.equals("CHANGER_USER")) user.setRole(Role.COMMON_USER);
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
    public void changeUserPin(ChangeUserPinRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        if(!passwordEncoder.matches(form.getPassword(),user.getPassword()))
            throw new CustomValidationException("Wrong password");
        user.setPin(passwordEncoder.encode(form.getPin()));
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

    private void createUserToken(User user, String jwt) {
        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .revoked(false)
                .build();
        revokeAllUserTokens(user);
        tokenRepository.save(token);
    }

    private User getUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) throw new CustomValidationException("Login incorrect");
        return user.get();
    }

    private User authenticateUserViaPassword(UserLoginRequest form) {
        User user = getUser(form.getEmail());
        if(!passwordEncoder.matches(form.getPassword(), user.getPassword()))
            throw new CustomValidationException("Password incorrect");
        return user;
    }

    private User authenticateUserViaPin(UserLoginRequest form) {
        User user = getUser(form.getEmail());
        if(!passwordEncoder.matches(form.getPin().toString(), user.getPin()))
            throw new CustomValidationException("Pin incorrect");
        return user;
    }

    private void saveUserIntoDb(UserRegistrationRequest form, Company company, Role role) {
        User user = User.builder()
                .email(form.getEmail())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(passwordEncoder.encode(form.getPassword()))
                .pin(passwordEncoder.encode(form.getPin()))
                .company(company)
                .role(role)
                .build();
        userRepository.save(user);
    }
}
