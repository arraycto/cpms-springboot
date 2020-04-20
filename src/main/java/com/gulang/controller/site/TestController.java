package com.gulang.controller.site;

import com.gulang.controller.site.common.CreateThreadByCallable;
import com.gulang.controller.site.service.WebSocketServer;

import com.gulang.model.User;
import com.gulang.service.UserService;
import com.gulang.util.common.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * DESC:
 *   网站测试功能控制器
 * @author gulang
 * @date 2020/01/11 17:29
 * @Email 1226740471@qq.com
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private  static Object shareLock = new Object();
    private  static  AtomicInteger atomicInt = new AtomicInteger(0); // 代替 count++,实现原子操作
    private  static ConcurrentHashMap<String,Object> formToken = new ConcurrentHashMap<>(); // 防止表单重复提交

    @Autowired
    UserService userService;


    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping(value = "/restApi/{id}",method = RequestMethod.GET)
    public String restFulApi(@PathVariable("id") Integer id) { //@PathVariable 获取restful 风格的url参数

        System.out.println("id:"+id);
        return  "获取restful 风格的url参数";
    }

    @PostMapping(value = "/upload")
    public  String  testUpload(@RequestParam("file")MultipartFile file, HttpServletRequest request){

        HashMap<String, Object> resultMap = new HashMap<>();

        String[] allowSuffix = {"png", "jpg", "txt"};
        String fileName = file.getOriginalFilename(); // 获取上传文件名 eg: 02.jpg
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1); // 获取后缀名

        boolean contains = Arrays.asList(allowSuffix).contains(fileSuffix);


        if(!contains) {
            return "文件格式不被允许上传";
        }

        SimpleDateFormat df     = new SimpleDateFormat("yyyy-MM-dd");
        Date now                = new Date();
        String dateFloder       =  df.format(now); // 保存上传文件的文件夹名字每个日期一个文件夹


        //使用UUID给图片重命名，并去掉四个“-”
        String resetFileName = UUID.randomUUID().toString().replaceAll("-", "")+"."+fileSuffix;

        //获取上传到指定的基础目录路径 在resources/static/upload下
        String uploadDir= null;

        try {
            uploadDir = ResourceUtils.getURL("classpath:").getPath()+ File.separator+"static"+File.separator+"upload";

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //如果目录不存在，自动创建文件夹
        File dir = new File(uploadDir+File.separator+dateFloder);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        //保存上传的文件
        try {

            file.transferTo(new File(dir+File.separator+ resetFileName));
            return "上传文件路径"+dir+File.separator+ resetFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "上传文件失败";
        }

    }

    /**
     * des: 分片上传大文件
     * @param file
     * @param request
     * @throws IOException
     * @return
     */
    @PostMapping(value = "/chunkUpload")
    public Map<String, Object> chunkUpload(
                MultipartFile file,
                String token ,
                Integer index,
                String filename,
                HttpServletRequest request
             )throws IOException{
        Map<String,Object> resultMap = new HashMap<String,Object>();

            SimpleDateFormat df     = new SimpleDateFormat("yyyy-MM-dd");
            Date now                = new Date();
            String dateFloder       =  df.format(now); // 保存上传文件的文件夹名字每个日期一个文件夹


            //分片文件名
            String resetFileName = filename+"-"+index;

            //获取上传到指定的基础目录路径 在resources/static/upload下
            String uploadDir= ResourceUtils.getURL("classpath:").getPath()+File.separator+"static"+File.separator+"upload";

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir+File.separator+dateFloder+File.separator+token); // 递归创建目录
            if(!dir.exists())
            {
                dir.mkdirs();
            }

            //保存上传的文件
             file.transferTo(new File(dir+File.separator+ resetFileName));

            resultMap.put("code",200);

            return resultMap;
    }

    /**
     * des : 合并分片文件
     * @param token
     * @param filename
     * @param chunkCount
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/mergeChunk")
    public Map<String, Object> mergeChunk(
                String token ,
                String filename,
                Integer chunkCount,
                HttpServletRequest request
            )throws IOException{

        Map<String,Object> resultMap = new HashMap<String,Object>();

        SimpleDateFormat df     = new SimpleDateFormat("yyyy-MM-dd");
        Date now                = new Date();
        String dateFloder       =  df.format(now); // 保存上传文件的文件夹名字每个日期一个文件夹

        String fileSuffix = filename.substring(filename.lastIndexOf(".")+1); // 获取后缀名

        //获取上传到指定的基础目录路径 在resources/static/upload下
        String fileDir= ResourceUtils.getURL("classpath:").getPath()+File.separator+"static"+File.separator+"upload"+File.separator+dateFloder;

        //使用UUID给图片重命名，并去掉四个“-”
        String mergeFileName = UUID.randomUUID().toString().replaceAll("-", "")+"."+fileSuffix;


        RandomAccessFile raf = null;
        int count = 0;
        try {

            //以【读写】的形式打开文件,如果文件不存在则创建该文件
            raf = new RandomAccessFile(new File(fileDir+File.separator+mergeFileName), "rw");

            //开始合并文件，以【读】的形式打开并读取对应切片的二进制文件
            for (int i = 0; i < chunkCount; i++) {
                //读取切片文件
                RandomAccessFile reader = new RandomAccessFile(new File(fileDir+File.separator+token+File.separator+filename+"-"+i), "r");
                byte[] b = new byte[1024];
                int n = 0;

                //先读后写
                while ((n = reader.read(b)) != -1) {//读
                    raf.write(b, 0, n);//写
                }

                reader.close(); //需要关闭打开的文件，要不然下面没法删除文件
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                raf.close(); // 关闭文件
                ToolUtil.deleteDir(fileDir+File.separator+token);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        resultMap.put("code",200);
        return resultMap;
    }


    /**
     * des : 导出数据为excel
     * @return
     */
    @GetMapping(value = "/exportExcel")
    public void  exportExcel(HttpServletResponse response){
        PageManager pageManager = userService.queryAllUser(1,100);

        List<HashMap<String, Object>> userList = pageManager.getLists();

        List<List<String>> excelData = new ArrayList<>();

        List<String> head = new ArrayList<>();
        head.add("用户名");
        head.add("角色");
        head.add("用户ID");
        excelData.add(head);

        for (HashMap o : userList) {

            List<String> data = new ArrayList<>();
            data.add(o.get("userName").toString());
            data.add(o.get("roleName").toString());
            data.add(o.get("userId").toString());
            excelData.add(data);
        }

        String sheetName = "测试";
        String fileName = "ExcelTest.xls";

        try {
            ExcelUtil.exportExcel(response, excelData, sheetName, fileName, 15);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * des : 导入excel数据
     * @return
     */
    @PostMapping(value = "/importExcel")
    public void   importExcel(@RequestParam("file")MultipartFile file){

        try {
            long time1 = System.currentTimeMillis();

            String fileName = file.getOriginalFilename(); // 获取上传文件名 eg: 02.jpg
            String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase(); // 获取后缀名
            if(!fileExtension.isEmpty()){
                String[] extensionArr = {"xlsx","xls"};

                boolean contains = Arrays.asList(extensionArr).contains(fileExtension);

                if(contains) {

                    Workbook workbook = ExcelUtil.getWorkbook(file,fileExtension);

                    Sheet sheet = workbook.getSheetAt(0);

                    int sheetTotalRows = sheet.getPhysicalNumberOfRows();  //获取sheet中的总行数

                    System.out.println("总行数："+sheetTotalRows);


                    /*
                     * 单线程读取Excel
                    */
//                    ExcelUtil.importExcel(file,fileExtension);

                    /*
                     * 线程池 创建多线程读取Excel
                    */

                    int startRow  = 0;
                    int chunkSize = 2000; //每个线程处理多少条数据

                    int threadCount = (sheetTotalRows-1)/chunkSize+1; //线程总数； 计算分页数也可以使用这个公式

                    CountDownLatch countDownLatch = new CountDownLatch(threadCount);  // 使用计数器，让线程有序执行

                    System.out.println("总线程数："+threadCount);

                    List<Future<List<HashMap<String, Object>>>> futures = new ArrayList<>(threadCount); // 接收线程池中线程的执行结果

                    ThreadPoolExecutor threadPoolReadExcel = new ThreadPoolExecutor(
                            threadCount, // 核心数
                            threadCount, // 最大线程数
                            1,  // 非核心线程空闲时间
                            TimeUnit.SECONDS,  // 空闲时间单位：秒
                            new ArrayBlockingQueue<Runnable>(1), // 队列暂存的任务个数 3个
                            Executors.defaultThreadFactory(), // 默认线程工厂，用于创建线程池里的线程
                            new ThreadPoolExecutor.AbortPolicy() // 抛出异常的任务拒绝策略，
                    );

                    for (int i = 0; i < threadCount; i++) {

                        int start = startRow;
                        int end   = 0;

                        if((i+1) == threadCount) {  // 防止最后一个线程分配多余的空行

                             end   = sheetTotalRows;

                        }else{
                             end   = startRow+chunkSize;
                        }

                        startRow += chunkSize;

                        /*
                         *  线程池提交任务有两种方式： execute 和 submit
                         *  execute : 没有返回值,如果需要返回值，还需要在 ReadExcelByRunnable 中添加一个返回值方法
                         *  submit : 返回一个future。可以用这个future来判断任务是否成功完成。
                         */

                          // //实现Runnable接口
//                        ReadExcelByRunnable readExcelByRunnable = new ReadExcelByRunnable(sheet,start,end,countDownLatch);
//                        threadPoolReadExcel.execute(readExcelByRunnable);

                        // 实现Callable接口
                        CreateThreadByCallable createThreadByCallable = new CreateThreadByCallable(sheet,start,end,countDownLatch);

                        // submit提交任务，并接收结果
                        Future<List<HashMap<String, Object>>> resultFuture = threadPoolReadExcel.submit(createThreadByCallable);

                        futures.add(resultFuture); // 把接收的结果放入list中

                    }

                    threadPoolReadExcel.shutdown(); //  向线程池发送关闭的指令，等到已经提交的任务都执行完毕之后，线程池会关闭

                    /*
                     * countDownLatch.await()
                     * 当计数器的值为0时，会往下执行，否则会阻塞在这里
                     *
                     * 在多线程情况下，一般需要等待子线程执行完毕，才能往下执行，实现了有序执行
                    */
                    countDownLatch.await();

                    int total = 0;
                    for (Future<List<HashMap<String, Object>>> future : futures) {

                        total += future.get().size();

                        List<HashMap<String, Object>> callReulte = future.get();
//                        System.out.println("解析结果："+callReulte);
                    }

                    System.out.println("解析总数："+total);

                    long time2 = System.currentTimeMillis(); // 毫秒

                    System.out.println("总耗时："+(double)(time2-time1) / 1000+"s");

                }else{

                    System.out.println("请上传Excel文件");
                }

            }else{

                System.out.println("不是标准的Excel文件");
            }

        }catch (IOException e) {

            e.printStackTrace();

        }catch (InterruptedException e) {

            e.printStackTrace();
        }catch (ExecutionException e) {

            e.printStackTrace();
        }

    }



    @GetMapping(value = "/httpGet")
    public  HashMap  httpGet(){
        HashMap hashMap = new HashMap();
        hashMap.put("code",200);
        hashMap.put("msg","httpclient 测试get请求");
        hashMap.put("data",null);
        return hashMap;
    }

    @PostMapping(value = "/httpPost")
    public  HashMap  httppost(String name){
        HashMap hashMap = new HashMap();
        hashMap.put("code",200);
        hashMap.put("msg","接收到的post请求参数："+name);
        hashMap.put("data",null);
        return hashMap;
    }


    /*
     *  java  API调用
     */
    @GetMapping(value = "/httpApi")
    public  void  httpApiRequest() throws IOException {

        // 创建Httpclient对象,相当于打开了浏览器
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建HttpGet请求，相当于在浏览器输入地址
//        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/test/httpGet");
//
//        CloseableHttpResponse response = null;
//        try {
//            httpGet.addHeader("Token","token"); // 添加header 头信息
//            // 执行请求，相当于敲完地址后按下回车。获取响应
//            response = httpclient.execute(httpGet);
//
//            // 判断返回状态是否为200
//            if (response.getStatusLine().getStatusCode() == 200) {
//                // 解析响应，获取数据
//                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//                System.out.println("***************响应内容********************");
//                System.out.println(content);
//            }
//        } finally {
//            if (response != null) {
//                // 关闭资源
//                response.close();
//            }
//            // 关闭浏览器
//            httpclient.close();
//        }

        HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/test/httpPost");

        CloseableHttpResponse response = null;
        try {

            httpPost.addHeader("Token",""); // 添加header 头信息

            // 设置post请求参数
            List<NameValuePair> parameters = new ArrayList();
            parameters.add(new BasicNameValuePair("name", "project"));

            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);

            // 执行请求，相当于敲完地址后按下回车。获取响应
            response = httpclient.execute(httpPost);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析响应，获取数据
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");

                System.out.println("***************响应内容********************");
                System.out.println(content);
            }
        } finally {
            if (response != null) {
                // 关闭资源
                response.close();
            }
            // 关闭浏览器
            httpclient.close();
        }
    }

    @GetMapping(value = "/tmpTest")
    public  User   tmpTest(){
        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());

        System.out.println(user);

        return user;
    }
}
