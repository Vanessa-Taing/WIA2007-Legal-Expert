package com.example.peacepulseco;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        //Defining variables
        final int black = getResources().getColor(R.color.black);
        TextInputLayout textInputLayout = findViewById(R.id.layoutType);
        AutoCompleteTextView selectedTag = findViewById(R.id.actvType);
        EditText editTextRequest = findViewById(R.id.etRequestName);
        EditText editTextDescription = findViewById(R.id.etDescription);

        //This method is responsible for emptying the edit text when clicked for the first time
        //while changing the font color to black
        editTextRequest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String defaultText = "Provide simple request name";
                    String currentText = editTextRequest.getText().toString().trim();
                    if (currentText.equals(defaultText)) {
                        editTextRequest.setText("");
                        editTextRequest.setTextColor(black);
                    }
                }
            }
        });

        //This method is responsible for emptying the edit text when clicked for the first time
        //while changing the font color to black
        editTextDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String defaultText = "Explain your situation here";
                    String currentText = editTextDescription.getText().toString().trim();
                    if (currentText.equals(defaultText)) {
                        editTextDescription.setText("");
                        editTextDescription.setTextColor(black);
                    }
                }
            }
        });

        //This method is responsible for getting the dropdown menu for tags working
        String[] Tags = getResources().getStringArray(R.array.tags);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.tags_item, Tags);
        selectedTag.setAdapter(adapter);

        selectedTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = adapter.getItem(position);
            }
        });

    }
}