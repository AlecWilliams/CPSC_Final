package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.Dialogs.BasicAlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alec on 9/2/2018.
 */

public class Login extends Fragment {

    private EditText emailLogin, passwordLogin;
    private EditText adminPin;
    private View loginButton, adminLoginButton, signupButton;
    private String enteredEmail;
    private TextView login2, forgotEmail;
    private VideoView vw;

    public Context context;
    public Activity activity;
    private FirebaseAuth firebaseAuth;
    public LoginListener loginListener;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View progressBar;
    View baseFragmentView;
    public FragmentManager fragmentManager;

    public interface LoginListener {
        void loggedIn(boolean isAdmin, boolean newUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.login_page, container, false);

        emailLogin = (EditText) baseFragmentView.findViewById(R.id.loginEmailEditText);
        passwordLogin = (EditText) baseFragmentView.findViewById(R.id.loginPasswordEditText);
        login2 = baseFragmentView.findViewById(R.id.loginButton2);
        forgotEmail = baseFragmentView.findViewById(R.id.forgot_email);
        updateUserList();

        forgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "webmaster@cspsconline.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forgot CPSC Email :(");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear almighty and extremely handsome webmaster,\n" +
                        "\n" +
                        "I don't know what my registered email is, which is ironic considering I'm sending you an email right now.\n" +
                        "\n" +
                        "Love,\n" +
                        "Helpless, lonely soul\n" +
                        "\n" +
                        "P.S. Did I mention that you're extremely handsome?");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }
        });

        vw = baseFragmentView.findViewById(R.id.viewViewBg);
        vw.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/cpscdatabase.appspot.com/o/skiflip1.mp4?alt=media&token=e065ce9f-9e3a-402b-a38e-c23c52ac1fff"));
        vw.start();
        vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vw.start();
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) vw.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        vw.setLayoutParams(params);

        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = baseFragmentView.findViewById(R.id.loginButton);
        signupButton = baseFragmentView.findViewById(R.id.signupButton);
        progressBar = baseFragmentView.findViewById(R.id.progressBar);
        emailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLogin.setBackgroundResource(R.drawable.edit_text_background);
                emailLogin.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray1));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cpsconline.com/join/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = emailLogin.getText().toString().toLowerCase();
               emailLogin.setBackgroundResource(R.drawable.edit_text_background);
               emailLogin.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray1));
               final FirebaseDatabase database = FirebaseDatabase.getInstance();
               final DatabaseReference myRef = database.getReference("users");
               progressBar.setVisibility(View.VISIBLE);
               myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       boolean found =false;
                       for(DataSnapshot snap :dataSnapshot.getChildren()) {
                           if (snap.child("email").getValue().toString().equals(email)) {
                               found=true;
                               if (snap.hasChild("clearance") && snap.child("clearance").getValue().toString().equals("admin")) {
                                   passwordLogin.setVisibility(View.VISIBLE);
                                   passwordLogin.requestFocus();
                                   login2.setVisibility(View.VISIBLE);
                                   progressBar.setVisibility(View.GONE);
                                   final DataSnapshot curSnap = snap;
                                   login2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           final String email = emailLogin.getText().toString().toLowerCase().trim();
                                           if (passwordLogin.getText().toString().equals("ski")) {
                                               progressBar.setVisibility(View.VISIBLE);
                                               FirebaseAuth.getInstance().signInWithEmailAndPassword(email, "skiski")
                                                       .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                               if (task.isSuccessful()) {
                                                                   progressBar.setVisibility(View.GONE);
                                                                   loginListener.loggedIn(false, false);
                                                               } else {
                                                                   progressBar.setVisibility(View.GONE);
                                                                   emailLogin.setBackgroundResource(R.drawable.edit_text_background_border);
                                                                   emailLogin.setBackgroundTintList(null);
                                                                   BasicAlertDialog.create("There was an issue contacting the server.", fragmentManager);
                                                               }
                                                           }
                                                       });

                                           } else {
                                               BasicAlertDialog.create("Incorrect password.", fragmentManager);

                                           }
                                       }
                                   });
                               } else {
                                   FirebaseAuth.getInstance().signInWithEmailAndPassword(email, snap.child("pass").getValue().toString())
                                           .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if (task.isSuccessful()) {
                                                       loginListener.loggedIn(false, false);
                                                       progressBar.setVisibility(View.GONE);
                                                   } else {
                                                       BasicAlertDialog.create("Sorry! Could not sign in with this email.", fragmentManager);
                                                       progressBar.setVisibility(View.GONE);
                                                       emailLogin.setBackgroundResource(R.drawable.edit_text_background_border);
                                                       emailLogin.setBackgroundTintList(null);
                                                   }
                                               }
                                           });
                               }
                           }
                       }
                       if(!found) {
                           DatabaseReference newRef = database.getReference("wpusersmasterSheet");
                           newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dS) {
                                   boolean found = false;
                                   for(DataSnapshot mySnap :dS.getChildren()) {
                                       if (mySnap.child("1").getValue().toString().toLowerCase().equals(email.toLowerCase())) {
                                           found = true;
                                           createUser(email);
                                       }
                                   }
                                   if(!found) {
                                       BasicAlertDialog.create("Sorry! Could not find this email.", fragmentManager);
                                       progressBar.setVisibility(View.GONE);
                                       emailLogin.setBackgroundResource(R.drawable.edit_text_background_border);
                                       emailLogin.setBackgroundTintList(null);
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });

                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError error) {
                       // Failed to read value
                   }
               });
           }
        }) ;
        return baseFragmentView;
    }
    private void createUser(String enteredEmail) {
        try {
            final String myEmail = enteredEmail.toLowerCase();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("users");
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(myEmail, "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(myEmail);
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue("New User");
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("clearance").setValue("member");
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pass").setValue("skiski");
                    loginListener.loggedIn(false, false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {

        }
    }

    private void updateUserList() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference wpRef = database.getReference("wpusersmasterSheet");
        wpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot wpSnap) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot fbSnap) {
                        String em = "";
                        ArrayList<String> listOfWpUsers = new ArrayList<>();
                        Outside:
                        for(DataSnapshot snapshot : wpSnap.getChildren()) {
                            boolean found =false;
                            listOfWpUsers.add(snapshot.child("email").getValue().toString());
                        }
                        for(DataSnapshot fbSnapshot: fbSnap.getChildren()) {
                            if(listOfWpUsers.contains(fbSnapshot.child("email").getValue().toString())) {
                                em = fbSnapshot.child("email").getValue().toString();
                            }
                        }
//                        if(!found) {
//                            found=false;
//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            final DatabaseReference myRef = database.getReference("users").push();
//                            myRef.child("email").setValue(em);
//                            myRef.child("name").setValue("New User");
//                            myRef.child("clearance").setValue("member");
//                            myRef.child("pass").setValue("skiski");
//                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(em, "skiski");
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



