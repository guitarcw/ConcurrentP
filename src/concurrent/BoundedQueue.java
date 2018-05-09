package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> {
    private Object[] item;
    private int addIndex,removeIndex,count;
    private Lock lock=new ReentrantLock();
    private Condition notEmpty=lock.newCondition();
    private Condition notFull=lock.newCondition();
    BoundedQueue(int k){
        item= new Object[k];
    }
    public void add(T t){
        lock.lock();
        try{
            while (count==item.length)
                notFull.await();
            item[addIndex]=t;
            if (++addIndex==item.length)
                addIndex=0;
            ++count;
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
    public T remove() throws InterruptedException {
        lock.lock();
        try{
            while (count==0){
                notEmpty.await();
            }
            Object x=item[removeIndex];
            if(++removeIndex==item.length){
                removeIndex=0;
            }
            notFull.signal();
            return (T) x;
        }
        finally {
            lock.unlock();
        }
    }
}
