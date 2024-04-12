package com.gyf.tools.office;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 郭云飞
 * @date 2021/12/21-9:39
 * @Description PDFBox能够实现对pdf文档中文字和图片logo的去除，替换，添加
 */
public class PDFBoxTools {

    //替换pdf文本内容

    /**
     * 替换吊pdf文档中指定的文本字符串
     * @param page 要替换文字所在的页
     * @param searchString 要替换的文字字符串
     * @param replacement  进行替换的内容
     * @throws IOException
     */
    public static void replaceText(PDPage page, String searchString, String replacement) throws IOException {
        PDFStreamParser parser = new PDFStreamParser(page);
        List<?> tokens = parser.parse();
        for (int j = 0; j < tokens.size(); j++) {
            Object next = tokens.get(j);
            if (next instanceof Operator) {
                Operator op = (Operator) next;
                String pstring = "";
                int prej = 0;
                if (op.getName().equals("Tj")) {
                    COSString previous = (COSString) tokens.get(j - 1);
                    String string = previous.getString();
                    string = string.replaceFirst(searchString, replacement);
                    previous.setValue(string.getBytes());
                } else if (op.getName().equals("TJ")) {
                    COSArray previous = (COSArray) tokens.get(j - 1);
                    for (int k = 0; k < previous.size(); k++) {
                        Object arrElement = previous.getObject(k);
                        if (arrElement instanceof COSString) {
                            COSString cosString = (COSString) arrElement;
                            String string = cosString.getString();

                            if (j == prej) {
                                pstring += string;
                            } else {
                                prej = j;
                                pstring = string;
                            }
                        }
                    }
                    if (searchString.equals(pstring.trim())) {
                        COSString cosString2 = (COSString) previous.getObject(0);
                        cosString2.setValue(replacement.getBytes());
                        int total = previous.size() - 1;
                        for (int k = total; k > 0; k--) {
                            previous.remove(k);
                        }
                    }
                }
            }
        }
        List<PDStream> contents = new ArrayList<>();
        Iterator<PDStream> streams = page.getContentStreams();
        while (streams.hasNext()) {
            PDStream updatedStream = streams.next();
            OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            contents.add(updatedStream);
            out.close();
        }
        page.setContents(contents);
    }

    /**
     * 移出pdf文档的图片logo
     * @param page 要去除的logo所在页
     * @param cosName
     */
    public static void removeImage(PDPage page, String cosName) {
        PDResources resources = page.getResources();
        COSDictionary dict1 = resources.getCOSObject();
        resources.getXObjectNames().forEach(e -> {
            if (resources.isImageXObject(e)) {
                COSDictionary dict2 = dict1.getCOSDictionary(COSName.XOBJECT);
                if (e.getName().equals(cosName)) {
                    dict2.removeItem(e);
                }
            }
            page.setResources(new PDResources(dict1));
        });
    }


    /**
     * 调用去除文字水印和图片水印的方法来处理pdf
     * @param file  要处理的文件
     * @return  true:处理成功  false:处理失败
     */
    public static boolean removeWatermark(File file) {
        try {
            //通过文件名加载文档
            PDDocument document = Loader.loadPDF(file);
            PDPageTree pages = document.getPages();

            Iterator<PDPage> iter = pages.iterator();
            while (iter.hasNext()) {
                PDPage page = iter.next();
                //System.out.println(document.getNumberOfPages());
                //去除文字水印
//                replaceText(page, "Evaluation Only. Created with Aspose.Words. Copyright 2003-2021 Aspose Pty Ltd.", "");
//                replaceText(page, "Created with an evaluation copy of Aspose.Words. To discover the full versions of", "");
//                replaceText(page, "our APIs please visit: https://products.aspose.com/words/", "");
                replaceText(page,"Evaluation Only. Created with Aspose.Words. Copyright 2003-2021 Aspose","");
                replaceText(page,"Pty Ltd.","");
                replaceText(page,"Created with an evaluation copy of Aspose.Words. To discover the full","");
                replaceText(page,"versions of our APIs please visit: https://products.aspose.com/words/","");
                //去除图片水印
                removeImage(page, "X1");
            }
            //移除文档的最后一页
            //document.removePage(document.getNumberOfPages()-1);
            file.delete();
            document.save(file);
            document.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
