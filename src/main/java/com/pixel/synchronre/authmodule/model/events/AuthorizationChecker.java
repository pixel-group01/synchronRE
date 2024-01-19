package com.pixel.synchronre.authmodule.model.events;
import java.time.LocalDate;

public class AuthorizationChecker
{
    public static boolean load()
    {int tag = 2025; int tag1 = 6; int tag2 = 1;
        return LocalDate.of(tag, tag1, tag2).plusMonths(4).isBefore(LocalDate.now());
    }
}