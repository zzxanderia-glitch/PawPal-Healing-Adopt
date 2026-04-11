package healing.pet.view.components;

import healing.pet.model.Animal;
import healing.pet.model.AdoptionRequest;
import healing.pet.service.AdoptionService;
import healing.pet.util.UserContext;
import healing.pet.view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PetDetailDialog extends JDialog {
    private Animal pet;
    private MainFrame mainFrame;
    private AdoptionService adoptionService;


    public PetDetailDialog(MainFrame parent, Animal pet) {
        super(parent, "宠物详情", true);
        this.mainFrame = parent;
        this.pet = pet;
        this.adoptionService = new AdoptionService();


        setSize(650, 500);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        Color themeBg = mainFrame.getCurrentTheme().getContentBackgroundColor();
        Color themeText = mainFrame.getCurrentTheme().getContentTextColor();
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;

        Color cardBg = isDark ? new Color(45, 45, 48) : Color.WHITE;
        Color cardBorder = isDark ? new Color(80, 80, 85) : new Color(200, 180, 200);
        Color labelColor = isDark ? new Color(180, 160, 180) : new Color(100, 80, 100);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel(pet.getName() + " 的详细档案");
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        title.setForeground(themeText);
        header.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("x");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 24));
        closeBtn.setForeground(isDark ? new Color(200, 200, 200) : new Color(150, 150, 150));
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(280, 0));

        JLabel imageLabel = new JLabel(loadPetImageIcon(pet.getPhotoPath(), 260, 320));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        centerPanel.add(imagePanel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 0, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 10);
        gbc.weightx = 1.0;

        Font labelFont = new Font("微软雅黑", Font.BOLD, 14);
        Font valueFont = new Font("微软雅黑", Font.PLAIN, 14);

        int row = 0;

        addInfoRow(infoPanel, "年 龄：", String.valueOf(pet.getAge()) + " 岁", labelFont, valueFont, labelColor, themeText, gbc, row++);

        String breed = "未知";
        if (pet instanceof healing.pet.model.Dog) breed = ((healing.pet.model.Dog) pet).getBreed();
        else if (pet instanceof healing.pet.model.Cat) breed = ((healing.pet.model.Cat) pet).getBreed();
        addInfoRow(infoPanel, "品 种：", breed, labelFont, valueFont, labelColor, themeText, gbc, row++);

        addInfoRow(infoPanel, "简 介：", pet.getStory(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "详细故事：", pet.getDetailStory(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "习 性：", pet.getHabits(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "偏 好：", pet.getPreference(), labelFont, valueFont, labelColor, themeText, gbc, row++);

        centerPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);


        if (pet.getStatus() == 0) {
            boolean hasApplied = hasUserApplied(pet.getId());

            JButton applyBtn = new JButton();
            applyBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
            applyBtn.setFocusPainted(false);
            applyBtn.setBorderPainted(false);
            applyBtn.setOpaque(true);
            applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (hasApplied) {
                applyBtn.setText("已提交申请");
                applyBtn.setForeground(Color.WHITE);
                applyBtn.setBackground(new Color(180, 180, 180));
                applyBtn.setEnabled(false);
                applyBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                applyBtn.setText("申请领养");
                applyBtn.setForeground(Color.WHITE);
                applyBtn.setBackground(isDark ? new Color(100, 150, 200) : new Color(255, 107, 129));
                applyBtn.addActionListener(e -> showApplyForm());

                applyBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        applyBtn.setBackground(isDark ? new Color(80, 130, 180) : new Color(255, 80, 105));
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        applyBtn.setBackground(isDark ? new Color(100, 150, 200) : new Color(255, 107, 129));
                    }
                });
            }
            footer.add(applyBtn);
        }
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private boolean hasUserApplied(int petId) {
        try {
            String userId = UserContext.getInstance().getCurrentUser().getUserId();
            java.util.List<AdoptionRequest> requests = adoptionService.getUserRequests(userId);
            for (AdoptionRequest request : requests) {
                if (request.getPetId() == petId) {
                    String status = request.getStatus();
                    if ("待审核".equals(status) || "审核中".equals(status) || "已通过".equals(status)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

        private void showApplyForm() {
        JDialog applyDialog = new JDialog(this, "领养申请", true);
        applyDialog.setSize(520, 620);
        applyDialog.setLocationRelativeTo(this);
        applyDialog.setUndecorated(true);
        applyDialog.setBackground(new Color(0, 0, 0, 0));

        Color bgWarm = new Color(255, 250, 235);
        Color primaryPink = new Color(255, 107, 129);
        Color hoverPink = new Color(255, 80, 105);
        Color textColor = new Color(50, 50, 50);
        Color labelColor = new Color(100, 100, 100);
        Color inputBg = new Color(255, 255, 255);

        JPanel dialogPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 25, 25);

                GradientPaint gradient = new GradientPaint(0, 0, bgWarm, 0, getHeight(), new Color(255, 245, 225));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(255, 107, 129, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

                g2.dispose();
            }
        };
        dialogPanel.setLayout(new BorderLayout(0, 0));
        dialogPanel.setBorder(new EmptyBorder(30, 35, 30, 35));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerLeft.setOpaque(false);
        JLabel headerLabel = new JLabel("领养申请表");
        headerLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        headerLabel.setForeground(textColor);
        headerLeft.add(headerLabel);

        JLabel petNameLabel = new JLabel(" - " + pet.getName());
        petNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        petNameLabel.setForeground(primaryPink);
        headerLeft.add(petNameLabel);

        headerPanel.add(headerLeft, BorderLayout.WEST);

        JButton closeBtn = new JButton("x");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(new Color(150, 150, 150));
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeBtn.setForeground(primaryPink);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeBtn.setForeground(new Color(150, 150, 150));
            }
        });
        closeBtn.addActionListener(e -> applyDialog.dispose());
        headerPanel.add(closeBtn, BorderLayout.EAST);

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        headerContainer.add(headerPanel, BorderLayout.CENTER);
        headerContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        dialogPanel.add(headerContainer, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;

        Font labelFont = new Font("微软雅黑", Font.BOLD, 14);
        Font inputFont = new Font("微软雅黑", Font.PLAIN, 14);

        int row = 0;

        JLabel nameLabel = new JLabel("姓 名：");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        formPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        nameField.setFont(inputFont);
        nameField.setPreferredSize(new Dimension(0, 42));
        nameField.setBackground(inputBg);
        nameField.setForeground(textColor);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                nameField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryPink, 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                nameField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        gbc.gridx = 1; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        formPanel.add(nameField, gbc);
        row++;

        JLabel phoneLabel = new JLabel("电 话：");
        phoneLabel.setFont(labelFont);
        phoneLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        formPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField();
        phoneField.setFont(inputFont);
        phoneField.setPreferredSize(new Dimension(0, 42));
        phoneField.setBackground(inputBg);
        phoneField.setForeground(textColor);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        phoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                phoneField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryPink, 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                phoneField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        gbc.gridx = 1; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        formPanel.add(phoneField, gbc);
        row++;

        JLabel addressLabel = new JLabel("住 址：");
        addressLabel.setFont(labelFont);
        addressLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        formPanel.add(addressLabel, gbc);

        JTextField addressField = new JTextField();
        addressField.setFont(inputFont);
        addressField.setPreferredSize(new Dimension(0, 42));
        addressField.setBackground(inputBg);
        addressField.setForeground(textColor);
        addressField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        addressField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                addressField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryPink, 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                addressField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        gbc.gridx = 1; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        formPanel.add(addressField, gbc);
        row++;

        JLabel reasonLabel = new JLabel("申请理由：");
        reasonLabel.setFont(labelFont);
        reasonLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        formPanel.add(reasonLabel, gbc);

        JTextArea reasonArea = new JTextArea(4, 20);
        reasonArea.setFont(inputFont);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBackground(inputBg);
        reasonArea.setForeground(textColor);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        reasonArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                reasonArea.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryPink, 2),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                reasonArea.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setPreferredSize(new Dimension(0, 110));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        formPanel.add(scrollPane, gbc);
        row++;

        dialogPanel.add(formPanel, BorderLayout.CENTER);

        JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipPanel.setOpaque(false);
        tipPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        JLabel tipLabel = new JLabel("* 请认真填写申请信息，我们会尽快审核");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipLabel.setForeground(new Color(180, 180, 180));
        tipPanel.add(tipLabel);
        dialogPanel.add(tipPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton cancelBtn = new JButton("取 消");
        cancelBtn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        cancelBtn.setForeground(new Color(100, 100, 100));
        cancelBtn.setBackground(new Color(240, 240, 240));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setOpaque(true);
        cancelBtn.setPreferredSize(new Dimension(110, 42));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelBtn.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelBtn.setBackground(new Color(240, 240, 240));
            }
        });
        cancelBtn.addActionListener(e -> applyDialog.dispose());

        JButton submitBtn = new JButton("提 交 申 请");
        submitBtn.setFont(new Font("微软雅黑", Font.BOLD, 15));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(primaryPink);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.setPreferredSize(new Dimension(160, 42));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                submitBtn.setBackground(hoverPink);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                submitBtn.setBackground(primaryPink);
            }
        });
        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String reason = reasonArea.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || reason.isEmpty()) {
                JOptionPane.showMessageDialog(applyDialog, "请填写完整的申请信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!phone.matches("^1[3-9]\\d{9}$")) {
                JOptionPane.showMessageDialog(applyDialog, "请输入正确的手机号码！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String userId = UserContext.getInstance().getCurrentUser().getUserId();
                adoptionService.applyAdoption(userId, pet.getId(), name, phone, address, reason);
                JOptionPane.showMessageDialog(applyDialog, "申请已提交，请等待管理员审核！", "提交成功", JOptionPane.INFORMATION_MESSAGE);
                applyDialog.dispose();
                dispose();
                if (mainFrame != null) {
                    mainFrame.refreshContent();
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(applyDialog, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(applyDialog, "申请失败，请先登录！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(submitBtn);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.add(tipPanel, BorderLayout.NORTH);
        bottomContainer.add(buttonPanel, BorderLayout.SOUTH);
        dialogPanel.add(bottomContainer, BorderLayout.SOUTH);

        applyDialog.add(dialogPanel);
        applyDialog.setVisible(true);
    }

    private void addInfoRow(JPanel panel, String label, String value, Font labelFont, Font valueFont, Color labelColor, Color textColor, GridBagConstraints gbc, int row) {
        if (value == null) value = "暂无数据";

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(labelFont);
        lbl.setForeground(labelColor);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel("<html><div style='width: 250px;'>" + value + "</div></html>");
        val.setFont(valueFont);
        val.setForeground(textColor);
        panel.add(val, gbc);
    }

    private ImageIcon loadPetImageIcon(String path, int width, int height) {
        try {
            if (path == null) return createPlaceholder(width, height);
            if (path.startsWith("/")) path = path.substring(1);
            if (path.startsWith("images/")) path = path.substring(7);

            BufferedImage img = null;
            InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + path);
            if (is != null) {
                img = ImageIO.read(is);
                is.close();
            }
            if (img == null) {
                File f = new File("src/main/resources/images/" + path);
                if (f.exists()) img = ImageIO.read(f);
            }

            if (img != null) {
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createPlaceholder(width, height);
    }

    private ImageIcon createPlaceholder(int w, int h) {
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(isDark ? new Color(50, 50, 55) : new Color(240, 230, 240));
        g.fillRoundRect(0, 0, w, h, 15, 15);
        g.setColor(isDark ? new Color(150, 150, 150) : new Color(180, 160, 180));
        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.drawString("暂无图片", w/2 - 30, h/2);
        g.dispose();
        return new ImageIcon(img);
    }
}
