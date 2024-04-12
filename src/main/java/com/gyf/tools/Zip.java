package com.gyf.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author 郭云飞
 * @date 2022/3/18-14:16
 * @Description TODO
 */
public class Zip {

    private static Logger log = LogManager.getLogger(ZipFile.class);

    public static void main( String[] args ) {
        String filePath = "C:\\Users\\22413\\Desktop\\简历"; // 代表一个文件夹
        String zipPath = "C:\\Users\\22413\\Desktop\\" + "tempTest.zip"; // 代表一个压缩文件
        //zipMutiFile(filePath, zipPath);
        unzip(zipPath,"C:\\Users\\22413\\Desktop\\1234");
    }

    //压缩文件
    public static void zipMutiFile( String filePath, String zipPath ) {
        // 文件夹为空
        if (StringUtils.isEmpty(filePath)) {
            log.error("filePath is null");
            return;
        }

        // 文件名为空
        if (StringUtils.isEmpty(zipPath)) {
            log.error("zipPath is null");
            return;
        }
        InputStream input = null;
        File file = new File(filePath); // 要被压缩的文件夹
        File zipFile = new File(zipPath); // 压缩后的文件
        ZipOutputStream zipOut = null; // 该类实现了以ZIP文件格式写入文件的输出流过滤器。 包括对压缩和未压缩条目的支持。
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));   // 1 申请的资源
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File tempFile : files) {
                        input = new FileInputStream(tempFile);  // 2 如果是两个文件，会两次申请 FileInputStream 资源
                        // ZipEntry:表示zip文件条目   http://www.matools.com/api/java8
                        // putNextEntry:开始编写新的ZIP文件条目，并将流定位到条目数据的开头。
                        zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + tempFile.getName()));
                        int temp;
                        while ((temp = input.read()) != -1) {
                            // write将一个字节写入压缩的输出流。 该方法将阻塞直到该字节被写入。
                            zipOut.write(temp);
                        }
                        // 要对每一个input都close()
                        // 这里不用判断input是否为空，因为openInputStream要么抛出一个异常，要么返回一个实例
                        try {
                            input.close();
                            input = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (zipOut != null) {
                    try {
                        zipOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //解压文件夹
    public static void unzip(String zippath,String resourcepath){
        //判断生成目录是否生成，如果没有就创建
//        File pathFile=new File(resourcepath);
//        if(!pathFile.exists()){
//            pathFile.mkdirs();
//        }
        ZipFile zp=null;
        try{
            //指定编码，否则压缩包里面不能有中文目录
            zp=new ZipFile(zippath, Charset.forName("gbk"));
            //遍历里面的文件及文件夹
            Enumeration entries=zp.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry= (ZipEntry) entries.nextElement();
                String zipEntryName=entry.getName();
                InputStream in=zp.getInputStream(entry);
                String outpath=(resourcepath+zipEntryName).replace("/",File.separator);
                //判断路径是否存在，不存在则创建文件路径
                File file = new  File(outpath.substring(0,outpath.lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是,不需要解压
                if(new File(outpath).isDirectory())
                    continue;
                OutputStream out=new FileOutputStream(outpath);
                byte[] bf=new byte[2048];
                int len;
                while ((len=in.read(bf))>0){
                    out.write(bf,0,len);
                }
                in.close();
                out.close();
            }
            zp.close();
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

}
