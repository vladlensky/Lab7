import classes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * Created by Mugenor on 13.04.2017.
 */
public class ButtonsUnderTable {
    private static Color c = null;
    private CollectTable collt;
    private JTable collections;
    private LinkedList<NormalHuman> coll;
    private EditWindow ew = new EditWindow();
    private boolean openedShowWindow = false;
    private boolean openedEditWindow= false;
    private DefaultListModel<String> dlm= new DefaultListModel<>();
    private JList<String> list = new JList<>(dlm);
    public void setColor(Color col){
        c = col;
        ew.setColor(col);
        list.setForeground(c);
    }
    ButtonsUnderTable(JTable table, CollectTable ct, LinkedList<NormalHuman> coll){
        collt=ct;
        collections=table;
        this.coll=coll;
    }
    public void delete(){
        System.out.println("size: " + coll.size() + "\n"+coll);
        System.out.println("trying get: " + collections.getSelectedRow());
        if(collections.getSelectedRow()!=-1){
            Interface.message.getData().clear();
            Interface.message.getData().add(
                    coll.get(
                            collections.getSelectedRow()));
            Interface.message.setState(ConnectionState.NEW_DATA);
            Interface.message.setTypeOfOperation(Message.delete);
            coll.remove(collections.getSelectedRow());
            collt.removeData(collections.getSelectedRow());
            Interface.sendMessage();}
    }
    public void edit(){
        if(collections.getSelectedRow()!=-1&&!openedEditWindow) {
            openedEditWindow=true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int k = collections.getSelectedRow();
                    if(Interface.getBlocking().contains(coll.get(k).getId()))
                        return;
                    Interface.message.setTypeOfOperation(Message.wait);
                    Interface.message.getData().clear();
                    Interface.message.getNotEditable().clear();
                    Interface.message.getNotEditable().add(coll.get(k).getId());
                    Interface.message.setState(ConnectionState.WAITING);
                    Interface.sendMessage();
                    ew = new EditWindow("Edit Person",
                            coll.get(k),
                            collt, k,
                            new EditExit() {
                        @Override
                        public void doOnExit() {
                            System.out.println("do on oxit0!");
                            System.out.println(Interface.message.getNotEditable().get(0));
                            System.out.println(coll.get(k).getId());
                            openedEditWindow = false;
                            Interface.message.setTypeOfOperation(Message.wait);
                            Interface.message.getData().clear();
                            Interface.message.getNotEditable().clear();
                            Interface.message.getNotEditable().add(coll.get(k).getId());
                            Interface.message.setState(ConnectionState.REWAIT);
                            Interface.sendMessage();
                        }
                    });
                    ew.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            System.out.println("do on oxit!");
                            System.out.println(Interface.message.getNotEditable().get(0));
                            System.out.println(coll.get(k).getId());
                            openedEditWindow = false;
                            Interface.message.setTypeOfOperation(Message.wait);
                            Interface.message.getData().clear();
                            Interface.message.getNotEditable().clear();
                            Interface.message.getNotEditable().add(coll.get(k).getId());
                            Interface.message.setState(ConnectionState.REWAIT);
                            Interface.sendMessage();
                        }
                    });
                    ew.setLocation((int)Interface.getFrameLocation().getX()-320,(int)Interface.getFrameLocation().getY());
                }
            });
        }
    }
    public void showThoughts(){
        if(collections.getSelectedRow()!=-1&&!openedShowWindow){
            openedShowWindow=true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame jf= new JFrame("Thoughts Frame");
                    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    jf.setResizable(false);
                    jf.setSize(new Dimension(400,200));
                    jf.setLocationRelativeTo(null);
                    dlm.removeAllElements();
                    for(int i=0;i<coll.get(collections.getSelectedRow()).getThoughtsCount();i++){
                        dlm.addElement(coll.get(collections.getSelectedRow()).getThoughts(i));
                    }
                    list.setFont(new Font("Verdana", Font.PLAIN, 13));
                    list.setLayoutOrientation(JList.VERTICAL);
                    list.setVisibleRowCount(3);
                    JScrollPane scroll = new JScrollPane(list);
                    scroll.setSize(150,75);
                    scroll.setLocation(30,100);
                    jf.add(scroll);
                    jf.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            openedShowWindow=false;
                        }
                    });
                    jf.setVisible(true);
                }
            });}
    }
}
