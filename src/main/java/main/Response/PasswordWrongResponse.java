package main.Response;

import main.dto.ErrorsPasswordDto;

public class PasswordWrongResponse {
  private boolean result;
  private ErrorsPasswordDto errors;

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public ErrorsPasswordDto getErrors() {
    return errors;
  }

  public void setErrors(ErrorsPasswordDto errors) {
    this.errors = errors;
  }
}
