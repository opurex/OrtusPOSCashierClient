package com.opurex.ortus.client.fragments; // Or your appropriate package

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opurex.ortus.client.R;
/**
 * TransactionMainFragment is the main fragment for handling transactions.
 * It contains child fragments for catalog and ticket functionalities.
 */
public class TransactionMainFragment extends Fragment {
    public static TransactionMainFragment newInstance(int position) {
        return new TransactionMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_catalog, CatalogFragment.newInstance(0))
                    .replace(R.id.container_ticket, TicketFragment.newInstance(1))
                    .commit();
        }
    }
    public CatalogFragment getCatalogFragment() {
        return (CatalogFragment) getChildFragmentManager().findFragmentById(R.id.container_catalog);
    }

    public TicketFragment getTicketFragment() {
        return (TicketFragment) getChildFragmentManager().findFragmentById(R.id.container_ticket);
    }

}
