package main.Response;

import main.Dto.ErrorsRegisterDto;

public class RegisterWrongResponse {
    private boolean result;
    private ErrorsRegisterDto errors;

    public RegisterWrongResponse()
    {
        errors = new ErrorsRegisterDto();
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ErrorsRegisterDto getErrors() {
        return errors;
    }

    public void setErrors(ErrorsRegisterDto errors) {
        this.errors = errors;
    }
}
