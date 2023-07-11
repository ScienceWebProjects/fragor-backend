package com.filament.measurement.Service;

import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Form.*;
import com.filament.measurement.Model.Company;
import com.filament.measurement.Model.Token;
import com.filament.measurement.Model.User;
import com.filament.measurement.Permission.Role;
import com.filament.measurement.Permission.TokenType;
import com.filament.measurement.Repository.CompanyRepository;
import com.filament.measurement.Repository.TokenRepository;
import com.filament.measurement.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    public boolean userRegistration(UserRegistrationForm form){
        Company company = companyRepository.findByToken(form.getToken()).get();
        User user = User.builder()
                .email(form.getEmail())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(passwordEncoder.encode(form.getPassword()))
                .company(company)
                .role(Role.commonUser)
                .build();
        userRepository.save(user);
        return true;
    }

    public AuthenticationToken userLogin (UserLoginForm form){
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
        return AuthenticationToken.builder()
                .accessToken(jwt)
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
            if (user.getRole().name().equals("masterUser")){
                return;
            }
            if (user.getRole().name().equals("changerUser")){
                changerUser = "true";
            }
            data.put("username",user.getUsername());
            data.put("is_changer",changerUser);
            userPermissions.add(data);
        });
        return userPermissions;
    }

    public User changeUserPermissionsByMaster(ChangeUserPermissionsForm form, HttpServletRequest request) {
        User masterUser = jwtService.extractUser(request);
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() ->new NotFound404Exception("No found user"));
        isUserInCompany(masterUser,user);
        if (form.getChanger()) user.setRole(Role.changerUser);
        else user.setRole(Role.commonUser);
        userRepository.save(user);
        return user;
    }

    public User changeUserEmail( ChangeUserEmailForm form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        user.setEmail(form.getEmail());
        userRepository.save(user);
        return user;
    }
    public void changeUserPassword(ChangeUserPasswordForm form, HttpServletRequest request) {
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
