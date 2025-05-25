package com.example.calendarproject.service;

import com.example.calendarproject.dto.ScheduleRequestDTO;
import com.example.calendarproject.dto.ScheduleResponseDTO;
import com.example.calendarproject.exception.PasswordMismatchException;
import com.example.calendarproject.exception.ScheduleNotFoundException;
import com.example.calendarproject.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository repo;

    public ScheduleResponseDTO createAndReturn(ScheduleRequestDTO dto) throws Exception {
        return repo.saveAndReturn(dto);
    }


    public List<ScheduleResponseDTO> readAll(String writer, String date) throws Exception {
        return repo.findAll(writer, date);
    }

    public ScheduleResponseDTO read(int id) throws Exception {
        return repo.findById(id);
    }

    public void update(int id, ScheduleRequestDTO dto) throws Exception {
        String dbPassword = repo.findPasswordById(id);
        if (dbPassword == null) {
            throw new ScheduleNotFoundException("해당 일정이 존재하지 않습니다.");
        }

        if (!dbPassword.equals(dto.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        repo.update(id, dto);
    }

    public void delete(int id, String password) throws Exception {
        String dbPassword = repo.findPasswordById(id);
        if (dbPassword == null) {
            throw new ScheduleNotFoundException("해당 일정이 존재하지 않습니다.");
        }
        if (!dbPassword.equals(password)) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        repo.delete(id);
    }
}
