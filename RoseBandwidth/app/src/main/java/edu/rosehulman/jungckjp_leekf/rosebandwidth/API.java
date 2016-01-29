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
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import eu.masconsult.android_ntlm.NTLMSchemeFactory;

/**
 * Created by jonathan on 1/16/16.
 */
public class API extends AsyncTask<String, Void, String>{

    static final String name = "jungckjp";
    static final String password = "";

    private static API me;

    ArrayList<Device> mDevices = new ArrayList<Device>();


    public API() throws IOException {

    }

    public static API getInstance() throws IOException {
        if (me == null) {
            me = new API();
        }

        return me;
    }

    static class MyAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            // I haven't checked getRequestingScheme() here, since for NTLM
            // and Negotiate, the usrname and password are all the same.
            System.err.println("Feeding username and password for " + getRequestingScheme());
            return (new PasswordAuthentication(name, password.toCharArray()));
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = "https://netreg.rose-hulman.edu/tools/networkUsageData.pl";
        String content = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            // register ntlm auth scheme
            httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
            httpclient.getCredentialsProvider().setCredentials(
                    // Limit the credentials only to the specified domain and port
                    new AuthScope("netreg.rose-hulman.edu", -1),
                    // Specify credentials, most of the time only user/pass is needed
                    new NTCredentials(name, password, "", "")
            );

            HttpGet request = new HttpGet();
            URI website = new URI(urlString);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String n;
            int i = 0;
            while ((n = in.readLine()) != null) {
                System.out.println(n);
                if (i >= 6) {
                    List<String> items = Arrays.asList(n.split("\\s*,\\s*"));
                    Device device = new Device(items.get(2), items.get(0),Float.parseFloat((String)items.get(3)),0);
                    mDevices.add(device);
                }
                i++;
            }
            in.close();
            for (Device d : mDevices) {
                System.out.println(d.getName() + " " + d.getMacAddress() + " " + d.getUsageAmount());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }
}
