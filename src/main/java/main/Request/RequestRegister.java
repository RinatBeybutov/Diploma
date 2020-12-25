package main.Request;

public class RequestRegister {
    private String e_mail;
    private String password;
    private String name;
    private String captcha;
    private String captcha_secret;

    public RequestRegister(String e_mail, String password, String name, String captcha, String captcha_secret) {
        this.e_mail = e_mail;
        this.password = password;
        this.name = name;
        this.captcha = captcha;
        this.captcha_secret = captcha_secret;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptcha_secret() {
        return captcha_secret;
    }

    public void setCaptcha_secret(String captcha_secret) {
        this.captcha_secret = captcha_secret;
    }
}
