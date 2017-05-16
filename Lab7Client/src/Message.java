import classes.NormalHuman;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class Message implements Serializable {
    protected static final long serialVersionUID = 42L;
    private byte state;
    private LinkedList<NormalHuman> data;
    private LinkedList<Integer> notEditable = new LinkedList<>();
    private byte typeOfOperation;
    public int maxID;
    public static final byte delete = 0;
    public static final byte add = 1;
    public static final byte change = 2;
    public static final byte wait = 3;
    public LinkedList<Integer> getNotEditable() {return notEditable;}
    public void setNotEditable(LinkedList<Integer> notEditable) {this.notEditable = notEditable;}
    public void setTypeOfOperation(byte typeOfOperation) {this.typeOfOperation = typeOfOperation;}
    public byte getTypeOfOperation() {return typeOfOperation;}
    public Message(Message message){
        this.state = message.getState();
        this.data = new LinkedList<>(message.getData());
        this.typeOfOperation = message.getTypeOfOperation();
        this.maxID = message.maxID;
    }
    public Message(byte state, LinkedList<NormalHuman> data){
        this.state=state;
        this.data=data;
    }
    public Message(byte state){
        this.state=state;
        data = new LinkedList<>();
    }
    public LinkedList<NormalHuman> getData(){
        return data;
    }
    public void setState(byte state){
        this.state=state;
    }
    public byte getState(){
        return state;
    }
    public void setData(LinkedList<NormalHuman> data){
        this.data=data;
    }
    public void clearData(){data=null;}
    public boolean isData(){return data!=null;}
}
