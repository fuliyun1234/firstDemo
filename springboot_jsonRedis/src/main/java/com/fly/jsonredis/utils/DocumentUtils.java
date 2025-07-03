package com.fly.jsonredis.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;


@Slf4j
public class DocumentUtils {


    //读取xml文件
    public static String readFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            log.error("文件路径不能为空");
            return null;
        }
        String xmlString = null;
        try {
            // 使用ClassLoader来获取资源文件的URL
            URL url = DocumentUtils.class.getClassLoader().getResource(filePath);
            if (url == null) {
                log.error("文件未找到: {}", filePath);
                return null;
            }
            try (BufferedReader bufferedReader = new BufferedReader(new java.io.InputStreamReader(url.openStream(), "UTF-8"))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonContent.append(line).append("\n");
                }
                xmlString = jsonContent.toString().trim();
            }
        } catch (IOException e) {
            log.error("读取文件失败: {}", e.getMessage());
        } catch (org.json.JSONException e) {
            log.error("解析文件失败: {}", e.getMessage());
        }
        return xmlString;
    }



}
