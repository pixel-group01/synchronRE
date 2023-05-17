package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.model.dtos.LoginDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.*;
import com.pixel.synchronre.logmodule.model.dtos.response.JwtInfos;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.controller.services.spec.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/users")
public class UserResource
{
    private final IUserService userService;
    private final IJwtService jwtService;

    @PostMapping(path = "/open/login")
    public AuthResponseDTO login(@RequestBody LoginDTO dto) throws UnknownHostException {
        return userService.login(dto);
    }

    @GetMapping(path = "/refresh-token")
    public AuthResponseDTO refreshToken() throws UnknownHostException {
        return userService.refreshToken();
    }

    @GetMapping(path = "/logout")
    public void logout() throws UnknownHostException {
        userService.logout();
    }

    @PostMapping(path = "/create")
    public ReadUserDTO createUser(@RequestBody @Valid CreateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.createUser(dto);
    }

    @PutMapping(path = "/activate-account")
    public ReadUserDTO activateUserAccount(@RequestBody @Valid ActivateAccountDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.activateAccount(dto);
    }

    @PutMapping(path = "/update")
    public ReadUserDTO updateUser(@RequestBody @Valid UpdateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.updateUser(dto);
    }

    @PutMapping(path = "/change-password")
    public ReadUserDTO changePassword(@RequestBody @Valid ChangePasswordDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.changePassword(dto);
    }

    @PutMapping(path = "/block/{userId}")
    public void blockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.blockAccount(userId);
    }

    @PutMapping(path = "/unblock/{userId}")
    public void unblockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.unBlockAccount(userId);
    }

    @PutMapping(path = "/open/reinit-password")
    public void reinitPassword(@RequestBody @Valid ReinitialisePasswordDTO dto ) throws UnknownHostException, IllegalAccessException {
        userService.reinitialisePassword(dto);
    }

    @PutMapping(path = "/send-reinit-password-email/{email}")
    public void sendReinitPasswordEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendReinitialisePasswordEmail(email);
    }

    @PutMapping(path = "/send-acitivation-email/{email}")
    public void sendActivationEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendAccountActivationEmail(email);
    }

    @GetMapping(path = "/open/click-link/{token}")
    public void clickLink(@PathVariable @Valid String token ) throws UnknownHostException, IllegalAccessException {
        userService.clickLink(token);
    }

    @GetMapping(path = "/token-introspection")
    public JwtInfos getJwtInfos() throws UnknownHostException, IllegalAccessException {
        return jwtService.getJwtInfos();
    }
}
