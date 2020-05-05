package com.gulang.controller.test.common;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * des： 通过实现Runnable接口创建线程（常用的创建方式，因为继承只能继承一个类，接口可以实现多个。）
 *
 *  使用 Runnable 创建多个线程来 完成同一个任务
 *
 *  所以一定要记住 继承Thread 和实现Runnable 它们实际的用途是不一样的，一般我们使用Runnable创建线程
 *
 */
public class CreatThreadByRunnable implements  Runnable, Serializable {
    private Object taskNumber;
    private volatile  static int num =0;  // 共享数据 会一直在内存中
    public  CreatThreadByRunnable(){

    }

    public  CreatThreadByRunnable(Object taskNumber){
        this.taskNumber = taskNumber;
    }

    /*
     *  理解：不管是实现Runnable 还是继承Thread 通过它们创建的对象，就相当于一个任务，而任务是用来给线程执行的
     *
     *  调用start() 和run()启动线程的区别：
     * 1.当用start()开始一个线程后，线程就进入就绪状态，使线程所代表的虚拟处理机处于可运行状态，这意味着它可以由JVM调度并执行。
     * 但是这并不意味着线程就会立即运行。只有当cpu分配时间片时，这个线程获得时间片时，才开始执行run()方法。start()是方法，
     * 它调用run()方法.而run()方法是你必须重写的.
     *
     * 2.调用run()就像调用一个普通的方法一样，由上往下执行，会立即执行run里面的逻辑代码。而不是启动一个线程,也不需要CPU执行权
     *
     */
    @Override
    public void run() {

        // run方法里写的是具体的业务逻辑
        try {

            num++;
            System.out.println("线程："+Thread.currentThread().getName()+": 执行了任务->"+taskNumber);

            //让线程阻塞，使后续任务进入缓存队列 （用于测试线程池）
            TimeUnit.MILLISECONDS.sleep(500);  // 代替Thread..sleep(500); 更加直观

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
