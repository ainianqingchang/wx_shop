package com.ainian.wxapp.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SpringUtils {

    private static String serverAddress;

    public static String getServerAddress() {
        return serverAddress;
    }

    @Value("${image.server-address}")
    public void  setServerAddress(String serverAddress) {
        this.serverAddress=serverAddress;
    }
}
