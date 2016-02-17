package edu.rosehulman.jungckjp_leekf.rosebandwidth.services;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.DevicesFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.UsageFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Device;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Usage;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;
import eu.masconsult.android_ntlm.NTLMSchemeFactory;

/**
 * Created by leekf on 1/29/2016.
 */
public class getBackgroundDataTask extends AsyncTask<String, Void, String>{

    private static String mName = "";
    private static String mPassword = "";
    private static String mUrl = "";
    private static DataConsumer mService;

    public getBackgroundDataTask(){
        //default empty constructor
    }

    public getBackgroundDataTask(String name, String password, String url, DataConsumer service){
        this.mName = name;
        this.mPassword = password;
        this.mUrl = url;
        mService = service;
    }

    public interface DataConsumer {
        public void onDataLoaded(float usage);
    }


    static class MyAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            // I haven't checked getRequestingScheme() here, since for NTLM
            // and Negotiate, the usrname and password are all the same.
            System.err.println("Feeding username and password for " + getRequestingScheme());
            return (new PasswordAuthentication(mName, mPassword.toCharArray()));
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = mUrl;
        String content = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            // register ntlm auth scheme
            httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
            httpclient.getCredentialsProvider().setCredentials(
                    // Limit the credentials only to the specified domain and port
                    new AuthScope("netreg.rose-hulman.edu", -1),
                    // Specify credentials, most of the time only user/pass is needed
                    new NTCredentials(mName, mPassword, "", "")
            );

            HttpGet request = new HttpGet();
            URI website = new URI(urlString);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String n;

            for (int i = 0; i < 3; i++) {
                in.readLine();
            }

            List<String> items = Arrays.asList(in.readLine().split("\\s*,\\s*"));
            Usage usage = parseUsage(items);
            mService.onDataLoaded(usage.getDownload());

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    public Usage parseUsage(List<String> items) {
        String status = items.get(0);
        float down = 0;
        int uploadIndex = 2;
        if (items.get(1).contains(".")) {
            down = Float.parseFloat((String) items.get(1));
        } else {
            down = (Float.parseFloat((String) items.get(1)) * 1000) + Float.parseFloat((String) items.get(2));
            uploadIndex++;
        }
        float up = 0;

        if (items.get(uploadIndex).contains(".")) {
            up = Float.parseFloat((String) items.get(uploadIndex));
        } else {
            up = (Float.parseFloat((String) items.get(uploadIndex)) * 1000) + Float.parseFloat((String) items.get(uploadIndex + 1));
        }

        Usage usage = new Usage(status, up, down);
        return usage;
    }

    @Override
    public void onPostExecute(String result){
        super.onPostExecute(result);

    }
}
