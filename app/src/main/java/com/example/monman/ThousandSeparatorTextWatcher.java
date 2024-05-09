package com.example.monman;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ThousandSeparatorTextWatcher implements TextWatcher {

    private final EditText editText;

    public ThousandSeparatorTextWatcher(EditText editText) {
        this.editText = editText;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText.removeTextChangedListener(this);

        String originalText = editable.toString();
        if (originalText.isEmpty()) {
            editText.addTextChangedListener(this);
            return;
        }

        // Remove commas from the original text
        String plainText = originalText.replace(",", "");

        try {
            // Format the number with a thousand separator
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            String formattedText = formatter.format(Long.parseLong(plainText)); // Parse as Long

            // Set the formatted text to the EditText
            editText.setText(formattedText);
            editText.setSelection(formattedText.length());
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            e.printStackTrace();
            // Keep the original text unchanged if parsing fails
            editText.setText(originalText);
            editText.setSelection(originalText.length());
            // Notify the user about the invalid format
        }

        editText.addTextChangedListener(this);
    }
}
