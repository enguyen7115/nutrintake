package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Handles basic authentication routes like login, registration, and logout.
//ISSUE: This implementation uses in-memory storage for development purposes. Users will be lost when the application restarts.
@Controller
public class AuthController {

    //displays the login page (GET request)
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    //Processes login form submission (POST request).
    @PostMapping("/login")
    public String doLogin(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes ra) {

        // Look up stored password for this email (null if user doesn't exist)
        boolean success = Login.verifyUserLoginSuccess(email, password, "", "");

        if (success) {
            session.setAttribute("userEmail", email);
            return "redirect:/dashboard";
        }

        //if the credentials are invalid the user is shown a message and redirected to login
        ra.addFlashAttribute("msg", "Invalid email or password");
        return "redirect:/login";
    }

    //Displays the sign-up page (GET request).
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    //Processes registration form submission (POST request).
    @PostMapping("/register")
    public String doRegister(@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, HttpSession session, RedirectAttributes ra) {

        // checks that both the password fields match
        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("msg", "Passwords do not match.");
            return "redirect:/register";
        }

        // Prevent duplicate accounts
        if (Login.userExists(email)) {
            ra.addFlashAttribute("msg", "Email is already registered.");
            return "redirect:/register";
        }

        String salt = registerUser.generateSalt();
        String hash = Login.hashPassword(salt, password);

        registerUser.saveUser(email, salt, hash);
        session.setAttribute("userEmail", email);
        return "redirect:/dashboard";
    }

    //Logs the user out by invalidating the session.
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}