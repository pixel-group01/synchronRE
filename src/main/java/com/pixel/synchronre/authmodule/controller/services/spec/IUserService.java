package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.appuser.LoginDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.*;
import com.pixel.synchronre.authmodule.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IUserService
{
    AuthResponseDTO login(LoginDTO dto) throws UnknownHostException;
    AuthResponseDTO refreshToken() throws UnknownHostException;
    void logout() throws UnknownHostException;
    ReadUserDTO createUser(CreateUserDTO dto) throws IllegalAccessException, UnknownHostException;
    ReadUserDTO updateUser(UpdateUserDTO dto) throws UnknownHostException;
    ReadUserDTO changePassword(ChangePasswordDTO dto) throws UnknownHostException;
    void blockAccount(Long userId) throws UnknownHostException;
    void unBlockAccount(Long userId) throws UnknownHostException;
    ReadUserDTO activateAccount(ActivateAccountDTO dto) throws UnknownHostException;

    ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto) throws UnknownHostException;

    ReadUserDTO findByUsername(String username);

    ReadUserDTO findByEmail(String email);

    ReadUserDTO findByTel(String tel);

    void sendAccountActivationEmail(String email) throws IllegalAccessException, UnknownHostException;
    void sendAccountActivationEmail(Long userId) throws IllegalAccessException, UnknownHostException;

    //void resendAccountActivationEmail(String username, String email) throws IllegalAccessException;

    void sendReinitialisePasswordEmail(String email) throws IllegalAccessException, UnknownHostException;

    void clickLink(String token) throws UnknownHostException;

    UserStatus getUserStatus(Long userId);

    Page<ListUserDTO> searchUsers(String key, List<String> userStaCode, Pageable pageable);

    ReadUserDTO createUserAndFunction(CreaterUserAndFunctionDTO dto) throws UnknownHostException, IllegalAccessException;



}
