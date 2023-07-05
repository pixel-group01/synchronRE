package com.pixel.synchronre.logmodule.controller.service;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.logmodule.controller.repositories.LogDetailsRepo;
import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.logmodule.model.entities.LogDetails;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import jakarta.persistence.Id;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.controller.repositories.LogRepo;
import com.pixel.synchronre.logmodule.model.entities.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service @RequiredArgsConstructor
public class LogService implements ILogService
{
    private final LogRepo logRepo;
    private final UserRepo userRepo;
    private final IJwtService jwtService;
    private final LogDetailsRepo histoRepo;
    private final FunctionRepo functionRepo;
    private final CedRepo cedRepo;
    //private final HistoService histoService;


    @Override @Transactional
    public Log logg(String action, Object oldObject, Object newObject, String tableName) throws UnknownHostException
    {
        Log log = this.saveLog(action);
        List<LogDetails> logDetails = this.saveLogDetails(oldObject, newObject, log, tableName, false);
        log.setLogDetails(logDetails);
        return log;
    }

    @Override @Transactional
    public Log loggOffConnection(String action, String actorEmail, Object oldObject, Object newObject, String tableName) throws UnknownHostException
    {
        Log log = this.saveLogOffConnection(action, actorEmail);
        List<LogDetails> logDetails = this.saveLogDetails(oldObject, newObject, log, tableName, true);
        log.setLogDetails(logDetails);
        return log;
    }

    @Override @Transactional
    public Log saveLog(String action) throws UnknownHostException {
        return this.saveLog(action, jwtService.getCurrentJwt());
    }

    @Override @Transactional
    public Log saveLogError(String errorMsg, String stackTrace) throws UnknownHostException {
        Log logErreur = this.saveLog("SYSTEM_ERROR", jwtService.getCurrentJwt());
        logErreur.setErrorMessage(errorMsg);
        logErreur.setStackTrace(stackTrace);
        return logErreur;
    }


    @Override @Transactional
    public Log saveLog(String action, String token) throws UnknownHostException {
        InetAddress Ip=InetAddress.getLocalHost();
        Log log = jwtService.getUserInfosFromJwt(token);
        log.setAction(action);
        log.setIpAddress(Ip.getHostAddress());
        log.setMacAddress(Ip.getAddress());
        log.setHostName(Ip.getHostName());
        return logRepo.save(log);
    }

    @Override @Transactional
    public Log saveLogOffConnection(String action, String actorEmail) throws UnknownHostException
    {
        InetAddress Ip=InetAddress.getLocalHost();
        Long userId = userRepo.getUserIdByEmail(actorEmail);
        Set<Long> fncIds = functionRepo.getCurrentFncIds(userId);

        Log log = new Log();
        log.setUserEmail(actorEmail);
        log.setUserId(userId);
        log.setConnectionId(null);
        log.setAction(action);
        log.setFunction(fncIds == null || fncIds.size() !=1 ? null : new AppFunction(new ArrayList<>(fncIds).get(0)));

        log.setAction(action);
        log.setIpAddress(Ip.getHostAddress());
        log.setMacAddress(Ip.getAddress());
        log.setHostName(Ip.getHostName());
        return logRepo.save(log);
    }

    @Override
    public Log logLoginOrLogout(String username, String action, String connectionId) throws UnknownHostException {
        AppUser user = userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Email non enregistré"));
        Log log = new Log();
        InetAddress Ip=InetAddress.getLocalHost();

        log.setUserEmail(username);
        log.setUserId(user.getUserId());
        log.setAction(action);
        log.setConnectionId(connectionId);

        log.setIpAddress(Ip.getHostAddress());
        log.setMacAddress(Ip.getAddress());
        log.setHostName(Ip.getHostName());

        Set<Long> ids = functionRepo.getCurrentFncIds(user.getUserId());
        Long functionId = ids == null || ids.size() != 1 ? null : new ArrayList<>(ids).get(0);
        log.setFunction(functionId == null ? null : functionRepo.findById(functionId).orElse(null));
        return logRepo.save(log);
    }

    @Override
    public Log logLoginOrLogout(String username, String action) throws UnknownHostException {
        return this.logLoginOrLogout(username,action, UUID.randomUUID().toString());
    }

    @Override @Transactional
    public List<LogDetails> saveLogDetails(Object oldObject, Object newObject, Log log, String tableName, boolean offConnection)
    {
        if(newObject == null) return new ArrayList<>();

        String objectId = this.getEntityId(newObject);

        List<LogDetails> histos = Arrays.stream(newObject.getClass().getDeclaredFields()).filter(f->
        {
            try
            {
                f.setAccessible(true);
                return oldObject == null || (f.get(oldObject) == null && f.get(newObject) != null)  || (f.get(oldObject) != null && !f.get(oldObject).equals(f.get(newObject))) ;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }).map(f-> {
            try {
                return LogDetails.builder()
                        .oldValue(oldObject == null || f.get(oldObject) == null ? null : f.get(oldObject).toString())
                        .newValue(newObject == null || f.get(newObject) == null ? null : f.get(newObject).toString())
                        .columnName(f.getName())
                        .tableName(tableName)
                        .connectionId(offConnection ? null : (String)jwtService.getClaim("connectionId"))
                        .objectId(objectId)
                        .log(log)
                        .build();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return histoRepo.saveAll(histos);
    }

    @Override
    public Page<ConnexionList> getConnextionLogs(String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable)
    {
        return this.getConnexionList(Collections.singletonList("Login"), null,key,userId, debut, fin.plusDays(1), pageable) ;
    }

    @Override
    public Page<ConnexionList> getSystemErrors(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable)
    {
        return this.getConnexionList(Collections.singletonList("SYSTEM_ERROR"), connId,key,userId, debut, fin.plusDays(1), pageable) ;
    }

    @Override
    public Page<ConnexionList> getConnexionActionLogs(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable)
    {
        List<String> actions = logRepo.getAllActionTypes().stream().filter(action->!"SYSTEM_ERROR".equals(action)).collect(Collectors.toList());
        return this.getConnexionList(actions, connId,key,userId, debut, fin.plusDays(1), pageable) ;
    }

    @Override
    public void deleteSystemErrors(List<Long> errorIds) {
        if(errorIds == null || errorIds.isEmpty()) return;
        errorIds.forEach(id->logRepo.deleteById(id));
    }

    private Page<ConnexionList> getConnexionList(List<String> actions, String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable)
    {
        key = key==null ? "" : StringUtils.stripAccentsToUpperCase(key);
        key = key.replace("'", "''");
        debut = debut == null ? LocalDate.now() : debut; fin = fin == null ? LocalDate.now() : fin;
        Page<ConnexionList> connexionPage = logRepo.getConnnexionLogs(actions, key,userId, debut, fin.plusDays(1), connId, pageable);
        return connexionPage;
    }

    private String getEntityId(Object obj)
    {
        String idValue = null;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class))
            {
                field.setAccessible(true);
                try {
                    idValue = String.valueOf(field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return idValue;
    }
}
