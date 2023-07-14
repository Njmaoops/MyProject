package com.example.blog_system_ssm.common;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 加盐算法
 *
 * @Date 2023/5/22 10:11
 */
public class PassWordUtils {
    // 1. 加盐并生成密码
    public static String encrypt(String password) {
        // a) 将uuid的 - 去掉
        String salt = String.valueOf(UUID.randomUUID()).replace("-", "");
        // b) 使用md5 生成加盐之后的密码
        String saltPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // c) 生成最终密码, 保存到数据库内
        String finalPassword = salt + saltPassword;
        return finalPassword;
    }

    // 2. 生成加盐的密码, ( 方法1的重载 )

    /**
     * 数据库中的最终密码
     *
     * @param password
     * @param salt
     * @return
     */
    public static String encrypt(String password, String salt) {
        String saltPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        String finalPassword = salt + saltPassword;
        return finalPassword;
    }

    // 3. 验证密码

    /**
     * @param inputPassword 用户输入的明文密码
     * @param finalPassword 数据库保存的最终密码
     * @return
     */
    public static boolean check(String inputPassword, String finalPassword) {
        // 数据合法性
        if (StringUtils.hasLength(inputPassword) && StringUtils.hasLength(finalPassword)
                && finalPassword.length() == 64) {
            //1. 得到盐值
            //分隔出来只会有两个数组, 0下标的是盐值 , 1下标的是 盐值 + password 生成的明文
            // 如果使用 $ 截取, 需要进行转义 \\$
            // 盐值是32位 , 因此也可以直接截取
            String salt = finalPassword.substring(0, 32);
            //2. 使用之前加密的步骤, 将明文密码和已经得到的盐值进行加密, 生成最终的密钥

            // 按照之前的逻辑生成的password ( 待确认的密码 )
            String confirm = PassWordUtils.encrypt(inputPassword, salt);
            //3. 进行对比校验是否正确
            return confirm.equals(finalPassword);
        }

        return false;
    }

    public static void main(String[] args) {
        String password = "123456";
        String finalPassword = PassWordUtils.encrypt(password);
        System.out.println("加密" + finalPassword);
        // 对比
        String inputPassword = "123456";
        System.out.println("对比 输入密码: " + inputPassword
                + "\n是否等于 : " + password + "->" + PassWordUtils.check(inputPassword, finalPassword));
        String inputPassword2 = "12345";
        System.out.println("对比 输入密码: " + inputPassword2
                + "\n是否等于 : " + password + "->" + PassWordUtils.check(inputPassword2, finalPassword));
    }
}
