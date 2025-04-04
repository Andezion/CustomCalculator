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
    private final StringBuilder inputText = new StringBuilder();

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
                R.id.button_to_float, R.id.button_procent};

        for(int id : buttonID)
        {
            Button button = findViewById(id);
            button.setOnClickListener(buttonClick);
        }

        Button button_znak = findViewById(R.id.button_znak);
        button_znak.setOnClickListener(v ->
        {
            if (inputText.length() == 0) return;

            int lastOperatorIndex = Math.max(
                    inputText.lastIndexOf("+"),
                    Math.max(inputText.lastIndexOf("-"),
                            Math.max(inputText.lastIndexOf("*"), inputText.lastIndexOf("/"))));

            int startIndex = lastOperatorIndex + 1;

            if (inputText.charAt(startIndex) == '-')
            {
                inputText.deleteCharAt(startIndex);
            }
            else
            {
                inputText.insert(startIndex, "-");
            }

            update();
        });

        Button delete_button = findViewById(R.id.button_delete);
        delete_button.setOnClickListener(v ->
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
                if (result % 1 == 0)
                {
                    inputText.append((int) result);
                }
                else
                {
                    inputText.append(result);
                }

                update();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Bruh, error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update()
    {
        textView.setText(inputText.toString());
        change_text();
    }

    private void change_text()
    {
        float defaultSize = 70f;
        float minSize = 20f;

        int maxLength = 8;
        int length = inputText.length();

        if (length > maxLength)
        {
            float newSize = defaultSize - (length - maxLength) * 5;
            if (newSize < minSize)
            {
                newSize = minSize;
                textView.setSingleLine(false);
            }
            else
            {
                textView.setSingleLine(true);
            }
            textView.setTextSize(newSize);
        }
        else
        {
            textView.setTextSize(defaultSize);
            textView.setSingleLine(true);
        }
    }


    private List<String> to_string(String expression)
    {
        Map<Character, Integer> our_types = new HashMap<>();
        our_types.put('+', 1);
        our_types.put('-', 1);
        our_types.put('*', 2);
        our_types.put('÷', 2);
        our_types.put('%', 3);

        List<String> output = new ArrayList<>();
        Deque<Character> operators = new ArrayDeque<>();
        StringBuilder number_buffer = new StringBuilder();

        boolean lastWasOperator = true;

        for (char c : expression.toCharArray())
        {
            if (Character.isDigit(c) || c == '.')
            {
                number_buffer.append(c);
                lastWasOperator = false;
            }
            else
            {
                if (number_buffer.length() > 0)
                {
                    output.add(number_buffer.toString());
                    number_buffer.setLength(0);
                }

                if (c == '-' && lastWasOperator)
                {
                    number_buffer.append(c);
                }
                else if (our_types.containsKey(c))
                {
                    while (!operators.isEmpty() &&
                            our_types.getOrDefault(operators.peek(), 0)
                                    >= our_types.get(c))
                    {
                        output.add(String.valueOf(operators.pop()));
                    }
                    operators.push(c);
                    lastWasOperator = true;
                }
            }
        }

        if (number_buffer.length() > 0)
        {
            output.add(number_buffer.toString());
        }

        while (!operators.isEmpty())
        {
            output.add(String.valueOf(operators.pop()));
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
                if (stack.size() < 2) throw new IllegalArgumentException("r u dumb");

                double b = stack.pop();
                double a = stack.pop();

                switch (token)
                {
                    case "%":
                        if (b == 0)
                        {
                            Toast.makeText(this, "% by zero", Toast.LENGTH_SHORT).show();
                            return 0;
                        }
                        stack.push(a % b);
                        break;
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "÷":
                        if (b == 0)
                        {
                            Toast.makeText(this, "Division by zero", Toast.LENGTH_SHORT).show();
                            return 0;
                        }
                        stack.push(a / b);
                        break;
                }
            }
        }

        return stack.pop();
    }


    private double trimmer(String primer)
    {
        List<String> postfix = to_string(primer.replaceAll("\\s+", ""));
        return do_math(postfix);
    }
}
