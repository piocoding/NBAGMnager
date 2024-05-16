/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nba;


/**
 *
 * @author nuraisyachezulkifli
 */
public class Player {
    private String name;
    private String age;
    private double height;
    private double weight;
    private String position;
    private String salary;
    private double points;
    private double rebounds;
    private double assists;
    private double steals;
    private double blocks;

    public Player(String name, String age, double height, double weight, String position, double points, double rebounds, double assists, double steals, double blocks) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getPosition() {
        if (position == "G")
            return "Guards";
        else if (position == "F")
            return "Forwards";
        else
            return "Centres";
    }

    public String getSalary() {
        if (points == 37.5)
            return "5000";
        else if (points >= 28.8 && points < 37.5)
            return "4000";
        else if (points >= 20.0 && points < 28.8)
            return "3000";
        else if (points >= 11.6 && points < 20)
            return "2000";
        else
            return "1000";
    }
    
    public double getPoints() {
        return points;
    }

    public double getRebounds() {
        return rebounds;
    }

    public double getAssists() {
        return assists;
    }

    public double getSteals() {
        return steals;
    }

    public double getBlocks() {
        return blocks;
    }
}