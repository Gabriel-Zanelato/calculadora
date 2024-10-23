package com.example.calculadora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultadoTv, solucaoTv;
    MaterialButton buttonC, buttonParentAberto, buttonPorcentagem;
    MaterialButton buttonDividir, buttonVezes, buttonMais, buttonMenos, buttonIgual;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonPonto;

    // Controla se o próximo parêntese deve ser de abertura ou fechamento
    boolean isParentOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        resultadoTv = findViewById(R.id.tv_resultado);
        solucaoTv = findViewById(R.id.tv_solucao);

        // Inicializa os botões
        assignId(buttonDividir, R.id.button_dividir);
        assignId(buttonVezes, R.id.button_vezes);
        assignId(buttonMenos, R.id.button_menos);
        assignId(buttonMais, R.id.button_mais);
        assignId(buttonIgual, R.id.button_igual);
        assignId(buttonPonto, R.id.button_ponto);
        assignId(buttonParentAberto, R.id.button_parent);
        assignId(buttonPorcentagem, R.id.button_porcentagem);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonC, R.id.button_c);
        assignId(buttonAC, R.id.button_ac);

        // Configura padding automático para se ajustar às barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Função que associa os botões e define o click listener
    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solucaoTv.getText().toString();

        // Processa cada botão com base no seu texto
        switch (buttonText) {
            case "=":
                if (dataToCalculate.isEmpty() || dataToCalculate.trim().isEmpty()) {
                    resultadoTv.setText("0");
                } else {
                    String finalResult = getResult(dataToCalculate);
                    if (!finalResult.equals("Erro")) {
                        resultadoTv.setText(finalResult);
                    }
                }
                break;
            case "C":
                if (dataToCalculate.length() > 0) {
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                }
                break;
            case "AC":
                dataToCalculate = "";
                resultadoTv.setText("0");
                break;

            case "%":

                if (!dataToCalculate.isEmpty() && Character.isDigit(dataToCalculate.charAt(dataToCalculate.length() - 1))) {
                    dataToCalculate = "(" + dataToCalculate + ")/100*";
                }
                break;

            case "()":
                if (isParentOpen) {
                    dataToCalculate += "(";
                    isParentOpen = false;
                } else {
                    dataToCalculate += ")";
                    isParentOpen = true;
                }
                break;
            default:
                dataToCalculate += buttonText;
                break;
        }


        solucaoTv.setText(dataToCalculate);
    }

    // Função para calcular a expressão usando JavaScript
    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Erro";
        }
    }
}
