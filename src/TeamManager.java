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
    private InjuryReserveManagement injuryReserve;
    private ContractExtensionQueue contractQueue;

    public TeamManager() {
        injuryReserve = new InjuryReserveManagement();
        contractQueue = new ContractExtensionQueue();
        createTeamTable();
    }

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
                       + "blocks DOUBLE)";
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
        String sql = "INSERT INTO team (name, age, position, salary, points, rebounds, assists, steals, blocks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(nbadb);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setString(2, player.getAge());
            pstmt.setString(3, player.getPosition());
            pstmt.setString(4, player.getSalary());
            pstmt.setDouble(5, player.getPoints());
            pstmt.setDouble(6, player.getRebounds());
            pstmt.setDouble(7, player.getAssists());
            pstmt.setDouble(8, player.getSteals());
            pstmt.setDouble(9, player.getBlocks());
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
        try (Connection conn = DriverManager.getConnection(nbadb);
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

    public void addPlayerToInjuryReserve(String playerName, String injury) {
        Player player = getPlayerByName(playerName);
        if (player != null) {
            injuryReserve.addPlayerToInjuryReserve(player, injury);
        } else {
            System.out.println("Player not found in the team.");
        }
    }

    public void removePlayerFromInjuryReserve() {
        injuryReserve.removePlayerFromInjuryReserve();
    }

    public void displayInjuredPlayers() {
        injuryReserve.displayInjuredPlayers();
    }

    public void addPlayerToContractQueue(String playerName) {
        Player player = getPlayerByName(playerName);
        if (player != null) {
            contractQueue.addPlayerToQueue(player);
        } else {
            System.out.println("Player not found in the team.");
        }
    }

    public void removePlayerFromContractQueue() {
        contractQueue.removePlayerFromQueue();
    }

    public void displayContractQueue() {
        contractQueue.displayContractQueue();
    }

    private Player getPlayerByName(String playerName) {
        try (Connection conn = DriverManager.getConnection(nbadb)) {
            String sql = "SELECT * FROM team WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, playerName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Player(
                            rs.getString("name"),
                            rs.getString("age"),
                            rs.getString("position"),
                            rs.getDouble("points"),
                            rs.getDouble("rebounds"),
                            rs.getDouble("assists"),
                            rs.getDouble("steals"),
                            rs.getDouble("blocks")
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String checkTeam(Player player) {
        StringBuilder result = new StringBuilder();
        int playerCount = 0;
        int F = 0, G = 0, C = 0;
        double totalSalary = 0;

        try (Connection conn = DriverManager.getConnection(nbadb);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM team");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                player = new Player(
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
                if (player.getPosition().equals("Forwards"))
                    F++;
                else if (player.getPosition().equals("Guards"))
                    G++;
                else
                    C++;
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
            if (playerCount < 10 || playerCount > 15)
                invalid += "\nNumber of players must be between 10 to 15 players.";
            if (totalSalary > 20000)
                invalid += "\nCumulative salary for all players exceeds 20000.";
            if (F < 2)
                invalid += "\nAt least 2 forwards (F) are required.";
            if (G < 2)
                invalid += "\nAt least 2 guards (G) are required.";
            if (C < 2)
                invalid += "\nAt least 2 centers (C) are required.";
            return invalid;
        }
    }
}