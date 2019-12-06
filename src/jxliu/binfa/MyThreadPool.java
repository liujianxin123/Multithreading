package jxliu.binfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 简化的线程池，仅用来说明工作原理
class MyThreadPool{
    // 利用阻塞队列实现生产者 - 消费者模式
    BlockingQueue<Runnable> workQueue;
    // 保存内部工作线程
    List<WorkerThread> threads = new ArrayList<>();
    // 构造方法
    MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue){
        this.workQueue = workQueue;
        // 创建工作线程
        for(int idx=0; idx<poolSize; idx++){
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }
    // 提交任务
    void execute(Runnable command){
        try {
            workQueue.put(command);//队列的put方法，如果队列已满就阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // 工作线程负责消费任务，并执行任务
    class WorkerThread extends Thread{
        public void run() {
            // 循环取任务并执行
            while(true){ //①
                Runnable task = null;
                try {
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        }
    }

    public static void main(String[] args) {
        /** 下面是使用示例 **/
        // 创建有界阻塞队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        // 创建线程池
        MyThreadPool pool = new MyThreadPool(10, workQueue);
        // 提交任务
        pool.execute(()->{
            System.out.println("hello");
        });

    }

    //int nums[] = {2,4,3,5,2,5}
    public static void test(int[] nums) {
        int target = 10;
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i<nums.length;i++){
            int tmp = target - nums[i];
            if(map.containsKey(tmp)){
                System.out.println("两个数相加等于10的分别为"+tmp);
                System.out.print(nums[i]);
                break;//结束循环
            }
            map.put(nums[i],i);  //不存在，则加入
        }
    }


}