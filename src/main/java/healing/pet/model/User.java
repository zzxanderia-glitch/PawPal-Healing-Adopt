package healing.pet.model;

public class User {
    private String userId;
    private String password;
    // 组长要求新增：角色 0=普通用户 1=管理员
    private int role;

    // 判断是否管理员
    public boolean isAdmin() {
        return role == 1 || (userId != null && userId.startsWith("G") && userId.length() == 7);
    }

    // 判断是否普通用户
    public boolean isNormalUser() {
        return role == 0 || (userId != null && userId.matches("\\d{6}"));
    }

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
}