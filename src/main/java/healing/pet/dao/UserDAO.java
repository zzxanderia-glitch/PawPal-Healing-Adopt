package healing.pet.dao;

import healing.pet.model.User;
import java.sql.SQLException;

public interface UserDAO {
    /** 根据用户ID查询普通用户 */
    User findByUserId(String userId) throws SQLException;
    
    /** 根据管理员ID查询管理员 */
    User findByAdminId(String adminId) throws SQLException;
    
    /** 插入新用户 */
    boolean insertUser(User user) throws SQLException;
    
    boolean updatePassword(String userId, String newPassword) throws SQLException;
}
