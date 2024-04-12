package com.gyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyf.entity.FileInfo;
import com.gyf.entity.TextLogo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 郭云飞
 * @since 2022-02-23
 */
public interface FileInfoService extends IService<FileInfo> {

    /**
     * 上传文件到远程文件服务器
     * @param file
     */
    String upLoadToFileServer(MultipartFile file) throws IOException;

    /**
     * 批量上传文件
     * @param files
     */
    void upLoadFiles(MultipartFile[] files) throws IOException;

    /**
     * word文档转化为pdf文档
     * @param file
     * @return  文件在服务器上的存储路径
     * @throws IOException
     */
    String wordToPdf(MultipartFile file) throws IOException;

    /**
     * pdf文档转word文档
     * @param file
     * @return  文件在服务器上的存储路径
     * @throws IOException
     */
    String pdfToWord(MultipartFile file) throws IOException;

    /**
     * 给图片添加文字logo
     * @param files 要添加logo的图片
     * @param textLogo logo的样式信息
     * @return
     */
    List<String> imgAddTextLogo(MultipartFile[] files, TextLogo textLogo) throws IOException;

    /**
     * 给图片添加图片logo
     * @param files 文件
     * @param zoomFactor 缩放比例
     * @param fontLocationX 横向位置
     * @param fontLocationY 纵向位置
     * @return
     */
    List<String> imgAddImgLogo(MultipartFile[] files, MultipartFile logoFile,float zoomFactor,float opacity,String fontLocationX, String fontLocationY) throws IOException;

    /**
     * 调整图片的分辨率
     * @param files 目标文件
     * @param targetSize 要挑整的大小
     * @return
     */
    List<String> compressImg(MultipartFile[] files,long targetSize) throws IOException;

    /**
     * 图片等比压缩
     * @param files 要调整的图片
     * @param heightSize  比例
     * @return
     */
    List<String> compressImgByRatio(MultipartFile[] files,Double heightSize) throws IOException;

    /**
     * 图片转pdf
     * @param files
     * @return
     */
    String imgToPdf(MultipartFile[] files) throws Exception;

    /**
     * word转html
     * @param file 文件
     * @return
     */
    String wordToHtml(MultipartFile file) throws Exception;

    /**
     * pdf转pptx
     * @param file
     * @return
     */
    String pdfToPPT(MultipartFile file) throws IOException;

    /**
     * 根据账号获取所有的文件信息
     * @return
     */
    List<FileInfo> getAllFileInfo();

    /**
     * 根据id删除文件信息
     * @param fileId 要删除的文件id
     */
    void deleteOneFileInfo(int fileId);
}
