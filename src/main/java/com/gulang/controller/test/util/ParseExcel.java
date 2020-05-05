package com.gulang.controller.test.util;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 解析Excel sheet数据
 */
public class ParseExcel {

    public static List<HashMap<String, Object>> readSheet(Sheet sheet,int start,int end ){

        int initCapacity = 16;

        if(end - start > 16) {

            // 阿里就推出了公式。
            // initialCapacity = (int) ((float) expectedSize / 0.75F + 1.0F)

            initCapacity = (int)((float)(end - start) / 0.75F +1.0F);  // 自定义容量，避免频繁扩容
        }

        List<HashMap<String, Object>> resultList = new ArrayList<>(end - start); // 初始化容量,避免频繁扩容


        for (int i = start; i < end; i++) {
            HashMap<String, Object> valueMap = new HashMap<>(initCapacity);

            //6. 获取行
            Row row = sheet.getRow(i);

            if (row == null || i <=1) { //跳过空行 和 头部标题

                continue;
            }

            DataFormatter dataFormatter = new DataFormatter(); // 数类型格式化。

            String resumeId       = dataFormatter.formatCellValue( row.getCell(0)); // ID

            String personal  = dataFormatter.formatCellValue( row.getCell(27)); // 工号

            String education     = dataFormatter.formatCellValue( row.getCell(37)); // 名字

            String job      = dataFormatter.formatCellValue( row.getCell(35)); // 性别

            String development = dataFormatter.formatCellValue( row.getCell(36)); // 出生年月

            valueMap.put("resume_id",resumeId);
            valueMap.put("personal_evaluation",personal);
            valueMap.put("education_description",education);
            valueMap.put("job_description",job);
            valueMap.put("development_description",development);

            resultList.add(valueMap);
        }

        return resultList;
    }
}

