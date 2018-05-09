package concurrent;


import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;


public class ConnectionPool {
    private List<Connection> pool=new LinkedList<>();
    ConnectionPool(int initialSize){
        if(initialSize>0) {
            for (int i = 0; i <initialSize ; i++) {
                pool.add(ConnectionDriver.creatConnection());
            }
        }
    }
    public void releaseConnection(Connection c){
        if(c!=null){
            synchronized (pool){
                pool.add(c);
                pool.notify();
            }
        }
    }
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            if (mills<0){
                while (pool.isEmpty())
                    pool.wait();
                return  pool.remove(pool.size());
            }else {
                long future =System.currentTimeMillis()+mills;
                long remain=mills;
                while (pool.isEmpty()&&remain>0){
                    pool.wait(remain);
                    remain=future-System.currentTimeMillis();
                }
                Connection connection=null;
                if(!pool.isEmpty())
                    connection=pool.remove(pool.size());
                return connection;
            }

    }


}}



