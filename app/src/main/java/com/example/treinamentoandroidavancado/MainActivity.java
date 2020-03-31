package com.example.treinamentoandroidavancado;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int LOADER_ID = 1;
    private AgendaAdapter agendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        MenuItem searcItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searcItem.getActionView();
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getBaseContext(), ContatoInserirActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        agendaAdapter = new AgendaAdapter(getApplicationContext(), R.layout.contato_item, new ArrayList<Agenda>());

        ListView listView = (ListView) findViewById(R.id.listViewContatos);
        listView.setAdapter(agendaAdapter);
        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, cursorLoaderCallbacks);
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Bundle param = new Bundle();
            param.putString("filter", newText);

            LoaderManager lm = getLoaderManager();
            lm.restartLoader(LOADER_ID, param, cursorLoaderCallbacks);
            return true;
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (args != null) {
                if (args.containsKey("filter")) {
                    String filter = args.getString("filter");

                    String selection = AgendaDbHelper.C_NOME + " like ? or " + AgendaDbHelper.C_TELEFONE + " like ?";
                    String[] selectionArgs = {"%" + filter + "%", "%" + filter + "%"};
                    return new CursorLoader(getApplicationContext(), AgendaProvider.CONTENT_URI, null, selection, selectionArgs, null);
                }
            }
            return new CursorLoader(getApplicationContext(), AgendaProvider.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Agenda> agendaList = getList(data);
            agendaAdapter.setDados(agendaList);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            agendaAdapter.setDados(new ArrayList<Agenda>());
        }
    };

    private List<Agenda> getList(Cursor cursor) {
        List<Agenda> agendaList = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Agenda contato = new Agenda();
                    contato.setId(cursor.getLong(0));
                    contato.setNome(cursor.getString(1));
                    contato.setTelefone(cursor.getString(2));
                    contato.setImagem(cursor.getBlob(3));

                    agendaList.add(contato);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agendaList;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView telefone = (TextView) view.findViewById(R.id.txtTelefoneItem);
            Uri uriTelefone = Uri.parse("tel:" + telefone.getText().toString());
            Intent intent = new Intent(Intent.ACTION_CALL, uriTelefone);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getBaseContext(), ContatoEditarActivity.class);
            intent.putExtra("id", String.valueOf(id));
            startActivity(intent);
            return true;
        }
    };
}
