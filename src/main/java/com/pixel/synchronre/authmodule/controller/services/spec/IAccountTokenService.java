package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.entities.AccountToken;
import com.pixel.synchronre.authmodule.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser);
    AccountToken createAccountToken(Long agentId);
}
