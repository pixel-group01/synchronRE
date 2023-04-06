package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.model.dtos.LoginDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.*;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.controller.services.spec.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/gest-asso")
public class UserResource
{
    private final IUserService userService;
    private final IJwtService jwtService;

    @PostMapping(path = "/open/users/login")
    public AuthResponseDTO login(@RequestBody LoginDTO dto) throws UnknownHostException {
        return userService.login(dto);
    }

    @GetMapping(path = "/users/refresh-token")
    public AuthResponseDTO refreshToken() throws UnknownHostException {
        return userService.refreshToken();
    }

    @GetMapping(path = "/users/logout")
    public void logout() throws UnknownHostException {
        userService.logout();
    }

    @PostMapping(path = "/users/create-user")
    public ReadUserDTO createUser(@RequestBody @Valid CreateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.createUser(dto);
    }

    @PostMapping(path = "/users/activate-user-account")
    public ReadUserDTO activateUserAccount(@RequestBody @Valid ActivateAccountDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.activateAccount(dto);
    }

    @PostMapping(path = "/users/update-user")
    public ReadUserDTO updateUser(@RequestBody @Valid UpdateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.updateUser(dto);
    }

    @PostMapping(path = "/users/change-password")
    public ReadUserDTO changePassword(@RequestBody @Valid ChangePasswordDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.changePassword(dto);
    }

    @PostMapping(path = "/users/change-block/{userId}")
    public void blockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.blockAccount(userId);
    }

    @PostMapping(path = "/users/change-unblock/{userId}")
    public void unblockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.unBlockAccount(userId);
    }

    @PostMapping(path = "/open/users/reinit-password")
    public void reinitPassword(@RequestBody @Valid ReinitialisePasswordDTO dto ) throws UnknownHostException, IllegalAccessException {
        userService.reinitialisePassword(dto);
    }

    @PostMapping(path = "/users/send-reinit-password-email/{email}")
    public void sendReinitPasswordEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendReinitialisePasswordEmail(email);
    }

    @PostMapping(path = "/users/send-acitivation-email/{email}")
    public void sendActivationEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendAccountActivationEmail(email);
    }

    @GetMapping(path = "/open/users/click-link/{token}")
    public void clickLink(@PathVariable @Valid String token ) throws UnknownHostException, IllegalAccessException {
        userService.clickLink(token);
    }
}