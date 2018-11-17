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
import java.util.concurrent.TimeUnit;

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
    private boolean paused=false;

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
        loginButton = baseFragmentView.findViewById(R.id.loginButton);
        signupButton = baseFragmentView.findViewById(R.id.signupButton);
        progressBar = baseFragmentView.findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

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
        passwordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(passwordLogin.getText().toString().equals("ski")) {
                    loginListener.loggedIn(true, false);
                }
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
               FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin.getText().toString(), "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       final FirebaseDatabase database = FirebaseDatabase.getInstance();
                       final DatabaseReference fbRef = database.getReference("users");
                       if(task.isSuccessful()) {
                           fbRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if(dataSnapshot.child("clearance").getValue().toString().equals("admin")) {
                                       passwordLogin.setVisibility(View.VISIBLE);
                                       login2.setVisibility(View.VISIBLE);
                                   } else {
                                       loginListener.loggedIn(true, false);
                                   }
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                       } else {
                           fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                       if(ds.child("email").getValue().toString().equals(emailLogin.getText().toString())) {
                                           final DataSnapshot userDS = ds;
                                           FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailLogin.getText().toString(), "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if(task.isSuccessful()) {
                                                       if(userDS.child("clearance").getValue().toString().equals("admin")) {
                                                           passwordLogin.setVisibility(View.VISIBLE);
                                                           login2.setVisibility(View.VISIBLE);
                                                       } else {
                                                           loginListener.loggedIn(true, false);
                                                       }
                                                   }
                                               }
                                           });
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                       }
                   }
               });
           }
        });
        return baseFragmentView;
    }
}



