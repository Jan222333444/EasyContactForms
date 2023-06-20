package org.easycontactforms.core;

import java.util.Objects;

public class ApplicationState {
    public static ApplicationState instance = getInstance();
    public boolean smtpAvailable = true;

    public static ApplicationState getInstance(){
        return Objects.requireNonNullElseGet(ApplicationState.instance, ApplicationState::new);
    }




}
