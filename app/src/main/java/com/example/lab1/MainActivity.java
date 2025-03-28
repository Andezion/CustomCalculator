package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private StringBuilder inputText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);

        View.OnClickListener buttonClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button) v;
                inputText.append(button.getText().toString());
                updateTextView();
            }
        };

        int[] buttonID = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
                R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_0};

        for(int id : buttonID)
        {
            Button button = findViewById(id);
            button.setOnClickListener(buttonClick);
        }

        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(inputText.length() > 0)
                {
                    inputText.deleteCharAt(inputText.length() - 1);
                    updateTextView();
                }
            }
        });
    }

    private void updateTextView()
    {
        textView.setText(inputText.toString());
    }
}
