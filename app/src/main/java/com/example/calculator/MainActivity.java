package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvResult;
    private AppCompatButton[] btnNumbers = new AppCompatButton[10];
    AppCompatButton btnAddition, btnSubtraction, btnMultiplication, btnDivision;
    AppCompatButton btnAC, btnDEL, btnDecimal, btnPercent, btnEqual;
    private String currentInput = "";
    private String operator = "";
    private BigDecimal firstNumber;
    private boolean isOperatorPressed = false;
    private boolean firstSignPressed = false;
    private boolean numberAfterSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        for (AppCompatButton button : btnNumbers) {
            button.setOnClickListener(this);
        }
        btnAddition.setOnClickListener(this);
        btnSubtraction.setOnClickListener(this);
        btnMultiplication.setOnClickListener(this);
        btnDivision.setOnClickListener(this);
        btnAC.setOnClickListener(this);
        btnDEL.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
        btnPercent.setOnClickListener(this);
        btnEqual.setOnClickListener(this);
    }

    private void initView(){
        tvResult = findViewById(R.id.tvResult);
        btnNumbers[0] = findViewById(R.id.btnNumber0);
        btnNumbers[1] = findViewById(R.id.btnNumber1);
        btnNumbers[2] = findViewById(R.id.btnNumber2);
        btnNumbers[3] = findViewById(R.id.btnNumber3);
        btnNumbers[4] = findViewById(R.id.btnNumber4);
        btnNumbers[5] = findViewById(R.id.btnNumber5);
        btnNumbers[6] = findViewById(R.id.btnNumber6);
        btnNumbers[7] = findViewById(R.id.btnNumber7);
        btnNumbers[8] = findViewById(R.id.btnNumber8);
        btnNumbers[9] = findViewById(R.id.btnNumber9);

        btnAddition = findViewById(R.id.btnAddition);
        btnSubtraction = findViewById(R.id.btnSubtraction);
        btnMultiplication = findViewById(R.id.btnMultiplication);
        btnDivision = findViewById(R.id.btnDivision);

        btnAC = findViewById(R.id.btnAC);
        btnDEL = findViewById(R.id.btnDEL);
        btnDecimal = findViewById(R.id.btnDecimal);
        btnPercent = findViewById(R.id.btnPercent);
        btnEqual = findViewById(R.id.btnEqual);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnNumber0 || id == R.id.btnNumber1 || id == R.id.btnNumber2 ||
                id == R.id.btnNumber3 || id == R.id.btnNumber4 || id == R.id.btnNumber5 ||
                id == R.id.btnNumber6 || id == R.id.btnNumber7 || id == R.id.btnNumber8 ||
                id == R.id.btnNumber9) {
            AppCompatButton button = (AppCompatButton) v;
            if (!currentInput.isEmpty() && currentInput.length() > 1) {
                char lastChar = currentInput.charAt(currentInput.length() - 1);
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    currentInput = "";
                }
            }
            currentInput += button.getText().toString();
            tvResult.setText(currentInput);
            firstSignPressed = false;
            numberAfterSign = true;

        } else if (id == R.id.btnAddition || id == R.id.btnSubtraction) {
            if (currentInput.isEmpty() && !firstSignPressed) {
                currentInput += ((AppCompatButton) v).getText().toString();
                tvResult.setText(currentInput);
                firstSignPressed = true;
                numberAfterSign = false;
            } else if (!currentInput.isEmpty() && numberAfterSign){
                handleOperator(((AppCompatButton) v).getText().toString());
            }
        } else if (id == R.id.btnMultiplication || id == R.id.btnDivision) {
            if (!currentInput.isEmpty() && numberAfterSign) {
                handleOperator(((AppCompatButton) v).getText().toString());
            }
        } else if (id == R.id.btnEqual) {
            if (!currentInput.isEmpty() && isOperatorPressed) {
                BigDecimal secondNumber = new BigDecimal(currentInput);
                BigDecimal result = BigDecimal.ZERO;
                switch (operator) {
                    case "+":
                        result = firstNumber.add(secondNumber);
                        break;
                    case "-":
                        result = firstNumber.subtract(secondNumber);
                        break;
                    case "*":
                        result = firstNumber.multiply(secondNumber);
                        break;
                    case "/":
                        if (!secondNumber.equals(BigDecimal.ZERO)) {
                            result = firstNumber.divide(secondNumber, 10, BigDecimal.ROUND_HALF_UP);
                        } else {
                            tvResult.setText("Cannot divide by zero");
                            return;
                        }
                        break;
                }
                tvResult.setText(String.valueOf(result.stripTrailingZeros().toPlainString()));
                currentInput = String.valueOf(result.stripTrailingZeros().toPlainString());
                isOperatorPressed = false;
            }
        } else if (id == R.id.btnAC) {
            currentInput = "";
            operator = "";
            firstNumber = BigDecimal.ZERO;
            isOperatorPressed = false;
            tvResult.setText("");
            firstSignPressed = false;
            numberAfterSign = false;
        } else if (id == R.id.btnDEL) {
            if (!currentInput.isEmpty()) {
                char lastChar = currentInput.charAt(currentInput.length() - 1);
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                tvResult.setText(currentInput);
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    isOperatorPressed = false;
                    numberAfterSign = true;
                }
                if (currentInput.isEmpty()) {
                    firstSignPressed = false;
                }
            }
        } else if (id == R.id.btnDecimal && numberAfterSign) {
            if (!currentInput.contains(".")) {
                currentInput += ".";
                tvResult.setText(currentInput);
            }
        } else if (id == R.id.btnPercent) {
            if (!currentInput.isEmpty() && numberAfterSign) {
                BigDecimal value = new BigDecimal(currentInput).divide(new BigDecimal(100));
                tvResult.setText(value.stripTrailingZeros().toPlainString());
                currentInput = value.stripTrailingZeros().toPlainString();
            }
        }
    }
    private void handleOperator(String operatorText) {
        if (!currentInput.isEmpty() && !isOperatorPressed && numberAfterSign) {
            firstNumber = new BigDecimal(currentInput);
            operator = operatorText;
            currentInput += operatorText;
            tvResult.setText(currentInput);
            isOperatorPressed = true;
            numberAfterSign = false;
        }
    }
}