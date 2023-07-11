package com.filament.measurement.Controller;

import com.filament.measurement.Form.*;
import com.filament.measurement.Model.User;
import com.filament.measurement.Repository.CompanyRepository;
import com.filament.measurement.Repository.UserRepository;
import com.filament.measurement.Service.JwtService;
import com.filament.measurement.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("api/account/")
@CrossOrigin
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserService service;
    @Autowired
    JwtService jwtService;
    @PostMapping("registration/")
    public ResponseEntity<Boolean> createUser(@Valid @RequestBody UserRegistrationForm form){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.userRegistration(form));
    }

    @PostMapping("login/")
    public ResponseEntity<AuthenticationToken> loginUser(@RequestBody UserLoginForm form){
        return ResponseEntity.status(HttpStatus.OK).body(service.userLogin(form));
    }
    @DeleteMapping("delete/{email}")
    @PreAuthorize("hasRole('masterUser')")
    public ResponseEntity<Boolean> deleteUserByMaster(@PathVariable String email, HttpServletRequest request){
        service.deleteUserByMaster(email,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
    }
    @GetMapping("permission/")
    @PreAuthorize("hasRole('masterUser')")
    public ResponseEntity<ArrayList<HashMap<String,String>>> getUsersPermissions(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsersPermissions(request));
    }
    @PutMapping("permission/set/")
    @PreAuthorize("hasRole('masterUser')")
    public ResponseEntity<User> changePermission(
            @RequestBody ChangeUserPermissionsForm form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.changeUserPermissionsByMaster(form,request));
    }
    @PutMapping("settings/email/")
    public ResponseEntity<User> changeUserEmailByMaster(
            @Valid
            @RequestBody ChangeUserEmailForm form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.changeUserEmail(form,request));
    }
    @PutMapping("settings/password/")
    public ResponseEntity<Boolean> changeUserPassword(
            @Valid
            @RequestBody ChangeUserPasswordForm form,
            HttpServletRequest request
    ){
        service.changeUserPassword(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
