package com.gyf.tools.office;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 郭云飞
 * @date 2022/2/15-13:56
 * @Description POI操作excel表格(xlsx和xls两种类型)
 */
public class POITools {

    public static void main(String[] args) throws IOException {
        readExcel();
    }

    public static void readExcel() throws IOException {

        String filePath="C:\\Users\\22413\\Desktop\\测试.xlsx";
        File file=new File(filePath);

        FileInputStream fileInputStream=new FileInputStream(filePath);
        String fileClass=file.getName().substring(file.getName().lastIndexOf(".")+1);
        System.out.println(fileClass);
        if (fileClass.equals("xlsx")){
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(fileInputStream);
            //读取sheet
            XSSFSheet sheet=xssfWorkbook.getSheetAt(0);

            String s=sheet.getRow(0).getCell(0).getStringCellValue();
            System.out.println(s);

            for (int x=0;x<=sheet.getLastRowNum();x++){
                for (int y=0;y<sheet.getRow(x).getLastCellNum();y++){
                    sheet.getRow(x).getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                    System.out.println(sheet.getRow(x).getCell(y).getStringCellValue());
                }
            }
        }else if (fileClass.equals("xls")){
            //2.读取workbook
            HSSFWorkbook hssfWorkbook=new HSSFWorkbook(fileInputStream);
            //读取sheet
            HSSFSheet sheet=hssfWorkbook.getSheetAt(0);

            String s=sheet.getRow(0).getCell(0).getStringCellValue();
            System.out.println(s);

            for (int x=0;x<=sheet.getLastRowNum();x++){
                for (int y=0;y<sheet.getRow(x).getLastCellNum();y++){
                    sheet.getRow(x).getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                    System.out.println(sheet.getRow(x).getCell(y).getStringCellValue());
                }
            }
        }

    }
}
