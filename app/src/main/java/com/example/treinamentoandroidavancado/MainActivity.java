package com.example.treinamentoandroidavancado;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtNum1, edtNum2;
    TextView txtResultado;
    Button btnCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNum1 = (EditText) findViewById(R.id.editTextNum1);
        edtNum2 = (EditText) findViewById(R.id.editTextNum2);
        btnCalcular = (Button) findViewById(R.id.btnCalc);
        btnCalcular.setOnClickListener(this);
        txtResultado = (TextView) findViewById(R.id.textViewResultado);

    }

    @Override
    public void onClick(View v) {
        int num1 = Integer.parseInt(edtNum1.getText().toString());
        int num2 = Integer.parseInt(edtNum2.getText().toString());

        Calculadora calc = new Calculadora(num1, num2);

        txtResultado.setText(Integer.toString(calc.somar()));
    }
}
