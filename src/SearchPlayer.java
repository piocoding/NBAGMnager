import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchPlayer {

    // Method to search for players based on specified criteria
    public static String search(String playerName, String position, String minAge, String maxAge, String points, String rebounds, String assists, String steals, String blocks){
        // StringBuilder to dynamically construct the SQL query
        StringBuilder query = new StringBuilder("SELECT * FROM statistics WHERE 1=1");
        // "WHERE 1=1" is a common trick to simplify appending further conditions

        // Constructing SQL query based on provided criteria
        // If the playerName parameter is not empty
        // it appends a condition to match player names containing the provided string using the SQL LIKE operator
        if (!playerName.isEmpty()) {
            query.append(" AND name LIKE '%").append(playerName).append("%'");
        }
        // If the position parameter is not equal to "Any"
        // it appends a condition to match players with the specified position
        if (!position.equals("Any")) {
            query.append(" AND position LIKE '%").append(position.charAt(0)).append("%'");
        }
        // If the minAge parameter is not empty
        // it appends a condition to match players with age greater than or equal to the specified minimum age
        if (!minAge.isEmpty()) {
            query.append(" AND age >= ").append(minAge);
        }
        // If the maxAge parameter is not empty
        // it appends a condition to match players with age less than or equal to the specified maximum age
        if (!maxAge.isEmpty()) {
            query.append(" AND age <= ").append(maxAge);
        }
        // If the points parameter is not empty
        // it appends a condition to match players with points greater than or equal to the specified value
        if (!points.isEmpty()) {
            query.append(" AND points >= ").append(Double.parseDouble(points));
        }
        // If the rebounds parameter is not empty
        // it appends a condition to match players with rebounds greater than or equal to the specified value
        if (!rebounds.isEmpty()) {
            query.append(" AND rebounds >= ").append(Double.parseDouble(rebounds));
        }
        // If the assists parameter is not empty
        // it appends a condition to match players with assists greater than or equal to the specified value
        if (!assists.isEmpty()) {
            query.append(" AND assists >= ").append(Double.parseDouble(assists));
        }
        // If the steals parameter is not empty
        // it appends a condition to match players with steals greater than or equal to the specified value
        if (!steals.isEmpty()) {
            query.append(" AND steals >= ").append(Double.parseDouble(steals));
        }
        // If the blocks parameter is not empty
        // it appends a condition to match players with blocks greater than or equal to the specified value
        if (!blocks.isEmpty()) {
            query.append(" AND blocks >= ").append(Double.parseDouble(blocks));
        }

        // Database connection and query execution
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/NBAdb");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            // Connection to database "NBAdb" on localhost, create statement and execute query

            StringBuilder sb = new StringBuilder(); // StringBuilder to accumulate the result string
            int count = 0; // Counter for the number of players found

            // Processing ResultSet to build player statistics
            while (rs.next()) {
                sb.append("Player: ").append(rs.getString("name")).append(" ");
                sb.append("[").append(rs.getString("position")).append("]\n");
                sb.append("Age: ").append(rs.getInt("age")).append("    ");
                sb.append("Points: ").append(rs.getDouble("points")).append("\n");
                sb.append("Rebounds: ").append(rs.getDouble("rebounds")).append("     ");
                sb.append("Assists: ").append(rs.getDouble("assists")).append("\n");
                sb.append("Steals: ").append(rs.getDouble("steals")).append("            ");
                sb.append("Blocks: ").append(rs.getDouble("blocks")).append("\n\n");
                count++; // Increment count for each player found
            }

            // Printing results or a message if no players found
            if (count == 0) {
                return "No players found with the given criteria."; // If no players found
            } else {
                return sb.toString(); // Return the accumulated result string
            }
        } catch (Exception e) {
            // Handling any exceptions that occur during database access or query execution
            e.printStackTrace();
        }
        return "Error getting players."; // Return error message if exception occurs
    }
}
