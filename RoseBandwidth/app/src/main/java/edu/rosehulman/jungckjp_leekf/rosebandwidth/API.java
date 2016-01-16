package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jonathan on 1/16/16.
 */
public class API extends AsyncTask<String, Void, String>{

    static final String name = "jungckjp";
    static final String password = "";


    public API() throws IOException {

    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = "https://netreg.rose-hulman.edu/tools/networkUsage.pl";

        URL url = null;
        String content = "";
        try {
//            url = new URL(urlString);
//            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//
//            String authString = name + ":" + password;
//            System.out.println("auth string: " + authString);
//            String authStringEnc = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT).replace("\n", "");
//            System.out.println("Base64 encoded auth string: " + authStringEnc);
//
//            con.setRequestProperty("Authorization", "Basic " + authStringEnc);
//
//
//            InputStream is =con.getInputStream();
//
//            // Once you have the Input Stream, it's just plain old Java IO stuff.
//
//            // For this case, since you are interested in getting plain-text web page
//            // I'll use a reader and output the text content to System.out.
//
//            // For binary content, it's better to directly read the bytes from stream and write
//            // to the target file.
//
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            String line = null;
//
//            // read each line and write to System.out
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//                content += line;
//            }

            Authenticator.setDefault(new MyAuthenticator());
            url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            System.out.println(con.getResponseMessage());
            System.out.println(con.getHeaderFields());
                        int status = con.getResponseCode();
            System.out.println(Integer.toString(status));

            InputStream ins = con.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String str;
            while ((str = reader.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    static class MyAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(name, password.toCharArray()));
        }
    }
}
