package me.kyllian.captcha.shared.data;

import java.io.Serializable;

public class StateData implements Serializable {

    private String uuid;
    private boolean inCaptcha;

    public StateData(String uuid, boolean inCaptcha) {
        this.uuid = uuid;
        this.inCaptcha = inCaptcha;
    }

    public String getUUID() {
        return uuid;
    }

    public boolean isInCaptcha() {
        return inCaptcha;
    }
}
