package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
public class API{

    private static API me;

    ArrayList<Device> mDevices = new ArrayList<Device>();
    Usage mUsage;
    public MainActivity mActivity;

    public API(MainActivity mainActivity) throws IOException {
        mActivity = mainActivity;
    }

    public static API getInstance(MainActivity mainActivity) throws IOException {
        if (me == null) {
            me = new API(mainActivity);
        }
        return me;
    }

    public static API createNew(MainActivity mainActivity)throws IOException {
        me = new API(mainActivity);
        return me;
    }

    public Usage getUsage() {
        return mUsage;
    }

    public void setUsage(Usage mUsage) {
        this.mUsage = mUsage;
    }

    public ArrayList<Device> getDevices() {
        return mDevices;
    }

    public void setDevices(ArrayList<Device> mDevices) {
        this.mDevices = mDevices;
    }

    public void getData(){
        new getDataTask(this).execute();
    }
}
