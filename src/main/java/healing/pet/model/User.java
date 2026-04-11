package healing.pet.model;

public class User {
    private String userId;
    private String password;
    private String username;
    private String phone;
    private int adoptedPetId;
    // 角色 0=普通用户 1=管理员
    private int role;

    // 判断是否管理员
    public boolean isAdmin() {
        return role == 1 || (userId != null && userId.startsWith("G"));
    }

    // 判断是否普通用户
    public boolean isNormalUser() {
        return role == 0 && !isAdmin();
    }

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getAdoptedPetId() { return adoptedPetId; }
    public void setAdoptedPetId(int adoptedPetId) { this.adoptedPetId = adoptedPetId; }
    
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
}