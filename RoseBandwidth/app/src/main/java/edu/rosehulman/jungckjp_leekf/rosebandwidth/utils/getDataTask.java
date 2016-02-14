package edu.rosehulman.jungckjp_leekf.rosebandwidth.utils;

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
import eu.masconsult.android_ntlm.NTLMSchemeFactory;

/**
 * Created by leekf on 1/29/2016.
 */
public class getDataTask extends AsyncTask<String, Void, String>{

    private static String name = "";
    private static String password = "";
    private MainActivity mActivity;
    private API mAPI;

    public getDataTask(){
        //default empty constructor
    }

    public getDataTask(API api){
        mAPI = api;
        mActivity = api.mActivity;
        name = PreferenceManager.getDefaultSharedPreferences(mActivity).getString(Constants.USERNAME, "");
        password = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("password", "");
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
        String urlString = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("data_url", "https://netreg.rose-hulman.edu/tools/networkUsageData.pl");
//        String urlString = "https://netreg.rose-hulman.edu/tools/networkUsageData.pl";
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
            ArrayList<Device> devices = new ArrayList<Device>();
            while ((n = in.readLine()) != null) {
                System.out.println(n);
                if(i == 3){
                    List<String> items = Arrays.asList(n.split("\\s*,\\s*"));
                    mAPI.setUsage(parseUsage(items));
                }

                if (i >= 6) {
                    List<String> items = Arrays.asList(n.split("\\s*,\\s*"));
                    devices.add(parseDevice(items));
                }
                i++;
            }
            mAPI.setDevices(devices);
            in.close();
            for (Device d : mAPI.mDevices) {
                System.out.println(d.getName() + " " + d.getMacAddress() + " " + d.getUsageAmount());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public Device parseDevice(List<String> items) {
        float download = 0;
        float upload = 0;
        int uploadIndex = 4;

        if (items.get(3).contains(".")) {
            download = Float.parseFloat((String)items.get(3));
        } else {
            download = (Float.parseFloat((String)items.get(3)) * 1000) + Float.parseFloat((String)items.get(4));
            uploadIndex++;
        }

        if (items.get(uploadIndex).contains(".")) {
            upload = Float.parseFloat((String)items.get(uploadIndex));
        } else {
            upload = (Float.parseFloat((String)items.get(uploadIndex)) * 1000) + Float.parseFloat((String)items.get(uploadIndex + 1));
        }

        Device device = new Device(items.get(2), items.get(0),download, upload, "");
        return device;
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

        Fragment f = mActivity.getCurrentFragment();
        if(f != null){
            if(f.getClass().equals(DevicesFragment.class)){
                DevicesFragment df = (DevicesFragment)mActivity.getCurrentFragment();
                df.getAdapter().notifyDataSetChanged();
            } else if(f.getClass().equals(UsageFragment.class)){
                UsageFragment uf = (UsageFragment)mActivity.getCurrentFragment();
                uf.notifyDataSetChanged();
            }
        }
    }
}
