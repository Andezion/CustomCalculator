package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        View.OnClickListener buttonClick = v ->
        {
            Button button = (Button) v;
            inputText.append(button.getText().toString());
            update();
        };

        int[] buttonID = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
                R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_0,
                R.id.button_delenie, R.id.button_plus, R.id.button_minus, R.id.button_multiply,
                R.id.button_to_float};

        for(int id : buttonID)
        {
            Button button = findViewById(id);
            button.setOnClickListener(buttonClick);
        }

        Button dotButton = findViewById(R.id.button_to_float);
        dotButton.setOnClickListener(v ->
        {
            if (to_float())
            {
                inputText.append(".");
                update();
            }
        });

        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v ->
        {
            if(inputText.length() > 0)
            {
                inputText.deleteCharAt(inputText.length() - 1);
                update();
            }
        });

        Button equalsButton = findViewById(R.id.button_result);
        equalsButton.setOnClickListener(v ->
        {
            try
            {
                double result = trimmer(inputText.toString());

                inputText.setLength(0);
                inputText.append(result);

                update();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Bruh, error", Toast.LENGTH_SHORT).show(); // stack
            }
        });
    }

    private void update()
    {
        textView.setText(inputText.toString());
    }

    private boolean to_float()
    {
        int lastOperatorIndex = Math.max(
                inputText.lastIndexOf("+"),
                Math.max(inputText.lastIndexOf("-"),
                        Math.max(inputText.lastIndexOf("*"), inputText.lastIndexOf("/"))));

        String lastNumber = inputText.substring(lastOperatorIndex + 1);

        return !lastNumber.contains(".");
    }

    private List<String> to_string(String expression)
    {
        Map<Character, Integer> precedence = new HashMap<>();

        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);

        List<String> output = new ArrayList<>();

        Deque<Character> operators = new ArrayDeque<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (char c : expression.toCharArray())
        {
            if (Character.isDigit(c) || c == '.')
            {
                numberBuffer.append(c);
            }
            else
            {
                if (numberBuffer.length() > 0)
                {
                    output.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }
                else if (precedence.containsKey(c))
                {
                    while (!operators.isEmpty() && precedence.get(operators.peek()) >= precedence.get(c))
                    {
                        output.add(operators.pop().toString());
                    }
                    operators.push(c);
                }
            }
        }

        if (numberBuffer.length() > 0)
        {
            output.add(numberBuffer.toString());
        }

        while (!operators.isEmpty())
        {
            output.add(operators.pop().toString());
        }

        return output;
    }

    private double do_math(List<String> postfix)
    {
        Deque<Double> stack = new ArrayDeque<>();

        for (String token : postfix)
        {
            if (token.matches("-?\\d+(\\.\\d+)?"))
            {
                stack.push(Double.parseDouble(token));
            }
            else
            {
                double b = stack.pop();
                double a = stack.pop();

                switch (token)
                {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }

    private double trimmer(String expression)
    {
        List<String> postfix = to_string(expression.replaceAll("\\s+", ""));
        return do_math(postfix);
    }
}
