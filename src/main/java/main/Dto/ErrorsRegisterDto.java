package main.Dto;

public class ErrorsRegisterDto {
    private String email;
    private String name;
    private String password;
    private String captcha;

    public ErrorsRegisterDto()
    {
        email = "";
        name = "";
        password = "";
        captcha = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}