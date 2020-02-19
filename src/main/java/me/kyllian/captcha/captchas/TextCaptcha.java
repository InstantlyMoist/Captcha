package me.kyllian.captcha.captchas;

import me.kyllian.captcha.CaptchaPlugin;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.concurrent.ThreadLocalRandom;

public class TextCaptcha implements Captcha {

    private CaptchaPlugin plugin;
    private Player player;
    private BufferedImage image;
    private String answer;

    public TextCaptcha(CaptchaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        this.answer = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        Bukkit.broadcastMessage("Generated string " + answer);
        String[] split = answer.split("");

        Canvas canvas = new Canvas();
        canvas.setSize(128, 128);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        canvas.paint(graphics);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 128, 128);

        graphics.setColor(Color.BLACK);
        graphics.drawString("Type the text you see:", 0, 10);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 34));

        for (int i = 0; i != split.length; i++) {
            AffineTransform original = graphics.getTransform();
            int rotation = ThreadLocalRandom.current().nextInt(0, 45);
            graphics.rotate(Math.toRadians(rotation) * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1), 10 + (20 * (i + 1)), 64);
            graphics.drawString(split[i], (20 * (i + 1)), 64);
            graphics.setTransform(original);
        }
        graphics.dispose();

    }

    @Override
    public CaptchaType getType() {
        return CaptchaType.TEXTCAPTCHA;
    }

    @Override
    public String getAnswer() {
        return this.answer;
    }

    @Override
    public void send() {
        plugin.getMapHandler().sendMap(player, image);
    }
}
