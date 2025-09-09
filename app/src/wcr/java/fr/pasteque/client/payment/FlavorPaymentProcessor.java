package com.opurex.ortus.client.payment;

import com.opurex.ortus.client.models.Payment;
import com.opurex.ortus.client.activities.TrackedActivity;

public abstract class FlavorPaymentProcessor extends PaymentProcessor {

    protected FlavorPaymentProcessor(TrackedActivity parentActivity, PaymentListener listener, Payment payment) {
        super(parentActivity, listener, payment);
    }

}
