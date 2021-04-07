package main.Response;

import main.Dto.ErrorsImageDto;

public class ImageWrongResponse {

  private boolean result;
  private ErrorsImageDto errors = new ErrorsImageDto();

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public ErrorsImageDto getErrors() {
    return errors;
  }

  public void setErrors(ErrorsImageDto errors) {
    this.errors = errors;
  }
}
