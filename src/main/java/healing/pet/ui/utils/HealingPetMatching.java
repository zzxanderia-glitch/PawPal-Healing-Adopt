package healing.pet.ui.utils;

import healing.pet.model.Animal;
import healing.pet.service.MatchService;
import healing.pet.service.UserPreferences;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

public class HealingPetMatching extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private QuestionnairePanel questionnairePanel;
    private ResultPanel resultPanel;
    private UserPreferences userPrefs;
    private MatchService matchService;

    public HealingPetMatching() {
        matchService = new MatchService();

        setLayout(new BorderLayout());
        setBackground(new Color(255, 245, 235));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        questionnairePanel = new QuestionnairePanel();
        resultPanel = new ResultPanel();

        cardPanel.add(questionnairePanel, "问卷");
        cardPanel.add(resultPanel, "结果");

        add(cardPanel, BorderLayout.CENTER);
    }

    class QuestionnairePanel extends JPanel {
        private JPanel questionPanel;
        private JLabel matchStatusLabel;
        private JLabel imageLabel;
        private Timer shakeTimer;
        private List<Question> questions;
        private List<JComboBox<String>> answerBoxes;

        public QuestionnairePanel() {
            setLayout(new BorderLayout(15, 15));
            setBackground(new Color(255, 245, 235));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            initQuestions();
            initUI();
        }

        private void initQuestions() {
            questions = Arrays.asList(
                    new Question("【性格】你和萌友的性格合拍度？",
                            new String[]{"高冷独立", "佛系随缘", "适度粘人", "喜欢撒娇", "超级粘人"}),
                    new Question("【居住】你家的居住空间？",
                            new String[]{"仅限桌面", "宿舍单间", "小户型", "普通楼房", "超大别墅"}),
                    new Question("【陪伴】你能陪伴萌友的时间？",
                            new String[]{"几乎无", "偶尔几分钟", "1-2 小时", "3-4 小时", "5 小时以上"}),
                    new Question("【健康】你愿意为萌友健康投入？",
                            new String[]{"零医疗投入", "仅限低维护", "只要不生病", "定期体检", "完全愿意护理"}),
                    new Question("【掉毛】你对掉毛的容忍度？",
                            new String[]{"洁癖零容忍", "最好不掉", "希望少掉毛", "可以每天粘毛", "完全不介意"}),
                    new Question("【预算】你每月的养宠预算？",
                            new String[]{"仅免费", "100 元内", "200-500 元", "500-1000 元", "无上限"})
            );
            answerBoxes = new ArrayList<>();
        }

        private void initUI() {
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(200, 150));
            add(imageLabel, BorderLayout.NORTH);

            questionPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            questionPanel.setBackground(new Color(255, 245, 235));
            questionPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(255, 180, 160), 2),
                    "  缘分小测验  ",
                    SwingConstants.CENTER,
                    SwingConstants.TOP
            ));

            questionPanel.setForeground(new Color(255, 100, 80));

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                JLabel label = new JLabel(q.text);
                label.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                label.setForeground(new Color(100, 80, 70));
                JComboBox<String> combo = new JComboBox<>(q.options);
                combo.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
                combo.setSelectedIndex(2);
                combo.setBackground(Color.WHITE);
                combo.setForeground(new Color(80, 60, 50));
                combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                final int questionIndex = i;
                combo.addActionListener(e -> onAnswerChanged(questionIndex, combo));
                answerBoxes.add(combo);
                questionPanel.add(label);
                questionPanel.add(combo);
            }

            add(questionPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            matchStatusLabel = new JLabel(" 等待你的回答，让我们一起寻找有缘的萌友吧~");
            matchStatusLabel.setFont(new Font("Microsoft YaHei", Font.ITALIC, 13));
            matchStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
            matchStatusLabel.setForeground(new Color(255, 100, 100));

            JButton submitBtn = new JButton(" 查看缘分报告 ");
            submitBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
            submitBtn.setBackground(new Color(255, 150, 130));
            submitBtn.setForeground(Color.WHITE);
            submitBtn.setFocusPainted(false);
            submitBtn.setBorderPainted(false);
            submitBtn.setOpaque(true);
            submitBtn.setContentAreaFilled(true);
            submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitBtn.addActionListener(e -> submitQuestionnaire());

            submitBtn.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    submitBtn.setBackground(new Color(255, 120, 100));
                }
            });

            submitBtn.addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent e) {
                    submitBtn.setBackground(new Color(255, 150, 130));
                }
            });



            bottomPanel.add(matchStatusLabel, BorderLayout.CENTER);
            bottomPanel.add(submitBtn, BorderLayout.SOUTH);
            add(bottomPanel, BorderLayout.SOUTH);

            updateImage(0);
        }

        private void updateImage(int questionIndex) {
            ImageIcon icon = createQuestionImage();
            imageLabel.setIcon(icon);
        }

        private ImageIcon createQuestionImage() {
            BufferedImage image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            // 渐变背景
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 230, 210),
                    200, 150, new Color(255, 200, 180));
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, 200, 150, 25, 25);

            // 绘制可爱猫脸
            g2d.setColor(new Color(255, 180, 140));
            g2d.fillOval(55, 45, 40, 35);
            g2d.fillOval(105, 45, 40, 35);

            // 眼睛
            g2d.setColor(Color.WHITE);
            g2d.fillOval(62, 52, 18, 18);
            g2d.fillOval(120, 52, 18, 18);
            g2d.setColor(Color.BLACK);
            g2d.fillOval(68, 56, 10, 10);
            g2d.fillOval(126, 56, 10, 10);

            // 鼻子和嘴巴
            g2d.setColor(new Color(255, 150, 130));
            g2d.fillOval(92, 72, 16, 12);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(85, 78, 30, 20, 0, -180);

            // 腮红
            g2d.setColor(new Color(255, 180, 180, 150));
            g2d.fillOval(50, 70, 20, 12);
            g2d.fillOval(130, 70, 20, 12);

            // 耳朵
            g2d.setColor(new Color(255, 180, 140));
            int[] xPoints = {45, 65, 55};
            int[] yPoints = {35, 35, 15};
            g2d.fillPolygon(xPoints, yPoints, 3);
            int[] xPoints2 = {135, 155, 145};
            int[] yPoints2 = {35, 35, 15};
            g2d.fillPolygon(xPoints2, yPoints2, 3);

            // 胡须
            g2d.setColor(new Color(200, 150, 130));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(40, 65, 20, 60);
            g2d.drawLine(40, 68, 18, 68);
            g2d.drawLine(40, 71, 20, 76);
            g2d.drawLine(160, 65, 180, 60);
            g2d.drawLine(160, 68, 182, 68);
            g2d.drawLine(160, 71, 180, 76);

            g2d.dispose();
            return new ImageIcon(image);
        }

        private void onAnswerChanged(int questionIndex, JComboBox<String> combo) {
            startShakeAnimation(combo);
            matchStatusLabel.setText(String.format(" 匹配度正在升温... 当前第 %d 题，共 %d 题 ",
                    questionIndex + 1, questions.size()));
        }

        private void startShakeAnimation(JComponent component) {
            if (shakeTimer != null && shakeTimer.isRunning()) {
                shakeTimer.stop();
            }
            Point originalPos = component.getLocation();
            final int[] offsets = {0, 5, -5, 5, -5, 0};
            shakeTimer = new Timer(50, new ActionListener() {
                int step = 0;
                public void actionPerformed(ActionEvent e) {
                    if (step < offsets.length) {
                        component.setLocation(originalPos.x + offsets[step], originalPos.y);
                        step++;
                    } else {
                        component.setLocation(originalPos);
                        ((Timer)e.getSource()).stop();
                    }
                }
            });
            shakeTimer.start();
        }

        private void submitQuestionnaire() {
            userPrefs = new UserPreferences();
            userPrefs.setPersonalityScore(answerBoxes.get(0).getSelectedIndex() + 1);
            userPrefs.setLivingSpaceScore(answerBoxes.get(1).getSelectedIndex() + 1);
            userPrefs.setCompanionTimeScore(answerBoxes.get(2).getSelectedIndex() + 1);
            userPrefs.setHealthCareScore(answerBoxes.get(3).getSelectedIndex() + 1);
            userPrefs.setSheddingScore(answerBoxes.get(4).getSelectedIndex() + 1);
            userPrefs.setBudgetScore(answerBoxes.get(5).getSelectedIndex() + 1);

            startCompletionAnimation();

            Timer animationTimer = new Timer(1200, e -> {
                try {
                    List<MatchService.MatchResult> results = matchService.matchWithScore(userPrefs);
                    resultPanel.setResults(results, userPrefs);
                    cardLayout.show(cardPanel, "结果");
                } catch (java.sql.SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            " 匹配失败：" + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            animationTimer.setRepeats(false);
            animationTimer.start();
        }

        private void startCompletionAnimation() {
            JDialog animDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(HealingPetMatching.this), " 缘分正在连接 ", true);
            animDialog.setSize(400, 300);
            animDialog.setLocationRelativeTo(HealingPetMatching.this);
            animDialog.setUndecorated(true);
            animDialog.setBackground(new Color(0, 0, 0, 0));

            JPanel animPanel = new JPanel() {
                private int frame = 0;
                private Timer timer;

                {
                    timer = new Timer(50, e -> {
                        frame++;
                        repaint();
                        if (frame > 60) {
                            timer.stop();
                            animDialog.dispose();
                        }
                    });
                    timer.start();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 半透明粉色背景
                    g2d.setColor(new Color(255, 200, 180, 220));
                    g2d.fillRoundRect(0, 0, 400, 300, 20, 20);

                    // 绘制动态爱心雨
                    for (int i = 0; i < 30; i++) {
                        double angle = frame * 0.1 + i * 0.5;
                        int x = 200 + (int)(Math.sin(angle) * (80 + i * 3));
                        int y = 150 + (int)(Math.cos(angle * 1.5) * (60 + i * 2)) - frame * 2;
                        int size = 12 + (i % 5) * 3;

                        // 绘制爱心形状
                        drawHeart(g2d, x, y, size, new Color(255, 100 + (i % 50), 100 + (i % 50), 200 - frame * 3));
                    }

                    // 中心文字
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
                    String text = " 缘分已到 ";
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.drawString(text, 200 - fm.stringWidth(text)/2, 270);
                }

                private void drawHeart(Graphics2D g2d, int x, int y, int size, Color color) {
                    g2d.setColor(color);
                    int r = size / 2;
                    // 上部分两个圆
                    g2d.fillOval(x - r, y - r, r * 2, r * 2);
                    g2d.fillOval(x, y - r, r * 2, r * 2);
                    // 下部分三角形
                    int[] xPoints = {x - r, x + r, x + r/2, x - r/2};
                    int[] yPoints = {y, y, y + r * 2, y + r * 2};
                    g2d.fillPolygon(xPoints, yPoints, 4);
                }
            };

            animPanel.setOpaque(false);
            animDialog.add(animPanel);
            animDialog.setVisible(true);
        }
    }

    class ResultPanel extends JPanel {
        private JTextArea resultArea;

        public ResultPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(255, 245, 235));
            setBorder(new EmptyBorder(20, 20, 20, 20));

            resultArea = new JTextArea();
            resultArea.setEditable(false);
            resultArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
            resultArea.setBackground(new Color(255, 250, 240));
            resultArea.setLineWrap(true);
            resultArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(resultArea);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(255, 180, 160), 2),
                    " 缘分报告 ",
                    SwingConstants.CENTER,
                    SwingConstants.TOP
            ));

            scrollPane.getViewport().setBackground(new Color(255, 250, 245));

            JButton backBtn = new JButton(" 重新测试 ");
            backBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
            backBtn.setBackground(new Color(255, 150, 130));
            backBtn.setForeground(Color.WHITE);
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setOpaque(true);
            backBtn.setContentAreaFilled(true);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> cardLayout.show(cardPanel, "问卷"));

            backBtn.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    backBtn.setBackground(new Color(255, 120, 100));
                }
            });

            backBtn.addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent e) {
                    backBtn.setBackground(new Color(255, 150, 130));
                }
            });

            add(scrollPane, BorderLayout.CENTER);
            add(backBtn, BorderLayout.SOUTH);
        }

        public void setResults(List<MatchService.MatchResult> results, UserPreferences userPrefs) {
            StringBuilder sb = new StringBuilder();
            sb.append("    萌友缘分报告    \n\n");

            if (!results.isEmpty()) {
                double topMatch = results.get(0).getSimilarity() * 100;
                sb.append("【总匹配度】").append(String.format("%.0f%%", topMatch)).append("\n\n");
            }

            sb.append("【最适配的 3 位萌友】\n\n");

            for (int i = 0; i < Math.min(3, results.size()); i++) {
                MatchService.MatchResult r = results.get(i);
                Animal pet = r.getPet();
                double matchPercent = r.getSimilarity() * 100;
                sb.append(i+1).append(". ").append(pet.getName()).append("\n");
                sb.append("   匹配度：").append(String.format("%.0f%%", matchPercent)).append("\n");
                sb.append("   故事：").append(pet.getCareGuide()).append("\n");
                sb.append("   合拍理由：").append(generateMatchReason(pet, userPrefs)).append("\n\n");
            }

            String[] quotes = {
                    "也许前世你们就曾互相陪伴过~",
                    "每一次相遇，都是命中注定的缘分~",
                    "在茫茫人海中，总有一个小生命在等你~",
                    "爱，是最好的治愈良药~"
            };
            Random random = new Random();
            sb.append("\n【治愈语录】").append(quotes[random.nextInt(quotes.length)]);

            resultArea.setText(sb.toString());
        }

        private String generateMatchReason(Animal pet, UserPreferences userPrefs) {
            return "根据你的问卷结果，这只萌宠与你的需求非常匹配！";
        }
    }

    static class Question {
        String text;
        String[] options;

        Question(String text, String[] options) {
            this.text = text;
            this.options = options;
        }
    }
}
