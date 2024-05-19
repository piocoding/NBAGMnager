package sem2;

public class Player {
    private String name;
    private double height;
    private double weight;
    private String position;
    private double ppg;
    private double rpg;
    private double apg;
    private double pie;
    private double salary;

    // Constructor
    public Player(String name, double height, double weight, String position, double ppg, double rpg, double apg, double pie, double salary) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.ppg = ppg;
        this.rpg = rpg;
        this.apg = apg;
        this.pie = pie;
        this.salary = salary;
    }

    // Default constructor
    public Player() {}

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getPpg() {
        return ppg;
    }

    public void setPpg(double ppg) {
        this.ppg = ppg;
    }

    public double getRpg() {
        return rpg;
    }

    public void setRpg(double rpg) {
        this.rpg = rpg;
    }

    public double getApg() {
        return apg;
    }

    public void setApg(double apg) {
        this.apg = apg;
    }

    public double getPie() {
        return pie;
    }

    public void setPie(double pie) {
        this.pie = pie;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", position='" + position + '\'' +
                ", ppg=" + ppg +
                ", rpg=" + rpg +
                ", apg=" + apg +
                ", pie=" + pie +
                ", salary=" + salary +
                '}';
    }
}
