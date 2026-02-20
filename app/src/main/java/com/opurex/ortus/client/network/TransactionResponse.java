package com.opurex.ortus.client.network;

public class TransactionResponse {

    private String status;
    private String message;

    public TransactionResponse(String status) {
        this.status = status;
    }

    public TransactionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
