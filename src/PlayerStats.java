/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ayu Sabrina
 */

public class PlayerStats {
    private static final String csvFile = "statistics.csv"; //change accordingly
    private static final String statsUrl = "jdbc:derby://localhost:1527/NBAdb;create=true";
    private static Connection connection = null;
    
    public static void main (String[] args){
        statsdatabase();
    }
    
    public static void statsdatabase(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(statsUrl);
            System.out.println("Database is connected.");
            
            createTable();
            csvToDatabase();

        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Connection unsuccessful.");
            e.printStackTrace();
        }
    }
    
    public static void createTable() throws SQLException{
        try{
            String createTable = "CREATE TABLE statistics (" +
                "name VARCHAR(50)," +
                "position VARCHAR(5)," +
                "age INT," +
                "rebounds DOUBLE," +
                "assists DOUBLE," +
                "steals DOUBLE," +
                "blocks DOUBLE," +
                "points DOUBLE" +
                ")";

        Statement stmt = connection.createStatement();
        
        //execute SQL statement to create the table
        stmt.executeUpdate(createTable);
            System.out.println("Table successfully created.");
        
        }catch(SQLException e){
            if (!"X0Y32".equals(e.getSQLState())) { // Table already exists
                e.printStackTrace();
            }
        }
    }
    
    public static void csvToDatabase(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            
            String line;
            //skip the header line if present
            reader.readLine();
            
            //prepare SQL INSERT statement
            String sql = "INSERT INTO statistics (name, position, age, rebounds, assists, steals, blocks, points) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Read and insert data from CSV
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Assuming CSV delimiter is comma
                // Set values for each column in the prepared statement
                pstmt.setString(1, data[0]); // PLAYER
                pstmt.setString(2, data[1]); // POS
                pstmt.setInt(3, Integer.parseInt(data[2])); // AGE
                pstmt.setDouble(4, Double.parseDouble(data[3])); // TRB
                pstmt.setDouble(5, Double.parseDouble(data[4])); // AST
                pstmt.setDouble(6, Double.parseDouble(data[5])); // STL
                pstmt.setDouble(7, Double.parseDouble(data[6])); // BLK
                pstmt.setDouble(8, Double.parseDouble(data[7])); // PTS
                // Execute the INSERT statement
                pstmt.executeUpdate();
            }
            System.out.println("Data transfer is completed.");
        
        }catch(IOException | NumberFormatException | SQLException e){
            e.printStackTrace();
        }
    }
    
    
}
