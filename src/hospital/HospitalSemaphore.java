package hospital;


import java.util.concurrent.TimeUnit;

public class HospitalSemaphore {

    protected int value=0;
    private int permissions=0;
   
    public HospitalSemaphore(int initial){
        value = initial;
    }

    public synchronized void P(){
        value--;
        if(value<0){
            while(permissions==0){
                try{wait();}
                catch (InterruptedException e){ }
            }
            permissions--;
        }
    }

    public synchronized void V(){
        value++;
        if(value<=0){
            permissions++;
            notify();
        }
    }

    public synchronized boolean tryP(long timeout,TimeUnit unit){
        value--;
        if(value>=0)
            return true;
        long deadline =System.nanoTime()+unit.toNanos(timeout);
        while (permissions==0) {
            long remaining = deadline - System.nanoTime();
            if(remaining<=0){
                value++;
                return false;
            }
            try{
                TimeUnit.NANOSECONDS.timedWait(this,remaining);
            } catch(InterruptedException e){}
                
        }
        permissions--;
        return true;
    }

}
