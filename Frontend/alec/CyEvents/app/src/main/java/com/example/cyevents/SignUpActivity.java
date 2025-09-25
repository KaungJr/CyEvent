package com.example.cyevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

/**
 *
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private Button createBtn;
    private Button deleteBtn;
    private TextView backBtn;

//    private RequestQueue requestQueue;

//    private static final String postURL = "http://10.90.73.72:8080/profiles";
    private static final String postURL = "https://36356748-026b-4950-8df9-247002157157.mock.pstmn.io";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);

        createBtn = findViewById(R.id.createbtn);
        deleteBtn = findViewById(R.id.deletebtn);
        backBtn = findViewById(R.id.back);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                try {
//                    if(userRegistration(username,email,password,confirmPassword)){
//                        startActivity(new Intent(SignUpActivity.this, InterestActivity.class));
//                    };
                    userRegistration(username,email,password,confirmPassword);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

//               try{
//                   deleteUser(username,email,password,confirmPassword);
//               }
//               catch (JSONException e){
//                   throw new RuntimeException(e);
//               }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
    }

    /**
     *
     */
    private boolean userRegistration(String username, String email, String password, String confirmPassword) throws JSONException {
        usernameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        confirmPasswordEditText.setError(null);

        boolean cancelAttempt = false;

        if (TextUtils.isEmpty(username) || !(usernameValidation(username))) {
            usernameEditText.setError("Username Invalid!");
            cancelAttempt = true;
        }

        if (TextUtils.isEmpty(email) || !(emailValidation(email))) {
            emailEditText.setError("Email Invalid!");
            cancelAttempt = true;
        }

        if (TextUtils.isEmpty(password) || !(passwordValidation(password))) {
            passwordEditText.setError("Password Invalid!");
            cancelAttempt = true;
        }

        if (!(confirmPassword.equals(password))) {
            confirmPasswordEditText.setError("Passwords do not match!");
            cancelAttempt = true;
        }

        if (cancelAttempt) {
            usernameEditText.setHint("Username");
            emailEditText.setHint("Email");
            passwordEditText.setHint("Password");
            confirmPasswordEditText.setHint("Confirm Password");
        } else {
            JSONObject userData = new JSONObject();
            try {
                userData.put("username", username);
                userData.put("email", email);
                userData.put("password", password);
                Toast.makeText(SignUpActivity.this, userData.toString(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, postURL, userData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                            Log.e("VolleyError", "Error: " + error.toString());

                            String errorMessage = "";
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                errorMessage = new String(error.networkResponse.data);
                            } else if (error.getMessage() != null) {
                                errorMessage = error.getMessage();
                            } else {
                                errorMessage = "Unknown error occurred";
                            }
                            Log.e("VolleyError", errorMessage);
                            Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
        return false;
    }

    /**
     *
     */
//    private boolean deleteUser(String username, String email, String password, String confirmPassword) {
//        usernameEditText.setError(null);
//        emailEditText.setError(null);
//        passwordEditText.setError(null);
//        confirmPasswordEditText.setError(null);
//
//        boolean cancelAttempt = false;
//
//        //check if username exists
//        if(TextUtils.isEmpty(username) || !(usernameCheck(username))){
//            usernameEditText.setError("Username Invalid!");
//            cancelAttempt = true;
//        }
//
//        //pull user info
//        String[] userInfo = new String[3];
//        userInfo = getRequest(username);
//
//        if(TextUtils.isEmpty(email) || !(email.equals(userInfo[1]))){
//            emailEditText.setError("Email Invalid!");
//            cancelAttempt = true;
//        }
//
//        if(TextUtils.isEmpty(password) || !(password.equals(userInfo[2]))){
//            passwordEditText.setError("Invalid Password!");
//            cancelAttempt = true;
//        }
//
//        if (TextUtils.isEmpty(confirmPassword) || !(confirmPassword.equals(password))){
//            confirmPasswordEditText.setError("Passwords Do Not Match!");
//            cancelAttempt = true;
//        }
//
//        if(cancelAttempt){
//            usernameEditText.setHint("Username");
//            emailEditText.setHint("Email");
//            passwordEditText.setHint("Password");
//            confirmPasswordEditText.setHint("Confirm Password");
//        }
//        else{
////            try{
//                 deleteRequest(userInfo);
////            }
////            catch (JSONException e){
////
////            }
//        }
//        return false;
//    }

    /**
     *
     * @param username
     * @return
     */
    private boolean usernameValidation(String username){
        return true;
    }

    /**
     *
     * @param email
     * @return
     */
    private  boolean emailValidation(String email){
        return true;
    }

    /**
     *
     * @param password
     * @return
     */
    private boolean passwordValidation(String password){
        return true;
    }

//    private void postRequest(JSONObject userObject){
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, postUrl, userObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(SignUpActivity.this,"Sign Up Successful!",Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
//                        Log.e("VolleyError", "Error: " + error.toString());
//
//                        String errorMessage = "";
//                        if (error.networkResponse != null && error.networkResponse.data != null) {
//                            errorMessage = new String(error.networkResponse.data);
//                        } else if (error.getMessage() != null) {
//                            errorMessage = error.getMessage();
//                        } else {
//                            errorMessage = "Unknown error occurred";
//                        }
//                        Log.e("VolleyError", errorMessage);
//                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//        Volley.newRequestQueue(this).add(jsonObjectRequest);
//    }


//    private void postRequest(String username, String email, String password) {
//        JSONObject userObject = new JSONObject();
//        userObject.put("username", username);
//        userObject.put("email", email);
//        userObject.put("password", password);
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                postUrl,
//                userObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(SignUpActivity.this,"Sign Up Successful!",Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
//                        Log.e("VolleyError", "Error: " + error.toString());
//
//                        String errorMessage = "";
//                        if (error.networkResponse != null && error.networkResponse.data != null) {
//                            errorMessage = new String(error.networkResponse.data);
//                        } else if (error.getMessage() != null) {
//                            errorMessage = error.getMessage();
//                        } else {
//                            errorMessage = "Unknown error occurred";
//                        }
//                        Log.e("VolleyError", errorMessage);
//                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//
//
//                    }
//                })
//
////        {
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                HashMap<String, String> headers = new HashMap<String, String>();
////                    headers.put("Authorization","");
////                    headers.put("Content-Type","application/json");
////                return headers;
////            }
////
////            @Override
////            protected Map<String, String> getParams() {
////                Map<String, String> params = new HashMap<String, String>();
////                    params.put("username",username);
////                    params.put("email",email);
////                    params.put("password",password);
////
////                return params;
////            }
////        }
//        ;
//
//
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
//    }

    /**
     *
     * @param username
     * @return
     */
    private String[] getRequest (String username){
        return null;
    }

    /**
     *
     * @param userinfo
     * @return
     */
    private boolean deleteRequest(String[] userinfo){
        return true;
    }

    /**
     *
     * @param username
     * @return
     */
    private boolean usernameCheck(String username){
        return true;
    }

}