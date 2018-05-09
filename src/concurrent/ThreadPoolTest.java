package concurrent;

import sun.management.counter.Units;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        ArrayBlockingQueue queue=new ArrayBlockingQueue(100);
        ExecutorService pool=Executors.newFixedThreadPool(10);
        ThreadPoolExecutor threadPoolExecutor;
        threadPoolExecutor = new ThreadPoolExecutor(12,15,
                10,TimeUnit.SECONDS,queue);
        for (int i = 0; i <100 ; i++) {
            pool.execute(new task(Math.random(),Math.random()));
        }

    }

    static class task implements Runnable{
        private double a;
        private double b;
        task(double a, double b){
            this.a=a;this.b=b;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(a+b);
        }
    }
}
