package com.pixel.synchronre.logmodule.controller.service;

import com.pixel.synchronre.logmodule.model.entities.LogDetails;
import com.pixel.synchronre.logmodule.model.entities.Log;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;

public interface ILogService
{
    Log logg(String action, Object oldObject, Object newObject, String tableName) throws UnknownHostException;

    @Transactional
    Log loggOffConnection(String action, String actorEmail, Object oldObject, Object newObject, String tableName) throws UnknownHostException;

    Log saveLog(String action) throws UnknownHostException;
    Log saveLog(String action, String token) throws UnknownHostException;

    @Transactional
    Log saveLogOffConnection(String action, String email) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action, String connectionId) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action) throws UnknownHostException;

    List<LogDetails> saveLogDetails(Object oldObject, Object newObject, Log log, String tableName, boolean offConnection);
}
