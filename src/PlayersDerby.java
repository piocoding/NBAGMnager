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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayersDerby {
    private static final String playerdb = "jdbc:derby:MyDatabase"; // playerdb URL

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(playerdb)) {
            if (connection != null) {
                System.out.println("Connected to the database");

                // Retrieve and process players
                Player player = getPlayerByName(connection, "John Doe");
                if (player != null) {
                    System.out.println("Player found: " + player.getName());
                } else {
                    System.out.println("Player not found");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Player getPlayerByName(Connection connection, String playerName) {
        String sql = "SELECT name, age, position, points, rebounds, assists, steals, blocks FROM MyTable WHERE name = '" + playerName + "'";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return createPlayerFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static Player createPlayerFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String age = rs.getString("age");
        String position = rs.getString("position");
        double points = rs.getDouble("points");
        double rebounds = rs.getDouble("rebounds");
        double assists = rs.getDouble("assists");
        double steals = rs.getDouble("steals");
        double blocks = rs.getDouble("blocks");

        return new Player(name, age, position, points, rebounds, assists, steals, blocks);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(playerdb);
    }
}
