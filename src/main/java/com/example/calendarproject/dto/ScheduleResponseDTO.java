package com.example.calendarproject.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ScheduleResponseDTO {
    private int id;
    private String title;
    private String writer;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
