package sem2;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JavaSqlite {
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";
    private static Connection connection;

    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Connected to SQLite database.");
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to load SQLite JDBC driver");
                e.printStackTrace();
            }
        }
        return connection;
    }
    // Creates a table for storing player information
    public static void createPlayerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Players (" +
                     "Name TEXT PRIMARY KEY, " +
                     "Height REAL, " +
                     "Weight REAL, " +
                     "Position TEXT, " +
                     "PPG REAL, " +
                     "RPG REAL, " +
                     "APG REAL, " +
                     "PIE REAL, " +
                     "Salary REAL)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Player table created successfully");
        } catch (SQLException e) {
            System.out.println("Error creating player table: " + e.getMessage());
        }
    }

    

    // Inserts a player into the database
    public static void insertPlayerIntoDatabase(Player player) {
        String sql = "INSERT INTO Players (Name, Height, Weight, Position, PPG, RPG, APG, PIE, Salary) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setDouble(2, player.getHeight());
            pstmt.setDouble(3, player.getWeight());
            pstmt.setString(4, player.getPosition());
            pstmt.setDouble(5, player.getPpg());
            pstmt.setDouble(6, player.getRpg());
            pstmt.setDouble(7, player.getApg());
            pstmt.setDouble(8, player.getPie());
            pstmt.setDouble(9, player.getSalary());
            
            pstmt.executeUpdate();
            System.out.println("Player " + player.getName() + " added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding player to the database: " + e.getMessage());
        }
    }


    // Removes a player from the Players table
    public static void removePlayer(String playerName) {
        String sql = "DELETE FROM Players WHERE Name = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player " + playerName + " removed from the database.");
            } else {
                System.out.println("Player " + playerName + " not found in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing player: " + e.getMessage());
        }
    }
    public  static List<List<String>> searchPlayersFromDatabase(double height, String heightOperator, double weight, String weightOperator, String position, double ppg, String ppgOperator) {
        List<List<String>> searchResult = new ArrayList<>();
        String sql = "SELECT * FROM Players WHERE ";

        // Constructing SQL query based on user inputs
        sql += "Height " + heightOperator + " " + height + " AND ";
        sql += "Weight " + weightOperator + " " + weight + " AND ";
        sql += "Position = '" + position + "' AND ";
        sql += "PPG " + ppgOperator + " " + ppg;

        try (Connection conn = JavaSqlite.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterating through the result set and adding player data to the search result list
            while (rs.next()) {
                List<String> player = new ArrayList<>();
                player.add(rs.getString("Name"));
                player.add(rs.getString("Height"));
                player.add(rs.getString("Weight"));
                player.add(rs.getString("Position"));
                player.add(rs.getString("PPG"));
                player.add(rs.getString("RPG"));
                player.add(rs.getString("APG"));
                player.add(rs.getString("PIE"));
                player.add(rs.getString("Salary"));
                searchResult.add(player);
            }
        } catch (SQLException e) {
            System.out.println("Error searching players: " + e.getMessage());
        }
        return searchResult;
    }
    
    // Method to create a table for storing injured players
    public static void createInjuredPlayersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS InjuredPlayers (" +
                     "Name TEXT PRIMARY KEY, " +
                     "Injury TEXT)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Injured players table created successfully");
        } catch (SQLException e) {
            System.out.println("Error creating injured players table: " + e.getMessage());
        }
    }

    // Method to add a player to the injury reserve
    public static void addPlayerToInjuryReserve(Player p, String problem) {
        String sql = "INSERT INTO InjuredPlayers (Name, Injury) VALUES (?, ?)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getName());
            pstmt.setString(2, problem);
            pstmt.executeUpdate();
            System.out.println("Player " + p.getName() + " added to the injury reserve.");
        } catch (SQLException e) {
            System.out.println("Error adding player to the injury reserve: " + e.getMessage());
        }
    }

    // Method to remove a player from the injury reserve
    public static void removePlayerFromInjuryReserve(String playerName) {
        String sql = "DELETE FROM InjuredPlayers WHERE Name = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player " + playerName + " removed from the injury reserve.");
            } else {
                System.out.println("Player " + playerName + " not found in the injury reserve.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing player from the injury reserve: " + e.getMessage());
        }
    }
    
   // Method to get player stats from the database
    public static List<Player> getPlayerStatsFromDatabase(String playerName) {
        List<Player> playerData = new ArrayList<>();
        String sql = "SELECT * FROM Players WHERE Name = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            // Check if the player is found in the database
            if (rs.next()) {
                // Create a new Player object with retrieved data
                Player player = new Player();
                player.setName(playerName);
                player.setPosition(rs.getString("Position"));
                player.setPpg(rs.getDouble("PPG"));
                player.setRpg(rs.getDouble("RPG"));
                player.setApg(rs.getDouble("APG"));
                
                // Add the player to the list
                playerData.add(player);
            } else {
                System.out.println("Player " + playerName + " is not in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving player stats: " + e.getMessage());
        }
        return playerData;
    }
    
    public static List<String> getAllPlayerNamesFromDatabase() {
        List<String> allPlayerNames = new ArrayList<>();
        String sql = "SELECT Name FROM Players";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            // Retrieve all player names from the database
            while (rs.next()) {
                allPlayerNames.add(rs.getString("Name"));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving player names from database: " + e.getMessage());
        }

        return allPlayerNames;
    }

} 

    
