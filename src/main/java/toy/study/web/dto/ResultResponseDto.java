package toy.study.web.dto;

import lombok.Getter;

@Getter
public class ResultResponseDto {
    private String result;
    private String msg;

    public ResultResponseDto(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }
}
