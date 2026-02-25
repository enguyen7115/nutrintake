import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DailyLog log = new DailyLog();

        Scanner scanner = new Scanner(System.in);


        while (true) {

            System.out.println("1. Add Food");
            System.out.println("2. Get Daily Log");
            System.out.println("3. Exit");
            System.out.println("Enter your choice by number");
            int choice = Integer.valueOf(scanner.nextLine());

            switch (choice) {

                case 1:

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

                case 2:

                    log.printLog();

                    break;

                case 3:
                    return;

                default:
                    System.out.println("Wrong choice");
                    break;
            }
        }
    }
}