package healing.pet.util;

import healing.pet.model.User;

public class UserContext {
    private static UserContext instance;
    private User currentUser;
    private int currentRole = 0; // 0:用户, 1:管理员

    private UserContext() {}

    public static UserContext getInstance() {
        if (instance == null) instance = new UserContext();
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) this.currentRole = user.getRole();
    }

    public User getCurrentUser() { return currentUser; }

    // 💡 关键：这个方法必须修改内部变量
    public void setAdminMode(boolean isAdmin) {
        this.currentRole = isAdmin ? 1 : 0;
    }

    public boolean isAdmin() {
        return currentRole == 1;
    }
}