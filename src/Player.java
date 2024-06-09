
public class Player {
    private String name;
    private String age;
    private String position;
    private String salary;
    private double points;
    private double rebounds;
    private double assists;
    private double steals;
    private double blocks;
    private PlayerPerformanceRanking compositeScore;
    
    public Player(String name, String age, String position, double points, double rebounds, double assists, double steals, double blocks) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.compositeScore = new PlayerPerformanceRanking(this);
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPosition() {
        if (position.contains("G"))
            return "Guards";
        else if (position.contains("F"))
            return "Forwards";
        else
            return "Centers";
    }

    // Setting player's salary based on average points
    public String getSalary() {
        if (points >= 35)
            return "5000";
        else if (points >= 31.25)
            return "4500";
        else if (points >= 27.5)
            return "4000";
        else if (points >= 23.75)
            return "3500";
        else if (points >= 20.0)
            return "3000";
        else if (points >= 17.5)
            return "2500";
        else if (points >= 15.0)
            return "2000";
        else if (points >= 11.5)
            return "1500";
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
    
    public double getCompositeScore(){
        return compositeScore.calcCompositeScore();
    }
}
