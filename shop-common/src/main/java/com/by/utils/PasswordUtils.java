package com.by.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 密码加密
     * @param password 密码
     * @return 散列密码
     */
    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 密码比对
     * @param password 用户输入的密码
     * @param dbpassword 数据库密文
     * @return 如果密码匹配，则为true，否则为false
     */
    public static boolean checkpw(String password, String dbpassword) {
        return BCrypt.checkpw(password, dbpassword);
    }

}
