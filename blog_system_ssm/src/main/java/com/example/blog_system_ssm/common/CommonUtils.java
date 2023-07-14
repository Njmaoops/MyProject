package com.example.blog_system_ssm.common;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

/**
 * @Date 2023/5/26 19:02
 */
public class CommonUtils {
    /**
     * 将图片以base64编码格式传给前端
     *
     * @return
     */
    public static HashMap<String, String> getCaptcha() {
        // 1. 将图片名字编辑好
        String captchaName = LocalDateTime.now().toString();
        String[] split = captchaName.split("[T:.]");
        captchaName = split[0] + split[1] + split[2] + split[3];
        // 2. 指定图片路径
        String projectPath
                = "G:\\codeInfo\\Captcha\\";
//        String projectPath
//                = "/javaproject/blogsystem/captcha";
        String finalPath = projectPath + captchaName + ".png";
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 40);
        // 3. 写入图片
        lineCaptcha.write(finalPath);
        //4. 将图片转成文字
        File file = new File(finalPath);
        FileInputStream imageInFile = null;
        String encodingImage = null;
        try {
            imageInFile = new FileInputStream(file);
            // 5. 将数据转换成byte数组
            byte[] fileData = new byte[(int) file.length()];
            imageInFile.read(fileData);

            // 6. 将图片转换成base64格式的编码
            encodingImage = Base64.getEncoder().encodeToString(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                imageInFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 获取当前图片的code值,后续登录验证需要
        String captchaCode = lineCaptcha.getCode();
        HashMap<String, String> map = new HashMap<>();
        map.put("captchaCode", captchaCode);
        map.put("base64ImageCode", encodingImage);
        // 测试, 如果删除本地的图片 base64是否能够传输成功
        if (file.delete()) {
            System.out.println("文件删除成功");
        }
        // 返回base64编码格式的图片
        return map;
    }
}
