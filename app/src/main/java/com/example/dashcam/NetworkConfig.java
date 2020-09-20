package com.example.dashcam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

public class NetworkConfig {
    private ConnectivityManager.NetworkCallback mWifiNetworkCallback, mMobileNetworkCallback;
    private Network mWifiNetwork, mMobileNetwork;
    ConnectivityManager manager;

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   public NetworkConfig(Context context)
   {
       manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       BUildNetworkCallBack();
       RequestNetwork();
   }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void BUildNetworkCallBack()
    {
        if(mWifiNetworkCallback == null){
            //Init only once
            mWifiNetworkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(final Network network) {
                    try {
                        //Save this network for later use
                        mWifiNetwork = network;
                        System.out.println("---------------------:mWifiNetwork");
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            };
        }

        if(mMobileNetworkCallback == null){
            //Init only once
            mMobileNetworkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(final Network network) {
                    try {
                        //Save this network for later use
                        mMobileNetwork = network;
                        System.out.println("---------------------:mMobileNetwork");
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            };
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void RequestNetwork()
    {
        NetworkRequest.Builder wifiBuilder;
        wifiBuilder = new NetworkRequest.Builder();
//set the transport type do WIFI
        wifiBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        manager.requestNetwork(wifiBuilder.build(), mWifiNetworkCallback);

        NetworkRequest.Builder mobileNwBuilder;
        mobileNwBuilder = new NetworkRequest.Builder();
//set the transport type do Cellular
        mobileNwBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        manager.requestNetwork(mobileNwBuilder.build(), mMobileNetworkCallback);
    }

    public Network GetWIfiNetwork()
    {
       return mMobileNetwork;
    }
public  static Boolean isWifi;
    public  static String httpUrl;
    public void makeHTTPRequestCellular(final String httpUrl, final String payloadJson,Boolean isWifi) {

        try {
            this.httpUrl=httpUrl;
            this.isWifi=isWifi;
            System.out.println("---------------------: "+httpUrl);
            new MyTask().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();
            String net="";
            try {
                URL url = new URL(NetworkConfig.httpUrl);
                HttpURLConnection conn = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    if(NetworkConfig.isWifi)
                    {
                        net="Response from WIFI";
                        conn = (HttpURLConnection) mWifiNetwork.openConnection(url);
                       // conn.setRequestProperty("Content-Type", "application/json");
                    }else {
                        net="Response from Mobile data";
                        conn = (HttpURLConnection) mMobileNetwork.openConnection(url);
                     //  conn.setRequestProperty("Content-Type", "text/html");
                      //  conn.setRequestProperty("Content-Type", "application/json");
                    }

                }
                conn.setRequestMethod("GET");
                conn.connect();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {


                    final String statusMessage = conn.getResponseMessage();
                    InputStream in = new BufferedInputStream(conn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    System.out.println("---------------------"+net);
                    System.out.println("---------------------"+ sb.toString()+"-----------------------");
                }else {
                    System.out.println("---------------------Error-----------------------");
                }
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
           // System.out.println("------------------------:"+result);
            super.onPostExecute(aVoid);
        }
    }

}
