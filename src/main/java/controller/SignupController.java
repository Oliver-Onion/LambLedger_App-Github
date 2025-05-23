package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import model.User;

public class SignupController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private List<User> existingUsers = new ArrayList<>();
    private static final String USER_DATA_FILE = "./resources/user_data.csv";

    @FXML
    public void initialize() {
        loadExistingUsers();
    }

    private void loadExistingUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    User user = new User();
                    user.setId(Integer.parseInt(data[0]));
                    user.setUsername(data[1]);
                    user.setPassword(data[2]);
                    user.setEmail(data[3]);
                    user.setPhone(data[4]);
                    existingUsers.add(user);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Data Loading Error", "Could not load existing user data.");
        }
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validation
        if (!validateInputs(username, password, email, phone)) {
            return;
        }

        // Check for duplicates
        if (isDuplicate(username, email, phone)) {
            return;
        }

        // Generate new user ID
        int newId = generateNewUserId();

        // Create new user
        User newUser = new User();
        newUser.setId(newId);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhone(phone);

        // Save to file
        saveNewUser(newUser);

        // Show success message and return to login
        showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful", "You can now login with your credentials.");
        backToLogin();
    }

    private boolean validateInputs(String username, String password, String email, String phone) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Empty Fields", "All fields must be filled.");
            return false;
        }

        if (username.length() < 3 || username.length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Username", "Username must be between 3 and 20 characters.");
            return false;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Password", "Password must be at least 6 characters long.");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        String phoneRegex = "^\\d{10,11}$";
        if (!Pattern.matches(phoneRegex, phone)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Phone", "Please enter a valid phone number (10-11 digits).");
            return false;
        }

        return true;
    }

    private boolean isDuplicate(String username, String email, String phone) {
        for (User user : existingUsers) {
            if (user.getUsername().equals(username)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Error", "Username Taken", "This username is already in use.");
                return true;
            }
            if (user.getEmail().equals(email)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Error", "Email Taken", "This email is already registered.");
                return true;
            }
            if (user.getPhone().equals(phone)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Error", "Phone Taken", "This phone number is already registered.");
                return true;
            }
        }
        return false;
    }

    private int generateNewUserId() {
        return existingUsers.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    private void saveNewUser(User user) {
        try (FileWriter fw = new FileWriter(USER_DATA_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            String newLine = String.format("%d,%s,%s,%s,%s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getPhone());
            
            bw.write(newLine);
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Save Error", "Could not save user data.");
        }
    }

    @FXML
    private void backToLogin() {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/loginpage.fxml")));
            AppInitializer.getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error", "Could not return to login page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 