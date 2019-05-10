package com.ksoftsas.poslite.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.models.User;
import com.ksoftsas.poslite.services.AppServices;

public class SignUpActivity extends AppCompatActivity {

    AppServices services;
    EditText username, email, password, confirm_password,business_name;
    CheckBox checkbox;
    TextView termsOfUse;
    Button sign_up_button;
    public static final String TAG = SignUpActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        services = new AppServices();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        business_name = findViewById(R.id.business_name);
        checkbox = findViewById(R.id.checkBox);
        termsOfUse = findViewById(R.id.termsOfUse);
        sign_up_button = findViewById(R.id.sign_up_button);


        username.setText("kimsong");
        email.setText("im@gmail.com");
        password.setText("123456");
        confirm_password.setText("123456");
        business_name.setText("Mini Mart");

        checkbox.setText("");
        termsOfUse.setText(Html.fromHtml("I understand and agree to POS Lite " +
                "<a href='https://www.ksoftsas.com/term-of-uses'>Terms of Use</a>" + " and " +  "<a href='https://www.ksoftsas.com/privacy'>Privacy Policy</a>"));
        termsOfUse.setClickable(true);
        termsOfUse.setMovementMethod(LinkMovementMethod.getInstance());
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset errors.
                username.setError(null);
                email.setError(null);
                business_name.setError(null);
                confirm_password.setError(null);
                password.setError(null);

                boolean cancel = false;
                View focusView = null;

                // Store values at the time of the login attempt.
                String strUsername = username.getText().toString();
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();
                String strConPassword = confirm_password.getText().toString();
                String strBusinessName = business_name.getText().toString();

                if (TextUtils.isEmpty(strUsername)) {
                    username.setError(getString(R.string.error_field_required));
                    focusView = username;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(strEmail)) {
                    email.setError(getString(R.string.error_field_required));
                    focusView = email;
                    cancel = true;
                }
                else if (!isEmailValid(strEmail)) {
                    email.setError(getString(R.string.error_invalid_email));
                    focusView = email;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(strPassword)) {
                    password.setError(getString(R.string.error_field_required));
                    focusView = password;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(strConPassword)) {
                    confirm_password.setError(getString(R.string.error_field_required));
                    focusView = confirm_password;
                    cancel = true;
                }
                else if (!strPassword.equals(strConPassword)) {
                    password.setError(getString(R.string.error_password_does_not_match));
                    focusView = password;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(strBusinessName)) {
                    business_name.setError(getString(R.string.error_field_required));
                    focusView = business_name;
                    cancel = true;
                }
                else if (!checkbox.isChecked()) {
                    Toast.makeText(SignUpActivity.this,R.string.error_terms_of_use, Toast.LENGTH_SHORT).show();
                    focusView = checkbox;
                    cancel = true;
                }
                else if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else{
                    User user = new User();
                    user.setEmail(strEmail);
                    user.setUserName(strUsername);
                    user.setPassword(services.encrypt(strPassword));
                    if(user.insert(user) == 1){
                        setResult(RESULT_OK);
                        finish();
                    }
                    else{
                        Toast.makeText(SignUpActivity.this,"Save Error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
