import classes.NormalHuman;
import com.sun.rowset.CachedRowSetImpl;
import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class DataBaseCommunication {
    private final PGConnectionPoolDataSource pooledDataSource = new PGConnectionPoolDataSource();
    private CachedRowSet rowSet;
    private final String username;
    private final String password;
    public DataBaseCommunication(String url, String username, String password, String driver)throws ClassNotFoundException{
        this.username=username;
        this.password=password;
        pooledDataSource.setUrl(url);
        Class.forName(driver);
    }
    public void registerQuery(String sql)throws SQLException{
        Connection connection = pooledDataSource.getConnection(username, password);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rowSet = new CachedRowSetImpl();
        rowSet.populate(rs);
        rs.close();
        statement.close();
        connection.close();
    }
    public CachedRowSet registerQueryAndGetRowSet(String sql) throws SQLException{
        registerQuery(sql);
        return rowSet;
    }
    public void Update(Message message){
        try {
            LinkedList<NormalHuman> NewData = message.getData();
            if(message.getTypeOfOperation()==2)
                registerQuery("update normalhuman set id = " + NewData.get(0).getId() + ", name = " + NewData.get(0).getName() + ", age = " + NewData.get(0).getAge() + ", troubleswiththelaw = " + NewData.get(0).getTroublesWithTheLaw() + " where id = " + NewData.get(0).getId() + ";" );
            if(message.getTypeOfOperation()==1)
                registerQuery("insert into normalhuman values (" + NewData.get(0).getId() + "," + NewData.get(0).getName() + "," + NewData.get(0).getAge() + "," + NewData.get(0).getTroublesWithTheLaw() + ");");
            if(message.getTypeOfOperation()==0)
                registerQuery("delete from normalhuman where id = " + NewData.get(0).getId() + ";");

        }catch(SQLException e){e.printStackTrace();}
    }
    public CachedRowSet getRowSet(){
        return rowSet;
    }
}
