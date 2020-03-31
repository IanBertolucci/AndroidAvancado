package com.example.treinamentoandroidavancado;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;


public class ContatoEditarActivity extends AppCompatActivity {
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_editar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            LoaderManager lm = getLoaderManager();
            lm.initLoader(LOADER_ID, intent.getExtras(), loaderManager);
        }else {
            finish();
        }

        findViewById(R.id.buttonImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione a foto"), 2);
            }
        });
    }

    public void alterar() {
        EditText txtId = (EditText) findViewById(R.id.editTextId);

        EditText txtNome = (EditText) findViewById(R.id.editTextNome);
        EditText txtTelefone = (EditText) findViewById(R.id.editTextTelefone);
        ImageView foto = (ImageView) findViewById(R.id.imageViewFoto);

        String titulo = txtNome.getText().toString();
        String autor = txtTelefone.getText().toString();
        String id = txtId.getText().toString();

        Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] img = outputStream.toByteArray();

        ContentValues values = new ContentValues();

        values.put(AgendaDbHelper.C_NOME, titulo);
        values.put(AgendaDbHelper.C_TELEFONE, autor);
        values.put(AgendaDbHelper.C_FOTO, img);

        ContentResolver provedor = getContentResolver();

        String selecion = AgendaDbHelper.C_ID+ "=?";
        String[] selectionArgs = { id };

        provedor.update(AgendaProvider.CONTENT_URI, values, selecion, selectionArgs);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void delete(){
        final ContatoEditarActivity _this = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Confirmação!")
                .setMessage("Gostaria de excluir este contato?")
                .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText txtId = (EditText) _this.findViewById(R.id.editTextId);
                        String idContato = txtId.getText().toString();

                        String selection = AgendaDbHelper.C_ID + "=?";
                        String[] selectionArgs = { idContato };

                        getContentResolver().delete(AgendaProvider.CONTENT_URI, selection, selectionArgs);

                        Toast.makeText(getApplicationContext(), "Contato excluído com sucesso!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNeutralButton("não", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_editar_contato, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_alter:
                alterar();
                return true;
            case R.id.action_delete:
                delete();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            Uri imagemSelecionada = data.getData();
            ImageView foto = (ImageView) findViewById(R.id.imageViewFoto);
            foto.setImageURI(imagemSelecionada);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderManager = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id == LOADER_ID) {
                String idContato = "";
                idContato = args.getString("id");
                Uri uri = Uri.withAppendedPath(AgendaProvider.CONTENT_URI, "id/"+idContato);
                return new CursorLoader(getApplicationContext(), uri, null, null, null, null);
            }
            else
                return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            String id = "";
            String nome = "";
            String telefone ="";
            byte[] img = null;

            if(data.moveToNext()){
                id = data.getString(0);
                nome = data.getString(1);
                telefone = data.getString(2);
                img = data.getBlob(3);
            }

            EditText txtId = (EditText) findViewById(R.id.editTextId);
            EditText txtNome = (EditText) findViewById(R.id.editTextNome);
            EditText txtTelefone = (EditText) findViewById(R.id.editTextTelefone);
            ImageView foto = (ImageView) findViewById(R.id.imageViewFoto);

            txtId.setText(id);
            txtNome.setText(nome);
            txtTelefone.setText(telefone);

            if(img != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                foto.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
}
