package com.opurex.ortus.client.network;

import com.opurex.ortus.client.models.Receipt;
import com.opurex.ortus.client.models.Ticket;
import com.opurex.ortus.client.models.User;
import com.opurex.ortus.client.models.Payment;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class NetworkTransactionTest {

    @Test
    public void testTicketTransaction() {
        Ticket ticket = new Ticket("test");
        NetworkTransaction transaction = new NetworkTransaction(ticket);

        assertTrue(transaction.isTicket());
        assertFalse(transaction.isReceipt());
        assertEquals(ticket, transaction.getTicket());
        assertNull(transaction.getReceipt());
    }

    @Test
    public void testReceiptTransaction() {
        Ticket ticket = new Ticket("test");
        User user = new User("1", "testuser", "pass", "all");
        Receipt receipt = new Receipt(ticket, new ArrayList<Payment>(), user);
        NetworkTransaction transaction = new NetworkTransaction(receipt);

        assertTrue(transaction.isReceipt());
        assertFalse(transaction.isTicket());
        assertEquals(receipt, transaction.getReceipt());
        assertNull(transaction.getTicket());
    }
}
