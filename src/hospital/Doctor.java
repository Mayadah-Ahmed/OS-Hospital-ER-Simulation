package hospital;
import java.util.concurrent.TimeUnit;

public class Doctor implements Runnable{
    private int ID;
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRooms;

    public Doctor(int ID, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms){
        this.ID = ID;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    public String getName() {
        return "Doctor " + ID;
    }

    @Override
    public void run(){
        while(true) {
            Patient patient = waitingRoom.TakePatient();
            if (patient.isShutdownSignal()){
                System.out.println(getName()+ " is shutting down");
                break;
            }

            boolean roomAcquired = treatmentRooms.tryAcquireRoom(3, TimeUnit.SECONDS);

            if (roomAcquired) {
                System.out.println(getName() + " acquired a treatment room ");
                System.out.println(getName() + " is treating " + patient.getName());

                //Random Treatment Time using Math.random()
                int randomTreatmentTime = (int) (Math.random() * 5) + 1;
                System.out.println(patient.getName() + " will be treated for " + randomTreatmentTime + " SECONDS");

                boolean treatmentCompleted = false;
                try {
                    Thread.sleep(randomTreatmentTime * 1000);
                    treatmentCompleted = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    treatmentRooms.releaseRoom();
                    System.out.println(getName() + " released the treatment room ");
                }
                if(treatmentCompleted) {
                    System.out.println(patient.getName() + " left ");
                }
            } else {
                System.out.println(getName() + " couldn't acquire a treatment room ");
                waitingRoom.ReturnPatient(patient);
            }

            if(Thread.currentThread().isInterrupted()){ break; }
        }
    }
}
