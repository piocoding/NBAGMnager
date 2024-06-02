/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sadiya
 */
import java.util.Stack;

public class InjuryReserveManagement {
    private Stack<Player> injuryReserve;

    public InjuryReserveManagement() {
        injuryReserve = new Stack<>();
    }

    // Add a player to the injury reserve
    public void addPlayerToInjuryReserve(Player player, String injury) {
        injuryReserve.push(player);
        System.out.println("-- Adding Player to Injury Reserve --");
        System.out.println("Player: " + player.getName());
        System.out.println("Injury: " + injury);
        System.out.println("Status: Added to Injury Reserve");
    }

    // Remove a player from the injury reserve
    public void removePlayerFromInjuryReserve() {
        if (!injuryReserve.isEmpty()) {
            Player player = injuryReserve.pop();
            System.out.println("-- Removing Player from Injury Reserve --");
            System.out.println("Player: " + player.getName());
            System.out.println("Status: Cleared to Play");
        } else {
            System.out.println("No players in the injury reserve.");
        }
    }

    // Display the list of injured players
    public void displayInjuredPlayers() {
        if (injuryReserve.isEmpty()) {
            System.out.println("No players are currently in the injury reserve.");
        } else {
            System.out.println("Injured players:");
            for (Player player : injuryReserve) {
                System.out.println("- " + player.getName());
            }
        }
    }
}