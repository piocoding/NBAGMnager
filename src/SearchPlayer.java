import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchPlayer {

    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb;create=true"; // teamdb URL

    // Method to search for a player based on specified attributes
    public Player searchPlayerByAttributes(String name, String age, String position, String salary,
                                           Double points, Double rebounds, Double assists,
                                           Double steals, Double blocks) {
        try (Connection conn = DriverManager.getConnection(nbadb)) {
            String sql = buildDynamicQuery(name, age, position, salary, points, rebounds, assists, steals, blocks);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int paramIndex = 1;

                // Set parameters for the prepared statement
                if (name != null && !name.isEmpty()) {
                    pstmt.setString(paramIndex++, name);
                }
                if (age != null && !age.isEmpty()) {
                    pstmt.setString(paramIndex++, age);
                }
                if (position != null && !position.isEmpty()) {
                    pstmt.setString(paramIndex++, position);
                }
                if (salary != null && !salary.isEmpty()) {
                    pstmt.setString(paramIndex++, salary);
                }
                if (points != null) {
                    pstmt.setDouble(paramIndex++, points);
                }
                if (rebounds != null) {
                    pstmt.setDouble(paramIndex++, rebounds);
                }
                if (assists != null) {
                    pstmt.setDouble(paramIndex++, assists);
                }
                if (steals != null) {
                    pstmt.setDouble(paramIndex++, steals);
                }
                if (blocks != null) {
                    pstmt.setDouble(paramIndex++, blocks);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return createPlayerFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if no player matching the criteria is found
    }

    // Method to dynamically build the SQL query based on search attributes
    private String buildDynamicQuery(String name, String age, String position, String salary,
                                     Double points, Double rebounds, Double assists,
                                     Double steals, Double blocks) {
        StringBuilder query = new StringBuilder("SELECT * FROM team WHERE 1=1");

        // Add conditions based on provided attributes
        if (name != null && !name.isEmpty()) {
            query.append(" AND name = ?");
        }
        if (age != null && !age.isEmpty()) {
            query.append(" AND age = ?");
        }
        if (position != null && !position.isEmpty()) {
            query.append(" AND position = ?");
        }
        if (salary != null && !salary.isEmpty()) {
            query.append(" AND salary = ?");
        }
        if (points != null) {
            query.append(" AND points = ?");
        }
        if (rebounds != null) {
            query.append(" AND rebounds = ?");
        }
        if (assists != null) {
            query.append(" AND assists = ?");
        }
        if (steals != null) {
            query.append(" AND steals = ?");
        }
        if (blocks != null) {
            query.append(" AND blocks = ?");
        }

        return query.toString();
    }

    // Method to create a Player object from the database result set
    private Player createPlayerFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String age = rs.getString("age");
        String position = rs.getString("position");
        String salary = rs.getString("salary");
        double points = rs.getDouble("points");
        double rebounds = rs.getDouble("rebounds");
        double assists = rs.getDouble("assists");
        double steals = rs.getDouble("steals");
        double blocks = rs.getDouble("blocks");

        return new Player(name, age, position, points, rebounds, assists, steals, blocks);
    }
    
   
}
