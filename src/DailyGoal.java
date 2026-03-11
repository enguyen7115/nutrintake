public class DailyGoal {

    private int caloriesGoal = 0;
    private int proteinGoal = 0;
    private int sugarGoal = 0;
    private int id;

    public DailyGoal(int id, int caloriesGoal, int proteinGoal, int sugarGoal) {
        this.caloriesGoal = caloriesGoal;
        this.proteinGoal = proteinGoal;
        this.sugarGoal = sugarGoal;
        this.id = id;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public int getSugarGoal() {
        return sugarGoal;
    }

    public String getAllGoals() {
        return "Calorie Goal: " + getCaloriesGoal() + " Protein: " + getProteinGoal() + " Sugar: " + getSugarGoal();
    }
}