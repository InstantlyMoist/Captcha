package me.kyllian.captcha.spigot.events;

import me.kyllian.captcha.spigot.captchas.Captcha;
import me.kyllian.captcha.spigot.captchas.SolveState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CaptchaCompleteEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    boolean cancelled = false;

    private final Captcha captcha;
    private final String chatInput;
    private SolveState state;

    public CaptchaCompleteEvent(Player who, Captcha captcha, String chatInput, SolveState state) {
        super(who);
        this.captcha = captcha;
        this.chatInput = chatInput;
        this.state = state;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public String getChatInput() {
        return chatInput;
    }

    public void setState(SolveState state) {
        this.state = state;
    }

    public SolveState getState () {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
