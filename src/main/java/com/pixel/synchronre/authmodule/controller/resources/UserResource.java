package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.model.dtos.appuser.LoginDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.*;
import com.pixel.synchronre.logmodule.model.dtos.response.JwtInfos;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.controller.services.spec.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController @RequestMapping("/users") @ResponseStatus(HttpStatus.OK)
public class UserResource
{
    private final IUserService userService;
    private final IJwtService jwtService;

    @GetMapping(path = "/infos/{userId}")
    public ReadUserDTO getUserInfos(@PathVariable Long userId) throws UnknownHostException {
        return userService.getUserInfos(userId);
    }

    @PostMapping(path = "/open/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginDTO dto) throws UnknownHostException {
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

    @PostMapping(path = "/create-user-and-function")
    public ReadUserDTO createUserAndFunction(@RequestBody @Valid CreaterUserAndFunctionDTO dto) throws UnknownHostException, IllegalAccessException {
        return userService.createUserAndFunction(dto);
    }

    @GetMapping(path = "/list")
    public Page<ListUserDTO> searchUser(@RequestParam(defaultValue = "") String key,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) throws UnknownHostException, IllegalAccessException {
        return userService.searchUsers(key, new ArrayList<>(), PageRequest.of(page, size));
    }

    @PutMapping(path = "/open/activate-account")
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
    public boolean blockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.blockAccount(userId);
        return true;
    }

    @PutMapping(path = "/unblock/{userId}")
    public boolean unblockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        userService.unBlockAccount(userId);
        return true;
    }

    @PutMapping(path = "/open/reinit-password")
    public ReadUserDTO reinitPassword(@RequestBody @Valid ReinitialisePasswordDTO dto ) throws UnknownHostException, IllegalAccessException {
        return userService.reinitialisePassword(dto);
    }

    @PutMapping(path = "/open/send-reinit-password-email/{email}")
    public boolean sendReinitPasswordEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendReinitialisePasswordEmail(email);
        return true;
    }

    @PutMapping(path = "/open/send-acitivation-email/{email}")
    public boolean sendActivationEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        userService.sendAccountActivationEmail(email);
        return true;
    }

    @GetMapping(path = "/open/click-link/{token}")
    public boolean clickLink(@PathVariable @Valid String token ) throws UnknownHostException, IllegalAccessException {
        userService.clickLink(token);
        return true;
    }

    @GetMapping(path = "/token-introspection")
    public JwtInfos getJwtInfos() throws UnknownHostException, IllegalAccessException {
        return jwtService.getJwtInfos();
    }
}
