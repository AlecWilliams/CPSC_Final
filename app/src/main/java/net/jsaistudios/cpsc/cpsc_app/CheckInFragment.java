package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CheckInFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {
    private View baseFragmentView, checkinHolder;
    private ImageView qrImage;
    private TextView emailView, titleView, userResult, userEmailText;
    private ImageView clearButton;
    private CheckInFragment context;
    private AutoCompleteTextView memberSearch;
    private View adminPanel, memberPanel, closebutton;
    public Observer closeObserver;
    private QRCodeReaderView qrCodeReaderView;
    private String currentName = "";
    public Activity activity;
    private String currentUid="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.check_in_frag, container, false);
        titleView =  baseFragmentView.findViewById(R.id.title_checkin);
        userEmailText =  baseFragmentView.findViewById(R.id.userResultEmail);
        checkinHolder = baseFragmentView.findViewById(R.id.event_check_in_holder);
        context = this;
        userResult =  baseFragmentView.findViewById(R.id.userResultView);
        closebutton = baseFragmentView.findViewById(R.id.close_button);
        clearButton = baseFragmentView.findViewById(R.id.clear);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUid = "";
                currentName = "";
                memberSearch.setText("");
                userEmailText.setText("");
                userResult.setText("Member Name:");
            }
        });
        baseFragmentView.findViewById(R.id.close_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentName.equals("")) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference myDatabaseRef = db.getReference("checkin").child(currentUid);
                    myDatabaseRef.setValue(currentName);
                }
                createEventCheckin();
                checkinHolder.setVisibility(View.VISIBLE);
            }
        });
        qrCodeReaderView = (QRCodeReaderView) baseFragmentView.findViewById(R.id.qrdecoderview);
        memberSearch = baseFragmentView.findViewById(R.id.member_search);
        qrImage = baseFragmentView.findViewById(R.id.qr_code);
        adminPanel = baseFragmentView.findViewById(R.id.adminCheckIn);
        memberPanel = baseFragmentView.findViewById(R.id.memberCheckIn);
        emailView = baseFragmentView.findViewById(R.id.email);

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                if(closeObserver!=null) {
                    closeObserver.update();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CAMERA}, 1);
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            final ArrayList<String> userList = new ArrayList<>();
            DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("clearance");
            userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue().toString().equals("admin")) {

                            memberPanel.setVisibility(View.GONE);
                            adminPanel.setVisibility(View.VISIBLE);
                            titleView.setText("Search for a member by email.");
                            qrCodeReaderView.setOnQRCodeReadListener(context);
                            qrCodeReaderView.setQRDecodingEnabled(true);
                            qrCodeReaderView.setAutofocusInterval(2000L);
                            qrCodeReaderView.setBackCamera();

                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = db.getReference("users");
                            final List<String> namesAndEmails = new ArrayList<>();
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        userList.add(snapshot.getKey());
                                        namesAndEmails.add(snapshot.child("name").getValue().toString() + " (" +snapshot.child("email").getValue().toString()+")");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, namesAndEmails);
                            memberSearch.setAdapter(adapter);
                            memberSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    getUserInfo(userList.get(i/2));
                                    TextView textView = (TextView) view.findViewById(R.id.text1);
                                    String email = adapterView.getItemAtPosition(i).toString().split("\\(")[1].split("\\)")[0];
                                    getUserIdByEmail(email);
                                    hideSoftKeyboard(getActivity());
                                }
                            });

                        } else {
                            memberPanel.setVisibility(View.VISIBLE);
                            adminPanel.setVisibility(View.GONE);
                            titleView.setText("Show a board member this code or tell them your email to check in to an event.");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null) {
                                QRGEncoder qrgEncoder = new QRGEncoder(user.getUid(), null, QRGContents.Type.TEXT, 1);
                                emailView.setText(user.getEmail());
                                Bitmap bitmap;
                                try {
                                    // Getting QR-Code as Bitmap
                                    bitmap = qrgEncoder.encodeAsBitmap();
                                    // Setting Bitmap to ImageView
                                    qrImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 700, 700, false));
                                } catch (Exception e) {
                                }
                            }

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return baseFragmentView;
    }

    private EventCheckInFragment createEventCheckin() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        EventCheckInFragment fragment = new EventCheckInFragment();
        fragment.activity = activity;
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.event_check_in_holder, fragment, "CheckIn Frag");
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private void getUserIdByEmail(final String email) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users");
        final List<String> namesAndEmails = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("email").getValue().toString().equals(email)) {
                        getUserInfo(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public void onQRCodeRead(final String id, PointF[] points) {
        getUserInfo(id);
    }

    public void getUserInfo(final String id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)) {
                    currentUid = id;
                    currentName = dataSnapshot.child(id).child("name").getValue().toString();
                    userResult.setText("Member Name: "+ currentName);
                    userEmailText.setText("Email: " + dataSnapshot.child(id).child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}
