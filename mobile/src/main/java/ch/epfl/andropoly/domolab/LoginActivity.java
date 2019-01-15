package ch.epfl.andropoly.domolab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import JsonUtilisties.myJsonReader;

import static JsonUtilisties.myJsonReader.jsonObjFromFileInternal;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    private static final boolean SIGN_IN = true;
    private static final boolean REGISTER = false;
    private static final String TAG = "-----LoginActivity-----";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginRegisterTask mAuthTask = null;


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // keep user credentials between two connections
    private JSONObject JSONCredential = new JSONObject();
    private boolean savedCredentials = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLoginRegistration(SIGN_IN);
                return true;
            }
            return false;
            }
        });

        Log.d(TAG, "look for saved credentials");

        try {
            savedCredentials = true;
            JSONCredential = jsonObjFromFileInternal(LoginActivity.this, "savedCredentials.json");
        } catch (IOException e) {
            savedCredentials = false;
            e.printStackTrace();
        } catch (JSONException e) {
            savedCredentials = false;
            e.printStackTrace();
        }

        if(savedCredentials) {
            Log.d(TAG, "found saved credentials");
            try {
                mEmailView.setText(JSONCredential.get("email").toString());
                mPasswordView.setText(JSONCredential.get("password").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginRegistration(SIGN_IN);
            }
        });

        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginRegistration(REGISTER);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLoginRegistration(boolean signin) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginRegisterTask(email, password, signin);
            mAuthTask.execute((Void) null);

        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private final boolean mSignin;
        private FirebaseUser mUser;
        private boolean mSuccess = false;
        private FirebaseAuth mAuth;
        private FirebaseDatabase database;
        private DatabaseReference profileGetRef;
        private DatabaseReference profileRef;
        private boolean mRunningThread = true;
        private String profileKey;
        private ValueEventListener listener;


        UserLoginRegisterTask(String email, String password, boolean signin) {
            mEmail = email;
            mPassword = password;
            mSignin = signin;
            mUser = null;
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            profileGetRef = database.getReference("profiles");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            if (mSignin == SIGN_IN) {

                signInAccount(mEmail, mPassword);
            }
            else{
                registerAccount(mEmail, mPassword);
                //register to database
            }

            return mSuccess;
        }


        private void registerAccount(String email, String password){
            mRunningThread = true;
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                mUser = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Registration successful.",
                                        Toast.LENGTH_SHORT).show();

                                profileRef = profileGetRef.push();
                                addProfileToFirebaseDB();
                                profileKey = profileRef.getKey();

                                mSuccess = true;
                                mRunningThread = false;
                            } else {
                                // If register fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                                mSuccess = false;
                                mRunningThread = false;
                            }
                        }
                    });

            while(mRunningThread){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void signInAccount(String email, String password) {
            mRunningThread = true;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                mUser = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();

                                listener = profileGetRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        boolean notFoundProfile = true;
                                        Log.d(TAG, "start looking for profiles");
                                        for (final DataSnapshot profile : dataSnapshot.getChildren()) {
                                            String userIdDatabase = profile.child("userID").getValue(String.class);
                                            Log.d(TAG, "found a userID");

                                            if (mUser.getUid().equals(userIdDatabase)) {
                                                Log.d(TAG, "same userID as connected user");
                                                notFoundProfile = false;
                                                profileKey = profile.getKey();
                                                break;
                                            }
                                        }
                                        if (notFoundProfile) {
                                            // What to do if new user (nothing in the database)
                                            Log.d(TAG, "No corresponding profile in the database");
                                            profileRef = profileGetRef.push();
                                            addProfileToFirebaseDB();
                                            profileKey = profileRef.getKey();
                                        }

                                        mRunningThread = false;

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        mRunningThread = false;
                                    }

                                });

                                mSuccess = true;

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                mSuccess = false;
                                mRunningThread = false;
                            }

                        }
                    });
            while(mRunningThread){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            Log.d(TAG, "On post exe");
            if (success) {
                profileGetRef.removeEventListener(listener);
                Intent intentHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);

                try {
                    JSONCredential.put("email", mEmail);
                    JSONCredential.put("password", mPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Save credentials in internal memory:" + JSONCredential);
                myJsonReader.jsonWriteFileInternal(LoginActivity.this, "savedCredentials.json", JSONCredential);

                intentHomeActivity.putExtra("PROFILEKEY", profileKey);
                //intentHomeActivity.putExtra("USERID", userID);
                startActivity(intentHomeActivity);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void addProfileToFirebaseDB() {
            /*final ArrayList<String> emptyListRoom = new ArrayList<>();
            emptyListRoom.add("Room");
            final ArrayList<String> emptyListFav = new ArrayList<>();
            emptyListFav.add("Kitchen");*/
            final JSONObject objRoom = new JSONObject();
            try {
                objRoom.put("Type", "Room");
                objRoom.put("Name", "MyRoom");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONArray roomsArray = new JSONArray();
            final JSONArray favsArray = new JSONArray();
            roomsArray.put(objRoom);
            favsArray.put(objRoom);

            Log.d(TAG, "Rooms array init: " + roomsArray);
            Log.d(TAG, "Favs array init: " + favsArray);

            profileRef.runTransaction( new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    mutableData.child("HomeName").setValue("DefaultName");
                    mutableData.child("userID").setValue(mUser.getUid());
                    //mutableData.child("listOfRooms").setValue(emptyListRoom);
                    //mutableData.child("listOfFav").setValue(emptyListFav);
                    mutableData.child("Rooms").setValue(roomsArray.toString());
                    mutableData.child("Favorites").setValue(favsArray.toString());
                    mutableData.child("MQTT").child("username").setValue("");
                    mutableData.child("MQTT").child("password").setValue("");
                    mutableData.child("MQTT").child("serverURL").setValue("");
                    return Transaction.success(mutableData);
                }
                @Override
                public void onComplete (@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot)
                {
                }
            });
        }

    }
}


