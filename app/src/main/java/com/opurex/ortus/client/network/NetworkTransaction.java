package com.opurex.ortus.client.network;

import com.opurex.ortus.client.models.Receipt;
import com.opurex.ortus.client.models.Ticket;

public class NetworkTransaction {

    private Ticket ticket;
    private Receipt receipt;

    public NetworkTransaction(Ticket ticket) {
        this.ticket = ticket;
    }

    public NetworkTransaction(Receipt receipt) {
        this.receipt = receipt;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public boolean isReceipt() {
        return receipt != null;
    }

    public boolean isTicket() {
        return ticket != null;
    }
}
