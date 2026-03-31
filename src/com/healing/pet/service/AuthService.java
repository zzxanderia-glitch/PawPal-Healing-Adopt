package com.healing.pet.service;

import com.healing.pet.model.User;

public class AuthService {

    /**
     * 用户注册方法
     * @param user 待注册的用户对象
     * @return 注册结果信息
     */
    public String register(User user) {
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

        // 检查管理员账号格式 (G + 6位数字)
        if (userId.startsWith("G")) {
            if (!userId.matches("G\\d{6}")) {
                return "管理员账号格式错误！必须是 G 开头后接6位数字。";
            }
        }
        // 检查普通用户账号格式 (6位数字)
        else {
            if (!userId.matches("\\d{6}")) {
                return "普通用户账号格式错误！必须是6位数字。";
            }
        }

        // 所有检查通过，注册成功
        return "注册成功！欢迎加入宠物领养系统。";
    }

    /**
     * 用户登录方法
     * @param user 待登录的用户对象
     * @return 登录结果信息
     */
    public String login(User user) {
        String userId = user.getUserId();
        String password = user.getPassword();

        // 检查账号或密码是否为空
        if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "账号或密码不能为空！";
        }

        // 检查账号格式是否正确（通过User类的方法判断）
        if (!user.isAdmin() && !user.isNormalUser()) {
            return "账号格式不正确！";
        }

        // 在实际项目中，这里应该去数据库验证账号密码是否匹配
        // 本示例中，只要格式正确即视为登录成功
        return "登录成功！欢迎回来，" + (user.isAdmin() ? "管理员" : "用户") + "。";
    }
}
