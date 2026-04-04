package healing.pet.dao;

import healing.pet.model.User;
import java.sql.SQLException;

public interface UserDAO {
    User findByUserId(String userId) throws SQLException;
    boolean insertUser(User user) throws SQLException;
    boolean updatePassword(String userId, String newPassword) throws SQLException;
}
