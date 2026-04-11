package healing.pet.service;

import healing.pet.dao.FeedbackDAO;
import healing.pet.dao.FeedbackDAOImpl;
import healing.pet.model.Feedback;

import java.sql.SQLException;
import java.util.List;

public class FeedbackService {
    private FeedbackDAO feedbackDAO;

    public FeedbackService() {
        this.feedbackDAO = new FeedbackDAOImpl();
    }

    public void submitFeedback(String userId, String username, String content) throws SQLException {
        if (content == null || content.trim().isEmpty()) {
            throw new SQLException("反馈内容不能为空！");
        }
        feedbackDAO.submitFeedback(userId, username, content);
    }

    public List<Feedback> getUserFeedbacks(String userId) throws SQLException {
        return feedbackDAO.getUserFeedbacks(userId);
    }

    public List<Feedback> getAllFeedbacks() throws SQLException {
        return feedbackDAO.getAllFeedbacks();
    }

    public void replyFeedback(int feedbackId, String replyContent) throws SQLException {
        if (replyContent == null || replyContent.trim().isEmpty()) {
            throw new SQLException("回复内容不能为空！");
        }
        feedbackDAO.replyFeedback(feedbackId, replyContent);
    }

    public void updateFeedbackStatus(int feedbackId, String status) throws SQLException {
        feedbackDAO.updateFeedbackStatus(feedbackId, status);
    }
}
