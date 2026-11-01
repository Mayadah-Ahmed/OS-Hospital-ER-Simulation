package hospital;

import java.util.concurrent.TimeUnit;

public class TreatmentRooms {
    private final HospitalSemaphore rooms;

    public TreatmentRooms(){
        rooms = new HospitalSemaphore(2);
    }

    public boolean tryAcquireRoom(long timeout,TimeUnit unit){
        return rooms.tryP(timeout,unit);
    }

    public void releaseRoom(){
        rooms.V();
    }
}
