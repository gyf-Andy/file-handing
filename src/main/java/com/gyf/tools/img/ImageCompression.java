package com.gyf.tools.img;

import com.gyf.tools.GetDateString;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 郭云飞
 * @date 2022/3/8-11:29
 * @Description TODO
 */
public class ImageCompression {

    private static Logger logger = LoggerFactory.getLogger(ImageCompression.class);

    final String path = this.getClass().getClassLoader().getResource("static/temporary/").getPath();

    public static void main(String[] args) throws IOException {
        byte[] bytes = ImageCompression.fileToByteArray("C:\\Users\\22413\\Pictures\\03.jpg");
        long l = System.currentTimeMillis();
        bytes = ImageCompression.compressPicForScale(bytes, 4096, "x");// 图片小于300kb
        System.out.println(System.currentTimeMillis() - l);
        ImageCompression.byteArrayToFile( bytes,"C:\\Users\\22413\\Pictures\\20.jpg");

    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @param imageId     影像编号
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize, String imageId) {
        if (imageBytes == null || imageBytes.length <= 0) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;

        long copyDesFileSize=desFileSize;
        while (imageBytes.length < copyDesFileSize * 1024){
            copyDesFileSize= copyDesFileSize/2;
        }

        double accuracy=(double) (copyDesFileSize*1024)/srcSize;

        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(1.0f)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();

                inputStream.close();
                outputStream.close();
            }
            logger.info("【图片压缩】imageId={} | 图片原大小={}kb | 压缩后大小={}kb",
                    imageId, srcSize / 1024, imageBytes.length / 1024);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 图片转字节数组
     * @param src_path
     * @return
     */
    public static byte[] fileToByteArray(String src_path){
        File src=new File(src_path);
        byte[] dest=null;
        FileInputStream fis=null;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try {
            fis=new FileInputStream(src);
            byte[] temp=new byte[1024];
            int len=-1;
            while((len=fis.read(temp))!=-1){
                baos.write(temp,0,len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字节数组转图片
     * @param src
     * @param dest_path
     */
    public static void byteArrayToFile(byte[] src,String dest_path){
        ByteArrayInputStream bais=new ByteArrayInputStream(src);
        File dest=new File(dest_path);
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(dest);
            byte[] temp=new byte[1024];
            int len=-1;
            while((len=bais.read(temp))!=-1) {
                fos.write(temp,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加图片logo
     * @param file 原文件
     * @param logoFile 图片文件
     * @return
     * @throws IOException
     */
    public String imgAddImageLogo(MultipartFile file,MultipartFile logoFile,float compression,float opacity,Positions positions) throws IOException {

        String fileName=file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        //获取文件类型
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        //为获取图片的长和高做准备
        BufferedImage bufferedImage =ImageIO.read(file.getInputStream());

        System.out.println(compression);
        //调整logo图片大小
        ImageCompression imageCompression=new ImageCompression();
        String filePath = imageCompression.scale(logoFile,Double.parseDouble(String.valueOf(compression)));

        //watermark(位置，水印图，透明度)
        Thumbnails.of(file.getInputStream()) //输入源文件
                .size(bufferedImage.getWidth(), bufferedImage.getHeight()) //设定宽高
                .watermark(positions, ImageIO.read(new File(filePath)), opacity) //添加水印
                .outputQuality(1.0f) //输出质量
                .toFile(path+fileName+GetDateString.getDate()+fileType); //输出位置
        Files.delete(Paths.get(filePath)); //删除水印图片
        return (path+fileName+GetDateString.getDate()+fileType).substring(1);
    }

    /**
     * 按照比例进行缩放
     * @param file 源图片
     * @param num 质量比例如 0.8
     * @throws IOException
     */
    public String scale(MultipartFile file, double num) throws IOException {
        String filePath=path+file.getOriginalFilename().replace(".",GetDateString.getUUID()+".");
        Thumbnails.of(file.getInputStream()).scale(num).toFile(filePath);
        return filePath.substring(1);
    }


}
