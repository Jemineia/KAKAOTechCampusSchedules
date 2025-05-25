package com.example.calendarproject.repository;

import com.example.calendarproject.dto.ScheduleRequestDTO;
import com.example.calendarproject.dto.ScheduleResponseDTO;
import com.example.calendarproject.util.DBUtil;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduleRepository {

    public ScheduleResponseDTO saveAndReturn(ScheduleRequestDTO req) throws Exception {
        String sql = "INSERT INTO schedules(title, writer, password) VALUES (?, ?, ?)";
        String idSql = "SELECT * FROM schedules WHERE id = LAST_INSERT_ID()";
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, req.getTitle());
                pstmt.setString(2, req.getWriter());
                pstmt.setString(3, req.getPassword());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(idSql)) {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    conn.commit();
                    return mapToResponse(rs);
                }
            }

            conn.rollback();
        }

        throw new SQLException("일정 저장에 실패했습니다.");
    }


    public List<ScheduleResponseDTO> findAll(String writer, String date) throws Exception {
        String sql = "SELECT * FROM schedules WHERE (? IS NULL OR writer=?) AND (? IS NULL OR DATE(updated_at)=?) ORDER BY updated_at DESC";
        List<ScheduleResponseDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, writer);
            pstmt.setString(2, writer);
            pstmt.setString(3, date);
            pstmt.setString(4, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResponse(rs));
            }
        }
        return list;
    }

    public ScheduleResponseDTO findById(int id) throws Exception {
        String sql = "SELECT * FROM schedules WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToResponse(rs);
            }
        }
        return null;
    }

    public String findPasswordById(int id) throws Exception {
        String sql = "SELECT password FROM schedules WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("password");
        }
        return null;
    }

    public void update(int id, ScheduleRequestDTO req) throws Exception {
        String sql = "UPDATE schedules SET title=?, writer=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, req.getTitle());
            pstmt.setString(2, req.getWriter());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM schedules WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private ScheduleResponseDTO mapToResponse(ResultSet rs) throws SQLException {
        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setId(rs.getInt("id"));
        dto.setTitle(rs.getString("title"));
        dto.setWriter(rs.getString("writer"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));
        dto.setUpdatedAt(rs.getTimestamp("updated_at"));
        return dto;
    }
}
