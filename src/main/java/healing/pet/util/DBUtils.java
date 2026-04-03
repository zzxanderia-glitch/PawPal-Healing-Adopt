package healing.pet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库工具类 —— 组长提供
 */
public class DBUtils {
    // 数据库连接配置 (根据你本地 MySQL 修改)
    private static final String URL = "jdbc:mysql://localhost:3306/pawpal?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8";
    private static final String USER = "root";  // 改成你的 MySQL 用户名
    private static final String PASSWORD = "123456";  // 改成你的 MySQL 密码

    // 静态代码块加载驱动 (只需加载一次)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ 找不到数据库驱动，请检查 Jar 包是否导入！");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭连接的通用方法 (养成好习惯，防止内存泄漏)
     */
    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
