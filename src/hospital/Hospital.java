package hospital;
import java.util.Scanner;

public class Hospital {

    //shared resources
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRooms;

    private HospitalSemaphore treatedSignal;

    private Doctor[] doctors;
    private Patient[] patients;

    private Thread[] doctorThreads;
    private Thread[] patientThreads;

    public void startSimulation(){

        //read simulation parameters
        Scanner input = new Scanner(System.in);

        System.out.print("Waiting room capacity: ");
        int capacity = input.nextInt();

        System.out.print("Number of doctors: ");
        int numOfDoctors = input.nextInt();

        System.out.print("Number of patients: ");
        int numOfPatients = input.nextInt();

        //create shared resources
        waitingRoom = new WaitingRoom(capacity);
        treatmentRooms = new TreatmentRooms();
        treatedSignal = new HospitalSemaphore(0);


        //create doctors and their threads
        doctors = new Doctor[numOfDoctors];
        doctorThreads = new Thread[numOfDoctors];
        for (int i = 0; i < numOfDoctors; i++) {
            doctors[i] = new Doctor(i + 1,
                    waitingRoom,
                    treatmentRooms,
                    treatedSignal);
            doctorThreads[i] = new Thread(doctors[i]);
        }

        // 4. create patients and their threads
        patients = new Patient[numOfPatients];
        patientThreads = new Thread[numOfPatients];
        for (int i = 0; i < numOfPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
            patientThreads[i] = new Thread(patients[i]);
        }

        //start doctor thread (they start and wait for the patient's arrival)
        for (Thread t : doctorThreads) t.start();

        //arrival of patints
        System.out.print("\nPatients arriving: ");
        for (Patient p : patients) System.out.print(p.getName() + " ");
        System.out.println("\n");

        for (Thread t : patientThreads) t.start();

        //wait until EVERY patient has been fully treated and has left
        for (int i = 0; i < numOfPatients; i++) {
            treatedSignal.P();
        }

        //now nothing can be returned to the queue, so shutdown signals are safe
        waitingRoom.AddShutdownSignals(numOfDoctors);


        //wait for all doctors to terminate
        for (Thread t : doctorThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("All patients have been treated.");
        System.out.println("Simulation completed.");
        input.close();
    }

    public static void main(String[] args){
        Hospital hospital = new Hospital();
        hospital.startSimulation();
    }







}
