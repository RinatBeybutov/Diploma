package main.Response.dto;

public class ResultDto {
    private boolean result;

    public ResultDto(boolean result)
    {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
