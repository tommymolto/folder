package com.example.paulomarinho.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void salvaLogin(View v){
        EditText login = (EditText) findViewById(R.id.txtLogin);
        EditText password = (EditText) findViewById(R.id.txtSenha);

        //valida no web service se o login esta correto
        //
        salvaLoginSP(login.getText().toString() , password.getText().toString());

        Intent t = new Intent(this, MainActivity.class);
        startActivity(t);

    }

    public boolean salvaLoginSP( String usuario, String senha){

        SharedPreferences sp = this.getSharedPreferences("lab245",0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("usuario", usuario);
        ed.putString("senha", senha);
        ed.commit();
        return true;

    }



}
