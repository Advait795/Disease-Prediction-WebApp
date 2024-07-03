package diseasewebapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class connectDB {
    private static Connection connection;

    static{
        String url = "jdbc:mysql://dragon.ukc.ac.uk:3306/ad918";
        String username = "ad918";
        String password = "m3rattu";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            if(connection != null){
                System.out.println("Connected to the database");

            }



        }catch(ClassNotFoundException | SQLException e){    
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}
