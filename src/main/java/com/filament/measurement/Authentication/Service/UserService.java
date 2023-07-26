package com.filament.measurement.Authentication.Service;

import com.filament.measurement.Authentication.DTO.AuthenticationTokenDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    private final JwtService jwtService;
    @Autowired
    private final TokenRepository tokenRepository;

    public boolean userRegistration(UserRegistrationRequest form){
        Company company = companyRepository.findByToken(form.getToken()).get();
        Role role = Role.COMMON_USER;

        if(company.getName().equals("FraGor")) role = Role.OWNER;

        User user = User.builder()
                .email(form.getEmail())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(passwordEncoder.encode(form.getPassword()))
                .company(company)
                .role(role)
                .build();
        userRepository.save(user);
        return true;
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
        return AuthenticationTokenDTO.builder()
                .token(jwt)
                .permission(user.getRole().name())
                .build();
    }
    public void deleteUserByMaster(String email, HttpServletRequest request){
        User masterUser = jwtService.extractUser(request);
        User deleteUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFound404Exception("No found user by email"));
        isUserInCompany(masterUser,deleteUser);
        userRepository.deleteById(deleteUser.getId());
    }



    public ArrayList<HashMap<String,String>> getUsersPermissions(HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        List<User> companyUsers = masterUser.getCompany().getUsers();
        ArrayList<HashMap<String,String>> userPermissions = new ArrayList<>();
        companyUsers.forEach(user -> {
            String changerUser = "false";
            HashMap<String,String> data = new HashMap<>();
            if (user.getRole().name().equals("MASTER_USER")){
                return;
            }
            if (user.getRole().name().equals("CHANGER_USER")){
                changerUser = "true";
            }
            data.put("username",user.getUsername());
            data.put("changer",changerUser);
            userPermissions.add(data);
        });
        return userPermissions;
    }

    public User changeUserPermissionsByMaster(ChangeUserPermissionsRequest form, HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() ->new NotFound404Exception("No found user"));
        isUserInCompany(masterUser,user);
        if (form.getChanger()) user.setRole(Role.CHANGER_USER);
        else user.setRole(Role.COMMON_USER);
        userRepository.save(user);
        return user;
    }

    public User changeUserEmail(ChangeUserEmailRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        user.setEmail(form.getEmail());
        userRepository.save(user);
        return user;
    }
    public void changeUserPassword(ChangeUserPasswordRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        if (passwordEncoder.matches(form.getOldPassword(),user.getPassword())){
            if (form.getPassword().equals(form.getPassword2())) {
                user.setPassword(passwordEncoder.encode(form.getPassword()));
                userRepository.save(user);
                return;
            }
            throw new CustomValidationException("Password don't match");
        }
        throw new CustomValidationException("Wrong old password");
    }

    private void isUserInCompany(User masterUser,User user){
        if (masterUser.getCompany().equals(user.getCompany())) {
            return;
        }
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
