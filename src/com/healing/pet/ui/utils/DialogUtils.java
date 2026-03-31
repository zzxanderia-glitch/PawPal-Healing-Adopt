package com.healing.pet.ui.utils;

import com.healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;

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
