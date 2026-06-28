package com.amitdaskne.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult, tvExpression;
    private String currentNumber = "";
    private String expression = "";
    private double result = 0;
    private String operator = "";
    private boolean isNewNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);

        setupButtons();
    }

    private void setupButtons() {
        int[] numIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                        R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numIds) {
            findViewById(id).setOnClickListener(this::onNumberClick);
        }

        findViewById(R.id.btnDecimal).setOnClickListener(this::onDecimalClick);
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> onOperatorClick("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperatorClick("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperatorClick("÷"));
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercentClick());
        findViewById(R.id.btnEquals).setOnClickListener(v -> onEqualsClick());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClear());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> onBackspace());
    }

    private void onNumberClick(View v) {
        Button btn = (Button) v;
        String num = btn.getText().toString();
        if (isNewNumber) {
            currentNumber = num;
            isNewNumber = false;
        } else {
            if (currentNumber.length() < 15) {
                currentNumber += num;
            }
        }
        updateDisplay();
    }

    private void onDecimalClick(View v) {
        if (isNewNumber) {
            currentNumber = "0.";
            isNewNumber = false;
        } else if (!currentNumber.contains(".")) {
            currentNumber += ".";
        }
        updateDisplay();
    }

    private void onOperatorClick(String op) {
        if (!currentNumber.isEmpty()) {
            calculate();
            operator = op;
            expression = formatNumber(result) + " " + op + " ";
            tvExpression.setText(expression);
            isNewNumber = true;
        }
    }

    private void onPercentClick() {
        if (!currentNumber.isEmpty()) {
            double num = Double.parseDouble(currentNumber);
            num = num / 100;
            currentNumber = String.valueOf(num);
            updateDisplay();
        }
    }

    private void onEqualsClick() {
        calculate();
        operator = "";
        expression = "";
        tvExpression.setText("");
        isNewNumber = true;
    }

    private void calculate() {
        if (currentNumber.isEmpty()) return;
        double current = Double.parseDouble(currentNumber);
        if (operator.isEmpty()) {
            result = current;
        } else {
            switch (operator) {
                case "+": result += current; break;
                case "-": result -= current; break;
                case "×": result *= current; break;
                case "÷": 
                    if (current != 0) result /= current;
                    else {
                        tvResult.setText("Error");
                        return;
                    }
                    break;
            }
        }
        currentNumber = formatNumber(result);
        updateDisplay();
    }

    private void onClear() {
        currentNumber = "";
        expression = "";
        result = 0;
        operator = "";
        isNewNumber = true;
        tvResult.setText("0");
        tvExpression.setText("");
    }

    private void onBackspace() {
        if (!currentNumber.isEmpty() && currentNumber.length() > 1) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
        } else {
            currentNumber = "0";
            isNewNumber = true;
        }
        updateDisplay();
    }

    private void updateDisplay() {
        if (currentNumber.isEmpty()) {
            tvResult.setText("0");
        } else {
            tvResult.setText(currentNumber);
        }
    }

    private String formatNumber(double num) {
        if (num == (long) num) {
            return String.valueOf((long) num);
        }
        String str = String.valueOf(num);
        if (str.length() > 15) {
            str = String.format("%.10f", num);
            if (str.endsWith("0000000000")) {
                str = str.substring(0, str.length() - 11);
            }
        }
        return str;
    }
}
