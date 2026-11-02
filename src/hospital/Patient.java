package hospital;
public class Patient implements Runnable {
    private int ID;
    private WaitingRoom wr;
    private boolean shutdownSignal;

    public Patient (int ID , WaitingRoom wr){
        this.ID = ID;
        this.wr = wr;
        this.shutdownSignal = false;
    }

    private Patient(int ID, WaitingRoom wr, boolean shutdownSignal) {
        this.ID = ID;
        this.wr = wr;
        this.shutdownSignal = shutdownSignal;
    }

    public static Patient createShutdownSignal() {
        return new Patient(-1, null, true);
    }

    public boolean isShutdownSignal(){
        return shutdownSignal;
    }

    public String getName(){
        return "P" + ID;
    }
    @Override
    public void run (){
        System.out.println(getName()+ " has arrived");

        wr.AddPatient(this);
    }
}
