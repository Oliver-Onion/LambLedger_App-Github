package com;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Transaction;
import service.TransactionManager;

public class ScrollpaneGoodsViewExpense extends ScrollPane{

	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
	public ScrollpaneGoodsViewExpense() {
		this.setStyle("-fx-border-width: 0; -fx-background-color: transparent; -fx-padding: 0;");

		VBox vbox=new VBox();
		vbox.setStyle("-fx-background-color:white");
		vbox.setSpacing(10);

		// Get all expense transactions from TransactionManager
		List<Transaction> allTransactions = TransactionManager.getInstance().getTransactions();
		List<Transaction> expenseTransactions = allTransactions.stream()
				.filter(t -> t.getTransactionDirection() == -1)
				.collect(Collectors.toList());

		for (Transaction transaction : expenseTransactions) {
			HBox hbox=new HBox();
			hbox.setSpacing(4);
			ImageView iv=new ImageView();
			Image image = new Image(getClass().getResourceAsStream("../image/4.png"));
			iv.setImage(image);
			iv.setPreserveRatio(false);
			iv.setFitWidth(30);
			iv.setFitHeight(30);
			hbox.getChildren().add(iv);
			VBox vb=new VBox();
			vb.setSpacing(2);

			// First line: Primary category and amount
			String amountStr = String.format("%.2f", transaction.getAmount());
			Label categoryAndAmount = new Label(String.format("%-14s -%s", 
				transaction.getPrimaryCategory(), amountStr));
			vb.getChildren().add(categoryAndAmount);

			// Second line: Secondary category and date
			Label detailsAndDate = new Label(String.format("%-14s %s", 
				transaction.getSecondaryCategory(),
				transaction.getTransactionTime().format(dateFormatter)));
			vb.getChildren().add(detailsAndDate);

			hbox.getChildren().add(vb);
			vbox.getChildren().add(hbox);
			Separator sep=new Separator();
			vbox.getChildren().add(sep);
		}
		
		this.setContent(vbox);
	}
}
