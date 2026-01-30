package com.opurex.ortus.client.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opurex.ortus.client.R;

/**
 * PaymentMainFragment is the main fragment for handling payment transactions.
 * It contains child fragments for ticket and payment functionalities.
 */
public class PaymentMainFragment extends Fragment {

    public static PaymentMainFragment newInstance(int position) {
        return new PaymentMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_ticket, TicketFragment.newInstance(1))
                    .replace(R.id.container_payment, PaymentFragment.newInstance(2))
                    .commit();
        }

        // Refresh the ticket data to ensure it shows current ticket
        refreshTicketData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ensure ticket data is current when fragment becomes visible
        refreshTicketData();
    }

    private void refreshTicketData() {
        TicketFragment ticketFragment = getTicketFragment();
        if (ticketFragment != null) {
            // Force the ticket fragment to update with current session data
            ticketFragment.updateWithCurrentTicket();
        }
    }

    public TicketFragment getTicketFragment() {
        return (TicketFragment) getChildFragmentManager().findFragmentById(R.id.container_ticket);
    }

    public PaymentFragment getPaymentFragment() {
        return (PaymentFragment) getChildFragmentManager().findFragmentById(R.id.container_payment);
    }
}
