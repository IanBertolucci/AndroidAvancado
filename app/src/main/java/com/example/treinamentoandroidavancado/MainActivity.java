package com.example.treinamentoandroidavancado;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    String pwd = "AndroidAvancado";

    String PBE_ALGORITMO = "PBEWithSHA256And256BitAES-CBC-BC";
    String CIPHER_ALGORITMO = "AES/CBC/PKCS5Padding";

    int NUM_ITERACOES = 1000;
    int KEY_SIZE = 256;

    byte[] salt = "abcdefgh123456789".getBytes();
    byte[] iv = "1234567890abcdef".getBytes(); // Deve possuir tamanho 16

    byte[] encryptedText, decryptedText;

    TextView txtEnc, txtDec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEnc = (TextView) findViewById(R.id.txtEnc);
        txtDec = (TextView) findViewById(R.id.txtDec);

        try
        {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(pwd.toCharArray(), salt, NUM_ITERACOES, KEY_SIZE);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITMO);
            SecretKey tempKey = keyFactory.generateSecret(pbeKeySpec);
            SecretKey secretKey = new SecretKeySpec(tempKey.getEncoded(), "AES");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher encCipher = Cipher.getInstance(CIPHER_ALGORITMO);
            encCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            encryptedText = encCipher.doFinal(pwd.getBytes());

            Cipher decCipher = Cipher.getInstance(CIPHER_ALGORITMO);
            decCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            decryptedText = decCipher.doFinal(encryptedText);

            txtEnc.setText(new String(encryptedText));
            txtDec.setText(new String(decryptedText));


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
