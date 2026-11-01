package hospital;
public class Patient implements Runnable {
    private int ID;
    private WaitingRoom wr;
    public Patient (int ID , WaitingRoom wr){
        this.ID = ID;
        this.wr = wr;
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



