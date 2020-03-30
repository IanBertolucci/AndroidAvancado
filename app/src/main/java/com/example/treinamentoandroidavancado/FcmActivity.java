package com.example.treinamentoandroidavancado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FcmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        if (getIntent().getExtras() != null){

            String nome = getIntent().getExtras().get("promocao_nome").toString();
            String valor = getIntent().getExtras().get("promocao_valor").toString();
            String validade = getIntent().getExtras().get("promocao_validade").toString();

            TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Aproveite a promoção " + nome + " com "+ valor + " de desconto, válida por " + validade + ".");
        }
    }
}
