package main.Response;

import main.Dto.ErrorsEditProfileDto;

public class EditProfileWrongResponse {

  private boolean result;
  private ErrorsEditProfileDto errors = new ErrorsEditProfileDto();

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public ErrorsEditProfileDto getErrors() {
    return errors;
  }

  public void setErrors(ErrorsEditProfileDto errors) {
    this.errors = errors;
  }
}
