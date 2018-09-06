package net.jsaistudios.cpsc.cpsc_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Alec on 9/2/2018.
 */

public class Login extends AppCompatActivity{

    private EditText emailLogin;
    private EditText adminPin;
    private Button loginButton, adminLoginButton;
    private String enteredEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        emailLogin = (EditText) findViewById(R.id.loginEmailEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        adminPin = findViewById(R.id.adminPin);
        adminLoginButton = findViewById(R.id.adminButtonLogin);

        final Boolean[] isAdmin = {false};


        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = emailLogin.getText().toString().toLowerCase();

               Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email);
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       //Email already in dtaabse AND not admin?
                       for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                           Log.i("Admin", "Query : " + userSnapshot.child("isAdmin").getValue());

                           if (userSnapshot.exists() && !(Boolean.valueOf(userSnapshot.child("isAdmin").getValue().toString()))) {
                               firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       if (task.isSuccessful()) {
                                           FirebaseUser user = firebaseAuth.getCurrentUser();

                                           Intent intent = new Intent(Login.this, MainActivity.class);
                                           startActivity(intent);
                                           finish();


                                       } else {
                                           Toast.makeText(Login.this, "Account not found", Toast.LENGTH_LONG).show();

//                                                                            Toast.makeText(Login.this, "Failed to sign in", Toast.LENGTH_LONG).show();

                                       }
                                   }
                               });
                           }
                           else if (dataSnapshot.exists()){
                               Log.d("Admin","must be an admin no??");
                               adminPin.setVisibility(View.VISIBLE);
                               loginButton.setVisibility(View.GONE);
                               adminLoginButton.setVisibility(View.VISIBLE);

                           }
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

       }
   }) ;

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            String password;
            @Override
            public void onClick(View view) {
                if(adminPin.getText() != null) {
                    password = adminPin.getText().toString();
                }
                else{
                    Toast.makeText(Login.this,"Must enter password",Toast.LENGTH_LONG);
                }
                String email = emailLogin.getText().toString().toLowerCase();
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(Login.this, "Account not found", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

    }
}

                /**
                String password = "password";

                if(adminPin.getVisibility() == View.VISIBLE) {
                    if (adminPin.getText().equals(null)){
                        Log.d("ADMIN","Password needed but its empty");
                    }
                    password = adminPin.getText().toString();
                }


                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            //  progressBar.setVisibility(View.GONE);
                                            if (!task.isSuccessful()) {
                                                final ArrayList<String> boardMemberEmails = new ArrayList<>();
                                                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("boardmembers");
                                                Log.d("Admin", dbr.
                                                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Log.d("Admin","Email 1 is: " + dataSnapshot.child("email").getValue().toString());
                                                        boardMemberEmails.add(dataSnapshot.child("email").getValue().toString());

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                                Log.d("Admin", "List of board member emails: " + boardMemberEmails);
**/
                                                //Check if email is Admin, default password didnt work
                                                /**String userID = task.getResult().getUser().getUid();
                                                DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("isAdmin");
                                                userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        //do what you want with the email
                                                        boolean isAdmin = false;
                                                        isAdmin = Boolean.valueOf(dataSnapshot.getValue().toString());
                                                        Log.d("Admin", "Is user an Admin: " + isAdmin);

                                                        if(isAdmin) {
                                                            adminPin.setVisibility(View.VISIBLE);


                                                        }
                                                        else{
                                                            // there was an error
                                                            Toast.makeText(Login.this, "Failed to sign in", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                 **/
                                                /**
                                            }
                                            else {

                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    });
                        }
            });
        }**/




/**
 if (TextUtils.isEmpty(email)) {
 Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
 return;
 }

 if (TextUtils.isEmpty(password)) {
 Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
 return;
 }
 **/

                    //progressBar.setVisibility(View.VISIBLE);


/**
                                        if(isAdmin[0]){
                                            Log.d("Admin", "User is an admin, ur shit works");
                                            adminPin.setVisibility(View.VISIBLE);
                                            //FINISH THIS
                                        }


                                       // task.getResult().getUser().getUid();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
 **/





