package com.filament.measurement.Authentication.Controller;

import com.filament.measurement.Authentication.DTO.AuthenticationTokenDTO;
import com.filament.measurement.Authentication.DTO.UserPermissionDTO;
import com.filament.measurement.Authentication.Request.*;
import com.filament.measurement.Authentication.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/account/")
@CrossOrigin

public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("registration/")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRegistrationRequest form){
        service.userRegistration(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @PostMapping("login/password/")
    public ResponseEntity<AuthenticationTokenDTO> loginUserViaPassword(@RequestBody UserLoginRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(service.userLoginViaPassword(form));
    }
    @PostMapping("login/pin/")
    public ResponseEntity<AuthenticationTokenDTO> loginUserViaPin(@RequestBody UserLoginRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(service.userLoginViaPin(form));
    }

    @DeleteMapping("logout/")
    public ResponseEntity<Void> logoutUser (HttpServletRequest request) throws ServletException {
        service.userLogout(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("delete/{email}/")
    @PreAuthorize("hasAuthority('master:delete')")
    public ResponseEntity<Void> deleteUserByMaster(@PathVariable String email, HttpServletRequest request){
        service.deleteUserByMaster(email,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @GetMapping("permission/")
    @PreAuthorize("hasAuthority('master:get')")
    public ResponseEntity<List<UserPermissionDTO>> getUsersPermissions(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsersPermissions(request));
    }
    @PutMapping("permission/set/")
    @PreAuthorize("hasAuthority('master:update')")
    public ResponseEntity<Void> changePermission(
            @RequestBody ChangeUserPermissionsRequest form,
            HttpServletRequest request
    ){
        service.changeUserPermissionsByMaster(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PutMapping("settings/email/")
    public ResponseEntity<Void> changeUserEmailByMaster(
            @Valid
            @RequestBody ChangeUserEmailRequest form,
            HttpServletRequest request
    ){
        service.changeUserEmail(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PutMapping("settings/password/")
    public ResponseEntity<Void> changeUserPassword(
            @Valid
            @RequestBody ChangeUserPasswordRequest form,
            HttpServletRequest request
    ){
        service.changeUserPassword(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
