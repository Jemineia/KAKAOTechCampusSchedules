package com.example.calendarproject.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class ScheduleRequestDTO {
    @NotBlank(message = "제목(title)은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "작성자(writer)는 필수 항목입니다.")
    private String writer;

    @NotBlank(message = "비밀번호(password)는 필수 항목입니다.")
    private String password;
}
