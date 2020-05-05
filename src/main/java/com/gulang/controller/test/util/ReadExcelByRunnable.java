package com.gulang.controller.test.util;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *  des : 使用多线程Excel
 *
 */
public class ReadExcelByRunnable implements Runnable{

    private Sheet sheet;
    private int start; // 起始行
    private int end;   // 结束行
    private CountDownLatch countDownLatch;  // 计数器
    public ReadExcelByRunnable(){

    }

    public ReadExcelByRunnable(Sheet sheet, int start, int end, CountDownLatch countDownLatch){
        this.sheet = sheet;
        this.start = start;
        this.end   = end;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {  // 线程就在这个方法里处理业务，
        try{
            if(sheet != null) {

                List<HashMap<String, Object>> parseResult = ParseExcel.readSheet(sheet, start, end);

                System.out.println("线程："+Thread.currentThread().getName()+":解析结果："+parseResult.size());

//                for (HashMap<String, Object> item : parseResult) {
//                    System.out.println("valueId="+item.get("valueId"));
//                    System.out.println("valueNumber="+item.get("valueNumber"));
//                    System.out.println("valueName="+item.get("valueName"));
//                    System.out.println("valueSex="+item.get("valueSex"));
//                    System.out.println("valueBirthday="+item.get("valueBirthday"));
                System.out.println(parseResult);
                    System.out.println("===========================");
//                }
            }

        }catch (Exception  e) {
            e.printStackTrace();

        }finally {

            countDownLatch.countDown(); //  当一个线程执行完 了计数要减一,不然这个线程会被一直挂起
        }

    }
}
