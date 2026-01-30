package com.opurex.ortus.client.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opurex.ortus.client.R;

/**
 * A modern Material Design styled progress dialog to replace the deprecated ProgressDialog
 */
public class MaterialProgressDialog {
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView messageText;
    private Context context;

    public MaterialProgressDialog(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {
        // Inflate custom layout for the progress dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.material_progress_dialog, null);

        progressBar = dialogView.findViewById(R.id.progress_bar);
        messageText = dialogView.findViewById(R.id.progress_message);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setView(dialogView);
        builder.setCancelable(false); // Default to non-cancelable like ProgressDialog

        dialog = builder.create();
        // Remove default dialog background to allow custom styling
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void setMessage(CharSequence message) {
        if (messageText != null) {
            messageText.setText(message);
        }
    }

    public void setCancelable(boolean cancelable) {
        if (dialog != null) {
            dialog.setCancelable(cancelable);
        }
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    /**
     * Creates and shows a simple progress dialog with a message
     */
    public static MaterialProgressDialog show(Context context, CharSequence message, boolean cancelable) {
        MaterialProgressDialog progressDialog = new MaterialProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
        return progressDialog;
    }
}