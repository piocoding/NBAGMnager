/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sadiya
 */

import java.util.LinkedList;
import java.util.Queue;

public class ContractExtensionQueue {
    private Queue<Player> contractQueue;

    public ContractExtensionQueue() {
        contractQueue = new LinkedList<>();
    }

    // Add a player to the contract extension queue
    public void addPlayerToQueue(Player player) {
        contractQueue.add(player);
        System.out.println("-- Adding Player to Contract Extension Queue --");
        System.out.println("Player: " + player.getName());
        System.out.println("Status: Added to Contract Extension Queue");
    }

    // Remove a player from the contract extension queue
    public void removePlayerFromQueue() {
        if (!contractQueue.isEmpty()) {
            Player player = contractQueue.poll();
            System.out.println("-- Removing Player from Contract Extension Queue --");
            System.out.println("Player: " + player.getName());
            System.out.println("Status: Contract Renewed");
        } else {
            System.out.println("No players in the contract extension queue.");
        }
    }

    // Display the list of players in the contract extension queue
    public void displayContractQueue() {
        if (contractQueue.isEmpty()) {
            System.out.println("No players are currently in the contract extension queue.");
        } else {
            System.out.println("Players in the contract extension queue:");
            for (Player player : contractQueue) {
                System.out.println("- " + player.getName());
            }
        }
    }
}