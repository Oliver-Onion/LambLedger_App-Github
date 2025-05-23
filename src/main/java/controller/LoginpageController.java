package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import service.CurrentUserService;
import service.TransactionManager;

public class LoginpageController implements Initializable {

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	private List<User> users = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 在初始化时读取用户数据
		loadUserData();
	}

	private void loadUserData() {
		java.io.File file = new java.io.File("/user_data.csv");
		try (BufferedReader br = new BufferedReader(
				new FileReader("./resources/user_data.csv"))) {
			String line;
			// 跳过标题行
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length == 7) {
					User user = new User();
					user.setId(Integer.parseInt(data[0]));
					user.setUsername(data[1]);
					user.setPassword(data[2]);
					user.setEmail(data[3]);
					user.setPhone(data[4]);
					users.add(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", "File Not Found", "Could not load user data.");
		}
	}

	@FXML
	private void login() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();

		// 验证输入
		if (username.isEmpty() || password.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid Credentials", "Username and password cannot be empty.");
			return;
		}

		// 验证用户
		boolean isValidUser = false;
		for (User user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				isValidUser = true;
				showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome", "Login successful!");
				CurrentUserService.setCurrentUser(user); // 设置当前用户
				TransactionManager.getInstance().loadTransactions();
				try {
					Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/home.fxml")));
					AppInitializer.getPrimaryStage().setScene(scene);
				} catch (IOException e) {
					e.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Error", "File Not Found", "Could not load the home screen.");
				}
				break;
			}
		}

		if (!isValidUser) {
			showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid Credentials", "Incorrect username or password.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void signUp() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/signup.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error", "Could not load the signup screen.");
		}
	}
}