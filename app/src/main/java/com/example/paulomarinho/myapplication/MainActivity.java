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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    /**
     * @2DO: isso tem que receber do login do usuario e criar um spinner
     */
    public static String topico = "TESTES FOLDRES";
    private static final String URL = "http://projetos.lab245.com.br/webservice/global.asmx";
    public String SoapInput;
    public String login = "";
    public String senha = "";
    public String extensao;
    public String nomeArquivo;
    public String arquivoBase64;
    public Uri arq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*GAMBIARRA*/
        SharedPreferences sp = this.getSharedPreferences("lab245",0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("usuario", "lab245");
        ed.putString("senha", "lab245");
        ed.commit();

        if (!verificaLogin()) {
            //redireciona para login
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {

            //pega dados de login
            SharedPreferences sp2 = this.getSharedPreferences("lab245",0);
            this.login = sp2.getString("login","");
            this.senha = sp2.getString("senha","");



            //vamos subir a imagem
            Intent intent = getIntent(); //pega chamada
            String action = intent.getAction(); // pega tipo de chamada
            String type = intent.getType(); // pega tipo de envio
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                Log.e("LAB245", "recebifo");
                Uri imgUpload = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
               // Uri imgUpload = (Uri) intent.getData();
                Bitmap bitmap;
                try {
                    Log.e("LAB245", "convertendo");
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUpload));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    nomeArquivo = ConvertBitmapToString(resizedBitmap);
                    Log.e("LAB245", "convertifo");


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Log.e("LAB245", "erro de caminho");
                    e.printStackTrace();
                }
                gerenciaImagemRecebida(imgUpload);
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                gerenciaImagensRecebidas(intent);
            } else {
                // Se houver outros
            }
        }
    }
    public static String ConvertBitmapToString(Bitmap bitmap){
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }
    public void gerenciaImagemRecebida(Uri imgUpload){
        Log.e("LAB245", "gerenciaImagemRecebida");
        //chamar o web servce
        arq = imgUpload;
        Log.e("imagem",imgUpload.toString());
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

    private String convertToBase64(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    public  class enviaArquivo extends AsyncTask<String, Float, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("LAB245", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... params) {
            //Log.e(params.toString());

            File imagefile = new File(arq.toString());
          // arquivoBase64 = convertToBase64(arq.toString());
            arquivoBase64 = nomeArquivo;
            Log.e("LAB245", "doInBackground");
            SoapInput = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> " ;
            SoapInput += "<soap:Body>" +
                    "<recebeArquivo xmlns=\"http://projetos.lab245.com.br/WebService/\">" +
                    "<Login>"+  login + "</Login>" +
                    "<Senha>" + senha +"</Senha>" +
                    "<Topico>TESTES FOLDRES</Topico>" +
                    "<Documento1>string</Documento1>" +
                    "<Documento2>string</Documento2>" +
                    "<Documento3>string</Documento3>" +
                    "<Documento4></Documento4>" +
                    "<Documento5></Documento5>" +
                    "<Documento6></Documento6>" +
                    "<Documento7></Documento7>" +
                    "<Documento8></Documento8>" +
                    "<Documento9></Documento9>" +
                    "<Documento10></Documento10>" +
                    "<Documento11></Documento11>" +
                    "<Documento12></Documento12>" +
                    "<Documento13></Documento13>" +
                    "<Documento14></Documento14>" +
                    "<Documento15></Documento15>" +
                    "<Documento16></Documento16>" +
                    "<Documento17></Documento17>" +
                    "<Documento18></Documento18>" +
                    "<Documento19></Documento19>" +
                    "<Documento20></Documento20>" +
                    "<arquivo>" +
                    "<objeto>" +
                    "<head>pm2.jpg</head>" +
                    "<corpo>" + arquivoBase64+ "</corpo>" +
                    "</objeto>" +
                    "</arquivo>" +
                    "</recebeArquivo>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            String urlString = URL;

            OutputStream out = null;
            try {

                //URL url = new URL(urlString + "?op=recebeArquivo");
                URL url = new URL(urlString );
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter (
                        new OutputStreamWriter(out, "UTF-8"));
                writer.write(SoapInput);
                writer.flush();
                writer.close();
                out.close();
                urlConnection.connect();
                int responseCode=urlConnection.getResponseCode();
                String response = "";
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    Log.e("resconsecode", urlConnection.getResponseMessage().toString());
                    response="";

                }
                Log.e("response", response);
            } catch (Exception e) {
                Log.e("erro", e.toString());
                e.toString();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {

            super.onProgressUpdate(values);
            Log.e("log",values.toString());
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("onPostExecute","onPostExecute");
            super.onPostExecute(s);
        }
    }


}
