package healing.pet.dao;

import healing.pet.model.Feedback;
import healing.pet.util.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAOImpl implements FeedbackDAO {

    @Override
    public void submitFeedback(String userId, String username, String content) throws SQLException {
        String sql = "INSERT INTO feedback (user_id, username, content, submit_time, status) " +
                "VALUES (?, ?, ?, NOW(), '待处理')";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, content);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Feedback> getUserFeedbacks(String userId) throws SQLException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE user_id = ? ORDER BY submit_time DESC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback(
                            rs.getInt("feedback_id"),
                            rs.getString("user_id"),
                            rs.getString("username"),
                            rs.getString("content"),
                            rs.getTimestamp("submit_time").toLocalDateTime(),
                            rs.getString("status"),
                            rs.getString("reply_content"),
                            rs.getTimestamp("reply_time") != null ?
                                    rs.getTimestamp("reply_time").toLocalDateTime() : null
                    );
                    list.add(feedback);
                }
            }
        }
        return list;
    }

    @Override
    public List<Feedback> getAllFeedbacks() throws SQLException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback ORDER BY submit_time DESC";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("feedback_id"),
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("content"),
                        rs.getTimestamp("submit_time").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("reply_content"),
                        rs.getTimestamp("reply_time") != null ?
                                rs.getTimestamp("reply_time").toLocalDateTime() : null
                );
                list.add(feedback);
            }
        }
        return list;
    }

    @Override
    public void replyFeedback(int feedbackId, String replyContent) throws SQLException {
        String sql = "UPDATE feedback SET reply_content = ?, reply_time = NOW(), status = '已回复' " +
                "WHERE feedback_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, replyContent);
            pstmt.setInt(2, feedbackId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateFeedbackStatus(int feedbackId, String status) throws SQLException {
        String sql = "UPDATE feedback SET status = ? WHERE feedback_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, feedbackId);
            pstmt.executeUpdate();
        }
    }
}
