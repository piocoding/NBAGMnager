
package sem2;

import java.util.List;

public class MainJavaSqlite {
    public static void main(String[] args) {
        // Connect to the database and create tables
        try {
            // Create tables
            JavaSqlite.createPlayerTable();
            JavaSqlite.createInjuredPlayersTable();

            // Insert a sample player into the database
            Player player1 = new Player("John Doe", 6.5, 210, "Forward", 22.5, 10.3, 4.1, 0.123, 1500000);
            JavaSqlite.insertPlayerIntoDatabase(player1);

            // Insert another sample player
            Player player2 = new Player("Jane Smith", 5.8, 150, "Guard", 18.2, 5.4, 6.3, 0.145, 1300000);
            JavaSqlite.insertPlayerIntoDatabase(player2);

            // Search for players with specific criteria
            List<List<String>> searchResults = JavaSqlite.searchPlayersFromDatabase(6.0, ">=", 200, "<=", "Forward", 20, ">=");
            System.out.println("Search results:");
            for (List<String> player : searchResults) {
                System.out.println(player);
            }

            // Get player stats
            List<Player> playerStats = JavaSqlite.getPlayerStatsFromDatabase("John Doe");
            for (Player p : playerStats) {
                System.out.println("Stats for John Doe: " + p);
            }

            // Get all player names
            List<String> allPlayerNames = JavaSqlite.getAllPlayerNamesFromDatabase();
            System.out.println("All player names:");
            for (String name : allPlayerNames) {
                System.out.println(name);
            }

            // Add a player to the injury reserve
            JavaSqlite.addPlayerToInjuryReserve(player1, "Knee injury");

            // Remove a player from the injury reserve
            JavaSqlite.removePlayerFromInjuryReserve("John Doe");

            // Remove a player from the database
            JavaSqlite.removePlayer("Jane Smith");

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    

