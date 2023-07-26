package com.filament.measurement.Authentication.Controller;

import com.filament.measurement.Authentication.DTO.AuthenticationTokenDTO;
import com.filament.measurement.Authentication.Request.*;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Authentication.Repository.UserRepository;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Authentication.Service.UserService;
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
    public ResponseEntity<Boolean> createUser(@Valid @RequestBody UserRegistrationRequest form){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.userRegistration(form));
    }

    @PostMapping("login/")
    public ResponseEntity<AuthenticationTokenDTO> loginUser(@RequestBody UserLoginRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(service.userLogin(form));
    }
    @DeleteMapping("delete/{email}")
    @PreAuthorize("hasAuthority('master:delete')")
    public ResponseEntity<Boolean> deleteUserByMaster(@PathVariable String email, HttpServletRequest request){
        service.deleteUserByMaster(email,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
    }
    @GetMapping("permission/")
    @PreAuthorize("hasAuthority('master:get')")
    public ResponseEntity<ArrayList<HashMap<String,String>>> getUsersPermissions(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsersPermissions(request));
    }
    @PutMapping("permission/set/")
    @PreAuthorize("hasAuthority('master:update')")
    public ResponseEntity<User> changePermission(
            @RequestBody ChangeUserPermissionsRequest form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.changeUserPermissionsByMaster(form,request));
    }
    @PutMapping("settings/email/")
    public ResponseEntity<User> changeUserEmailByMaster(
            @Valid
            @RequestBody ChangeUserEmailRequest form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.changeUserEmail(form,request));
    }
    @PutMapping("settings/password/")
    public ResponseEntity<Boolean> changeUserPassword(
            @Valid
            @RequestBody ChangeUserPasswordRequest form,
            HttpServletRequest request
    ){
        service.changeUserPassword(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
