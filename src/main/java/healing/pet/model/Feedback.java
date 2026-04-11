package healing.pet.model;

import java.time.LocalDateTime;

public class Feedback {
    private int feedbackId;
    private String userId;
    private String username;
    private String content;
    private LocalDateTime submitTime;
    private String status;
    private String replyContent;
    private LocalDateTime replyTime;

    public Feedback(int feedbackId, String userId, String username, String content,
                    LocalDateTime submitTime, String status) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.submitTime = submitTime;
        this.status = status;
    }

    public Feedback(int feedbackId, String userId, String username, String content,
                    LocalDateTime submitTime, String status, String replyContent,
                    LocalDateTime replyTime) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.submitTime = submitTime;
        this.status = status;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
    }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSubmitTime() { return submitTime; }
    public void setSubmitTime(LocalDateTime submitTime) { this.submitTime = submitTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }

    public LocalDateTime getReplyTime() { return replyTime; }
    public void setReplyTime(LocalDateTime replyTime) { this.replyTime = replyTime; }
}
