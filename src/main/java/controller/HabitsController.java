package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class HabitsController implements Initializable {

	@FXML
	ListView<String> list1;
	@FXML
	ListView<String> list2;
	@FXML
	ListView<String> list3;
    ObservableList<String> items = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {
		for (int i = 0; i < 4; i++) {
			items.add("note"+i);
		}
		list1.setItems(items);
		list2.setItems(items);
		list3.setItems(items);
		list1.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });
		list3.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });
		list2.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });
	}

}
