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
import java.util.Stack;

public class InjuryReserveManagement {
    
    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb;create=true";
    private static Connection conn = null;

    public InjuryReserveManagement() {
        createStackTable();
    }

    // create stack table
    private void createStackTable() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(nbadb);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE stack ("
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

    private boolean isPlayerInStack(String name) {
        String sql = "SELECT COUNT(*) FROM stack WHERE name = ?";
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

    // method that checks if player should be added to stack before adding to table
    public String stackPlayer(String playerName) {
        try ( Connection sourceConn = PlayersDerby.getConnection()) {
            Player player = PlayersDerby.getPlayerByName(sourceConn, playerName);
            
            // Don't add to stack if player doesn't exist/ is not in the team/ already in the stack.
            if (player == null) {
                return "Player not found in team.";
            }
            if (!isPlayerInTeam(player.getName())) {
                return "Player not found in team.";
            }
            if (isPlayerInStack(player.getName())) {
                return "Player already in the reserve.";
            }

            return addPlayerToStack(player);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error connecting to source database.";
        }
    }

    // actual method that adds player to stack
    private String addPlayerToStack(Player player) {

        String sql = "INSERT INTO stack (name) VALUES (?)";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.executeUpdate();
            return "Player added successfully to injury reserve.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding player to stack.";
        }
    }

    // removes player that was last added to the stack
    public String clearedToPlay() {
        String playerName = getTopInStack();
        String sql = "DELETE FROM stack WHERE name = ?";
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Player cleared to play.";
            } else {
                return "Empty reserve.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error clearing player to play.";
        }
    }

    private String getTopInStack() {
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM stack");  
                ResultSet rs = pstmt.executeQuery()) {
            String playerName = "";
            while (rs.next())
            playerName = rs.getString("Name");
            return playerName;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting top of stack.";
        }
    }

    // get String of the whole stack
    public String getStack() {
        try ( Connection conn = DriverManager.getConnection(nbadb);  
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM stack");  
                ResultSet rs = pstmt.executeQuery()) {
            String stackstr = "";
            int i = 1;
            while (rs.next()) {
                stackstr = rs.getString("Name") + "\n" + stackstr;
            }
            return "Monitoring Recovery: " + stackstr;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting Injury Reserve Management.";
        }
    }

}
