package com.pixel.synchronre.sharedmodule.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler
{
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException err)
    {
        Map<String, String> errorMap = new HashMap<>();
        err.getGlobalErrors().forEach(e->{String[] errTab = e.getDefaultMessage().split("::"); errorMap.put(errTab[0], errTab[1]);});
        err.getBindingResult().getFieldErrors().forEach(e->errorMap.put(e.getField(), e.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAppException(AppException err)
    {
        return err.getMessage();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAuthException(AuthenticationException exception)
    {
        String errMsg = exception instanceof DisabledException ?
                "Votre compte a bien été créé mais n'est pas encore activé.\nPour recevoir un lien d'activation, veillez cliquer sur le lien ci-dessous." :
                exception instanceof LockedException ? "Compte bloqué" :
                exception instanceof InsufficientAuthenticationException ? exception.getMessage() : "Username ou mot de passe incorrect";
        return errMsg;
    }
}
