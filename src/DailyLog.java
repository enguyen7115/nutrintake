import java.util.ArrayList;

public class DailyLog{
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<DailyGoal> goalLog = new ArrayList<>();

    //Adds food to array list
    public void addFood(Food food){
        foods.add(food);
    }
    public void addGoal(DailyGoal goal) {
        goalLog.add(goal);
    }

    //Calculates the total calorie amount of current food input
    public int getTotalCalories(){
        int total = 0;
        for(Food food : foods){
            total += food.getCalories();
        }
        return total;
    }

    //Calculates the total protein amount of current food input
    public int getTotalProteins(){
        int total = 0;
        for(Food food : foods){
            total += food.getProteins();
        }
        return total;
    }

    //Calculates the total sugar amount of current food input
    public int getTotalSugars(){
        int total = 0;
        for(Food food : foods){
            total += food.getSugars();
        }
        return total;
    }

    //Prints log of all foods input, plus the totals for all input food
    public void printLog(){
        for(Food food : foods){
            System.out.println(food.getAll());
        }
        System.out.println("Daily Totals:");
        System.out.println("Calories: " + getTotalCalories());
        System.out.println("Proteins: " + getTotalProteins());
        System.out.println("Sugars: " + getTotalSugars());
    }
}