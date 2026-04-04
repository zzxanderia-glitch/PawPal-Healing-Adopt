package healing.pet.view.components;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import javax.swing.*;
import java.awt.*;

public class PetDetailDialog extends JDialog {

    public PetDetailDialog(JFrame parent, Animal pet) {
        super(parent, "宠物详情", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // 左侧大图
        JLabel imgLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(pet.getPhotoPath()));
        Image img = icon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(img));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imgLabel, BorderLayout.WEST);

        // 右侧故事
        JTextArea storyArea = new JTextArea();
        storyArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        storyArea.setLineWrap(true);
        storyArea.setWrapStyleWord(true);
        storyArea.setEditable(false);

        StringBuilder info = new StringBuilder();
        info.append("名字：").append(pet.getName()).append("\n\n");
        info.append("年龄：").append(pet.getAge()).append("\n\n");

        // 根据宠物类型显示不同信息
        if (pet instanceof Dog) {
            info.append("品种：").append(((Dog) pet).getBreed()).append("\n\n");
        } else if (pet instanceof Cat) {
            info.append("类型：猫咪\n\n");
        }

        info.append("简介：").append(pet.getStory()).append("\n\n");
        info.append("详细故事：").append(pet.getDetailStory()).append("\n\n");
        info.append("习性：").append(pet.getHabits()).append("\n\n");
        info.append("偏好：").append(pet.getPreference());

        storyArea.setText(info.toString());
        JScrollPane sp = new JScrollPane(storyArea);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(sp, BorderLayout.CENTER);

        setVisible(true);
    }
}
