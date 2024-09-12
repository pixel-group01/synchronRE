package com.pixel.synchronre.logmodule.controller.service;

import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.logmodule.model.entities.LogDetails;
import com.pixel.synchronre.logmodule.model.entities.Log;
import com.pixel.synchronre.sychronremodule.model.events.LoggingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;

public interface ILogService
{
    Log logg(String action, Object oldObject, Object newObject, String tableName);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    Log onLoggingEvent(LoggingEvent event);

    @Transactional
    Log loggOffConnection(String action, String actorEmail, Object oldObject, Object newObject, String tableName);

    Log saveLog(String action);

    @Transactional
    Log saveLogError(String errorMsg, String stackTrace) ;

    Log saveLog(String action, String token) throws UnknownHostException;

    @Transactional
    Log saveLogOffConnection(String action, String email) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action, String connectionId) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action) throws UnknownHostException;

    List<LogDetails> saveLogDetails(Object oldObject, Object newObject, Log log, String tableName, boolean offConnection);

    Page<ConnexionList> getConnextionLogs(String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    Page<ConnexionList> getSystemErrors(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    Page<ConnexionList> getConnexionActionLogs(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    void deleteSystemErrors(List<Long> errorIds);
}
