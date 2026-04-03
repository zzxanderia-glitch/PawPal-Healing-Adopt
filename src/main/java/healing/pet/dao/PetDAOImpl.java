package healing.pet.dao;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.util.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 宠物DAO实现类
 * 组长提示：已优化为使用 try-with-resources 自动释放资源
 */
public class PetDAOImpl implements PetDAO {

    @Override
    public List<Animal> getAllPets() throws SQLException {
        List<Animal> petList = new ArrayList<>();
        String sql = "SELECT * FROM pet";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("数据库连接成功！");
            int count = 0;
            while (rs.next()) {
                count++;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int age = rs.getInt("age");
                String story = rs.getString("story");
                String photoPath = rs.getString("photo_path");
                String breed = rs.getString("breed");

                System.out.println("读取到宠物：" + id + " - " + name + " - type=" + type + " - story=" + story + " - photoPath=" + photoPath);

                Animal animal = null;

                if ("猫".equals(type) || "cat".equals(type)) {
                    System.out.println("创建 Cat 对象：" + name);
                    animal = new Cat(id, name, age, story, photoPath);
                } else if ("狗".equals(type) || "dog".equals(type)) {
                    System.out.println("创建 Dog 对象：" + name);
                    animal = new Dog(id, name, age, story, photoPath, breed);
                } else {
                    System.out.println("未知类型：" + type + "，跳过");
                }

                if (animal != null) {
                    petList.add(animal);
                }
            }
            System.out.println("总共读取到 " + count + " 条记录，成功加载 " + petList.size() + " 只宠物");
        } catch (SQLException e) {
            System.err.println("查询宠物列表失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return petList;
    }

    @Override
    public void updatePetStatus(int id, int status) throws SQLException {
        String sql = "UPDATE pet SET status = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("更新宠物状态失败 (ID: " + id + "): " + e.getMessage());
            throw e;
        }
    }
}