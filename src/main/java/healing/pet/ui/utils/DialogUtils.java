package healing.pet.ui.utils;

import healing.pet.view.MainFrame;
import javax.swing.*;

/**
 * 对话框工具类
 * 组长提示：已合并冲突，保留了完整的功能函数
 */
public class DialogUtils {

    /**
     * 显示信息对话框
     */
    public static void showInfoDialog(MainFrame parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "提示",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * 显示确认对话框
     */
    public static int showConfirmDialog(MainFrame parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "确认",
                JOptionPane.YES_NO_OPTION
        );
    }

    /**
     * 显示错误对话框
     */
    public static void showErrorDialog(MainFrame parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "错误",
                JOptionPane.ERROR_MESSAGE
        );
    }
}