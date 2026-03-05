import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DailyLog log = new DailyLog();

        Scanner scanner = new Scanner(System.in);


        while (true) {

            System.out.println("1. Add Daily Goal");
            System.out.println("2. Add Food");
            System.out.println("3. Get Daily Log");
            System.out.println("4. Exit");
            System.out.println("Enter your choice by number");
            int choice = Integer.valueOf(scanner.nextLine());

            switch (choice) {

                case 1:

                    System.out.println("Enter Daily Calorie Goal");
                    int caloriesGoal = Integer.valueOf(scanner.nextLine());
                    System.out.println("Enter Daily Protein Goal");
                    int proteinGoal = Integer.valueOf(scanner.nextLine());
                    System.out.println("Enter Daily Sugar Goal");
                    int sugarGoal = Integer.valueOf(scanner.nextLine());

                    DailyGoal goalLog = new DailyGoal(caloriesGoal, proteinGoal, sugarGoal);
                    log.addGoal(goalLog);

                    break;


                case 2:

                    System.out.println("Enter Food Name: ");
                    String name = scanner.nextLine();

                    System.out.println("Enter Calorie Amount: ");
                    int calories = Integer.valueOf(scanner.nextLine());
                    System.out.println("Enter Protein Amount: ");
                    int protein = Integer.valueOf(scanner.nextLine());
                    System.out.println("Enter Sugar Amount: ");
                    int sugar = Integer.valueOf(scanner.nextLine());

                    Food food = new Food (name, calories, protein, sugar);
                    log.addFood(food);

                    break;


                case 3:

                    log.printLog();

                    break;

                case 4:
                    return;

                default:
                    System.out.println("Wrong choice");
                    return;
            }
        }
    }
}