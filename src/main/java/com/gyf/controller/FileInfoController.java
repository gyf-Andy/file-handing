package com.gyf.controller;


import com.gyf.entity.FileInfo;
import com.gyf.entity.TextLogo;
import com.gyf.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 郭云飞
 * @since 202Rest-23
 */
@Api(tags = "word文档转换接口",value = "word文档转换接口")
@RestController
@RequestMapping("/api/fileInfo")
public class FileInfoController {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 把文件上传到文件服务器
     * @param file 文件本身
     * @return 文件在远程服务器的路径
     * @throws IOException
     */
    @ApiOperation("把文件上传到文件服务器")
    @RequestMapping(value = "upLoadToFileServer", method = RequestMethod.POST)
    public String upLoadToFileServer(MultipartFile file) throws IOException {
        return fileInfoService.upLoadToFileServer(file);
    }

    /**
     * 批量上传文件（各种类型）
     * @param files
     */
    @ApiOperation("批量上传文件")
    @RequestMapping(value = "upLoadFiles", method = RequestMethod.POST)
    public void upLoadFiles(MultipartFile[] files) throws IOException {
        fileInfoService.upLoadFiles(files);
    }

    /**
     * word文档转pdf文档，并把转换后的pdf文档上传到远程文件服务器
     * @param file 要转换上传的文件
     * @return 文件在远程服务器的路径
     * @throws IOException
     */
    @ApiOperation("word文档转pdf文档，并把转换后的pdf文档上传到远程文件服务器")
    @RequestMapping(value = "wordToPdf", method = RequestMethod.POST)
    public String wordToPdf(MultipartFile file) throws IOException {
        return fileInfoService.wordToPdf(file);
    }

    /**
     * pdf文档转word文档
     * @param file 要被转换的文件
     * @return 转换后的文件在服务器上的路径
     * @throws IOException
     */
    @ApiOperation("pdf文档转word文档，并把转换后的word文档上传到远程文件服务器")
    @RequestMapping(value = "pdfToWord", method = RequestMethod.POST)
    public String pdfToWord(MultipartFile file) throws IOException {
        return fileInfoService.pdfToWord(file);
    }

    /**
     * 为图片添加文字Logo
     * @param files 要添加水印的图片
     * @param fontContent 要添加的文字水印
     * @param fontSize 水印字体大小
     * @param fontFamily 字体文体
     * @param fontColor 字体颜色
     * @param fontOpacity 字体透明度
     * @param fontStyle 字体样式
     * @param fontLocationX 水印在图片的横坐标
     * @param fontLocationY 水印在图片的纵坐标
     * @return 添加水印后的图片在文件服务器的路径
     * @throws IOException
     */
    @ApiOperation("为图片添加文字水印")
    @RequestMapping(value = "imgAddTextLogo", method = RequestMethod.POST)
    public List<String> imgAddTextLogo(MultipartFile[] files, String fontContent, int fontSize, String fontFamily, String fontColor, double fontOpacity, String fontStyle, String fontLocationX, String fontLocationY) throws IOException {
        TextLogo textLogo=new TextLogo(fontContent,fontSize,fontFamily,fontColor,fontOpacity,fontStyle,fontLocationX,fontLocationY);
        return fileInfoService.imgAddTextLogo(files,textLogo);
    }

    /**
     * 为图片添加图片水印
     * @param files 要添加水印的图片
     * @param logoFile 水印图片
     * @param zoomFactor 水印缩放比例
     * @param opacity 水印透明度
     * @param fontLocationX 水印在图片的横坐标
     * @param fontLocationY 水印在图片的纵坐标
     * @return 添加水印后的图片在文件服务器的路径
     * @throws IOException
     */
    @ApiOperation("为图片添加图片水印")
    @RequestMapping(value = "imgAddImgLogo", method = RequestMethod.POST)
    public List<String> imgAddImgLogo(MultipartFile[] files, MultipartFile logoFile,String zoomFactor,String opacity,String fontLocationX, String fontLocationY) throws IOException {
        return fileInfoService.imgAddImgLogo(files, logoFile, Float.parseFloat(zoomFactor), Float.parseFloat(opacity),fontLocationX, fontLocationY);
    }

//    /**
//     * 其他类型的文件 转html （效果太差）
//     * @return
//     * @throws FileNotFoundException
//     */
//    @ApiOperation("获取文件html类型文本")
//    @RequestMapping(value = "getHtmlText", method = RequestMethod.POST)
//    public String getHtmlText() throws FileNotFoundException {
//        return fileInfoService.getHtmlText();
//    }

    /**
     * 压缩图片，调整分辨率，不改变图片的长，高
     * @param files 要进行压缩的图片
     * @param targetSize 压缩的目标大小
     * @return 压缩后的图片在服务器的路径
     * @throws IOException
     */
    @ApiOperation("压缩图片，调整分辨率")
    @RequestMapping(value = "compressImg", method = RequestMethod.POST)
    public List<String> compressImg(MultipartFile[] files,long targetSize) throws IOException {
        return fileInfoService.compressImg(files,targetSize);
    }

    /**
     * 压缩图片，等比压缩 改变图片的长，高，不改变单位面积内的像素
     * @param files 文件
     * @param heightSize 缩放比例
     * @return 压缩后的图片在文件服务器的路径
     * @throws IOException
     */
    @ApiOperation("压缩图片，等比压缩")
    @RequestMapping(value = "compressImgByRatio", method = RequestMethod.POST)
    public List<String> compressImgByRatio(MultipartFile[] files,String heightSize) throws IOException {
        return fileInfoService.compressImgByRatio(files,Double.parseDouble(heightSize));
    }

    /**
     * 图片合并成pdf
     * @param files 图片
     * @return 合并成的pdf在服务器的路径
     */
    @ApiOperation("图片转PDF")
    @RequestMapping(value = "imgToPdf", method = RequestMethod.POST)
    public String imgToPdf(MultipartFile[] files) throws Exception {
        return fileInfoService.imgToPdf(files);
    }

    /**
     * word转html
     * @param file word文件
     * @return html文件在服务器上的公共路径
     * @throws Exception
     */
    @ApiOperation("word转html")
    @RequestMapping(value = "wordToHtml", method = RequestMethod.POST)
    public String wordToHtml(MultipartFile file) throws Exception {
        return fileInfoService.wordToHtml(file);
    }

    /**
     * pdf转pptx
     * @param file pdf文件
     * @return pptx文件在服务器上的公共路径
     * @throws IOException
     */
    @ApiOperation("pdf转pptx")
    @RequestMapping(value = "pdfToPptx", method = RequestMethod.POST)
    public String pdfToPptx(MultipartFile file) throws IOException {
        return fileInfoService.pdfToPPT(file);
    }

    /**
     * 获取跟用户相关的所有的文件信息
     * @return
     */
    @ApiOperation("获取所有的文件信息")
    @RequestMapping(value = "getAllFileInfo", method = RequestMethod.GET)
    public List<FileInfo> getAllFileInfo(){
        return fileInfoService.getAllFileInfo();
    }

    /**
     * 根据id删除文件信息
     * @param fileId 信息id
     */
    @ApiOperation("根据id删除文件信息")
    @RequestMapping(value = "deleteOneFileInfo/{fileId}", method = RequestMethod.GET)
    public void deleteOneFileInfo(@PathVariable int fileId){
        fileInfoService.deleteOneFileInfo(fileId);
    }
}

