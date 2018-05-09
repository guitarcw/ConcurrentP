package concurrent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static int a=0;
    public static String A="a";
    public static String B="b";
    public static void main(String[] args) throws InterruptedException {
        InheritableThreadLocal<Integer> threadLocal=new InheritableThreadLocal<>();
        Thread prev=Thread.currentThread();
        for (int i = 0; i <10 ; i++) {
            Thread thread=new Thread(new Demino(prev),String.valueOf(i));
            thread.start();
            prev=thread;
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread().getName()+"terminate.");
    }
    static class Demino implements Runnable{
        private  Thread thread;
        Demino(Thread t){
            thread=t;
        }
        @Override
        public void run() {

            try {
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }

    }

}
