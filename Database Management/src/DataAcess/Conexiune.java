package DataAcess;
import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexiune {
    private static final String Driver = "com.mysql.jdbc.Driver";
    private static final String DbURL = "jdbc:mysql://localhost:3306/TP?useSSL=false";
    private static final String User = "root";
    private static final String Pass = "7saadjv9d02";
    private static Connection connection;

    private Conexiune(){
        try{
            Class.forName(Driver);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Deoarece aceasta clasa este un SingleTon avem nevoie de functia statica getConnection pentru
     * a ne lua legatura cu tabela
     * @return Connection returneaza conexiunea creata
     */
    public static Connection getConnection(){
        if(connection==null){
            try {
                connection = (Connection) DriverManager.getConnection(DbURL, User, Pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }else{
            return connection;
        }
    }

}