package healing.pet.dao;

import healing.pet.model.User;
import healing.pet.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));
                user.setUsername(rs.getString("username"));
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByAdminId(String adminId) throws SQLException {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User admin = new User();
                admin.setUserId(rs.getString("admin_id")); // 统一用 userId 存储 ID
                admin.setPassword(rs.getString("password"));
                admin.setRole(rs.getInt("role"));
                admin.setUsername("管理员"); 
                return admin;
            }
        }
        return null;
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        // 检查是否已存在
        if (findByUserId(user.getUserId()) != null) {
            return false;
        }
        
        String sql = "INSERT INTO user (user_id, password, username, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUsername() != null ? user.getUsername() : "新用户");
            pstmt.setString(4, user.getPhone() != null ? user.getPhone() : "");
            pstmt.setInt(5, 0); // 默认普通用户
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePassword(String userId, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }
}

