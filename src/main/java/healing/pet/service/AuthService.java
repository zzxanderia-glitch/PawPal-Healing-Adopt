package healing.pet.service;

import healing.pet.dao.UserDAO;
import healing.pet.dao.UserDAOImpl;
import healing.pet.model.User;

import java.sql.SQLException;

public class AuthService {
    private UserDAO userDAO = new UserDAOImpl();

    /**
     * 用户注册方法
     * @param user 待注册的用户对象
     * @return 注册结果信息
     */
    public String register(healing.pet.model.User user) {
        String userId = user.getUserId();
        String password = user.getPassword();

        // 检查账号是否为空
        if (userId == null || userId.trim().isEmpty()) {
            return "账号不能为空";
        }

        // 检查密码是否为空
        if (password == null || password.trim().isEmpty()) {
            return "密码不能为空";
        }

        // 检查密码长度
        if (password.length() < 6) {
            return "密码长度不能少于6位";
        }

        // 检查管理员账号格式 (G + 6位数字)
        if (userId.startsWith("G")) {
            if (!userId.matches("G\\d{6}")) {
                return "管理员账号格式错误！必须是 G 开头后接6位数字。";
            }
            user.setRole(1); // 管理员
        }
        // 检查普通用户账号格式 (6位数字)
        else {
            if (!userId.matches("\\d{6}")) {
                return "普通用户账号格式错误！必须是6位数字。";
            }
            user.setRole(0); // 普通用户
        }

        // 检查账号是否已存在
        try {
            User existingUser = userDAO.findByUserId(userId);
            if (existingUser != null) {
                return "该账号已被注册，请直接登录或使用其他账号。";
            }

            // 插入新用户
            boolean success = userDAO.insertUser(user);
            if (success) {
                return "注册成功！欢迎加入宠物领养系统。";
            } else {
                return "注册失败，请稍后重试。";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "注册失败：数据库错误 - " + e.getMessage();
        }
    }

    /**
     * 用户登录方法
     * @param user 待登录的用户对象
     * @return 登录结果信息
     */
    public String login(healing.pet.model.User user) {
        String userId = user.getUserId();
        String password = user.getPassword();

        // 检查账号或密码是否为空
        if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "账号或密码不能为空！";
        }

        // 检查账号格式是否正确
        if (!userId.matches("G\\d{6}") && !userId.matches("\\d{6}")) {
            return "账号格式不正确！普通用户为6位数字，管理员为G+6位数字。";
        }

        try {
            // 从数据库查询用户
            User dbUser = userDAO.findByUserId(userId);

            if (dbUser == null) {
                return "账号不存在，请先注册！";
            }

            // 验证密码
            if (!dbUser.getPassword().equals(password)) {
                return "密码错误，请重新输入！";
            }

            // 登录成功，复制用户信息
            user.setRole(dbUser.getRole());
            return "登录成功！欢迎回来，" + (user.isAdmin() ? "管理员" : "用户") + "。";

        } catch (SQLException e) {
            e.printStackTrace();
            return "登录失败：数据库错误 - " + e.getMessage();
        }
    }
}
