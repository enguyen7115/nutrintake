import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class Login {

    private static final String myPepper = "MyTestPepper";

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String usernameInput = scanner.nextLine();

        System.out.print("Enter your password: ");
        String passwordInput = scanner.nextLine();

        String filePath = "AccountData.txt";

        boolean success = verifyUserLoginSuccess(usernameInput, passwordInput, filePath, ",");

        System.out.println(success);

        scanner.close();
    }

    public static String hashPassword(String salt, String password) {
        try {
            String saltPass = salt + password + myPepper;

            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] hash = digester.digest(saltPass.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02X", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyUserLoginSuccess(String usernameInput, String passwordInput, String filePath, String delimiter) {
        //String currentLine;
        //String data[];
        String sql = "SELECT salt, passwordHash FROM users WHERE username = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement prepState = connection.prepareStatement(sql)) {

            prepState.setString(1, usernameInput);

            ResultSet resultSet = prepState.executeQuery();

            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String passwordHash = resultSet.getString("passwordHash");

                String computedHash = hashPassword(salt, passwordInput);

                return passwordHash.equals(computedHash);

            }


            //FileReader filereader = new FileReader(filePath);
            //BufferedReader bufferedReader = new BufferedReader(filereader);

            //while ((currentLine = bufferedReader.readLine()) != null) {
            //    data = currentLine.split(delimiter);
            //    String username = data[0];
            //    String salt = data[1];
            //    String password = data[2];

            //    String computedHashPass = hashPassword(salt, passwordInput);

            //    if (username.equals(usernameInput) && password.equals(computedHashPass)){
            //        return true;
            //    }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
            // catch (FileNotFoundException e) {
            // throw new RuntimeException(e);
            // } catch (IOException e) {
            //     throw new RuntimeException(e);
            // }
        }
    }
}