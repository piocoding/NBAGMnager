/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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

    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb;create=true"; // teamdb URL
    private static Connection conn = null;

    public TeamManager() {
        createTeamTable();
    }

    // create team table
    private void createTeamTable() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(nbadb);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE team ("
                    + "name VARCHAR(255), "
                    + "age VARCHAR(255), "
                    + "position VARCHAR(255), "
                    + "salary VARCHAR(255), "
                    + "points DOUBLE, "
                    + "rebounds DOUBLE, "
                    + "assists DOUBLE, "
                    + "steals DOUBLE, "
                    + "blocks DOUBLE, "
                    + "composite_score DOUBLE)";
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
        try (Connection conn = DriverManager.getConnection(nbadb);
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
        String returnstr = "";
        
        String position = player.getPosition();
        double points = player.getPoints();
        double rebounds = player.getRebounds();
        double steals = player.getSteals();
        double assists = player.getAssists();
        double blocks = player.getBlocks();

        double compositeScore = player.getCompositeScore();        
        
        String sql = "INSERT INTO team (name, age, position, salary, points, rebounds, assists, steals, blocks, composite_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setString(2, player.getAge());
            pstmt.setString(3, position);
            pstmt.setString(4, player.getSalary());
            pstmt.setDouble(5, points);
            pstmt.setDouble(6, rebounds);
            pstmt.setDouble(7, assists);
            pstmt.setDouble(8, steals);
            pstmt.setDouble(9, blocks);
            pstmt.setDouble(10, compositeScore);
            pstmt.executeUpdate();
            returnstr = "Player added successfully.";
            return returnstr + "\n" + checkTeam();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding player.";
        }
    }

    public String removePlayer(String playerName) {
        String returnstr = "";
        if (!isPlayerInTeam(playerName)) {
            return "Player not found in the team.";
        }

        String sql = "DELETE FROM team WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                returnstr = "Player removed successfully.";
                removePlayerFromStackAndQueue(playerName);
                return returnstr + "\n" + checkTeam() + "\n";
            } else {
                return "Error removing player.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error removing player.";
        }
    }
    
    public void removePlayerFromStackAndQueue(String playerName) {
        
        String sql = "DELETE FROM stack WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player removed successfully from stack.");
                
            } else {
                System.out.println("Error removing player from stack.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error removing player from stack.");
        }
        
        sql = "DELETE FROM queue WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player removed successfully from queue.");
                
            } else {
                System.out.println("Error removing player from queue.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error removing player from queue.");
        }
        
    }

    private String checkTeam() {
        StringBuilder result = new StringBuilder();
        int playerCount = 0;
        int F = 0, G = 0, C = 0;
        double totalSalary = 0;

        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM team");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Player player = new Player(
                        rs.getString("name"),
                        rs.getString("age"),
                        rs.getString("position"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks")
                );
                playerCount++;
                totalSalary += Double.parseDouble(player.getSalary());
                if (player.getPosition().equals("Forwards")) {
                    F++;
                } else if (player.getPosition().equals("Guards")) {
                    G++;
                } else {
                    C++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error checking team eligibility.";
        }
        if (F >= 2 && G >= 2 && C >= 2 && totalSalary <= 20000 && playerCount >= 10 && playerCount <= 15) {
            return "Team formed follows NBA rules. You may proceed.";
        } else {
            // Determine specific invalid condition
            String invalid = "Team formed does not follow NBA rules.";
            if (playerCount < 10 || playerCount > 15) {
                invalid += "\nNumber of players must be between 10 to 15 players.";
            }
            if (totalSalary > 20000) {
                invalid += "\nCumulative salary for all players exceeds 20000.";
            }
            if (F < 2) {
                invalid += "\nAt least 2 forwards (F) are required.";
            }
            if (G < 2) {
                invalid += "\nAt least 2 guards (G) are required.";
            }
            if (C < 2) {
                invalid += "\nAt least 2 centers (C) are required.";
            }
            return invalid;
        }
    }

    public String getTeam() {
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM team");
                ResultSet rs = pstmt.executeQuery()) {
            String teamstr = "";
            while (rs.next()) {
                teamstr += rs.getString("Name") + "   [" + rs.getString("Position") + "]   " + rs.getDouble("Salary") + "MYR \n";
            }
            return teamstr;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error getting team.";
        }
    }
}
