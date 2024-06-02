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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerPerformanceRanking {
    private Player player;
    
    private static final String nbadb = "jdbc:derby://localhost:1527/NBAdb;create=true"; // teamdb URL
    private static Connection conn = null;

    public PlayerPerformanceRanking(Player player) {
        this.player = player;
    }

    public double calcCompositeScore() {

        double actualpoints = 100 / 34;
        double actualrebounds = 100 / 15;
        double actualsteals = 100 / 3;
        double actualassists = 100 / 8;
        double actualblocks = 100 / 3;

        double points = player.getPoints();
        double rebounds = player.getRebounds();
        double steals = player.getSteals();
        double assists = player.getAssists();
        double blocks = player.getBlocks();

        switch (player.getPosition()) {
            case "Forwards":
                return (0.2 * points * actualpoints)       +
                       (0.2 * rebounds * actualrebounds)   +
                       (0.2 * steals * actualsteals)       +
                       (0.2 * assists * actualassists)     +
                       (0.2 * blocks * actualblocks);
            case "Guards":
                return (0.1625 * points * actualpoints)    +
                       (0.1625 * rebounds * actualrebounds)+
                       (0.35 * steals * actualsteals)      +
                       (0.1625 * assists * actualassists)  +
                       (0.1625 * blocks * actualblocks);
            case "Centers":
                return (0.1 * points * actualpoints)       +
                       (0.35 * rebounds * actualrebounds)  +
                       (0.1 * steals * actualsteals)       +
                       (0.1 * assists * actualassists)     +
                       (0.35 * blocks * actualblocks);
            default:
                return 0;

        }
    }
    
    public String printTeamByRank(){
        String teamByRank = "";
        int rank = 0;
        try (Connection conn = DriverManager.getConnection(nbadb);
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM team GROUP BY composite_score");
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
                rank++;
                teamByRank += player.getName() + " [" + player.getCompositeScore() + "] Rank : " + rank + "\n";
             }
        }catch (SQLException e) {
            e.printStackTrace();
            return "Error getting team by rank.";
        }
        return teamByRank;
    }
}
