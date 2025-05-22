package controller;

import model.User;
import util.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProfileController {
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    private User currentUser;

    public void initialize() {
        // 这里可以加载当前用户的信息
        // 例如从session或通过其他方式获取当前登录用户
        // 假设我们有一个方法getCurrentUser()可以获取当前登录用户
        currentUser = UserManager.getUserById(1); // 示例用户ID
        if (currentUser != null) {
            txtUsername.setText(currentUser.getUsername());
            txtPassword.setText(currentUser.getPassword());
            txtEmail.setText(currentUser.getEmail());
            txtPhone.setText(currentUser.getPhone());
        }
    }

    @FXML
    private void saveProfile() {
        if (currentUser != null) {
            currentUser.setUsername(txtUsername.getText());
            currentUser.setPassword(txtPassword.getText());
            currentUser.setEmail(txtEmail.getText());
            currentUser.setPhone(txtPhone.getText());

            boolean success = UserManager.saveUser(currentUser);
            if (success) {
                System.out.println("个人信息保存成功！");
            } else {
                System.out.println("个人信息保存失败！");
            }
        }
    }
}