package com.gyf.test;

import com.gyf.tools.UploadFile;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * @author 郭云飞
 * @date 2021/12/21-11:10
 * @Description TODO
 */
public class test {

    final String url= "http://39.103.221.127:80/file/uploadFile";

    @Test
    public void pdfToDoc() throws FileNotFoundException {
//        AsposeTools asposeTools=new AsposeTools();
//        File file=new File("C:\\Users\\22413\\Desktop\\教育部学籍在线验证报告_郭云飞.pdf");
//        FileInputStream fileInputStream=new FileInputStream(file);
//        asposeTools.pdfToDoc(fileInputStream,"教育部学籍在线验证报告_郭云飞");

        File file=new File("C:\\Users\\22413\\IdeaProjects\\file-handling\\target\\classes\\static\\temporary\\教育部学籍在线验证报告_郭云飞2022-02-28.docx");

        FileInputStream newInputStream= new FileInputStream(file);
        String newFileName = file.getName();//新生成的文件名
        String newFileType = file.getName().substring(file.getName().lastIndexOf(".")).replace(".","");//新生成的文件类型
        HashMap<String,String> parameters=new HashMap<>();
        parameters.put("path","destination_files");
        UploadFile uploadFile=new UploadFile();
        String response=uploadFile.singleFileUploadWithParameters(url,
                "file",
                newInputStream,
                newFileName,
                newFileType,
                parameters);
    }
}
