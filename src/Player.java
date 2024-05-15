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
    private String height;
    private String weight;
    private String position;
    private String salary;
    private String points;
    private String rebounds;
    private String assists;
    private String steals;
    private String blocks;

    public Player(String name, String age, String height, String weight, String position, String points, String rebounds, String assists, String steals, String blocks) {
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

    public String getHeight() {
        return height;
    }

    public String getWeight() {
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
        double pnt = Double.parseDouble(points);
        if (pnt == 37.5)
            return Double.toString(5000);
        else if (pnt >= 28.8 && pnt < 37.5)
            return Double.toString(4000);
        else if (pnt >= 20.0 && pnt < 28.8)
            return Double.toString(3000);
        else if (pnt >= 11.6 && pnt < 20)
            return Double.toString(2000);
        else
            return Double.toString(1000);
    }
    
    public String getPoints() {
        return points;
    }

    public String getRebounds() {
        return rebounds;
    }

    public String getAssists() {
        return assists;
    }

    public String getSteals() {
        return steals;
    }

    public String getBlocks() {
        return blocks;
    }
}
