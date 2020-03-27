package com.example.treinamentoandroidavancado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tarefa;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarefa = (TextView) findViewById(R.id.textView);
        tarefa.setMovementMethod(new ScrollingMovementMethod());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else return false;
    }

    protected void atualizarView(String msg){
        tarefa.append(msg + "\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.iniciarTarefa){
            if (isOnline()){
                MyTask myTask = new MyTask();
                buscarDados(myTask, "http://10.0.2.2:3000/api/produtos/json");
            }
            else {
                Toast.makeText(this, "rede não conectada", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscarDados(MyTask task, String mUrl) {
        task.execute(mUrl);
    }

    private class MyTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            atualizarView("tarefa inciada");
        }

        @Override
        protected String doInBackground(String... strings) {

            String conteudo = HttpManager.getDados(strings[0]);
            return conteudo;

        }

        @Override
        protected void onPostExecute(String s) {
            atualizarView(s);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            atualizarView(values[0]);
        }
    }

}
