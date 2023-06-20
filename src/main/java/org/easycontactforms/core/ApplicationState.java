package org.easycontactforms.core;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * Stores state of application
 */
public class ApplicationState {
    public static ApplicationState instance = getInstance();
    public static boolean smtpAvailable = true;

    @Value("${redirect.mode.resend.interval}")
    public static int resendInterval;

    public static ApplicationState getInstance(){
        return Objects.requireNonNullElseGet(ApplicationState.instance, ApplicationState::new);
    }




}
