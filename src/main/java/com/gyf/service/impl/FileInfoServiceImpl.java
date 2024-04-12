package com.gyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyf.entity.FileInfo;
import com.gyf.entity.TextLogo;
import com.gyf.mapper.FileInfoMapper;
import com.gyf.service.FileInfoService;
import com.gyf.tools.GetDateString;
import com.gyf.tools.UploadFile;
import com.gyf.tools.img.ImageAddText;
import com.gyf.tools.img.ImageAddTextOrLogoBean;
import com.gyf.tools.img.ImageCompression;
import com.gyf.tools.img.TextStyle;
import com.gyf.tools.office.AsposeTools;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 郭云飞
 * @since 2022-02-23
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    //远程接口路径(实际上是本地的另一个端口)
    //final String url= "http://localhost:8082/file/uploadFile";
    //远程接口路径(真正的远程服务器)
    final String url= "http://39.103.221.127:80/file/uploadFile";

    final String ip="http://39.103.221.127:8082";

    //生成文件的目标路径  临时文件存储路径
    final String path = this.getClass().getClassLoader().getResource("static/temporary/").getPath();

    @Override
    public String upLoadToFileServer(MultipartFile file) throws IOException {
        InputStream inputStream=file.getInputStream();
        //获取文件名
        String fileName= file.getOriginalFilename();
        //获取文件类型
        String fileType = fileName.substring(fileName.lastIndexOf(".")).replace(".","");
        HashMap<String,String> parameters=new HashMap<>();
        parameters.put("path","source_files");
        UploadFile uploadFile=new UploadFile();
        String response=uploadFile.singleFileUploadWithParameters(url,
                "file",
                inputStream,
                fileName,
                fileType,
                parameters);
        //将操作记录保存到数据库
        String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
        FileInfo fileInfo=new FileInfo(null,fileName,fileType,LocalDateTime.now(),file.getSize(), userAccount);
        addFileInfo(fileInfo);
        return ip+response;
    }

    @Override
    @Transactional
    public void upLoadFiles(MultipartFile[] files) throws IOException {
        for (MultipartFile file:files) {
            upLoadToFileServer(file);
        }
    }

    /**
     * 新文件上传到服务器
     * @param inputStream
     * @param fileName
     * @param fileType
     * @return
     * @throws IOException
     */
    public String upLoadToFileServer2(InputStream inputStream,String fileName,String fileType) throws IOException {
        //获取文件名
        HashMap<String,String> parameters=new HashMap<>();
        parameters.put("path","destination_files");
        UploadFile uploadFile=new UploadFile();
        String response=uploadFile.singleFileUploadWithParameters(url,
                "file",
                inputStream,
                fileName,
                fileType,
                parameters);
        return response;
    }

    @Override
    public String wordToPdf(MultipartFile file) throws IOException {
        //获取文件名
        String fileName=file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));

        //进行文件转换
        InputStream inputStream= file.getInputStream();
        AsposeTools asposeTools=new AsposeTools();
        String filePath= asposeTools.docToPdf(inputStream,fileName);

        //向远程接口传递文件
        InputStream newInputStream= new FileInputStream(filePath);
        File newFile = new File(filePath);
        String newFileName = newFile.getName();//新生成的文件名
        String newFileType = newFile.getName().substring(newFile.getName().lastIndexOf(".")).replace(".","");//新生成的文件类型
        HashMap<String,String> parameters=new HashMap<>();
        parameters.put("path","destination_files");
        UploadFile uploadFile=new UploadFile();
        String response=uploadFile.singleFileUploadWithParameters(url,
                "file",
                newInputStream,
                newFileName,
                newFileType,
                parameters);
        if (response!=null&& new File(filePath).exists()){
            Files.delete(Paths.get(filePath)); //当文件上传到文件服务器后，删除本服务器上的临时文件
        }
        return ip+response;
    }

    @Override
    public String pdfToWord(MultipartFile file) throws IOException {
        //获取文件名
        String fileName=file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));

        //进行文件转换
        InputStream inputStream= file.getInputStream();
        AsposeTools asposeTools=new AsposeTools();
        String filePath= asposeTools.pdfToDoc(inputStream,fileName);

        //向远程接口传递文件
        File newFile = new File(filePath);
        FileInputStream newInputStream= new FileInputStream(newFile);
        String newFileName = newFile.getName();//新生成的文件名
        String newFileType = newFile.getName().substring(newFile.getName().lastIndexOf(".")).replace(".","");//新生成的文件类型
        HashMap<String,String> parameters=new HashMap<>();
        parameters.put("path","destination_files");
        UploadFile uploadFile=new UploadFile();
        String response=uploadFile.singleFileUploadWithParameters(url,
                "file",
                newInputStream,
                newFileName,
                newFileType,
                parameters);
        if (response!=null&& new File(filePath).exists()){
            Files.delete(Paths.get(filePath)); //当文件上传到文件服务器后，删除本服务器上的临时文件
        }
        return ip+response;
    }

    @Override
    public List<String> imgAddTextLogo(MultipartFile[] files, TextLogo textLogo) throws IOException {

        Color color_l = TextStyle.getTextColor(textLogo.getFontColor()); //获取字体颜色
        ImageAddTextOrLogoBean.Position_X position_x=TextStyle.getPositionX(textLogo.getFontLocationX()); //获取logo横向位置
        ImageAddTextOrLogoBean.Position_Y position_y=TextStyle.getPositionY(textLogo.getFontLocationY()); //获取logo纵向位置
        int fontStyle=TextStyle.getFontStyle(textLogo.getFontStyle());//获取字体样式

        List<ImageAddTextOrLogoBean> beans = new ArrayList<ImageAddTextOrLogoBean>();
        beans.add(new ImageAddTextOrLogoBean(position_x, position_y, 1, 12, textLogo.getFontContent(), color_l,
                new Font(textLogo.getFontFamily(), fontStyle, textLogo.getFontSize())));

        UploadFile uploadFile=new UploadFile();
        //图片路径
        List<String> urlList=new ArrayList<>();

        for (MultipartFile file:files) {
            //源文件上传到文件服务器
            upLoadToFileServer(file);
            //beans.add(new ImageAddTextOrLogoBean(ImageAddTextOrLogoBean.Position_X.LEFT, ImageAddTextOrLogoBean.Position_Y.TOP, left1,
            try{
                String fileName= file.getOriginalFilename().replace(".",GetDateString.getDate()+".");
                String newFilePath = ImageAddText.ImageAddTextOrImage(file.getInputStream(), path+fileName, beans); //生成的新文件的路径

                File newFile=new File(newFilePath);
                FileInputStream fileInputStream=new FileInputStream(newFile);
                String newFileName= newFile.getName();//获取 文件名
                String newFileType = newFileName.substring(newFileName.lastIndexOf(".")).replace(".","");//新生成的文件类型
                HashMap<String,String> newParameters=new HashMap<>();
                newParameters.put("path","destination_files");
                String newResponse=uploadFile.singleFileUploadWithParameters(url,
                        "file",
                        fileInputStream,
                        newFileName,
                        newFileType,
                        newParameters);
                if (newResponse!=null&& new File(newFilePath).exists()){
                    Files.delete(Paths.get(newFilePath)); //当文件上传到文件服务器后，删除本服务器上的临时文件
                }
                urlList.add(newResponse);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return urlList;
    }

    @Override
    public List<String> imgAddImgLogo(MultipartFile[] files, MultipartFile logoFile,float zoomFactor, float opacity,String fontLocationX, String fontLocationY) throws IOException {

        //获取logo的坐标
        Positions positions= TextStyle.imgGetLogoPosition(fontLocationX,fontLocationY);

        ImageCompression imageCompression=new ImageCompression();
        //图片路径
        List<String> urlList=new ArrayList<>();
        for (MultipartFile file:files) {
            upLoadToFileServer(file);
            String newFilePath = imageCompression.imgAddImageLogo(file,logoFile,zoomFactor,opacity,positions);

            File newFile=new File(newFilePath);
            FileInputStream fileInputStream=new FileInputStream(newFile);
            String newFileName= newFile.getName();//获取 文件名
            String newFileType = newFileName.substring(newFileName.lastIndexOf(".")).replace(".","");//新生成的文件类型

            String newResponse=upLoadToFileServer2(fileInputStream,newFileName,newFileType);
            if (newResponse!=null&& new File(newFilePath).exists()){
                Files.delete(Paths.get(newFilePath)); //当文件上传到文件服务器后，删除本服务器上的临时文件
            }
            urlList.add(newResponse);
        }
        return urlList;
    }

//    @Override
//    public String getHtmlText() throws FileNotFoundException {
//        return new AsposeTools().parseWord2Html();
//    }

    @Override
    public List<String> compressImg(MultipartFile[] files, long targetSize) throws IOException {
        //ImageCompression imageCompression=new ImageCompression();
        //图片路径
        List<String> urlList=new ArrayList<>();
        for (MultipartFile file:files) {
            //上传源文件
            upLoadToFileServer(file);
            byte[] compressByte= ImageCompression.compressPicForScale(file.getBytes(),targetSize,file.getOriginalFilename());
            ImageCompression.byteArrayToFile(compressByte,path+file.getOriginalFilename().replace(".",GetDateString.getDate()+"."));
            //上传压缩后的文件32
            String filePath=(path+file.getOriginalFilename().replace(".",GetDateString.getDate()+".")).substring(1);
            InputStream inputStream=new FileInputStream(filePath);
            String fileName=file.getOriginalFilename().replace(".",GetDateString.getDate()+".");
            String fileType=fileName.substring(fileName.lastIndexOf(".")).replace(".","");
            String response = upLoadToFileServer2(inputStream,fileName,fileType);
            if (response!=null&& new File(filePath).exists()){
                Files.delete(Paths.get(filePath)); //当文件上传到文件服务器后，删除本服务器上的临时文件
            }
            urlList.add(response);
        }

        return urlList;
    }

    @Override
    public List<String> compressImgByRatio(MultipartFile[] files, Double heightSize) throws IOException {
        List<String> urlList=new ArrayList<>();
        for (MultipartFile file:files) {
            //上传源文件
            upLoadToFileServer(file);
            //
            ImageCompression imageCompression=new ImageCompression();
            String filePath = imageCompression.scale(file,heightSize);
            //上传转换后的文件
            File targetFile=new File(filePath);
            InputStream inputStream=new FileInputStream(filePath);

            String fileName=targetFile.getName();
            String fileType=fileName.substring(fileName.lastIndexOf(".")).replace(".","");
            String response = upLoadToFileServer2(inputStream,fileName,fileType);
            if (response!=null&& new File(filePath).exists()){
                Files.delete(Paths.get(filePath)); //当文件上传到文件服务器后，删除临时文件
            }
            urlList.add(response);
        }
        return urlList;
    }

    @Override
    public String imgToPdf(MultipartFile[] files) throws Exception {
        
        //源文件上传到服务器
        for (MultipartFile file:files) {
            upLoadToFileServer(file);
        }
        
        AsposeTools asposeTools=new AsposeTools();
        String targetPath = asposeTools.imageTopdf(files);
        File targetFile=new File(targetPath);
        InputStream inputStream=new FileInputStream(targetFile);
        //文件名
        String fileName = targetFile.getName();
        //文件类型
        String fileType=fileName.substring(fileName.lastIndexOf(".")).replace(".","");

        String response = upLoadToFileServer2(inputStream,fileName,fileType);
        if (response!=null&& targetFile.exists()){
            Files.delete(Paths.get(targetPath)); //当文件上传到文件服务器后，删除临时文件
        }
        return ip+response;
    }

    @Override
    public String wordToHtml(MultipartFile file) throws Exception {
        //上传源文件
        upLoadToFileServer(file);
        //转换为html
        AsposeTools asposeTools=new AsposeTools();
        String oldFileName= file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        String htmlPath = asposeTools.wordToHtml(file.getInputStream(), oldFileName);
        //上传转换后的文件
        File targetFile=new File(htmlPath);
        InputStream inputStream=new FileInputStream(htmlPath);

        String fileName=targetFile.getName();
        String fileType=fileName.substring(fileName.lastIndexOf(".")).replace(".","");
        String response = upLoadToFileServer2(inputStream,fileName,fileType);
        if (response!=null&& new File(htmlPath).exists()){
            Files.delete(Paths.get(htmlPath)); //当文件上传到文件服务器后，删除临时文件
        }
        return ip+response;
    }

    @Override
    public String pdfToPPT(MultipartFile file) throws IOException {
        //上传源文件
        upLoadToFileServer(file);
        //转换文件
        AsposeTools asposeTools=new AsposeTools();
        String oldFileName= file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        String pdfPath = asposeTools.pdfToPPT(file.getInputStream(), oldFileName);
        //上传转换后的文件
        File targetFile=new File(pdfPath);
        InputStream inputStream=new FileInputStream(pdfPath);

        String fileName=targetFile.getName();
        String fileType=fileName.substring(fileName.lastIndexOf(".")).replace(".","");
        String response = upLoadToFileServer2(inputStream,fileName,fileType);
        if (response!=null&& new File(pdfPath).exists()){
            Files.delete(Paths.get(pdfPath)); //当文件上传到文件服务器后，删除临时文件
        }
        return ip+response;

    }

    @Override
    public List<FileInfo> getAllFileInfo() {

        String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        List<FileInfo> fileInfoList=fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("uploader",userAccount));
        return fileInfoList;
    }

    @Override
    public void deleteOneFileInfo(int fileId) {
        //删除服务器上的文件
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        UploadFile uploadFile=new UploadFile();
        Map<String,String> map=new HashMap<>();
        map.put("fileName", fileInfo.getFileName());
        uploadFile.httpGet("http://39.103.221.127:80/file/deleteFile",map);

        fileInfoMapper.deleteById(fileId);//删除数据库中的信息
    }

    public void addFileInfo(FileInfo fileInfo){
        fileInfoMapper.insert(fileInfo);
    }
}
