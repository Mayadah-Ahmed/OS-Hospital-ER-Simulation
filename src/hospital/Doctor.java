package hospital;
import java.util.concurrent.TimeUnit;

public class Doctor implements Runnable{
    private int ID;
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRooms;
    private HospitalSemaphore treatedSignal;

    public Doctor(int ID, WaitingRoom waitingRoom,
                  TreatmentRooms treatmentRooms,
                  HospitalSemaphore treatedSignal){
        this.ID = ID;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.treatedSignal = treatedSignal;
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
            System.out.println(getName() + " is assigned to " + patient.getName());

            System.out.println(getName() + "  is trying to acquire a treatment room");
            boolean roomAcquired = treatmentRooms.tryAcquireRoom(3, TimeUnit.SECONDS);

            if (roomAcquired) {
                System.out.println(getName() + " acquired a treatment room");
                System.out.println(getName() + " begins treating " + patient.getName());

                //Random Treatment Time using Math.random()
                int randomTreatmentTime = (int) (Math.random() * 5) + 1;
                System.out.println(patient.getName() + " will be treated for " + randomTreatmentTime + " SECONDS");

                boolean treatmentCompleted = false;
                try {
                    Thread.sleep(randomTreatmentTime * 1000L);
                    treatmentCompleted = true;
                    System.out.println(getName() + " finished treating " + patient.getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    treatmentRooms.releaseRoom();
                    System.out.println(getName() + " released the treatment room");
                }
                if(treatmentCompleted) {
                    System.out.println(patient.getName() + " left");
                    treatedSignal.V();
                }
            } else {
                System.out.println(getName() + " couldn't acquire a treatment room");
                System.out.println(getName() + " is sending " + patient.getName() + " back to the waiting room");
                waitingRoom.ReturnPatient(patient);
            }
            System.out.println(getName() + " is now available");

            if(Thread.currentThread().isInterrupted()){ break; }
        }
    }
}