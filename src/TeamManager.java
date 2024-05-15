/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nba;


/**
 *
 * @author nuraisyachezulkifli
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TeamManager {
    private static final String teamdb = "jdbc:derby:teamDB;create=true";

    public TeamManager() {
        createTeamTable();
    }

    private void createTeamTable() {
        try (Connection conn = DriverManager.getConnection(teamdb);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE team ("
                       + "name VARCHAR(255), "
                       + "age VARCHAR(255), "
                       + "height VARCHAR(255), "
                       + "weight VARCHAR(255), "
                       + "position VARCHAR(255), "
                       + "points VARCHAR(255), "
                       + "rebounds VARCHAR(255), "
                       + "assists VARCHAR(255), "
                       + "steals VARCHAR(255), "
                       + "blocks VARCHAR(255))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) { // Table already exists
                e.printStackTrace();
            }
        }
    }

    public String addPlayerFromSourceDB(String playerName) {
        try (Connection sourceConn = PlayersDerby.getConnection()) {
            Player player = PlayersDerby.getPlayerByName(sourceConn, playerName);
            if (player == null) {
                return "Player not found in source database.";
            }

            if (isPlayerInTeam(player.getName())) {
                return "Player already in the team.";
            }

            return addPlayerToTeamDB(player);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error connecting to source database.";
        }
    }

    private boolean isPlayerInTeam(String name) {
        String sql = "SELECT COUNT(*) FROM team WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(teamdb);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String addPlayerToTeamDB(Player player) {
        String sql = "INSERT INTO team (name, age, height, weight, position, points, rebounds, assists, steals, blocks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(teamdb);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setString(2, player.getAge());
            pstmt.setString(3, player.getHeight());
            pstmt.setString(4, player.getWeight());
            pstmt.setString(5, player.getPosition());
            pstmt.setString(6, player.getPoints());
            pstmt.setString(7, player.getRebounds());
            pstmt.setString(8, player.getAssists());
            pstmt.setString(9, player.getSteals());
            pstmt.setString(10, player.getBlocks());
            pstmt.executeUpdate();
            return "Player added successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding player.";
        }
    }

    public String removePlayer(String playerName) {
        if (!isPlayerInTeam(playerName)) {
            return "Player not found in the team.";
        }

        String sql = "DELETE FROM team WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(teamdb);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Player removed successfully.";
            } else {
                return "Error removing player.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error removing player.";
        }
    }
}

