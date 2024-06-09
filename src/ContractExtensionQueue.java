/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sadiya
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class ContractExtensionQueue {

    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb;create=true";
    private static Connection conn = null;

    public ContractExtensionQueue() {
        createQueueTable();
    }

    // create queue table
    private void createQueueTable() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(nbadb);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE queue ("
                    + "name VARCHAR(255))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) { // Table already exists
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Connection unsuccessful.");
            e.printStackTrace();
        }
    }

    private boolean isPlayerInTeam(String name) {
        String sql = "SELECT COUNT(*) FROM team WHERE name = ?";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPlayerInQueue(String name) {
        String sql = "SELECT COUNT(*) FROM queue WHERE name = ?";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // method that checks if player should be queued before adding player to table
    public String queuePlayer(String playerName) {
        try ( Connection sourceConn = PlayersDerby.getConnection()) {
            Player player = PlayersDerby.getPlayerByName(sourceConn, playerName);
            
            // Don't queue player if player does not exist/ not in team/ already in queue.
            if (player == null) {
                return "Player not found in team.";
            }
            if (!isPlayerInTeam(player.getName())) {
                return "Player not found in team.";
            }
            if (isPlayerInQueue(player.getName())) {
                return "Player already in the queue.";
            }
            
            return addPlayerToQueue(player);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error connecting to source database.";
        }
    }

    // actual method that adds player to queue
    private String addPlayerToQueue(Player player) {

        String sql = "INSERT INTO queue (name) VALUES (?)";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.executeUpdate();
            return "Player added successfully to queue.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding player to queue.";
        }
    }

    // removes player that was first in queue
    public String extend() {
        String playerName = getFirstInQueue();
        String sql = "DELETE FROM queue WHERE name = ?";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Contract extended.";
            } else {
                return "Empty queue.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error extending contract.";
        }
    }
    
    private String getFirstInQueue() {
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM queue");  
                ResultSet rs = pstmt.executeQuery()) {
            String playerName = "";
            if (rs.next())
            playerName = rs.getString("Name");
            return playerName;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting first in queue.";
        }
    }

    // get String of the whole queue
    public String getQueue() {
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM queue");  
                ResultSet rs = pstmt.executeQuery()) {
            String queuestr = "Next to extend: ";
            int i = 1;
            while (rs.next()) {
                queuestr += rs.getString("Name") + "\n";
            }
            return queuestr;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting Contract Extension Queue.";
        }
    }

}
