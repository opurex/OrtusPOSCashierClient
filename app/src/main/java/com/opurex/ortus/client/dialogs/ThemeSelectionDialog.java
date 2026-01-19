package com.opurex.ortus.client.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.ThemeUtils;

/**
 * Dialog fragment for selecting app theme
 */
public class ThemeSelectionDialog extends AppCompatDialogFragment {

    private ThemeSelectionListener listener;
    private RadioGroup themeRadioGroup;
    private RadioButton radioLight, radioDark, radioSystem;

    public interface ThemeSelectionListener {
        void onThemeSelected(int themeMode);
    }

    public static ThemeSelectionDialog newInstance(ThemeSelectionListener listener) {
        ThemeSelectionDialog dialog = new ThemeSelectionDialog();
        dialog.setListener(listener);
        return dialog;
    }

    public void setListener(ThemeSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_theme_selector, null);

        initViews(view);

        builder.setView(view);

        return builder.create();
    }

    private void initViews(View view) {
        themeRadioGroup = view.findViewById(R.id.theme_radio_group);
        radioLight = view.findViewById(R.id.radio_light_theme);
        radioDark = view.findViewById(R.id.radio_dark_theme);
        radioSystem = view.findViewById(R.id.radio_system_theme);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnApply = view.findViewById(R.id.btn_apply);

        // Set current theme selection
        int currentTheme = ThemeUtils.getCurrentThemeMode(getContext());
        switch (currentTheme) {
            case ThemeUtils.THEME_LIGHT:
                radioLight.setChecked(true);
                break;
            case ThemeUtils.THEME_DARK:
                radioDark.setChecked(true);
                break;
            case ThemeUtils.THEME_SYSTEM:
            default:
                radioSystem.setChecked(true);
                break;
        }

        btnCancel.setOnClickListener(v -> dismiss());

        btnApply.setOnClickListener(v -> {
            int selectedTheme = ThemeUtils.THEME_SYSTEM; // Default to system

            if (themeRadioGroup.getCheckedRadioButtonId() == R.id.radio_light_theme) {
                selectedTheme = ThemeUtils.THEME_LIGHT;
            } else if (themeRadioGroup.getCheckedRadioButtonId() == R.id.radio_dark_theme) {
                selectedTheme = ThemeUtils.THEME_DARK;
            }

            if (listener != null) {
                listener.onThemeSelected(selectedTheme);
            }

            dismiss();
        });
    }
}