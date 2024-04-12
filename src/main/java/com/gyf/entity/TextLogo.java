package com.gyf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 郭云飞
 * @date 2022/3/2-15:24
 * @Description 添加文字logo的时候需要根据选择的logo信息进行添加
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TextLogo implements Serializable {

    private String fontContent;

    private int fontSize;

    private String fontFamily;

    private String fontColor;

    private double fontOpacity;

    private String fontStyle;

    private String fontLocationX;

    private String fontLocationY;
}
