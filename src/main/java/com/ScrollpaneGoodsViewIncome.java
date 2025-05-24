package com;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Transaction;
import service.TransactionManager;

public class ScrollpaneGoodsViewIncome extends ScrollPane {

	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");

	public ScrollpaneGoodsViewIncome() {
		this.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-padding: 0;");

		VBox vbox = new VBox();
		vbox.setStyle("-fx-background-color:white");
		vbox.setSpacing(10);

		// Get all income transactions from TransactionManager
		List<Transaction> allTransactions = TransactionManager.getInstance().getTransactions();
		List<Transaction> incomeTransactions = allTransactions.stream()
				.filter(t -> t.getTransactionDirection() == 1)
				.collect(Collectors.toList());

		for (Transaction transaction : incomeTransactions) {
			HBox hbox = new HBox();
			hbox.setSpacing(4);

			// Add income icon
			ImageView iv = new ImageView();
			Image image = new Image(getClass().getResourceAsStream("../image/4.png"));
			iv.setImage(image);
			iv.setPreserveRatio(false);
			iv.setFitWidth(30);
			iv.setFitHeight(30);
			hbox.getChildren().add(iv);

			// Add transaction details
			VBox vb = new VBox();
			vb.setSpacing(2);

			// First line: Primary category and amount
			String amountStr = String.format("%.2f", transaction.getAmount());
			Label categoryAndAmount = new Label(String.format("%-14s +%s", 
				transaction.getPrimaryCategory(), amountStr));
			vb.getChildren().add(categoryAndAmount);

			// Second line: Secondary category and date
			Label detailsAndDate = new Label(String.format("%-14s %s", 
				transaction.getSecondaryCategory(),
				transaction.getTransactionTime().format(dateFormatter)));
			vb.getChildren().add(detailsAndDate);

			hbox.getChildren().add(vb);
			vbox.getChildren().add(hbox);

			// Add separator
			Separator sep = new Separator();
			vbox.getChildren().add(sep);
		}
		
		this.setContent(vbox);
	}
}
