package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private final StringBuilder storage = new StringBuilder();
    private TextView text;

    private Button delete_all_content;
    private Button delete_last_number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView2);
        delete_all_content = findViewById(R.id.delete);
        delete_last_number = findViewById(R.id.edit);

        int[] button_numbers = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
        };

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View.OnClickListener our_number = v ->
        {
            Button clicked_button = (Button) v;
            String buttonText = clicked_button.getText().toString();

            if (buttonText.equals("Delete"))
            {
                storage.setLength(0);
            }
            else if (buttonText.equals("Edit"))
            {
                if (storage.length() > 0)
                {
                    storage.deleteCharAt(storage.length() - 1);
                }
            }
            else
            {
                storage.append(buttonText);
            }

            text.setText(storage.toString());
        };


        for(int id : button_numbers)
        {
            findViewById(id).setOnClickListener(our_number);
        }

        delete_all_content.setOnClickListener(our_number);
        delete_last_number.setOnClickListener(our_number);
    }
}