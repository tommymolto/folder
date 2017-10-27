package com.example.paulomarinho.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    /**
     * @2DO: isso tem que receber do login do usuario e criar um spinner
     */
    public static String topico = "DEMONSTRAÇÂO";
    private static final String URL = "http://projetos.lab245.com.br/webservice/global.asmx";
    public String SoapInput;
    public String login = "";
    public String senha = "";
    public String extensao;
    public String nomeArquivo;
    public String arquivoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!verificaLogin()) {
            //redireciona para login
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {

            //pega dados de login
            SharedPreferences sp = this.getSharedPreferences("lab245",0);
            this.login = sp.getString("login","");
            this.senha = sp.getString("senha","");



            //vamos subir a imagem
            Intent intent = getIntent(); //pega chamada
            String action = intent.getAction(); // pega tipo de chamada
            String type = intent.getType(); // pega tipo de envio
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                Log.e("pd", "pd");
                Uri imgUpload = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                gerenciaImagemRecebida(imgUpload);
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                gerenciaImagensRecebidas(intent);
            } else {
                // Se houver outros
            }
        }
    }
    public void gerenciaImagemRecebida(Uri imgUpload){
        //chamar o web servce
        extensao = imgUpload.getLastPathSegment();
        enviaArquivo x = new enviaArquivo();
        x.execute();
    }
    public void gerenciaImagensRecebidas(Intent ittUpload){
        ArrayList<Uri> imageUris = ittUpload.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        for(Uri imgUpload:  imageUris) {
            gerenciaImagemRecebida(imgUpload);
        }
    }




    public boolean verificaLogin(){
        SharedPreferences sp = this.getSharedPreferences("lab245",0);
        if (!sp.getString("usuario","").isEmpty() && !sp.getString("senha","").isEmpty()){
            return true;
        }
        return false;
    }

    public String arquivoPara64(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());
            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    private String encodeImageBitmapToBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    private String encodeImageFileTobase64(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    public  class enviaArquivo extends AsyncTask<String, Float, String> {
        @Override
        protected void onPreExecute() {




            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            arquivoBase64 = encodeImageFileTobase64(params[0]);
            SoapInput = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> " ;
            SoapInput += "<soap:Body>" +
                    "<recebeArquivo xmlns=\"http://projetos.lab245.com.br/WebService/\">" +
                    "<Login>"+  login + "</Login>" +
                    "<Senha>" + senha +"</Senha>" +
                    "<Topico>" + topico +"</Topico>" +
                    "<Documento1>a</Documento1>" +
                    "<Documento2>s</Documento2>" +
                    "<Documento3>d</Documento3>" +
                    "<Documento4>f</Documento4>" +
                    "<Documento5>g</Documento5>" +
                    "<Documento6>h</Documento6>" +
                    "<Documento7>j</Documento7>" +
                    "<Documento8>k</Documento8>" +
                    "<Documento9>ll</Documento9>" +
                    "<Documento10>z</Documento10>" +
                    "<Documento11>x</Documento11>" +
                    "<Documento12>c</Documento12>" +
                    "<Documento13>v</Documento13>" +
                    "<Documento14>b</Documento14>" +
                    "<Documento15>n</Documento15>" +
                    "<Documento16>m</Documento16>" +
                    "<Documento17>p</Documento17>" +
                    "<Documento18>we</Documento18>" +
                    "<Documento19>sdf</Documento19>" +
                    "<Documento20>sdf</Documento20>" +
                    "<arquivo>" +
                    "<objeto>" +
                    "<head>"+ Math.random()*10000 +"."+ extensao +"</head>" +
                    "<corpo>" + arquivoBase64+ "</corpo>" +
                    "</objeto>" +
                    "</arquivo>" +
                    "</recebeArquivo>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            String urlString = URL;

            String data = params[1];

            OutputStream out = null;
            try {

                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter (
                        new OutputStreamWriter(out, "UTF-8"));
                writer.write(SoapInput);
                writer.flush();
                writer.close();
                out.close();
                urlConnection.connect();
            } catch (Exception e) {
                e.toString();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }


}
