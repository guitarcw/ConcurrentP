package concurrent;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadPool<Job extends Runnable> implements wangyi4.ThreadPool {
    private static  final int MAX_WORKER_NUMBERS=10;
    private static final int DEFAULT_WOEKER_NUMBERS=5;
    private static final int MIN_WOEKERpNUMBERS=1;
    private final LinkedList<Job> jobs=new LinkedList<>();
    private final List<Worker> workers=Collections.synchronizedList((List<Worker>) new LinkedList<Worker>());
    private int workerNum=DEFAULT_WOEKER_NUMBERS;
    private AtomicLong threadNum=new AtomicLong();
    DefaultThreadPool(){
        initializeWorkers(workerNum);
    }
    DefaultThreadPool(int num){
        initializeWorkers(num);
    }

    @Override
    public void execute(Runnable runnable) {
        if(runnable!=null){
            synchronized (jobs){
                jobs.add((Job) runnable);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker w:workers
             ) {
            w.shutDown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs){
            if(num+this.workerNum>MAX_WORKER_NUMBERS){
                num=MAX_WORKER_NUMBERS-this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum+=num;
        }
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs){
            if(num>=this.workerNum){
                throw  new IllegalArgumentException("beyond workNum");
            }
            int count=0;
            while (count<num){
                Worker worker=workers.get(count);
                if(workers.remove(worker)){
                    worker.shutDown();
                    count++;
                }
            }
            this.workerNum-=count;
        }

    }

    @Override
    public int getJobSize() {
        return 0;
    }
    private void initializeWorkers(int num){
        for (int i = 0; i < num; i++) {
            Worker worker=new Worker();
            workers.add(worker);
            Thread thread=new Thread(worker,"ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }
    class Worker implements Runnable{
        private volatile boolean running =true;
        @Override
        public void run() {
            while (running){
                Job job=null;
                synchronized(jobs){
                    while ((jobs.isEmpty())){
                        try{
                            jobs.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    job=jobs.removeFirst();
                }
                if (job!=null){
                    job.run();
                }
            }
        }
        public void shutDown(){
            running=false;
        }
    }
}
