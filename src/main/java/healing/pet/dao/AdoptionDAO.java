package healing.pet.dao;

import healing.pet.model.AdoptionRequest;

import java.sql.SQLException;
import java.util.List;

public interface AdoptionDAO {
    void createAdoptionRequest(String userId, int petId, String applicantName,
                               String applicantPhone, String applicantAddress,
                               String applyReason) throws SQLException;

    List<AdoptionRequest> getUserRequests(String userId) throws SQLException;

    void updateRequestStatus(int requestId, String status) throws SQLException;

    List<AdoptionRequest> getPendingRequests() throws SQLException;

    List<AdoptionRequest> getReviewingRequests() throws SQLException;

    List<AdoptionRequest> getAllRequests() throws SQLException;
}
