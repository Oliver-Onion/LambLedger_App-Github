package model;

import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime transactionTime; // 交易时间
    private String transactionType; // 交易类型/分类
    private String counterparty; // 交易对方
    private String counterpartyAccount; // 对方账号
    private String commodityDescription; // 商品/商品说明
    private String transactionDirection; // 收/支
    private Double amount; // 金额
    private String paymentMethod; // 支付方式/收/付款方式
    private String transactionStatus; // 交易状态/当前状态
    private String transactionOrderNumber; // 交易订单号
    private String merchantOrderNumber; // 商家订单号
    private String notes; // 备注
    private String primaryCategory;
    private String secondaryCategory;

    // Getters and Setters
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getCounterpartyAccount() {
        return counterpartyAccount;
    }

    public void setCounterpartyAccount(String counterpartyAccount) {
        this.counterpartyAccount = counterpartyAccount;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public String getTransactionDirection() {
        return transactionDirection;
    }

    public void setTransactionDirection(String transactionDirection) {
        this.transactionDirection = transactionDirection;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionOrderNumber() {
        return transactionOrderNumber;
    }

    public void setTransactionOrderNumber(String transactionOrderNumber) {
        this.transactionOrderNumber = transactionOrderNumber;
    }

    public String getMerchantOrderNumber() {
        return merchantOrderNumber;
    }

    public void setMerchantOrderNumber(String merchantOrderNumber) {
        this.merchantOrderNumber = merchantOrderNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getSecondaryCategory() {return secondaryCategory;}

    public void setSecondaryCategory(String secondaryCategory) {this.secondaryCategory = secondaryCategory;}

}