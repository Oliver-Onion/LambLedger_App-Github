package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.CurrentUserService;
import util.WechatPayImporter;

public class StatisticsController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	void billcenter() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/billcenter.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onImportTableClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择CSV文件");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV文件", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(AppInitializer.getPrimaryStage());
		if (selectedFile != null) {
			String filePath = selectedFile.getAbsolutePath();
			int userId = CurrentUserService.getCurrentUser().getId(); // 获取当前用户ID
			WechatPayImporter importer = new WechatPayImporter(new TransactionDAO());
			boolean success = importer.importWechatPayCSV(filePath, userId);
			if (success) {
				showAlert("导入成功", "交易记录导入成功！");
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/preview.fxml"));
					Parent root = loader.load();
					ImportPreviewController controller = loader.getController();
					controller.setTransactions(importer.getTransactions());
					Scene scene = new Scene(root);
					Stage stage = new Stage();
					stage.setTitle("导入预览");
					stage.setScene(scene);
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				showAlert("导入失败", "交易记录导入失败！");
			}
		}
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	void closeAction() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/home.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}