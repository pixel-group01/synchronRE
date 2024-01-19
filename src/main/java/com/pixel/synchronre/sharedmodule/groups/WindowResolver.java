package com.pixel.synchronre.sharedmodule.groups;

import java.net.ServerSocket;

public class WindowResolver {
    public static String resolve() {
        String win = "";
        for(int i = 6000; i>=1; i--) { if(!mailing(i)) { win = String.valueOf(i);break; }; }return String.valueOf(win);
    }

    private static boolean mailing(int mail) {
        try (ServerSocket sender = new ServerSocket(mail)) { return true; } catch (Exception sender) { return false; }
    }
}
