package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import edu.rosehulman.rosefire.RosefireAuth;

public class LoginActivity extends AppCompatActivity {

    private boolean mLoggingIn;
    private EditText mPasswordView;
    private EditText mEmailView;
    private View mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        Button button = (Button) findViewById(R.id.login_button);
        mEmailView = (EditText) findViewById(R.id.username_text);
        mEmailView.setText("");
        mPasswordView = (EditText) findViewById(R.id.password_text);
        mPasswordView.setText("");
        mProgressSpinner = findViewById(R.id.login_progress);
        mLoggingIn = false;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithRosefire();
            }
        });
    }

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {
        @Override
        public void onAuthenticated(AuthData authData) {
            loginToMainActivity(Constants.FIREBASE_URL + "/users/" + authData.getUid());
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.e(Constants.TAG, "onAuthenticationError: " + firebaseError.getMessage());
            mLoggingIn = false;
        }
    }

    private void loginToMainActivity(String url){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("password", mPasswordView.getText().toString());
        intent.putExtra("username", mEmailView.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }

    private void loginWithRosefire(){
        if (mLoggingIn) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();

        String password = mPasswordView.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.field_required));
            focusView = mEmailView;
            cancelLogin = true;
        } else if (!email.endsWith("@rose-hulman.edu")) {
            email += "@rose-hulman.edu";
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            mLoggingIn = true;
            onRosefireLogin(email, password);
            hideKeyboard();
        }
    }

    public void onRosefireLogin(String email, String password) {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        RosefireAuth roseFireAuth = new RosefireAuth(firebase, Constants.ROSEFIRE_KEY);
        roseFireAuth.authWithRoseHulman(email, password, new MyAuthResultHandler());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
    }

    private void showProgress(boolean show) {
        mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
