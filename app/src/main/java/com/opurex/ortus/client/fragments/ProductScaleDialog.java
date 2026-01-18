package com.opurex.ortus.client.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.utils.ScaleManager;

public class ProductScaleDialog extends DialogFragment implements ScaleManager.ScaleWeightListener, ScaleManager.ConnectionStateListener {

    public static final String TAG = "ProductScaleDialog";

    private static final String ARG_PRODUCT = "arg_product";
    private static final String ARG_IS_RETURN = "arg_is_return";

    private Listener mListener;
    private Product mProd;
    private boolean mIsProductReturn;

    private EditText weightInput;
    private TextView weightDisplay;
    private TextView priceDisplay;
    private TextView statusDisplay;
    private Button zeroButton;
    private Button tareButton;

    private ScaleManager scaleManager;
    private ScaleManager externalScaleManager; // External ScaleManager passed from caller
    private boolean isScaleConnected = false;

    public interface Listener {
        void onPsdPositiveClick(Product p, double weight, boolean isProductReturned);
    }

    public static ProductScaleDialog newInstance(Product p, boolean isProductReturn) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, p);
        args.putBoolean(ARG_IS_RETURN, isProductReturn);
        ProductScaleDialog dialog = new ProductScaleDialog();
        dialog.setArguments(args);
        return dialog;
    }

    // Overloaded method to accept external ScaleManager
    public static ProductScaleDialog newInstance(Product p, boolean isProductReturn, ScaleManager scaleManager) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, p);
        args.putBoolean(ARG_IS_RETURN, isProductReturn);
        ProductScaleDialog dialog = new ProductScaleDialog();
        dialog.setArguments(args);
        dialog.externalScaleManager = scaleManager; // Store the external ScaleManager
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Set the listener from the parent fragment only if no external listener was set beforehand
        if (mListener == null) { // Only set target fragment as listener if no external listener was set via setDialogListener
            if (getTargetFragment() instanceof Listener) {
                mListener = (Listener) getTargetFragment();
            } else if (getActivity() instanceof Listener) {
                // Check if the calling activity implements the listener interface
                mListener = (Listener) getActivity();
            } else {
                throw new ClassCastException("Calling fragment or activity must implement ProductScaleDialog.Listener");
            }
        }
    }

    // Method to set an external listener (used when called from Transaction)
    public void setDialogListener(Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProd = (Product) getArguments().getSerializable(ARG_PRODUCT);
            mIsProductReturn = getArguments().getBoolean(ARG_IS_RETURN);
        }
        // Use external ScaleManager if provided, otherwise create a new one
        if (externalScaleManager != null) {
            scaleManager = externalScaleManager;
        } else {
            // The dialog is responsible for creating its own ScaleManager
            scaleManager = new ScaleManager(requireContext());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_product_scale, null);
        weightInput = view.findViewById(R.id.scale_input);
        weightDisplay = view.findViewById(R.id.scale_weight_display);
        priceDisplay = view.findViewById(R.id.scale_price_display);
        statusDisplay = view.findViewById(R.id.scale_status_display);
        zeroButton = view.findViewById(R.id.scale_zero_button);
        tareButton = view.findViewById(R.id.scale_tare_button);
        
        weightInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                updatePriceDisplayFromInput();
            }
        });
        
        zeroButton.setOnClickListener(v -> zeroScale());
        tareButton.setOnClickListener(v -> tareScale());
        
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(mProd.getLabel());
        alertDialogBuilder
                .setIcon(R.drawable.scale)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    String weightString = weightInput.getText().toString();
                    if (mListener != null && !TextUtils.isEmpty(weightString)) {
                        double weight = Double.parseDouble(weightString);
                        if (weight > 0) {
                            mListener.onPsdPositiveClick(mProd, weight, mIsProductReturn);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
                
        return alertDialogBuilder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        scaleManager.setScaleWeightListener(this);
        scaleManager.setConnectionStateListener(this);
        isScaleConnected = scaleManager.isConnected();
        updateScaleStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        scaleManager.setScaleWeightListener(null);
        scaleManager.setConnectionStateListener(null);
    }
    
    private void updateScaleStatus() {
        if (isScaleConnected) {
            statusDisplay.setText("Scale connected");
            statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            zeroButton.setEnabled(true);
            tareButton.setEnabled(true);
        } else {
            // Check if manual weight entry is enabled in settings
            boolean manualEntryEnabled = isManualWeightEntryEnabled();
            statusDisplay.setText("Scale not connected" + (manualEntryEnabled ? " - enter weight manually" : ""));

            // Use different colors based on manual entry availability
            if (manualEntryEnabled) {
                // Orange color when scale is not connected but manual entry is available
                statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                // Red color when scale is not connected and manual entry is not available
                statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            // Enable buttons if manual entry is enabled, otherwise disable them
            zeroButton.setEnabled(manualEntryEnabled);
            tareButton.setEnabled(manualEntryEnabled);
        }
    }

    /**
     * Check if manual weight entry is enabled in settings
     */
    private boolean isManualWeightEntryEnabled() {
        if (getContext() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            return prefs.getBoolean("enable_manual_weight_entry", true); // Default to true as requested
        }
        return true; // Default to true if context is not available
    }
    
    private void zeroScale() {
        if (scaleManager != null && isScaleConnected) {
            // Connected mode: use actual scale
            scaleManager.zeroScale();
            Toast.makeText(getContext(), "Scale zeroed", Toast.LENGTH_SHORT).show();
        } else {
            // Manual mode: clear the manual input field
            if (isManualWeightEntryEnabled()) {
                weightInput.setText("0.0");
                Toast.makeText(getContext(), "Manual input cleared", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scale not connected and manual entry disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tareScale() {
        if (scaleManager != null && isScaleConnected) {
            // Connected mode: use actual scale
            scaleManager.tareScale();
            Toast.makeText(getContext(), "Scale tared", Toast.LENGTH_SHORT).show();
        } else {
            // Manual mode: clear the manual input field (simulate tare)
            if (isManualWeightEntryEnabled()) {
                weightInput.setText("0.0");
                Toast.makeText(getContext(), "Manual input cleared (tare)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scale not connected and manual entry disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void updatePriceDisplayFromInput() {
        String weightStr = weightInput.getText().toString();
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                double weight = Double.parseDouble(weightStr);
                double price = mProd.getPrice() * weight;
                priceDisplay.setText(String.format("Price: %.2f", price));
            } catch (NumberFormatException e) {
                priceDisplay.setText("Invalid weight");
            }
        } else {
            priceDisplay.setText("Enter weight to calculate price");
        }
    }
    
    // --- Callbacks from ScaleManager ---
    
    @Override
    public void onWeightReceived(double weight, String unit) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            String weightText = String.format("%.3f %s", weight, unit);
            weightDisplay.setText("Weight: " + weightText);
            // Auto-fill the input field with the weight from the scale
            weightInput.setText(String.format("%.3f", weight));
        });
    }
    
    @Override
    public void onScaleConnected() {
        isScaleConnected = true;
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateScaleStatus);
        }
    }
    
    @Override
    public void onScaleDisconnected() {
        isScaleConnected = false;
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateScaleStatus);
        }
    }

    @Override
    public void onScaleError(String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Scale Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
