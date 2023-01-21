package me.kyllian.captcha.spigot.captchas;

import me.kyllian.captcha.spigot.CaptchaPlugin;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
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
        // Generate random string:
        this.answer = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        String[] split = answer.split("");

        Canvas canvas = new Canvas();
        canvas.setSize(128, 128);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.paint(graphics);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 128, 128);

        try {
            File file = new File(plugin.getDataFolder(), "background.png");
            BufferedImage background = ImageIO.read(file);
            AffineTransform affineTransform = new AffineTransform();
            graphics.drawImage(background, affineTransform, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        boolean color = plugin.getConfig().getBoolean("captcha-settings.color");

        if (plugin.getConfig().getBoolean("captcha-settings.lines")) {
            int lines = ThreadLocalRandom.current().nextInt(10, 25);
            while (lines != 0) {
                graphics.setColor(color ? getRandomColor() : Color.DARK_GRAY);
                lines--;
                graphics.drawLine(getRandomCoordinate(), getRandomCoordinate(), getRandomCoordinate(), getRandomCoordinate());
            }
        }

        graphics.setFont(plugin.getFontHandler().getFont());

        for (int i = 0; i != split.length; i++) {
            AffineTransform original = graphics.getTransform();
            int rotation = ThreadLocalRandom.current().nextInt(0, 45);
            graphics.rotate(Math.toRadians(rotation) * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1), 10 + (20 * (i + 1)), 64);
            graphics.setColor(color ? getRandomColor() : Color.BLACK);
            graphics.drawString(split[i], (20 * (i + 1)), 70);
            graphics.setTransform(original);
        }

        graphics.dispose();

        if (plugin.getConfig().getBoolean("captcha-settings.reverse")) answer = StringUtils.reverse(answer);
    }

    public int getRandomCoordinate() {
        return ThreadLocalRandom.current().nextInt(0, 128);
    }

    public Color getRandomColor() {
        return new Color(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
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
