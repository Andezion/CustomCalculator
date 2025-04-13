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
                R.id.button_to_float, R.id.button_procent
        };

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
        Map<String, Integer> precedence = new HashMap<>();

        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("÷", 2);
        precedence.put("%", 3);
        precedence.put("!", 4);
        precedence.put("sin", 5);
        precedence.put("cos", 5);
        precedence.put("tan", 5);
        precedence.put("ctg", 5);
        precedence.put("sqrt", 5);
        precedence.put("log", 5);
        precedence.put("ln", 5);

        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();
        StringBuilder token = new StringBuilder();

        int i = 0;
        while (i < expression.length())
        {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.')
            {
                token.setLength(0);
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
                {
                    token.append(expression.charAt(i));
                    i++;
                }
                output.add(token.toString());
                continue;
            }

            if (Character.isLetter(c))
            {
                token.setLength(0);
                while (i < expression.length() && Character.isLetter(expression.charAt(i)))
                {
                    token.append(expression.charAt(i));
                    i++;
                }
                output.add(token.toString());
                continue;
            }

            if (c == 'π')
            {
                output.add(String.valueOf(Math.PI));
                i++;
                continue;
            }
            if (c == 'e')
            {
                output.add(String.valueOf(Math.E));
                i++;
                continue;
            }

            if (c == '(')
            {
                operators.push("(");
                i++;
                continue;
            }
            if (c == ')')
            {
                while (!operators.isEmpty() && !operators.peek().equals("("))
                {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty() && operators.peek().equals("("))
                {
                    operators.pop();
                }
                i++;
                continue;
            }

            if (c == '!')
            {
                output.add("!");
                i++;
                continue;
            }

            String op = String.valueOf(c);
            if (precedence.containsKey(op))
            {
                while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get(op))
                {
                    output.add(operators.pop());
                }
                operators.push(op);
                i++;
                continue;
            }

            if (Character.isWhitespace(c))
            {
                i++;
                continue;
            }

            throw new IllegalArgumentException("Unknown character: " + c);
        }

        while (!operators.isEmpty())
        {
            output.add(operators.pop());
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
                    case "sin":
                        stack.push(Math.sin(stack.pop()));
                        break;
                    case "cos":
                        stack.push(Math.cos(stack.pop()));
                        break;
                    case "tan":
                        stack.push(Math.tan(stack.pop()));
                        break;
                    case "ctg":
                        stack.push(1.0 / Math.tan(stack.pop()));
                        break;
                    case "sqrt":
                        stack.push(Math.sqrt(stack.pop()));
                        break;
                    case "log":
                        stack.push(Math.log10(stack.pop()));
                        break;
                    case "ln":
                        stack.push(Math.log(stack.pop()));
                        break;
                    case "𝛑":
                        stack.push(Math.PI);
                        break;
                    case "e":
                        stack.push(Math.E);
                        break;
                    case "!":
                        stack.push(fact(stack.pop()));
                        break;
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

    private double fact(double x)
    {
        if (x < 0 || x != Math.floor(x))
        {
            throw new IllegalArgumentException("> 0 dude...");
        }

        int n = (int) x;

        double result = 1;
        for (int i = 2; i <= n; i++)
        {
            result = result * i;
        }

        return result;
    }


    private double trimmer(String primer)
    {
        List<String> postfix = to_string(primer.replaceAll("\\s+", ""));
        return do_math(postfix);
    }
}
