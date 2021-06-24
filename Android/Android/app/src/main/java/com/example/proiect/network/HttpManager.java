package com.example.proiect.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager {
    private URL url;
    private HttpURLConnection connection;

    //clase utilitare pentru preluarea informatiilor din retea
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    private final String urlAddress;

    public HttpManager(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String process() { // in loc de call din HttpManager
        try {
            return getResultFromHttp();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return null;
    }

    private String getResultFromHttp() throws IOException {
        //obiectul url asigura verificarea unui string ca fiind un url valid. prin el se obtine o conexiune
        url = new URL(urlAddress);
        //conexiunea realizata de url
        connection = (HttpURLConnection) url.openConnection();
        //se preia toata informatia prin intermediul conexiunii
        inputStream = connection.getInputStream();
        //informatia este sectionata in unitati mai mici
        inputStreamReader = new InputStreamReader(inputStream);
        //fiecare unitate este la randul ei impartita in bucati mai mici si incarcate intr-o zona tampon din memorie
        bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String line;
        //se citeste linie cu linie toate informatiile care ajung in zona tampon
        while ((line = bufferedReader.readLine()) != null) {
            //informatiile din zona tampon sunt concatenate intr-un StringBuilder
            result.append(line);
        }
        return result.toString();
    }

    private void closeConnections() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }
}
