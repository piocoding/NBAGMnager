import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchPlayer {

    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb"; // teamdb URL

    public Player searchPlayerByAttributes(double height, double weight, String position, double defenseSkill) {
        try (Connection conn = DriverManager.getConnection(nbadb)) {
            String sql = buildDynamicQuery(height, weight, position, defenseSkill);
            try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPlayerFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String buildDynamicQuery(double height, double weight, String position, double defenseSkill) {
        StringBuilder query = new StringBuilder("SELECT * FROM team WHERE ");
        // Add conditions based on provided attributes
        if (height > 0) {
            query.append("height = ").append(height).append(" AND ");
        }
        if (weight > 0) {
            query.append("weight = ").append(weight).append(" AND ");
        }
        if (position != null && !position.isEmpty()) {
            query.append("position = '").append(position).append("' AND ");
        }
        if (defenseSkill > 0) {
            query.append("defenseSkill = ").append(defenseSkill).append(" AND ");
        }
        query.append("1=1"); // To complete the query

        return query.toString();
    }

    private Player createPlayerFromResultSet(ResultSet rs) throws SQLException {
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
}
