
package hospital;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class WaitingRoom {
    private Queue<Patient> patients;
    private HospitalSemaphore EmptySeats;
    private HospitalSemaphore TakenSeats;
    private HospitalSemaphore lock;
    public WaitingRoom(int capacity) {
        patients = new LinkedList<>();
        EmptySeats = new HospitalSemaphore (capacity);
        TakenSeats = new HospitalSemaphore (0);
        lock = new HospitalSemaphore(1);

    }
    public void AddPatient(Patient patient){
        if (!EmptySeats.tryP(0 , TimeUnit.MILLISECONDS)){
            System.out.println("Waiting room is full!, Please wait outside");
            System.out.println(patient.getName() + "  is waiting outside for an empty seat");
            EmptySeats.P();
        }
        lock.P();
        patients.add(patient);
        lock.V();
        TakenSeats.V();
        System.out.println(patient.getName()+ " entered Waiting room");

    }
    public  Patient TakePatient(){
        TakenSeats.P();
        lock.P();
        Patient patient = patients.remove();
        lock.V();
        EmptySeats.V();
        return patient;

    }
    public void ReturnPatient (Patient patient) {
        System.out.println(patient.getName() + " is returning to the waiting room");
        AddPatient(patient);
    }
}