package com.gyf.tools.img;

import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;

/**
 * @author 郭云飞
 * @date 2022/3/2-17:03
 * @Description TODO
 */
public class TextStyle {

    /**
     * 获取字体颜色
     * @param str 中文颜色
     * @return
     */
    public static Color getTextColor(String str){
        Color color=null;
        if (str.equals("black")){
            color=Color.BLACK;
        }else if (str.equals("green")){
            color=Color.GREEN;
        }else if (str.equals("red")){
            color=Color.RED;
        }else if (str.equals("yellow")){
            color=Color.YELLOW;
        }
        return color;
    }

    /**
     * 返回logo的纵向的位置
     * @param fontLocationX 中文的横向位置
     * @return
     */
    public static ImageAddTextOrLogoBean.Position_X getPositionX(String fontLocationX){
        ImageAddTextOrLogoBean.Position_X position_x=null;
        if (fontLocationX.equals("左")){
            position_x= ImageAddTextOrLogoBean.Position_X.LEFT;
        }else if (fontLocationX.equals("中")){
            position_x= ImageAddTextOrLogoBean.Position_X.CONTER;
        }else if (fontLocationX.equals("右")){
            position_x= ImageAddTextOrLogoBean.Position_X.RIGHT;
        }
        return position_x;
    }

    /**
     * 返回logo的横向位置
     * @param fontLocationY logo的横向位置
     * @return
     */
    public static ImageAddTextOrLogoBean.Position_Y getPositionY(String fontLocationY){
        ImageAddTextOrLogoBean.Position_Y position_y=null;
        if (fontLocationY.equals("上")){
            position_y= ImageAddTextOrLogoBean.Position_Y.TOP;
        }else if (fontLocationY.equals("中")){
            position_y= ImageAddTextOrLogoBean.Position_Y.CONTER;
        }else if (fontLocationY.equals("下")){
            position_y= ImageAddTextOrLogoBean.Position_Y.BOTTOM;
        }
        return position_y;
    }

    /**
     * Thumbnails 获取图片logo位置
     * @param positionX 横坐标
     * @param positionY 纵坐标
     * @return
     */
    public static Positions imgGetLogoPosition(String positionX,String positionY){
        Positions positions=null;
        if (positionX.equals("左")&&positionY.equals("上")){
            positions=Positions.TOP_LEFT;
        }else if (positionX.equals("左")&&positionY.equals("中")){
            positions=Positions.CENTER_LEFT;
        }else if (positionX.equals("左")&&positionY.equals("下")){
            positions=Positions.BOTTOM_LEFT;
        }else if (positionX.equals("中")&&positionY.equals("上")){
            positions=Positions.TOP_CENTER;
        }else if (positionX.equals("中")&&positionY.equals("中")){
            positions=Positions.CENTER;
        }else if (positionX.equals("中")&&positionY.equals("下")){
            positions=Positions.BOTTOM_CENTER;
        }else if (positionX.equals("右")&&positionY.equals("上")){
            positions=Positions.TOP_RIGHT;
        }else if (positionX.equals("右")&&positionY.equals("中")){
            positions=Positions.CENTER_RIGHT;
        }else if (positionX.equals("右")&&positionY.equals("下")){
            positions=Positions.BOTTOM_RIGHT;
        }
        return positions;
    }

    /**
     * 获取字体样式
     * @param fontStyle 中文字体样式
     * @return
     */
    public static int getFontStyle(String fontStyle){
        int style=0;
        if (fontStyle.equals("normal")){
            style=Font.PLAIN;
        }else if (fontStyle.equals("BOLD")){
            style=Font.BOLD;
        }else if (fontStyle.equals("ITALIC")){
            style=Font.ITALIC;
        }
        return style;
    }

}
