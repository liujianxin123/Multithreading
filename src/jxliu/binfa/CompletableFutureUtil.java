package jxliu.binfa;

import java.io.IOException;
import java.util.concurrent.*;

//业务背景：甲方调取公司的服务接口，由于该接口需要返回近半年的数据，数据量较大
//并且里面含有python爬去数据的逻辑，时间也较长，所以决定采用甲方调取接口时提供一个回调地址
//接口获取数据的时候通过异步调用回调地址返回数据，并且还要做一些异常处理以及容错措施
//使用CompletableFuture工具类正好满足需求
//伪代码，省略校验的逻辑
public class CompletableFutureUtil {

//    @WebLog
//    @PostMapping(value = "getResultData")
    public String getResultData(String jsonData){


        //参数校验。。。。
        //balabala...

        ArrayBlockingQueue<Runnable> blockingDeque = new ArrayBlockingQueue<>(10);
        //创建线程池，避免重复创建线程
        ExecutorService es = new ThreadPoolExecutor(5,10,5000, TimeUnit.MILLISECONDS,blockingDeque);

        //异步获取python数据并返回给回调地址
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

            String result = "爬虫数据";
            //获取python数据的逻辑


            return result;
        },es).exceptionally(e->{
            //log.error("异步获取数据异常信息{}",e.getMessage());
            System.out.println(e.getMessage());
            return "爬去数据异常";
        });


        CompletableFuture<String> futureb = future.thenApply(result->{
            System.out.println("输出任务a的执行结果："+result);
            System.out.println("开始回调.....");

            //回调逻辑

//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            HttpPost httpPost = new HttpPost(institution.getTokenUrl());
//            StringEntity requestEntity = new StringEntity(result,"utf-8");
//            requestEntity.setContentEncoding("UTF-8");
//            httpPost.setHeader("Content-type", "application/json");
//            httpPost.setEntity(requestEntity);
//            String returnValue = "";
//            String message = "success";
//            try {
//                //发送HttpPost请求，获取返回值
//                returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
//            } catch (IOException e) {
//                message = "fail";
//                log.error("请求回调接口失败...:{}",e);
//            }finally {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    log.error("close is fail...:{}",e);
//                }
//            }
//
//            JSONObject json = JSONObject.parseObject(returnValue);
//
//            String msg = json.getString("msg");
//            insertCallbackLog(charges,msg,institutionCode,institutionName);

            return result;
        }).exceptionally(e -> {
            System.out.println("回调失败，回调地址有误：{}"+e.getMessage());
            //记录回调状态
            //insertCallbackLog(charges,"fail",institutionCode,institutionName);
            return "回调失败";
        });


        return "成功";
    }
}
