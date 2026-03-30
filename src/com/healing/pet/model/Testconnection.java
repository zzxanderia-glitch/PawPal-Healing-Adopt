package com.healing.pet.model;

import com.healing.pet.util.DBUtils;

import java.sql.Connection;

public class Testconnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBUtils.getConnection();
            if (conn != null) {
                System.out.println("✅ 治愈系数据库连接成功！");
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("❌ 连接失败，请检查密码或数据库名！");
            e.printStackTrace();
        }
    }
}