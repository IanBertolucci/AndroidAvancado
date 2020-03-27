package com.example.treinamentoandroidavancado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
            MyTask task = new MyTask();
            task.execute("task 1", "task 2", "task 3", "task 4");
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            atualizarView("tarefa inciada");
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < strings.length; i++){
                try {
                    publishProgress("Trabalhando com: " + strings[i]);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Tarefa concluída.";
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
