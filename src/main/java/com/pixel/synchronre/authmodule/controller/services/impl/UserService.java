package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.*;
import com.pixel.synchronre.authmodule.model.constants.AuthActions;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateInitialFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.ReadFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.LoginDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.*;
import com.pixel.synchronre.authmodule.model.entities.AccountToken;
import com.pixel.synchronre.authmodule.model.enums.UserStatus;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.authmodule.controller.repositories.AccountTokenRepo;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.model.constants.AuthTables;
import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import com.pixel.synchronre.authmodule.model.constants.SecurityErrorMsg;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.model.entities.Log;
import com.pixel.synchronre.notificationmodule.controller.dao.EmailNotificationRepo;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailServiceConfig;
import com.pixel.synchronre.notificationmodule.model.entities.EmailNotification;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCessionnaire;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class UserService implements IUserService
{
    private final UserRepo userRepo;
    private final IAccountTokenService accountTokenService;
    private final UserMapper userMapper;
    private final IJwtService jwtService;
    private final AccountTokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailServiceConfig emailServiceConfig;
    private final EmailNotificationRepo emailRepo;
    private final ILogService logger;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService uds;
    private final ObjectCopier<AppUser> userCopier;
    private final StatutRepository staRepo;
    private final IFunctionService functionService;
    private final CedRepo cedRepo;
    private final IserviceCessionnaire cesService;
    private final FunctionRepo functionRepo;

    @Override @Transactional
    public AuthResponseDTO login(LoginDTO dto) throws UnknownHostException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        UserDetails userDetails = uds.loadUserByUsername(dto.getUsername());
        Log log = logger.logLoginOrLogout(dto.getUsername(), AuthActions.LOGIN);
        return jwtService.generateJwt(userDetails, log.getConnectionId());
    }
    @Override
    public AuthResponseDTO refreshToken() throws UnknownHostException {
        String username = jwtService.extractUsername(); String conId = jwtService.extractConnectionId();
        UserDetails userDetails = uds.loadUserByUsername(username);
        Log log = logger.logLoginOrLogout(username, AuthActions.REFRESH_TOKEN, conId);
        return jwtService.generateJwt(userDetails, log.getConnectionId());
    }
    @Override
    public void logout() throws UnknownHostException
    {
        String username = jwtService.extractUsername();
        logger.logLoginOrLogout(username, AuthActions.LOGOUT);
    }

    @Override @Transactional
    public ReadUserDTO createUser(CreateUserDTO dto) throws IllegalAccessException, UnknownHostException {
        Long actorUserId = userRepo.getUserIdByEmail(jwtService.extractUsername());

        AppUser user = userMapper.mapToUser(dto);
        if(dto.getCedId() == null)
        {
            Cessionnaire nre = cesService.getCourtier();
            if(nre == null) throw new AppException("Impossible de trouver le courtier");
            user.setCesId(nre.getCesId());
        }
        //if(dto.getCesId() == null && dto.getVisibilityId() != null) user.setCesId(cedRepo.getCedanteCesId(dto.getVisibilityId()));
        user.setStatut(new Statut("USR-BLQ"));
        user = userRepo.save(user);
        logger.logg(AuthActions.CREATE_USER, null, user, AuthTables.USER_TABLE);
        AccountToken accountToken = new AccountToken(UUID.randomUUID().toString(), user);
        accountToken = tokenRepo.save(accountToken);

        EmailNotification emailNotification = new EmailNotification(user, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), actorUserId);
        emailSenderService.sendAccountActivationEmail(user.getEmail(), user.getFirstName(), emailServiceConfig.getActivateAccountLink() + "/" + accountToken.getToken());
        emailNotification.setSent(true);
        emailRepo.save(emailNotification);
        accountToken.setEmailSent(true);
        return userMapper.mapToReadUserDTO(user);
    }
    @Override @Transactional
    public ReadUserDTO activateAccount(ActivateAccountDTO dto) throws UnknownHostException
    {
        AppUser user = userRepo.findByEmail(dto.getEmail()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = new AppUser();BeanUtils.copyProperties(user, oldUser);
        user.setActive(true);
        user.setNotBlocked(true);
        user.setStatut(new Statut("USR-ACT"));
        //user.setEmail(dto.getEmail());
        user.setLastModificationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        logger.loggOffConnection(AuthActions.ACTIVATE_USER_ACCOUNT, dto.getEmail(),oldUser, user, AuthTables.USER_TABLE);
        AccountToken token = tokenRepo.findByToken(dto.getActivationToken()).orElseThrow(()->new AppException(SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG));
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        readUserDTO.setStatus(this.getUserStatus(user.getUserId()));
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO updateUser(UpdateUserDTO dto) throws UnknownHostException {
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = userCopier.copy(user); //new AppUser();BeanUtils.copyProperties(user, oldUser);
        Long odlVisibilityId = oldUser.getVisibilityId();
        if(odlVisibilityId != null && !odlVisibilityId.equals(dto.getCedId()))
        {
            user.setVisibilityId(dto.getCedId());
            functionRepo.findAllByUser(user.getUserId()).forEach(f->f.setVisibilityId(dto.getCedId()));
        }
        user.setVisibilityId(dto.getCedId());
        BeanUtils.copyProperties(dto, user);
        userRepo.save(user);
        logger.logg(AuthActions.UPDATE_USER, oldUser, user, AuthTables.USER_TABLE);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        readUserDTO.setStatus(this.getUserStatus(dto.getUserId()));
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO changePassword(ChangePasswordDTO dto) throws UnknownHostException
    {
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = userCopier.copy(user);
        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) throw new AppException("L'ancien mot de passe est incorrect");
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        logger.logg(AuthActions.CHANGE_PASSWORD, oldUser, user, AuthTables.USER_TABLE);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        readUserDTO.setStatus(this.getUserStatus(dto.getUserId()));
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto) throws UnknownHostException {
        AppUser user = userRepo.findByEmail(dto.getEmail()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = userCopier.copy(user);
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        logger.loggOffConnection(AuthActions.REINIT_PASSWORD, dto.getEmail(),oldUser, user, AuthTables.USER_TABLE);
        AccountToken token = tokenRepo.findByToken(dto.getPasswordReinitialisationToken()).orElseThrow(()->new AppException(SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG));

        AccountToken oldToken = new AccountToken();BeanUtils.copyProperties(token, oldToken);
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);
        logger.loggOffConnection(AuthActions.REINIT_PASSWORD, dto.getEmail(),oldToken, token, AuthTables.ACCOUNT_TOKEN);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        readUserDTO.setStatus(this.getUserStatus(user.getUserId()));
        return readUserDTO;
    }

    @Override @Transactional
    public void blockAccount(Long userId) throws UnknownHostException {
        AppUser user = userRepo.findById(userId).orElse(null);
        AppUser oldUser = new AppUser();BeanUtils.copyProperties(user, oldUser);
        if(user == null) return;
        if(!user.isNotBlocked()) return;
        user.setNotBlocked(false);
        user.setStatut(new Statut("USR-BLQ"));
        logger.logg(AuthActions.LOCK_USER_ACCOUNT, oldUser, user, AuthTables.USER_TABLE);
    }

    @Override @Transactional
    public void unBlockAccount(Long userId) throws UnknownHostException {
        AppUser user = userRepo.findById(userId).orElse(null);
        AppUser oldUser = new AppUser();BeanUtils.copyProperties(user, oldUser);
        if(user == null) return;
        if(user.isNotBlocked()) return;
        user.setNotBlocked(true);
        if(user.isActive()) user.setStatut(new Statut("USR-ACT"));

        logger.logg(AuthActions.UNLOCK_USER_ACCOUNT, oldUser, user, AuthTables.USER_TABLE);
    }

    @Override
    public ReadUserDTO findByUsername(String username)
    {
        AppUser user = userRepo.findByEmail(username).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByEmail(String email)
    {
        AppUser user = userRepo.findByEmail(email).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByTel(String tel)
    {
        AppUser user = userRepo.findByTel(tel).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public void sendAccountActivationEmail(String email) throws IllegalAccessException, UnknownHostException {
        boolean isNotLoggedIn = jwtService.getCurrentJwt() == null;
        AppUser loadedUser = this.userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        String username = loadedUser.getFirstName();
        //if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        if(loadedUser.isActive() && loadedUser.isNotBlocked()) {throw new AppException(SecurityErrorMsg.ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);
        if(isNotLoggedIn) logger.loggOffConnection(AuthActions.CREATE_ACCOUT_TOKEN, email,null, accountToken, AuthTables.ACCOUNT_TOKEN);
        if(!isNotLoggedIn) logger.logg(AuthActions.CREATE_ACCOUT_TOKEN, null, accountToken, AuthTables.ACCOUNT_TOKEN);

        Long actorUserId = userRepo.getUserIdByEmail(isNotLoggedIn ? loadedUser.getEmail() : jwtService.extractUsername());
        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), actorUserId);
        emailSenderService.sendAccountActivationEmail(email, username, emailServiceConfig.getActivateAccountLink() + "/" + accountToken.getToken());
        emailNotification.setSent(true);
        accountToken.setEmailSent(true);
        emailNotification = emailRepo.save(emailNotification);
        if(!loadedUser.isActive() || !loadedUser.isNotBlocked()) loadedUser.setStatut(new Statut("USR-AACT"));
        if(isNotLoggedIn) logger.loggOffConnection(AuthActions.SEND_ACCOUNT_ACTIVATION_EMAIL, email, null, emailNotification, AuthTables.EMAIL_NOTIFICATION);
        if(!isNotLoggedIn) logger.logg(AuthActions.SEND_ACCOUNT_ACTIVATION_EMAIL, null, emailNotification, AuthTables.EMAIL_NOTIFICATION);
    }

    @Override @Transactional
    public void sendAccountActivationEmail(Long userId) throws IllegalAccessException, UnknownHostException {
        AppUser loadedUser = this.userRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        this.sendAccountActivationEmail(loadedUser.getEmail());
            }

    @Override @Transactional
    public void sendReinitialisePasswordEmail(String email) throws IllegalAccessException, UnknownHostException {

        AppUser loadedUser = this.userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        String username = loadedUser.getFirstName();
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);
        logger.loggOffConnection(AuthActions.CREATE_ACCOUT_TOKEN, loadedUser.getEmail(), null, accountToken, AuthTables.ACCOUNT_TOKEN);

        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), loadedUser.getUserId());
        emailSenderService.sendReinitialisePasswordEmail(email, username, emailServiceConfig.getReinitPasswordLink() + "/" + accountToken.getToken());
        emailNotification.setSent(true);
        accountToken.setEmailSent(true);
        emailRepo.save(emailNotification);
        logger.loggOffConnection(AuthActions.SEND_REINIT_PASSWORD_EMAIL, loadedUser.getEmail(), null, emailNotification, AuthTables.EMAIL_NOTIFICATION);
    }

    @Override @Transactional
    public void clickLink(String token) throws UnknownHostException {
        Optional<AccountToken> accountToken$ = tokenRepo.findByToken(token);
        if(!accountToken$.isPresent()) return; //tCohrow new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG);
        EmailNotification notification = emailRepo.findByToken(accountToken$.get().getToken()).orElse(null);
        EmailNotification oldNotification = new EmailNotification();BeanUtils.copyProperties(notification, oldNotification);
        if(notification != null)
        {
            notification.setSeen(true);
            notification.setSent(true);
            logger.loggOffConnection(AuthActions.CLICK_LINK, notification.getEmail(), oldNotification, notification, AuthTables.EMAIL_NOTIFICATION);
        }
    }

    @Override
    public UserStatus getUserStatus(Long userId)
    {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return  UserStatus.UN_KNOWN;
        if(user.isActive() && user.isNotBlocked()) return UserStatus.ACTIVE;
        if(user.isActive() && !user.isNotBlocked()) return UserStatus.BLOCKED;
        if(!user.isActive() && user.isNotBlocked() && tokenRepo.hasValidActivationToken(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION;
        if(!user.isActive() && user.isNotBlocked()  && tokenRepo.lastActivationTokenHasExpired(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION_LINK;
        return  UserStatus.UN_KNOWN;
    }

    @Override
    public Page<ListUserDTO> searchUsers(String key, List<String> userStaCodes, Pageable pageable)
    {
        Long cedId = jwtService.getConnectedUserCedId();
        Long cesId = jwtService.getConnectedUserCesId();
        userStaCodes = userStaCodes == null || userStaCodes.isEmpty() ? staRepo.getStaCodesByTypeStatut(TypeStatut.USER) : userStaCodes;
        key = key == null ? "" : StringUtils.stripAccentsToUpperCase(key);
        return userRepo.searchUsers(key, cedId, cesId, userStaCodes, pageable);
    }

    @Override @Transactional
    public ReadUserDTO createUserAndFunction(CreaterUserAndFunctionDTO dto) throws UnknownHostException, IllegalAccessException {
        CreateUserDTO userDto = dto.getCreateUserDTO();
        ReadUserDTO  user = this.createUser(userDto);

        List<CreateInitialFncDTO> createInitialFncDTOs = dto.getCreateInitialFncDTO();
        if(createInitialFncDTOs == null || createInitialFncDTOs.isEmpty()) return user;

        ReadFncDTO readFncDTO = createInitialFncDTOs.stream().map(createInitialFncDTO -> {

            try {
                return this.createUserInitialFonction(userDto, user, createInitialFncDTO);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(isCurrentFunction).collect(Collectors.toList()).stream().findFirst().get();

        user.setStatus(this.getUserStatus(user.getUserId()));
        user.setCurrentFnc(readFncDTO);
        return user;
    }

    private Predicate<ReadFncDTO> isCurrentFunction = (readFncDTO)->readFncDTO != null && readFncDTO.getFncStatus() == 1;

    private ReadFncDTO createUserInitialFonction(CreateUserDTO userDto, ReadUserDTO user, CreateInitialFncDTO dto) throws UnknownHostException {
        CreateFncDTO createFncDTO = new CreateFncDTO();
        BeanUtils.copyProperties(dto, createFncDTO);

        createFncDTO.setVisibilityId(userDto.getCedId());
        createFncDTO.setCesId(userDto.getCesId());
        //createFncDTO.setFncStatus(1);
        createFncDTO.setUserId(user.getUserId());
        return functionService.createFnc(createFncDTO);
    }

    @Override
    public ReadUserDTO getUserInfos(Long userId)
    {
        ReadUserDTO user = userRepo.findReadUserDto(userId);
        user.setStatus(this.getUserStatus(userId));
        user.setCurrentFnc(functionService.getActiveCurrentFunction(userId));
        return user;
    }
}
