package com.gyf.tools.office;

import com.aspose.pdf.Image;
import com.aspose.pdf.Page;
import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.SaveFormat;
import com.gyf.tools.GetDateString;
import com.gyf.tools.img.ImageCompression;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author 郭云飞
 * @date 2021/12/21-9:39
 * @Description 使用第三方库Aspose，进行各种文件间的转换
 */
public class AsposeTools {

    //生成文件的目标路径
    private String path = this.getClass().getClassLoader().getResource("static/temporary").getPath();


    /**
     * 把word文档（docx和doc，aspose对两者兼容）转换为pdf文档。
     * @param inputStream
     * @param fileName
     */
    public String docToPdf(InputStream inputStream,String fileName) {
        long old = System.currentTimeMillis();
        try {
            //新建一个pdf文档
            String pdfPath=path+"/"+fileName+ GetDateString.getDate()+".pdf";
            File file = new File(pdfPath);
            FileOutputStream os = new FileOutputStream(file);
            //Address是将要被转化的word文档
            Document doc = new Document(inputStream);
            System.out.println("文档页数"+doc.getPageCount());
            
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.PDF);
            os.close();
            System.out.println("被转换的word文档共 "+doc.getPageCount()+"页！");
            //去除水印
            //removeWatermark(new File(pdfPath));
            //转化用时
            long now = System.currentTimeMillis();
            System.out.println("Word 转 Pdf 共耗时：" + ((now - old) / 1000.0) + "秒");
            return file.getPath();
        } catch (Exception e) {
            System.out.println("Word 转 Pdf 失败...");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * pdf转word
     * @param inputStream 要转换的文件
     * @param fileName 文件名
     * @return
     */
    public String pdfToDoc(InputStream inputStream,String fileName){
        long old = System.currentTimeMillis();
        try{
            //新建一个word文档
            String wordPath=path+"/"+fileName+GetDateString.getDate()+".docx";
            File file = new File(wordPath);
            FileOutputStream os = new FileOutputStream(file);
            //Address是将要被转化的word文档
            com.aspose.pdf.Document doc=new com.aspose.pdf.Document(inputStream);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, com.aspose.pdf.SaveFormat.DocX);
            os.close();
            System.out.println("被转换的pdf文档共 "+doc.getOptimizeSize()+"页！");
            //转化用时
            long now = System.currentTimeMillis();
            System.out.println("Pdf 转 Word 共耗时：" + ((now - old) / 1000.0) + "秒");
            return file.getPath();
        }catch (Exception e){
            System.out.println("Pdf 转 Word 失败...");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片转PDF
     *
     * @param sourcePaths 源文件
     * @throws IOException
     */
    public String imageTopdf(MultipartFile[] sourcePaths) throws Exception {
        //创建文档
        String targetPath= path+"/"+GetDateString.getUUID()+".pdf";
        com.aspose.pdf.Document doc=new com.aspose.pdf.Document();

        ImageCompression imageCompression = new ImageCompression();
        for (MultipartFile sourcePath:sourcePaths) {

            //新增一页
            Page page = doc.getPages().add();
            //设置页边距
            page.getPageInfo().getMargin().setBottom(0);
            page.getPageInfo().getMargin().setTop(0);
            page.getPageInfo().getMargin().setLeft(0);
            page.getPageInfo().getMargin().setRight(0);
            //创建图片对象
            Image image = new Image();
            BufferedImage bufferedImage = ImageIO.read(sourcePath.getInputStream());
            //获取图片尺寸
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //获取文件类型
            String fileType = sourcePath.getOriginalFilename().substring(sourcePath.getOriginalFilename().lastIndexOf(".")+1);
            ImageIO.write(bufferedImage, fileType, baos);
            baos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            image.setImageStream(bais);
            //设置pdf页的尺寸与图片一样
            page.getPageInfo().setHeight(height);
            page.getPageInfo().setWidth(width);
            //添加图片
            page.getParagraphs().add(image);
            baos.close();
        }
        //保存
        doc.save(targetPath,com.aspose.pdf.SaveFormat.Pdf);
        return targetPath.substring(1);
    }


    /**
     *word转html
     * @throws Exception
     * @return
     */
    public String wordToHtml(InputStream inputStream, String fileName) throws Exception {
//        // Load the document from disk.
//        Document doc = new Document("C:\\Users\\22413\\Desktop\\计算机组装实验报告1.doc");
//
//        // Save the document into HTML.
//        doc.save("C:\\Users\\22413\\Desktop\\" + "Document_out.html", SaveFormat.HTML);

        PrintStream printStream = new PrintStream(new FileOutputStream(path+"/"+fileName+".html"));
        //InputStream inputStream=new FileInputStream("C:\\Users\\22413\\Desktop\\技算机组装维护\\计算机组装实验报告1.doc");
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
        String htmlText = "";
        try {
            Document doc = new Document(inputStream);
            HtmlSaveOptions opts = new HtmlSaveOptions(SaveFormat.HTML);
            opts.setExportXhtmlTransitional(true);
            opts.setExportImagesAsBase64(true);
            opts.setExportPageSetup(true);
            doc.save(htmlStream,opts);
            htmlText = new String(htmlStream.toByteArray(), StandardCharsets.UTF_8);
            htmlStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printStream.print(htmlText);
        printStream.close();
        return (path+"/"+fileName+".html").substring(1);
    }

    /**
     * pdf转html
     */
    public void pdfToHtml(){
        // Load the document from disk.
        com.aspose.pdf.Document doc = new com.aspose.pdf.Document("C:\\Users\\22413\\Desktop\\教育部学籍在线验证报告_郭云飞.pdf");

        // Save the document into HTML.
        doc.save("C:\\Users\\22413\\Desktop\\" + "Document_out.html", com.aspose.pdf.SaveFormat.Html);
    }


//    public static void main(String[] args) throws FileNotFoundException {
//        InputStream inputStream=new FileInputStream("C:\\Users\\22413\\Desktop\\计算机组装实验报告12022-03-21.pdf");
//        new AsposeTools().pdfToPPT(inputStream,"test");
//    }

    /**
     * pdf转ppt
     * @param inputStream pdf文件
     * @param fileName 文件名
     * @return
     */
    public String pdfToPPT(InputStream inputStream,String fileName){
        long old = System.currentTimeMillis();
        try{
            //新建一个word文档
            String wordPath=path+"/"+fileName+GetDateString.getDate()+".pptx";
            File file = new File(wordPath);
            FileOutputStream os = new FileOutputStream(file);
            //Address是将要被转化的word文档
            com.aspose.pdf.Document doc=new com.aspose.pdf.Document(inputStream);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, com.aspose.pdf.SaveFormat.Pptx);
            os.close();
            System.out.println("被转换的pdf文档共 "+doc.getOptimizeSize()+"页！");
            //转化用时
            long now = System.currentTimeMillis();
            System.out.println("Pdf 转 Word 共耗时：" + ((now - old) / 1000.0) + "秒");
            return file.getPath();
        }catch (Exception e){
            System.out.println("Pdf 转 Word 失败...");
            e.printStackTrace();
        }
        return null;
    }



//    public static boolean getpdfLicense() {
//        boolean result2 = false;
//        try {
//            String license2 = "<License>\n"
//                    + "  <Data>\n"
//                    + "    <Products>\n"
//                    + "      <Product>Aspose.Total for Java</Product>\n"
//                    + "      <Product>Aspose.Words for Java</Product>\n"
//                    + "    </Products>\n"
//                    + "    <EditionType>Enterprise</EditionType>\n"
//                    + "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n"
//                    + "    <LicenseExpiry>20991231</LicenseExpiry>\n"
//                    + "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n"
//                    + "  </Data>\n"
//                    + "  <Signature>111</Signature>\n"
//                    + "</License>";
//            InputStream is2 = new ByteArrayInputStream(
//                    license2.getBytes("UTF-8"));
//            com.aspose.pdf.License asposeLic2 = new com.aspose.pdf.License();
//            asposeLic2.setLicense(is2);
//            result2 = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result2;
//    }


}
