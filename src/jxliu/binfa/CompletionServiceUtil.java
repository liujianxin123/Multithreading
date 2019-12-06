package jxliu.binfa;

import java.util.List;
import java.util.concurrent.*;

/**
 * 业务背景：跟公司合作的第三方有三个：A、B、C
 * 需要调用三个供应商提供的接口，并把接口返回的数据保存到数据库中
 * 由于三个接口返回数据量较大，并且获取返回结果的时间各不相同，如果采取同步的方式
 * 查询结果会很慢，因此此处使用异步获取结果并保存到数据库中的方法
 * 又因为每个接口返回的时间不同，批量异步操作，如果有一个很慢会造成下面两个线程的阻塞，所以采用CompletionService工具类来完成
 * CompletionService工具类内部实现了阻塞队列
 * 此处使用伪代码。。。
 */
public class CompletionServiceUtil {
    public void getRebackResult(){
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        //创建有届队列
        //ArrayBlockingQueue<Runnable> blockingDeque = new ArrayBlockingQueue<>(10);
        //创建线程池，避免重复创建线程
        //ExecutorService es = new ThreadPoolExecutor(5,10,5000,TimeUnit.MILLISECONDS,blockingDeque);

        //创建一个有界队列
        BlockingQueue<Future<String>> bq =  new LinkedBlockingQueue<>(10);

        // 创建 CompletionService
        CompletionService<String> cs = new ExecutorCompletionService<String>(executor,bq);
        // 异步调用供应商A
        cs.submit(()->getPriceByS1());
        // 异步调用供应商B
        cs.submit(()->getPriceByS2());
        // 异步调用供应商B
        cs.submit(()->getPriceByS3());
        // 将询价结果异步保存到数据库
        for (int i=0; i<3; i++) {
            Object r = null;
            try {
                r = cs.take().get();//用 Future 对象的 get() 方法获取接口调用的执行结果
                executor.execute(()->save());//保存到数据库
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }

    public static void save(){
        //此处是数据库保存的逻辑
    }
    public String getPriceByS1(){
        //此处是调取供应商A的逻辑
        return null;
    }
    public String getPriceByS2(){
        //此处是调取供应商B的逻辑
        return null;
    }
    public String getPriceByS3(){
        //此处是调取供应商C的逻辑
        return null;
    }
}
