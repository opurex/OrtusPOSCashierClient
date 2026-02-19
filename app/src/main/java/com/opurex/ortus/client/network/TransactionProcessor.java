package com.opurex.ortus.client.network;

public interface TransactionProcessor {
    void processTransaction(NetworkTransaction transaction, String clientInfo);
}
