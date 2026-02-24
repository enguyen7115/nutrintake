public class Food {
    private String name = "";
    private int calories = 0;
    private int proteins = 0;
    private int sugars = 0;

    public Food(String name, int calories, int proteins, int sugars) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.sugars = sugars;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getProteins() {
        return proteins;
    }

    public int getSugars() {
        return sugars;
    }

    public String getAll() {
        return name + " " + calories + " " + proteins + " " + sugars + " ";
    }
}