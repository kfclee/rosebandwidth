package edu.rosehulman.jungckjp_leekf.rosebandwidth.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;
import edu.rosehulman.rosefire.RosefireAuth;

public class LoginActivity extends AppCompatActivity {

    private boolean mLoggingIn;
    private EditText mPasswordView;
    private EditText mEmailView;
    private View mProgressSpinner;
    private Button mLoginButton;
    private boolean layoutShiftedUp;
    private RelativeLayout mView;
    private ArrayList<View> mLayoutViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        mView = (RelativeLayout) findViewById(R.id.login_view);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mEmailView = (EditText) findViewById(R.id.username_text);
        mEmailView.setText("");
        mPasswordView = (EditText) findViewById(R.id.password_text);
        mPasswordView.setText("");
        mProgressSpinner = findViewById(R.id.login_progress);
        mLoggingIn = false;

        mEmailView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    mEmailView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    mEmailView.clearFocus();
                    mPasswordView.requestFocus();
                }
            }
        });

        mPasswordView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Log.d(Constants.TAG, "Enter clicked");
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    mPasswordView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    mPasswordView.clearFocus();
                    mLoginButton.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoginButton.performClick();
                        }
                    });
                }
            }
        });

        mEmailView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateLoginLayout(true);
                    return false;
                }
            }

            );
            mPasswordView.setOnTouchListener(new View.OnTouchListener()

            {
                @Override
                public boolean onTouch (View v, MotionEvent event){
                updateLoginLayout(true);
                return false;
            }
            }

            );
            mView.setOnTouchListener(new View.OnTouchListener()

            {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    updateLoginLayout(false);
                    return true;
                }
            });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithRosefire();
                updateLoginLayout(false);
            }
        });

        mLayoutViews = new ArrayList<View>();
        mLayoutViews.add(mEmailView);
        mLayoutViews.add(mPasswordView);
        mLayoutViews.add(mLoginButton);

        mEmailView.clearFocus();
        hideKeyboard();
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

    private void updateLoginLayout(boolean shiftLayoutUp) {
        int shiftAmount = 0;

        if (shiftLayoutUp && !layoutShiftedUp) {
            shiftAmount = -200;
            layoutShiftedUp = true;
        } else if (!shiftLayoutUp && layoutShiftedUp) {
            shiftAmount = 200;
            layoutShiftedUp = false;
            hideKeyboard();
        } else {
            return;
        }

        for (View v : mLayoutViews) {
            v.animate().x(v.getX()).y(v.getY() + shiftAmount).setDuration(500).start();
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

//        releaseFocus(mEmailView);
//        releaseFocus(mPasswordView);
    }

    public static void releaseFocus(View view) {
        ViewParent parent = view.getParent();
        ViewGroup group = null;
        View child = null;
        while (parent != null) {
            if (parent instanceof ViewGroup) {
                group = (ViewGroup) parent;
                for (int i = 0; i < group.getChildCount(); i++) {
                    child = group.getChildAt(i);
                    if(child != view && child.isFocusable())
                        child.requestFocus();
                }
            }
            parent = parent.getParent();
        }
    }

    private void showProgress(boolean show) {
        mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
