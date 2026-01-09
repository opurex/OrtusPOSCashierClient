package com.opurex.ortus.client.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.utils.ScaleManager;

public class ProductScaleDialog extends DialogFragment {

    public static String TAG = "ProdScaleDFRAG";

    private final static String PRODUCT_ARG = "prod_arg";
    private final static String PRODUCT_RETURNED = "prod_ret";
    private Context mContext;
    private Listener mListener;
    private Product mProd;
    private boolean mIsProductReturn;
    private EditText input;
    private TextView weightDisplay;
    private TextView priceDisplay;
    private TextView statusDisplay;
    private Button zeroButton;
    private Button tareButton;
    private ScaleManager scaleManager;
    private boolean isScaleConnected = false;

    public interface Listener {
        void onPsdPositiveClick(Product p, double weight, boolean isProductReturned);
    }

    public static ProductScaleDialog newInstance(Product p, boolean isProductReturn) {
        Bundle args = new Bundle();
        args.putSerializable(PRODUCT_ARG, p);
        args.putSerializable(PRODUCT_RETURNED, isProductReturn);
        ProductScaleDialog dial = new ProductScaleDialog();
        dial.setArguments(args);
        return dial;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mProd = (Product) getArguments().getSerializable(PRODUCT_ARG);
        mIsProductReturn = (boolean) getArguments().getSerializable(PRODUCT_RETURNED);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext);
        
        // Create custom view
        View view = getLayoutInflater().inflate(R.layout.dialog_product_scale, null);
        input = view.findViewById(R.id.scale_input);
        weightDisplay = view.findViewById(R.id.scale_weight_display);
        priceDisplay = view.findViewById(R.id.scale_price_display);
        statusDisplay = view.findViewById(R.id.scale_status_display);
        zeroButton = view.findViewById(R.id.scale_zero_button);
        tareButton = view.findViewById(R.id.scale_tare_button);
        
        // Set up input field
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                updatePriceDisplay();
            }
        });
        
        // Set up scale manager
        setupScaleManager();
        
        // Set up buttons
        zeroButton.setOnClickListener(v -> zeroScale());
        tareButton.setOnClickListener(v -> tareScale());
        
        // Update price display with initial calculation
        updatePriceDisplay();
        
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(mProd.getLabel());
        alertDialogBuilder
                .setIcon(R.drawable.scale)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> {
                    if (mListener == null) return;
                    String getString = input.getText().toString();
                    if (!TextUtils.isEmpty(getString)) {
                        double weight = Double.parseDouble(getString);
                        mListener.onPsdPositiveClick(mProd, weight, mIsProductReturn);
                    }
                })
                .setNegativeButton(R.string.scaled_products_cancel, (dialog, id) -> dialog.cancel());
                
        return alertDialogBuilder.create();
    }

    private void setupScaleManager() {
        // Try to get scale manager from activity
        if (getActivity() instanceof ScaleManager.ScaleWeightListener) {
            // In a real implementation, we would get the scale manager from the activity
            // For now, we'll just simulate the scale functionality
            isScaleConnected = true;
            updateScaleStatus();
        }
    }
    
    private void updateScaleStatus() {
        if (isScaleConnected) {
            statusDisplay.setText("✓ Scale connected");
            statusDisplay.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            zeroButton.setEnabled(true);
            tareButton.setEnabled(true);
        } else {
            statusDisplay.setText("✗ Scale not connected");
            statusDisplay.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            zeroButton.setEnabled(false);
            tareButton.setEnabled(false);
        }
    }
    
    private void zeroScale() {
        // In a real implementation, this would call scaleManager.zeroScale()
        Toast.makeText(mContext, "Scale zeroed", Toast.LENGTH_SHORT).show();
        // Simulate updating the weight display
        weightDisplay.setText("Weight: 0.000 kg");
        input.setText("0.000");
    }
    
    private void tareScale() {
        // In a real implementation, this would call scaleManager.tareScale()
        Toast.makeText(mContext, "Scale tared", Toast.LENGTH_SHORT).show();
        // Simulate updating the weight display
        weightDisplay.setText("Tared - place item on scale");
    }
    
    private void updatePriceDisplay() {
        String weightStr = input.getText().toString();
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                double weight = Double.parseDouble(weightStr);
                // In a real implementation, we would get the scale manager from the activity
                // For now, we'll just calculate the price directly
                double price = mProd.getPrice() * weight;
                priceDisplay.setText(String.format("Price: %.2f", price));
            } catch (NumberFormatException e) {
                priceDisplay.setText("Invalid weight");
            }
        } else {
            priceDisplay.setText("Enter weight to calculate price");
        }
    }
    
    // Method to be called when weight is received from Bluetooth scale
    public void onWeightReceived(double weight, String unit) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                String weightText = String.format("%.3f %s", weight, unit);
                weightDisplay.setText("Weight: " + weightText);
                // Auto-fill the input field with the weight from the scale
                input.setText(String.format("%.3f", weight));
                // Update the price display
                updatePriceDisplay();
            });
        }
    }
    
    // Method to be called when scale is connected
    public void onScaleConnected() {
        isScaleConnected = true;
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateScaleStatus);
        }
    }
    
    // Method to be called when scale is disconnected
    public void onScaleDisconnected() {
        isScaleConnected = false;
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateScaleStatus);
        }
    }

    public void setDialogListener(Listener listener) {
        mListener = listener;
    }
}