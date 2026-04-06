package healing.pet.model;

import java.time.LocalDateTime;

public class AdoptionRequest {
    private int requestId;
    private String userId;
    private int petId;
    private String applicantName;
    private String applicantPhone;
    private String applicantAddress;
    private String applyReason;
    private LocalDateTime applyTime;
    private String status;
    private Animal pet;

    public AdoptionRequest(int requestId, String userId, int petId,
                           String applicantName, String applicantPhone,
                           String applicantAddress, String applyReason,
                           LocalDateTime applyTime, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.petId = petId;
        this.applicantName = applicantName;
        this.applicantPhone = applicantPhone;
        this.applicantAddress = applicantAddress;
        this.applyReason = applyReason;
        this.applyTime = applyTime;
        this.status = status;
    }

    public AdoptionRequest(int requestId, String userId, int petId,
                           String applicantName, String applicantPhone,
                           String applicantAddress, String applyReason,
                           LocalDateTime applyTime, String status, Animal pet) {
        this.requestId = requestId;
        this.userId = userId;
        this.petId = petId;
        this.applicantName = applicantName;
        this.applicantPhone = applicantPhone;
        this.applicantAddress = applicantAddress;
        this.applyReason = applyReason;
        this.applyTime = applyTime;
        this.status = status;
        this.pet = pet;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getApplicantPhone() { return applicantPhone; }
    public void setApplicantPhone(String applicantPhone) { this.applicantPhone = applicantPhone; }

    public String getApplicantAddress() { return applicantAddress; }
    public void setApplicantAddress(String applicantAddress) { this.applicantAddress = applicantAddress; }

    public String getApplyReason() { return applyReason; }
    public void setApplyReason(String applyReason) { this.applyReason = applyReason; }

    public LocalDateTime getApplyTime() { return applyTime; }
    public void setApplyTime(LocalDateTime applyTime) { this.applyTime = applyTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Animal getPet() { return pet; }
    public void setPet(Animal pet) { this.pet = pet; }

    public String getStatusDesc() {
        return status;
    }

    public java.awt.Color getStatusColor() {
        switch (status) {
            case "审核中": return new java.awt.Color(255, 180, 50);
            case "未通过": return new java.awt.Color(150, 150, 150);
            default: return new java.awt.Color(100, 200, 150);
        }
    }
}
