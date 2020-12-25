package main.Response;

import main.Response.dto.ErrorsDto;

public class RegisterWrongResponse {
    private boolean result;
    private ErrorsDto errors;

    public RegisterWrongResponse()
    {
        errors = new ErrorsDto();
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ErrorsDto getErrors() {
        return errors;
    }

    public void setErrors(ErrorsDto errors) {
        this.errors = errors;
    }
}
