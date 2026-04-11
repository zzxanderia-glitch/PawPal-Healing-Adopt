package healing.pet.dao;

import healing.pet.model.AdoptionRequest;
import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.util.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdoptionDAOImpl implements AdoptionDAO {

    @Override
    public void createAdoptionRequest(String userId, int petId, String applicantName,
                                      String applicantPhone, String applicantAddress,
                                      String applyReason) throws SQLException {
        String sql = "INSERT INTO adoption_request (user_id, pet_id, applicant_name, applicant_phone, " +
                "applicant_address, apply_reason, apply_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), '待审核')";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, petId);
            pstmt.setString(3, applicantName);
            pstmt.setString(4, applicantPhone);
            pstmt.setString(5, applicantAddress);
            pstmt.setString(6, applyReason);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<AdoptionRequest> getUserRequests(String userId) throws SQLException {
        List<AdoptionRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, p.* FROM adoption_request ar " +
                "LEFT JOIN pet p ON ar.pet_id = p.id " +
                "WHERE ar.user_id = ? ORDER BY ar.apply_time DESC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AdoptionRequest request = new AdoptionRequest(
                            rs.getInt("request_id"),
                            rs.getString("user_id"),
                            rs.getInt("pet_id"),
                            rs.getString("applicant_name"),
                            rs.getString("applicant_phone"),
                            rs.getString("applicant_address"),
                            rs.getString("apply_reason"),
                            rs.getTimestamp("apply_time").toLocalDateTime(),
                            rs.getString("status")
                    );

                    Animal pet = extractPet(rs);
                    request.setPet(pet);
                    list.add(request);
                }
            }
        }
        return list;
    }

    @Override
    public void updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE adoption_request SET status = ? WHERE request_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<AdoptionRequest> getPendingRequests() throws SQLException {
        List<AdoptionRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, p.* FROM adoption_request ar " +
                "LEFT JOIN pet p ON ar.pet_id = p.id " +
                "WHERE ar.status = '待审核' ORDER BY ar.apply_time ASC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AdoptionRequest request = new AdoptionRequest(
                        rs.getInt("request_id"),
                        rs.getString("user_id"),
                        rs.getInt("pet_id"),
                        rs.getString("applicant_name"),
                        rs.getString("applicant_phone"),
                        rs.getString("applicant_address"),
                        rs.getString("apply_reason"),
                        rs.getTimestamp("apply_time").toLocalDateTime(),
                        rs.getString("status")
                );
                Animal pet = extractPet(rs);
                request.setPet(pet);
                list.add(request);
            }
        }
        return list;
    }

    @Override
    public List<AdoptionRequest> getReviewingRequests() throws SQLException {
        List<AdoptionRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, p.* FROM adoption_request ar " +
                "LEFT JOIN pet p ON ar.pet_id = p.id " +
                "WHERE ar.status = '审核中' ORDER BY ar.apply_time ASC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AdoptionRequest request = new AdoptionRequest(
                        rs.getInt("request_id"),
                        rs.getString("user_id"),
                        rs.getInt("pet_id"),
                        rs.getString("applicant_name"),
                        rs.getString("applicant_phone"),
                        rs.getString("applicant_address"),
                        rs.getString("apply_reason"),
                        rs.getTimestamp("apply_time").toLocalDateTime(),
                        rs.getString("status")
                );
                Animal pet = extractPet(rs);
                request.setPet(pet);
                list.add(request);
            }
        }
        return list;
    }

    @Override
    public List<AdoptionRequest> getAllRequests() throws SQLException {
        List<AdoptionRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, p.* FROM adoption_request ar " +
                "LEFT JOIN pet p ON ar.pet_id = p.id " +
                "ORDER BY ar.apply_time DESC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AdoptionRequest request = new AdoptionRequest(
                        rs.getInt("request_id"),
                        rs.getString("user_id"),
                        rs.getInt("pet_id"),
                        rs.getString("applicant_name"),
                        rs.getString("applicant_phone"),
                        rs.getString("applicant_address"),
                        rs.getString("apply_reason"),
                        rs.getTimestamp("apply_time").toLocalDateTime(),
                        rs.getString("status")
                );
                Animal pet = extractPet(rs);
                request.setPet(pet);
                list.add(request);
            }
        }
        return list;
    }

    private Animal extractPet(ResultSet rs) throws SQLException {
        int petId = rs.getInt("pet_id");
        if (rs.wasNull()) return null;

        String name = rs.getString("name");
        String type = rs.getString("type");
        int age = rs.getInt("age");
        String breed = rs.getString("breed");
        String story = rs.getString("story");
        String photoPath = rs.getString("photo_path");
        int status = rs.getInt("p.status");
        String detailStory = rs.getString("detail_story");
        String habits = rs.getString("habits");
        String preference = rs.getString("preference");

        if ("cat".equalsIgnoreCase(type) || "猫".equals(type)) {
            return new Cat(petId, name, age, breed, story, photoPath, status, detailStory, habits, preference);
        } else if ("dog".equalsIgnoreCase(type) || "狗".equals(type)) {
            return new Dog(petId, name, age, breed, story, photoPath, status, detailStory, habits, preference);
        }
        return null;
    }
}
