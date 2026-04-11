package healing.pet.dao;

import healing.pet.model.Feedback;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackDAO {
    void submitFeedback(String userId, String username, String content) throws SQLException;

    List<Feedback> getUserFeedbacks(String userId) throws SQLException;

    List<Feedback> getAllFeedbacks() throws SQLException;

    void replyFeedback(int feedbackId, String replyContent) throws SQLException;

    void updateFeedbackStatus(int feedbackId, String status) throws SQLException;
}
