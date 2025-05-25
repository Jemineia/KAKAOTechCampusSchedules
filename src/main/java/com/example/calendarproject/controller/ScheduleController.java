package com.example.calendarproject.controller;

import com.example.calendarproject.dto.ScheduleRequestDTO;
import com.example.calendarproject.dto.ScheduleResponseDTO;
import com.example.calendarproject.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> create(@Valid @RequestBody ScheduleRequestDTO dto) throws Exception {
        ScheduleResponseDTO response = service.createAndReturn(dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public List<ScheduleResponseDTO> getAll(@RequestParam(required = false) String writer,
                                            @RequestParam(required = false) String date) throws Exception {
        return service.readAll(writer, date);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
        ScheduleResponseDTO dto = service.read(id);
        if (dto == null) return ResponseEntity.status(404).body("일정이 존재하지 않습니다.");
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ScheduleRequestDTO dto) throws Exception {
        boolean updated = service.update(id, dto);
        return updated ? ResponseEntity.ok("일정이 수정되었습니다.")
                : ResponseEntity.status(403).body("비밀번호가 일치하지 않습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestBody ScheduleRequestDTO dto) throws Exception {
        boolean deleted = service.delete(id, dto.getPassword());
        return deleted ? ResponseEntity.ok("일정이 삭제되었습니다.")
                : ResponseEntity.status(403).body("비밀번호가 일치하지 않습니다.");
    }
}
