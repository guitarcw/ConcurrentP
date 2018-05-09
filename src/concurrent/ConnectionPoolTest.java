package concurrent;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    static  ConnectionPool pool=new ConnectionPool(10);
    static CountDownLatch start= new CountDownLatch(1);
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        int threadCount=10;
        end =new CountDownLatch(10);
        int count=20;
        AtomicInteger got=new AtomicInteger();
        AtomicInteger notGot=new AtomicInteger();
        for (int i = 0; i <threadCount ; i++) {
            new Thread(new ConnectionRunner(count,got,notGot),"ConntctionRunnerThread").start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke:"+(threadCount* count));
        System.out.println("Got Connection" + got);
        System.out.println("NotGot Connection" + notGot);
    }

    static class  ConnectionRunner implements Runnable{
    int count;
    AtomicInteger get;
    AtomicInteger notGot;
    ConnectionRunner(int count,AtomicInteger get,AtomicInteger notGot){
        this.count=count;
        this.get=get;
        this.notGot=notGot;
    }
        @Override
        public void run() {
        try {
            start.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        while (count>0){
            try {
                Connection connection=pool.fetchConnection(1000);
                if (connection!=null){
                    try {
                        connection.createStatement();
                        connection.commit();
                    }
                    finally {
                        pool.releaseConnection(connection);
                        get.incrementAndGet();
                    }
                }
                else notGot.incrementAndGet();
            }catch (Exception e){

            }
            finally {
                count--;
            }
        }
           end.countDown();
        }
    }
}
