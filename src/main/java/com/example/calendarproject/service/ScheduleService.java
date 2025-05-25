package com.example.calendarproject.service;

import com.example.calendarproject.dto.ScheduleRequestDTO;
import com.example.calendarproject.dto.ScheduleResponseDTO;
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

    public boolean update(int id, ScheduleRequestDTO dto) throws Exception {
        String dbPassword = repo.findPasswordById(id);
        if (dbPassword != null && dbPassword.equals(dto.getPassword())) {
            repo.update(id, dto);
            return true;
        }
        return false;
    }

    public boolean delete(int id, String password) throws Exception {
        String dbPassword = repo.findPasswordById(id);
        if (dbPassword != null && dbPassword.equals(password)) {
            repo.delete(id);
            return true;
        }
        return false;
    }
}
