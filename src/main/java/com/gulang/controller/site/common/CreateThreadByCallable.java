package com.gulang.controller.site.common;

import com.gulang.controller.site.util.ParseExcel;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 *  通过 Callable 创建线程，获取线程的执行结果
 *
 *  因为 通过 Runnable 创建的线程，run方法是没有返回结果的，所以一般我们需要获取线程的执行结果，可以使用 Callable 来创建线程
 *
 *  Callable<List<HashMap<String, Object>>>  指定返回结果的类型
 */
public class CreateThreadByCallable  implements Callable<List<HashMap<String, Object>>> {

    private Sheet sheet;
    private int start; // 起始行
    private int end;   // 结束行
    private CountDownLatch countDownLatch;  // 计数器
    public CreateThreadByCallable(){}

    public CreateThreadByCallable(Sheet sheet, int start, int end, CountDownLatch countDownLatch){

        this.sheet = sheet;
        this.start = start;
        this.end   = end;
        this.countDownLatch = countDownLatch;

    }


    @Override
    public List<HashMap<String, Object>> call() throws Exception {  // call 就相当于 Runnable中的run方法

        List<HashMap<String, Object>> parseResult = null;
        try{
            if(sheet != null) {

                /*
                 *  在这里我们可以直接把 解析的结果 插入到数据库中，实现多线程插入数据库
                 */
                 parseResult = ParseExcel.readSheet(sheet, start, end); // 解析结果

                System.out.println("线程："+Thread.currentThread().getName()+":解析结果："+parseResult.size());

            }

        }catch (Exception  e) {

            e.printStackTrace();

        }finally {

            countDownLatch.countDown(); //  当一个线程执行完 了计数要减一,不然这个线程会被一直挂起
        }

        return parseResult;
    }
}
