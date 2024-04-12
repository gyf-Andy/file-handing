package com.gyf.tools;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 郭云飞
 * @date 2022/3/6-16:07
 * @Description TODO
 */
public class CreateFile {

    public String makeDir(MultipartFile file){
        //生成文件的目标路径  临时文件存储路径
        String path = this.getClass().getClassLoader().getResource("static/temporary/").getPath();
        try {
            // 获取InoutString
            InputStream inputStream = file.getInputStream();
            // 创建文件  C:\测试\
            File toFile = new File(path+"/"+file.getOriginalFilename());
            String filePath=toFile.getAbsolutePath();
            if (!toFile .getParentFile().exists()) {
                toFile .getParentFile().mkdirs();
            }
            if(!toFile .exists()) {
                toFile .createNewFile();
            }
            // 写入文件
            OutputStream outputStream = new FileOutputStream(toFile);
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            return filePath;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
