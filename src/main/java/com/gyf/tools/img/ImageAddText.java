package com.gyf.tools.img;

import com.gyf.tools.CreateFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 郭云飞
 * @date 2022/3/1-14:01
 * @Description TODO
 */
public class ImageAddText {


    /**
     *
     * @param source//原图片路径
     * @param output//新图片路径
     * @param beans
     * @return 0 完成 1图片不存在 2异常错误
     */
    public static String ImageAddTextOrImage(InputStream source, String output, List<ImageAddTextOrLogoBean> beans) throws IOException {

        try {
//            File sourceFile = new File(source);
//            if (!sourceFile.exists()) {
//                return 1;
//            }
//            for (int i = 0; i < beans.size(); i++) {
//                if (i == 0) {
//                    if (beans.get(i).getState() == 0) {//添加图片
//                         imageAddImage(beans.get(i), source, output);
//                    } else if (beans.get(i).getState() == 1) {//添加文字
//                        imageAddText(beans.get(i), source, output);
//                    }
//                }
//                else {
//                    if (beans.get(i).getState() == 0) {//添加图片
//                        imageAddImage(beans.get(i), output, output);
//                    } else if (beans.get(i).getState() == 1) {//添加文字
//                        imageAddText(beans.get(i), output, output);
//                    }
//                }
//
//            }
            if (beans.get(0).getState() == 0) {//添加图片
                return imageAddImage(beans.get(0), source, output);
            } else if (beans.get(0).getState() == 1) {//添加文字
                return imageAddText(beans.get(0), source, output);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 添加图片
     *
     * @param iBean  要添加的图片的信息
     * @param source 源图片路径
     * @param output 新图片路径
     * @throws Exception
     */
    private static String imageAddImage(ImageAddTextOrLogoBean iBean, InputStream source, String output) throws Exception {
        // TODO Auto-generated method stub

        Image img = ImageIO.read(source);
        int srcImgWidth = img.getWidth(null);//水印宽度
        int srcImgHeight = img.getHeight(null);//水印高
//        int srcImgWidth = 100;//水印宽度
//        int srcImgHeight = 100;//水印高
        BufferedImage bi = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img.getScaledInstance(srcImgWidth, srcImgHeight, Image.SCALE_SMOOTH), 0, 0, null);


        //生成logo文件
        CreateFile createFile=new CreateFile();
        String logoPath = createFile.makeDir(iBean.getMultipartFile());

        ImageIcon imgIcon = new ImageIcon(logoPath);
        Image con = imgIcon.getImage();

        if (iBean.getToMinSize() != 1) {  //new File(iBean.getImageUrl())
            BufferedImage bufImg = ImageIO.read(iBean.getMultipartFile().getInputStream()); //读取图片
            con = bufImg.getScaledInstance((int) (con.getWidth(null) * iBean.getToMinSize()),
                    (int) (con.getHeight(null) * iBean.getToMinSize()), bufImg.SCALE_SMOOTH);//设置缩放目标图片模板

        }

        int x = setPoitionX(iBean, srcImgWidth, g, con);
        int y = setPoitionY(iBean, srcImgHeight, g, con);

        g.drawImage(con, x, y, null);//水印的位置
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.dispose();
        File sf = new File(output);
        ImageIO.write(bi, "jpg", sf); // 保存图片
        return sf.getPath();
//		System.out.println("完成");
        //		return result;

    }

    /**
     * 添加文字
     *
     * @param iBean  文字信息
     * @param source 原图片路径
     * @param output 新图片路径
     * @throws Exception
     */
    private static String imageAddText(ImageAddTextOrLogoBean iBean, InputStream source, String output) throws Exception {
        // TODO Auto-generated method stub
//		System.out.println("添加文字-" + iBean.getWatermark());
        //File srcImgFile = new File(source);
        Image srcImg = ImageIO.read(source);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        if (iBean.getColor() != null) {
            g.setColor(iBean.getColor());
        }

        if (iBean.getFont() != null) {
            g.setFont(iBean.getFont());
        }
        //设置水印的坐标
        int x = setPoitionX(iBean, srcImgWidth, g, null);
        int y = setPoitionY(iBean, srcImgHeight, g, null);

        g.drawString(iBean.getWatermark(), x, y); //加水印
        g.dispose();
        // 输出图片
        File sf = new File(output);
        ImageIO.write(bufImg, "jpg", sf);
        return sf.getPath();
//		System.out.println("完成");

    }

    /**
     * 计算x位置
     *
     * @param iBean       要添加的内容的信息
     * @param srcImgWidth 背景图片宽度
     * @param g
     * @param image
     * @return
     * @throws Exception
     */
    private static int setPoitionX(ImageAddTextOrLogoBean iBean, int srcImgWidth, Graphics2D g, Image image)
            throws Exception {
        int x = 0;
        if (iBean.getPositon_X() == ImageAddTextOrLogoBean.Position_X.LEFT) {
            if (iBean.getX() < 0) {
                x = 0;
            } else {

            }
            if (iBean.getX() > 100) {
                x = srcImgWidth;
            } else {
                x = (int) Math.round(srcImgWidth / 100.0 * iBean.getX());
            }

        } else if (iBean.getPositon_X() == ImageAddTextOrLogoBean.Position_X.CONTER) {
            if (iBean.getState() == 0) {
                x = srcImgWidth / 2 - image.getWidth(null) / 2;
            } else if (iBean.getState() == 1) {
                x = srcImgWidth / 2 - (g.getFontMetrics(g.getFont()).charsWidth(iBean.getWatermark().toCharArray(), 0,
                        iBean.getWatermark().length()) / 2);
            }

        } else if (iBean.getPositon_X() == ImageAddTextOrLogoBean.Position_X.RIGHT) {
            if (iBean.getState() == 0) {
                x = (int) (srcImgWidth - image.getWidth(null) - Math.round(srcImgWidth / 100.0 * iBean.getX()));
            } else if (iBean.getState() == 1) {
                x = (int) (srcImgWidth - (g.getFontMetrics(g.getFont()).charsWidth(iBean.getWatermark().toCharArray(),
                        0, iBean.getWatermark().length())) - Math.round(srcImgWidth / 100.0 * iBean.getX()));
            }
        }
        return x;
    }

    /**
     * 计算y位置
     *
     * @param iBean
     * @param srcImgHeight //原图片高度
     * @param g
     * @param image
     * @return
     * @throws Exception
     */
    private static int setPoitionY(ImageAddTextOrLogoBean iBean, int srcImgHeight, Graphics2D g, Image image)
            throws Exception {
        int y = 0;
        if (iBean.getPositon_Y() == ImageAddTextOrLogoBean.Position_Y.TOP) {
            if (iBean.getY() < 0) {
                y = 0;
            } else if (iBean.getY() > 100) {
                y = srcImgHeight;
            } else {
                y = (int) Math.round(srcImgHeight / 100.0 * iBean.getY());
            }
        } else if (iBean.getPositon_Y() == ImageAddTextOrLogoBean.Position_Y.CONTER) {

            if (iBean.getState() == 0) {
                y = srcImgHeight / 2 - image.getHeight(null) / 2;
            } else if (iBean.getState() == 1) {
                y = srcImgHeight / 2 - (g.getFontMetrics(g.getFont()).getHeight()) / 2;
            }

        } else if (iBean.getPositon_Y() == ImageAddTextOrLogoBean.Position_Y.BOTTOM) {
            if (iBean.getState() == 0) {
                y = (int) (srcImgHeight - image.getHeight(null) - Math.round(srcImgHeight / 100.0 * iBean.getY()));
            } else if (iBean.getState() == 1) {
                y = (int) (srcImgHeight - (g.getFontMetrics(g.getFont()).getHeight())
                        - Math.round(srcImgHeight / 100.0 * iBean.getY()));
            }

        }
        return y;
    }
}
