package com.elf.elfparent.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.elf.elfparent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Network.AppRequestQueue;
import Utils.BundleKey;
import Utils.DataStore;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ELF on 05-01-2017.
 *
 */

public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONArray> , Response.ErrorListener {
    private static final String TAG = "LOGIN";
    private static final String LOGIN_URL = "";
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_password_box)
    EditText loginPasswordBox;
    @BindView(R.id.login_email_box)
    EditText loginEmailBox;


    @BindView(R.id.textView25)
    TextView mForgotButton;
    ProgressDialog mDialog = null;
    private String email;
    private AppRequestQueue mRequestQueue;
    private DataStore mStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);




        mDialog = new ProgressDialog(this);
        mDialog.setIndeterminate(true);
        mDialog.setMessage("Logging In. Please Wait");

        mStore = DataStore.getStorageInstance(this);



        if (getIntent() !=  null){
            email  = getIntent().getStringExtra(BundleKey.ARG_EMAIL_ID_TAG);

        }


        if (loginEmailBox !=null){
            Log.d(TAG, "onCreate: ");
            loginEmailBox.setText(email);
            Toast.makeText(this,"Please Login with your Current Password",Toast.LENGTH_SHORT).show();
        }

        //intitlalize Req Que
        mRequestQueue = AppRequestQueue.getInstance(this.getApplicationContext());

        mStore = DataStore.getStorageInstance(getApplicationContext());



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonClicked();
            }
        });

        mForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgotPAssword();
            }
        });

    }


    private void forgotPAssword() {
        final Intent i   = new Intent(this,ForgotPassword.class);
        startActivity(i);
    }

    private void loginButtonClicked() {

        ShowDialog();
        String userName = loginEmailBox.getText().toString();

        String password = loginPasswordBox.getText().toString();

        JSONObject mObject = new JSONObject();
        try {

            mObject.put("username", userName);
            mObject.put("password", password);
        } catch (Exception e) {
            Log.d(TAG, "loginButtonClicked: ");
        }


        JsonArrayRequest mRequest = new JsonArrayRequest(Request.Method.POST, LOGIN_URL, mObject, this, this);
        mRequestQueue.addToRequestQue(mRequest);
    }

    private void ShowDialog() {
        if (mDialog != null && !mDialog.isShowing()){
            mDialog.show();
        }
    }





    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(this,"Please Make Sure you have Data Connection",Toast.LENGTH_SHORT);

        stopDialog();
    }


    private void stopDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void onResponse(JSONArray response) {
        //parse the Response


        Log.d(TAG, "onResponse: "+response.toString());
        JSONObject mObject = null;

        String studentId = null;
        String StudentName = null;
        String classId = null;
        String insId = null;
        String insName = null;
        String groupId = null;


        try {

            mObject = response.getJSONObject(0);
            if (mObject != null) {

                String result = mObject.getString("StatusCode");
                if (result.equals("1000")) {
                    //validation went through , get Student details
                    Log.d(TAG, "onResponse: validation went through");
                    studentId = mObject.getString("StudentId");
                    StudentName = mObject.getString("FirstName");

                    // class id is either 1 or 2
                    classId = mObject.getString("ClassId");
                    groupId = mObject.getString("GroupId");
                    insId = mObject.getString("InstitutionId");
                    insName = mObject.getString("InstitutionName");
                    String boardId = mObject.getString("BoardId");



                    if (studentId != null) {
                        //Save it file
                        mStore.setStudentId(studentId);
                        mStore.setBoardId(boardId);
                        mStore.setInstitutionId(insId);
                        mStore.setUserName(StudentName);
                        mStore.setStudentStandard(classId);
                        mStore.setInstituionName(insName);

                        mStore.setStudentPrefrerredSubject(groupId);
            //       mStore.setEmailId(userName);

                        //set First to false , so as to not show it again
                        mStore.setIsFirstTime(false);

                        stopDialog();
                        //Show Home Activitu
                        final Intent i = new Intent(this, HomeActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    //wrong Details
                    stopDialog();
                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.shake);
                    loginEmailBox.setText("");
                    loginPasswordBox.setText("");

                    loginEmailBox.startAnimation(anim);
                    loginPasswordBox.startAnimation(anim);
                    Toast.makeText(this,"Incorrect Details",Toast.LENGTH_SHORT).show();

                }
            }
            else{
                throw new NullPointerException("Object Cannot e null");
            }

        } catch (JSONException e) {


            Log.d(TAG, "onResponse: exception "+e.getLocalizedMessage());
            stopDialog();

            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
//            FirebaseCrash.log("Error in parsing LOgin Info");
        }

    }

}

