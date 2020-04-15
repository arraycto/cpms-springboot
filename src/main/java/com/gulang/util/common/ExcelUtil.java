package com.gulang.util.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    /**
     * Excel表格导出
     * @param response HttpServletResponse对象
     * @param excelData Excel表格的数据，封装为List<List<String>>
     * @param sheetName sheet的名字
     * @param fileName 导出Excel的文件名
     * @param columnWidth Excel表格的宽度，建议为15
     * @throws IOException 抛IO异常
     */
    public static void exportExcel(HttpServletResponse response,
                                   List<List<String>> excelData,
                                   String sheetName,
                                   String fileName,
                                   int columnWidth) throws IOException {

        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称
        HSSFSheet sheet = workbook.createSheet(sheetName);

        //设置表格列宽度
        sheet.setDefaultColumnWidth(columnWidth);

        //写入List<List<String>>中的数据
        int rowIndex = 0;
        for(List<String> data : excelData){
            //创建一个row行，然后自增1
            HSSFRow row = sheet.createRow(rowIndex++);

            //遍历添加本行数据
            for (int i = 0; i < data.size(); i++) {
                //创建一个单元格
                HSSFCell cell = row.createCell(i);

                //创建一个内容对象
                HSSFRichTextString text = new HSSFRichTextString(data.get(i));

                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        workbook.write(response.getOutputStream());

        //关闭workbook
        workbook.close();
    }

    public  static  Workbook getWorkbook(MultipartFile file,String fileType)  throws IOException{
        //1. 创建一个 workbook 对象
        Workbook workbook = null;

        if(fileType.equals("xlsx")) {

            workbook = new XSSFWorkbook(file.getInputStream());
        }else{

            workbook = new HSSFWorkbook(file.getInputStream()); // 读取excel文件
        }

        return workbook;
    }

    /**
     * des :读取excel数据
     * @param file
     */
    public static void  importExcel(MultipartFile file,String fileType) throws IOException{

        try {

            Workbook workbook = getWorkbook(file,fileType);

            //2. 获取 workbook 中sheet表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();

            for (int i = 0; i < numberOfSheets; i++) {
                //3. 获取表单
                Sheet sheet = workbook.getSheetAt(i);
                //4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                System.out.println("总行数："+physicalNumberOfRows);

                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行
                    }

                    //6. 获取行
                    Row row = sheet.getRow(j);
                    if (row == null) {

                        continue;//防止数据中间有空行
                    }

                    //7. 获取一行的所有列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();

                    for (int k = 0; k < physicalNumberOfCells; k++) {

                        Cell cell = row.getCell(k); // 获取某一单元格

                        if(cell != null) {
                            DataFormatter dataFormatter = new DataFormatter(); // 数类型格式化。

                            String cellValue = dataFormatter.formatCellValue( cell); // 获取单元格value

                            System.out.println(cellValue);

                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
