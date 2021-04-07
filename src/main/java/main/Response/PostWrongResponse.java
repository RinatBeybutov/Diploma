package main.Response;

import main.Dto.ErrorsPostDto;

public class PostWrongResponse {

  private boolean result;
  private ErrorsPostDto errors;

  public PostWrongResponse() {
    errors = new ErrorsPostDto();
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public ErrorsPostDto getErrors() {
    return errors;
  }

  public void setErrors(ErrorsPostDto errors) {
    this.errors = errors;
  }
}
