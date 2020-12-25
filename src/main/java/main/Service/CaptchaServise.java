package main.Service;

import com.github.cage.Cage;
import main.Model.Captcha;
import main.Repository.CaptchaRepository;
import main.Response.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Service
public class CaptchaServise {

    @Autowired
    CaptchaRepository captchaRepository;


    public CaptchaResponse getCaptcha() throws IOException {

        CaptchaResponse captchaResponse = new CaptchaResponse();

        Cage cage = new Cage();
        String captchaCode = cage.getTokenGenerator().next();
        String secretCode = cage.getTokenGenerator().next();
        Date currentDate = new Date();

        Captcha captcha = new Captcha();
        captcha.setCode(captchaCode);
        captcha.setSecretCode(secretCode);
        captcha.setTime(currentDate);

        Captcha captcha1 = captchaRepository.save(captcha);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //captchaRepository.deleteAllBeforeHour();

        BufferedImage bigCaptcha = cage.drawImage(captchaCode);
        BufferedImage littleCaptcha = createResizedCopy(bigCaptcha,
                100, 35, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);

        ImageIO.write(littleCaptcha, "jpg", byteArrayOutputStream);

        byteArrayOutputStream.close();


        Base64.Encoder enc = Base64.getEncoder();
        byte[] encbytes = enc.encode(byteArrayOutputStream.toByteArray());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < encbytes.length; i++)
        {
            builder.append((char)encbytes[i]);
        }

        //System.out.println(builder.toString());

        captchaResponse.setImage("data:image/png;base64, " + builder.toString());
        captchaResponse.setSecret(secretCode);

        return captchaResponse;
    }

    private BufferedImage createResizedCopy(Image originalImage,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }
}
