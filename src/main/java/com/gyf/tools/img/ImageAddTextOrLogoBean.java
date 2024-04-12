package com.gyf.tools.img;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

/**
 * @author 郭云飞
 * @date 2022/3/1-14:03
 * @Description TODO
 */
public class ImageAddTextOrLogoBean {

    public enum Position_X{
        LEFT,
        CONTER,
        RIGHT,
    }
    public enum Position_Y{
        TOP,
        CONTER,
        BOTTOM,
    }


    private Position_X positon_X = Position_X.LEFT;
    private Position_Y positon_Y = Position_Y.TOP;

    private Integer state;//0图片 1文字

    private int x;//x坐标
    private int y;//y坐标

    private String watermark;//文字内容
    private Color color;//颜色
    private Font font;//字体

    private String imageUrl;//图片路径

    private double toMinSize = 1;//缩小程度

    private MultipartFile multipartFile;//图片本身

    public ImageAddTextOrLogoBean() {

    }


    /**
     * 可以设置xy方位的方法
     * @param positon_X
     * @param positon_Y
     * @param x
     * @param y
     * @param watermark
     * @param color
     * @param font
     */
    public ImageAddTextOrLogoBean(Position_X positon_X, Position_Y positon_Y, int x, int y,
                                  String watermark, Color color, Font font) {
        super();
        this.positon_X = positon_X;
        this.positon_Y = positon_Y;
        this.state = 1;
        this.x = x;
        this.y = y;
        this.watermark = watermark;
        this.color = color;
        this.font = font;
    }


    public ImageAddTextOrLogoBean(Position_X positon_X, Position_Y positon_Y, int x, int y, double toMinSize, MultipartFile multipartFile) {
        super();
        this.positon_X = positon_X;
        this.positon_Y = positon_Y;
        this.x = x;
        this.y = y;
        this.toMinSize = toMinSize;
        this.multipartFile = multipartFile;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Position_X getPositon_X() {
        return positon_X;
    }

    public void setPositon_X(Position_X positon_X) {
        this.positon_X = positon_X;
    }

    public Position_Y getPositon_Y() {
        return positon_Y;
    }

    public void setPositon_Y(Position_Y positon_Y) {
        this.positon_Y = positon_Y;
    }

    public double getToMinSize() {
        return toMinSize;
    }

    public void setToMinSize(double toMinSize) {
        this.toMinSize = toMinSize;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

}
