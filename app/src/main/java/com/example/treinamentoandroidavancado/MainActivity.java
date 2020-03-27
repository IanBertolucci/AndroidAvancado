package com.example.treinamentoandroidavancado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextSenha;
    Button btnLoginEmail;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        btnLoginEmail = (Button) findViewById(R.id.btnLogin);

        btnLoginEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            String userEmail = editTextEmail.getText().toString();
            String userSenha = editTextSenha.getText().toString();

            mFirebaseAuth
                    .signInWithEmailAndPassword(userEmail, userSenha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this,
                                                task.getException()
                                                        .toString(),
                                                Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
