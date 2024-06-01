/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Sadiya
 */
public class Test_main_Injury_Queue{
    public static void main(String[] args) {
        TeamManager teamManager = new TeamManager();
        
        // Example of adding and removing players from injury reserve
        System.out.println("=== Injury Reserve Management ===");
        teamManager.addPlayerToInjuryReserve("LeBron James", "Sprained Ankle");
        teamManager.displayInjuredPlayers();
        teamManager.removePlayerFromInjuryReserve();
        teamManager.displayInjuredPlayers();
        
        // Example of adding and removing players from contract queue
        System.out.println("\n=== Contract Extension Queue ===");
        teamManager.addPlayerToContractQueue("Stephen Curry");
        teamManager.displayContractQueue();
        teamManager.removePlayerFromContractQueue();
        teamManager.displayContractQueue();
    }
}
