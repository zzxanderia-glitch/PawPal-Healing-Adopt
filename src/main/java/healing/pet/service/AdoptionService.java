package healing.pet.service;

import healing.pet.dao.*;
import healing.pet.model.AdoptionRequest;
import healing.pet.model.Feedback;

import java.sql.SQLException;
import java.util.List;

public class AdoptionService {
    private AdoptionDAO adoptionDAO;
    private PetDAO petDAO;

    public AdoptionService() {
        this.adoptionDAO = new AdoptionDAOImpl();
        this.petDAO = new PetDAOImpl();
    }

    public void applyAdoption(String userId, int petId, String applicantName,
                              String applicantPhone, String applicantAddress,
                              String applyReason) throws SQLException {
        List<AdoptionRequest> userRequests = adoptionDAO.getUserRequests(userId);
        for (AdoptionRequest request : userRequests) {
            if (request.getPetId() == petId && "待审核".equals(request.getStatus())) {
                throw new SQLException("您已经提交过该宠物的领养申请，请等待审核！");
            }
        }

        adoptionDAO.createAdoptionRequest(userId, petId, applicantName,
                applicantPhone, applicantAddress,
                applyReason);
        petDAO.updatePetStatus(petId, 1);
    }

    public List<AdoptionRequest> getUserRequests(String userId) throws SQLException {
        return adoptionDAO.getUserRequests(userId);
    }

    public void reviewAdoption(int requestId, String newStatus) throws SQLException {
        adoptionDAO.updateRequestStatus(requestId, newStatus);

        List<AdoptionRequest> allRequests = adoptionDAO.getAllRequests();
        AdoptionRequest request = null;
        for (AdoptionRequest r : allRequests) {
            if (r.getRequestId() == requestId) {
                request = r;
                break;
            }
        }

        if (request != null && "已通过".equals(newStatus)) {
            petDAO.updatePetStatus(request.getPetId(), 2);
        } else if (request != null && "未通过".equals(newStatus)) {
            List<AdoptionRequest> pendingRequests = adoptionDAO.getPendingRequests();
            boolean hasOtherPending = false;
            for (AdoptionRequest r : pendingRequests) {
                if (r.getPetId() == request.getPetId()) {
                    hasOtherPending = true;
                    break;
                }
            }
            if (!hasOtherPending) {
                petDAO.updatePetStatus(request.getPetId(), 0);
            }
        }
    }

    public List<AdoptionRequest> getPendingRequests() throws SQLException {
        return adoptionDAO.getPendingRequests();
    }

    public List<AdoptionRequest> getReviewingRequests() throws SQLException {
        return adoptionDAO.getReviewingRequests();
    }

    public List<AdoptionRequest> getAllRequests() throws SQLException {
        return adoptionDAO.getAllRequests();
    }

    public static class FeedbackService {
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
}
