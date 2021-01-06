package main.Response;

import main.dto.UserBigDto;

public class LoginResponse
{
    private boolean result;
    private UserBigDto user;

    public LoginResponse()
    {
        user = new UserBigDto();
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserBigDto getUser() {
        return user;
    }

    public void setUser(UserBigDto user) {
        this.user = user;
    }
}
