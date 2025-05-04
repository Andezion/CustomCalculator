package com.example.lab1;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import java.util.Stack;
import java.util.Set;

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

        int current = getResources().getConfiguration().orientation;
        if (current == Configuration.ORIENTATION_LANDSCAPE)
        {
            int[] functionButtonsID = {
                    R.id.button_sin, R.id.button_cos, R.id.button_tan, R.id.button_ctg,
                    R.id.button_log, R.id.button_ln, R.id.button_pi, R.id.button_e,
                    R.id.button_fact, R.id.button_sqrt, R.id.button_begin, R.id.button_end
            };

            for (int id : functionButtonsID)
            {
                Button button = findViewById(id);
                button.setOnClickListener(v ->
                {
                    Button clickedButton = (Button) v;
                    inputText.append(clickedButton.getText().toString());
                    update();
                });
            }
        }

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
            if (inputText.length() == 0)
            {
                return;
            }

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
            if (inputText.length() == 0)
                return;

            String[] functions = {"sin", "cos", "tan", "ctg", "log", "ln"};
            String[] constants = {"P", "e"};
            String[] special = {"⎷"}; // символ корня

            for (String func : functions)
            {
                if (inputText.length() >= func.length() &&
                        inputText.substring(inputText.length() - func.length()).equals(func))
                {
                    inputText.delete(inputText.length() - func.length(), inputText.length());
                    update();
                    return;
                }
            }

            for (String c : constants)
            {
                if (inputText.length() >= c.length() &&
                        inputText.substring(inputText.length() - c.length()).equals(c))
                {
                    inputText.delete(inputText.length() - c.length(), inputText.length());
                    update();
                    return;
                }
            }

            for (String s : special)
            {
                if (inputText.length() >= s.length() &&
                        inputText.substring(inputText.length() - s.length()).equals(s))
                {
                    inputText.delete(inputText.length() - s.length(), inputText.length());
                    update();
                    return;
                }
            }

            // иначе просто удаляем 1 символ
            inputText.deleteCharAt(inputText.length() - 1);
            update();
        });

        Button changer = findViewById(R.id.button_mode);
        changer.setOnClickListener(v ->
        {
            int currentOrientation = getResources().getConfiguration().orientation;

            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            else
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                Toast.makeText(this, "Error in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

//    private List<String> to_string(String expression)
//    {
//        Map<String, Integer> what_do_first = new HashMap<>();
//
//        what_do_first.put("+", 1);
//        what_do_first.put("-", 1);
//
//        what_do_first.put("*", 2);
//        what_do_first.put("÷", 2);
//
//        what_do_first.put("%", 3);
//
//        what_do_first.put("!", 4);
//
//        what_do_first.put("sin", 5);
//        what_do_first.put("cos", 5);
//        what_do_first.put("tan", 5);
//        what_do_first.put("ctg", 5);
//        what_do_first.put("⎷", 5);
//        what_do_first.put("log", 5);
//        what_do_first.put("ln", 5);
//
//        List<String> output = new ArrayList<>();
//        Deque<String> operators = new ArrayDeque<>();
//        StringBuilder token = new StringBuilder();
//
//        int is_brackets_okay = 0;
//
//        int i = 0;
//        while (i < expression.length())
//        {
//            char c = expression.charAt(i);
//
//            if (c == '-' && (i == 0 || expression.charAt(i - 1) == '(' || "+-*/÷".indexOf(expression.charAt(i - 1)) != -1))
//            {
//                token.setLength(0);
//                token.append('-');
//                i++;
//
//                while (i < expression.length() &&
//                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
//                {
//                    token.append(expression.charAt(i));
//                    i++;
//                }
//
//                output.add(token.toString());
//                continue;
//            }
//
//            // тут у нас числа обрабатываются
//            if (Character.isDigit(c) || c == '.')
//            {
//                token.setLength(0);
//                while (i < expression.length() &&
//                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
//                {
//                    token.append(expression.charAt(i));
//                    i++;
//                }
//                output.add(token.toString());
//                continue;
//            }
//
//            // тут функции и константы
//            if (Character.isLetter(c))
//            {
//                token.setLength(0);
//                while (i < expression.length() && Character.isLetter(expression.charAt(i)))
//                {
//                    token.append(expression.charAt(i));
//                    i++;
//                }
//
//                String func = token.toString();
//                if (what_do_first.containsKey(func))
//                {
//                    operators.push(func);
//                }
//                else if (func.equals("e"))
//                {
//                    output.add(String.valueOf(Math.E));
//                }
//                else if (func.equals("P"))
//                {
//                    output.add(String.valueOf(Math.PI));
//                }
//                else
//                {
//                    throw new IllegalArgumentException("not function and not const: " + func);
//                }
//
//                continue;
//            }
//
//            if (c == 'P')
//            {
//                output.add(String.valueOf(Math.PI));
//                i++;
//                continue;
//            }
//            if (c == 'e')
//            {
//                output.add(String.valueOf(Math.E));
//                i++;
//                continue;
//            }
//
//            if (c == '(')
//            {
//                is_brackets_okay++;
//                operators.push("(");
//                i++;
//                continue;
//            }
//            if (c == ')')
//            {
//                is_brackets_okay--;
//                if (is_brackets_okay < 0)
//                {
//                    throw new IllegalArgumentException("brackets is wrong!!!");
//                }
//
//                while (!operators.isEmpty() && !operators.peek().equals("("))
//                {
//                    output.add(operators.pop());
//                }
//                if (!operators.isEmpty() && operators.peek().equals("("))
//                {
//                    operators.pop();
//                }
//                i++;
//                continue;
//            }
//
//            if (c == '!')
//            {
//                output.add("!");
//                i++;
//                continue;
//            }
//
//            String op = String.valueOf(c);
//            if (what_do_first.containsKey(op))
//            {
//                while (!operators.isEmpty() &&
//                        what_do_first.getOrDefault(operators.peek(), 0) >= what_do_first.get(op))
//                {
//                    output.add(operators.pop());
//                }
//                operators.push(op);
//                i++;
//                continue;
//            }
//
//            throw new IllegalArgumentException("u put smth new: " + c);
//        }
//
//        while (!operators.isEmpty())
//        {
//            output.add(operators.pop());
//        }
//
//        return output;
//    }

    private List<String> to_string(String expression)
    {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("÷", 2);
        precedence.put("%", 2);
        precedence.put("!", 3);
        precedence.put("sin", 4);
        precedence.put("cos", 4);
        precedence.put("tan", 4);
        precedence.put("ctg", 4);
        precedence.put("⎷", 4);
        precedence.put("log", 4);
        precedence.put("ln", 4);
        precedence.put("u-", 5); // унарный минус если мы хотим -sin сделать

        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();

        StringBuilder token = new StringBuilder();
        int bracketsBalance = 0;
        boolean expectUnary = true; // после ( или начала — ожидаем унарный минус

        int i = 0;
        while (i < expression.length())
        {
            char c = expression.charAt(i);

            // Пропуск пробелов
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Унарный минус
            if (c == '-' && expectUnary)
            {
                operators.push("u-");
                i++;
                continue;
            }

            // Число
            if (Character.isDigit(c) || c == '.')
            {
                token.setLength(0);
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
                {
                    token.append(expression.charAt(i));
                    i++;
                }
                output.add(token.toString());
                expectUnary = false;
                continue;
            }

            // Буквы (функции и константы)
            if (Character.isLetter(c))
            {
                token.setLength(0);
                while (i < expression.length() && Character.isLetter(expression.charAt(i)))
                {
                    token.append(expression.charAt(i));
                    i++;
                }

                String word = token.toString();
                if (word.equals("e"))
                {
                    output.add(String.valueOf(Math.E));
                    expectUnary = false;
                }
                else if (word.equals("P"))
                {
                    output.add(String.valueOf(Math.PI));
                    expectUnary = false;
                }
                else if (precedence.containsKey(word))
                {
                    operators.push(word);
                    expectUnary = true;
                }
                else
                {
                    throw new IllegalArgumentException("Incorrect word: " + word);
                }
                continue;
            }

            // Скобки
            if (c == '(')
            {
                bracketsBalance++;
                if (!output.isEmpty())
                {
                    // Неявное умножение, например: 2(3+4) или π(2)
                    String last = output.get(output.size() - 1);
                    if (last.matches("\\d+(\\.\\d+)?") || last.equals(String.valueOf(Math.PI)) || last.equals(String.valueOf(Math.E)))
                    {
                        while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get("*"))
                        {
                            output.add(operators.pop());
                        }
                        operators.push("*");
                    }
                }

                operators.push("(");
                i++;
                expectUnary = true;
                continue;
            }
            if (c == ')')
            {
                bracketsBalance--;
                if (bracketsBalance < 0)
                {
                    throw new IllegalArgumentException("Incorrect brackets");
                }

                while (!operators.isEmpty() && !operators.peek().equals("("))
                {
                    output.add(operators.pop());
                }

                if (operators.isEmpty())
                {
                    throw new IllegalArgumentException("Too much brackets");
                }

                operators.pop(); // убираем '('
                i++;
                expectUnary = false;
                continue;
            }

            // Факториал
            if (c == '!')
            {
                output.add("!");
                i++;
                expectUnary = false;
                continue;
            }

            // Операторы
            String op = String.valueOf(c);
            if (precedence.containsKey(op))
            {
                if (expectUnary)
                {
                    throw new IllegalArgumentException("You entered two operators");
                }

                while (!operators.isEmpty() &&
                        precedence.getOrDefault(operators.peek(), 0) >= precedence.get(op))
                {
                    output.add(operators.pop());
                }
                operators.push(op);
                i++;
                expectUnary = true;
                continue;
            }

            throw new IllegalArgumentException("Incorrect char: " + c);
        }

        if (bracketsBalance != 0)
        {
            throw new IllegalArgumentException("Brackets error");
        }

        while (!operators.isEmpty())
        {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")"))
            {
                throw new IllegalArgumentException("Dude..brackets");
            }
            output.add(op);
        }

        return output;
    }

    public double do_math(String expr) {
        expr = expr.replace(" ", "")
                .replace("π", "P")
                .replace("e", "E");

        Stack<String> output = new Stack<>();
        Stack<String> operators = new Stack<>();

        Map<String, Integer> precedence = Map.of(
                "+", 1,
                "-", 1,
                "*", 2,
                "/", 2,
                "!", 4
        );

        Set<String> functions = Set.of("sin", "cos", "tan", "ctg", "log", "ln", "⎷");
        int i = 0;
        boolean expectUnary = true;

        while (i < expr.length()) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    number.append(expr.charAt(i));
                    i++;
                }
                output.add(number.toString());
                expectUnary = false;
                continue;
            }

            if (c == 'P') {
                output.add(String.valueOf(Math.PI));
                i++;
                expectUnary = false;
                continue;
            }

            if (c == 'E') {
                output.add(String.valueOf(Math.E));
                i++;
                expectUnary = false;
                continue;
            }

            if (Character.isLetter(c)) {
                StringBuilder func = new StringBuilder();
                while (i < expr.length() && Character.isLetter(expr.charAt(i))) {
                    func.append(expr.charAt(i));
                    i++;
                }
                if (!functions.contains(func.toString())) {
                    throw new IllegalArgumentException("Unknown function: " + func);
                }
                operators.push(func.toString());
                expectUnary = true;
                continue;
            }

            if (c == '(') {
                operators.push("(");
                i++;
                expectUnary = true;
                continue;
            }

            if (c == ')') {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop(); // remove "("
                if (!operators.isEmpty() && functions.contains(operators.peek())) {
                    output.add(operators.pop());
                }
                i++;
                expectUnary = false;
                continue;
            }

            if (c == '-' && expectUnary) {
                // Унарный минус = (0 - x)
                output.add("0");
            }

            String op = String.valueOf(c);
            if (precedence.containsKey(op)) {
                if (expectUnary && !op.equals("-") && !op.equals("+")) {
                    throw new IllegalArgumentException("Unexpected operator: " + op);
                }

                while (!operators.isEmpty() &&
                        precedence.getOrDefault(operators.peek(), 0) >= precedence.get(op)) {
                    output.add(operators.pop());
                }
                operators.push(op);
                i++;
                expectUnary = true;
                continue;
            }

            throw new IllegalArgumentException("Invalid character: " + c);
        }

        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(op);
        }

        // Evaluate RPN
        Stack<Double> stack = new Stack<>();
        for (String token : output) {
            if (functions.contains(token)) {
                if (stack.isEmpty()) throw new IllegalArgumentException("Too few arguments for function: " + token);
                double val = stack.pop();
                switch (token) {
                    case "sin": stack.push(Math.sin(Math.toRadians(val))); break;
                    case "cos": stack.push(Math.cos(Math.toRadians(val))); break;
                    case "tan": stack.push(Math.tan(Math.toRadians(val))); break;
                    case "ctg": stack.push(1.0 / Math.tan(Math.toRadians(val))); break;
                    case "log": stack.push(Math.log10(val)); break;
                    case "ln": stack.push(Math.log(val)); break;
                    case "⎷":
                        if (val < 0) throw new IllegalArgumentException("sqrt of negative number");
                        stack.push(Math.sqrt(val));
                        break;
                }
            } else if (precedence.containsKey(token)) {
                if (token.equals("!")) {
                    if (stack.isEmpty()) throw new IllegalArgumentException("Too few numbers for '!'");
                    double val = stack.pop();
                    if (val < 0 || val != Math.floor(val)) {
                        throw new IllegalArgumentException("Factorial only defined for non-negative integers");
                    }
                    double fact = 1;
                    for (int j = 2; j <= (int) val; j++) fact *= j;
                    stack.push(fact);
                } else {
                    if (stack.size() < 2) throw new IllegalArgumentException("Too few numbers for '" + token + "'");
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token) {
                        case "+": stack.push(a + b); break;
                        case "-": stack.push(a - b); break;
                        case "*": stack.push(a * b); break;
                        case "/": stack.push(a / b); break;
                    }
                }
            } else {
                stack.push(Double.parseDouble(token));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression. Stack: " + stack);
        }

        return stack.pop();
    }


//    private double do_math(List<String> postfix)
//    {
//        Deque<Double> stack = new ArrayDeque<>();
//
//        for (String token : postfix)
//        {
//            if (token.matches("-?\\d+(\\.\\d+)?"))
//            {
//                stack.push(Double.parseDouble(token));
//            }
//            else if (token.equals("sin") || token.equals("cos") || token.equals("tan") ||
//                    token.equals("ctg") || token.equals("⎷") || token.equals("log") ||
//                    token.equals("ln") || token.equals("!") )
//            {
//                if (stack.isEmpty())
//                {
//                    throw new IllegalArgumentException("u entered not enough numbers: " + token);
//                }
//
//                double a = stack.pop();
//
//                switch (token)
//                {
//                    case "sin":
//                        stack.push(Math.sin(Math.toRadians(a)));
//                        break;
//                    case "cos":
//                        stack.push(Math.cos(Math.toRadians(a)));
//                        break;
//                    case "tan":
//                        stack.push(Math.tan(Math.toRadians(a)));
//                        break;
//                    case "ctg":
//                        stack.push(1.0 / Math.tan(Math.toRadians(a)));
//                        break;
//                    case "⎷":
//                        stack.push(Math.sqrt(a));
//                        break;
//                    case "log":
//                        stack.push(Math.log10(a));
//                        break;
//                    case "ln":
//                        stack.push(Math.log(a));
//                        break;
//                    case "!":
//                        stack.push(fact(a));
//                        break;
//                }
//            }
//            else
//            {
//                if (stack.size() < 2)
//                {
//                    throw new IllegalArgumentException("u entered not enough numbers: " + token);
//                }
//
//                double b = stack.pop(); // lifo
//                double a = stack.pop();
//
//                switch (token)
//                {
//                    case "+":
//                        stack.push(a + b);
//                        break;
//                    case "-":
//                        stack.push(a - b);
//                        break;
//                    case "*":
//                        stack.push(a * b);
//                        break;
//                    case "÷":
//                        if (b == 0)
//                        {
//                            Toast.makeText(this, "division by zero", Toast.LENGTH_SHORT).show();
//                            return 0;
//                        }
//                        stack.push(a / b);
//                        break;
//                    case "%":
//                        if (b == 0)
//                        {
//                            Toast.makeText(this, "% by zero", Toast.LENGTH_SHORT).show();
//                            return 0;
//                        }
//                        stack.push(a % b);
//                        break;
//                }
//            }
//        }
//
//        if (stack.size() != 1)
//        {
//            throw new IllegalStateException("invalid expression");
//        }
//
//        return stack.pop();
//    }


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
//        List<String> postfix = to_string(primer.replaceAll("\\s+", ""));
//        return do_math(postfix);
        return do_math(primer.replaceAll("\\s+", ""));
    }
}
