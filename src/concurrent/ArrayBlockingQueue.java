package concurrent;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayBlockingQueue<E> {
    private final Condition notFull;
    private final ArrayList<E> arrayList=new ArrayList<>();
    private final  int count;
    private final Condition notEmpty;
    private final ReentrantLock lock=new ReentrantLock(true);
    public ArrayBlockingQueue(int capacity){
        notEmpty=lock.newCondition();
        notFull=lock.newCondition();
        count=capacity;
    }
    public void put(E e) throws InterruptedException {
        final ReentrantLock lock=this.lock;
        lock.lockInterruptibly();
        try {
            while(count==arrayList.size()){
                notFull.await();
            }
            arrayList.add(e);
        }finally {
            lock.unlock();
        }
    }

}
