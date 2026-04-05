package healing.pet.service;

import healing.pet.dao.UserDAO;
import healing.pet.dao.UserDAOImpl;
import healing.pet.model.User;

import java.sql.SQLException;

public class AuthService {
    private UserDAO userDAO = new UserDAOImpl();

    /**
     * 用户登录方法
     * @param userId 账号
     * @param password 密码
     * @return 登录成功返回 User 对象，失败返回 null
     */
    public User login(String userId, String password) {
        if (userId == null || password == null) return null;

        try {
            // 1. 先尝试作为管理员登录
            User admin = userDAO.findByAdminId(userId);
            if (admin != null && admin.getPassword().equals(password)) {
                admin.setRole(1); // 确保角色正确
                return admin;
            }

            // 2. 再尝试作为普通用户登录
            User user = userDAO.findByUserId(userId);
            if (user != null && user.getPassword().equals(password)) {
                user.setRole(0); // 确保角色正确
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户注册方法
     */
    public String register(User user) {
        String userId = user.getUserId();
        String password = user.getPassword();

        if (userId == null || userId.trim().isEmpty()) return "账号不能为空";
        if (password == null || password.trim().isEmpty()) return "密码不能为空";
        if (password.length() < 6) return "密码长度不能少于6位";

        // 管理员账号格式检查 (G + 6位数字)
        if (userId.startsWith("G")) {
            return "普通用户账号不能以 G 开头";
        }

        try {
            boolean success = userDAO.insertUser(user);
            if (success) {
                return "注册成功！欢迎加入宠物领养系统。";
            } else {
                return "注册失败，该账号可能已存在。";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "注册失败，数据库错误。";
        }
    }
}
