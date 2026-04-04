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
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("查询用户失败: " + e.getMessage());
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, password, role) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRole());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("插入用户失败: " + e.getMessage());
        } finally {
            DBUtils.close(null, pstmt, conn);
        }
    }

    @Override
    public boolean updatePassword(String userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("更新密码失败: " + e.getMessage());
        } finally {
            DBUtils.close(null, pstmt, conn);
        }
    }
}

