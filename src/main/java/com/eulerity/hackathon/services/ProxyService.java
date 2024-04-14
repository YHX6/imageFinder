package com.eulerity.hackathon.services;

import com.eulerity.hackathon.models.ProxyResponseModel;
import com.google.gson.Gson;
import sun.net.www.http.HttpClient;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


public class ProxyService {

    public ProxyService() {

    }


    public Proxy getHttpsProxy() {
        ProxyResponseModel proxyModel = sendGetRequest("http://localhost:5010/get/?https=true");
        try{
            System.out.println(proxyModel.toString());
            String[] info = proxyModel.getProxy().split(":");

            Proxy proxy = new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(info[0], Integer.parseInt(info[1])));
            return proxy;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private ProxyResponseModel sendGetRequest(String urlAddress) {
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Use Gson to parse JSON response
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), ProxyResponseModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public Proxy getSampleProxy() {
//        Proxy proxy = new Proxy(Proxy.Type.HTTPS, new InetSocketAddress("66.225.246.238", 8123));
//        return proxy;
//    }
}
