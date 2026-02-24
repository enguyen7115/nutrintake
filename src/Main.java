import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ArrayList<Food> Log = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Add Food");
        System.out.println("2. Get Daily Log");
        System.out.println("3. Exit");
        System.out.println("Enter your choice by number");
        int choice = Integer.valueOf(scanner.nextLine());

        while (true) {
            if (choice == 1) {

                System.out.println("Enter Food Name: ");
                String name = scanner.nextLine();

                if (name.equals("")) {
                    break;
                }

                System.out.println("Enter Calorie Amount: ");
                int calories = Integer.valueOf(scanner.nextLine());
                System.out.println("Enter Protein Amount: ");
                int protein = Integer.valueOf(scanner.nextLine());
                System.out.println("Entere Sugar Amount: ");
                int sugar = Integer.valueOf(scanner.nextLine());

                Food food = new Food(name, calories, protein, sugar);
            }

            if (choice == 2) {

                break;
            }

            if (choice == 3) {
                break;
            }
        }
    }
}