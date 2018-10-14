package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CheckInFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {
    private View baseFragmentView;
    private ImageView qrImage;
    private TextView emailView, titleView, userResult;
    private CheckInFragment context;
    private View adminPanel, memberPanel;
    private QRCodeReaderView qrCodeReaderView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.check_in_frag, container, false);
        titleView =  baseFragmentView.findViewById(R.id.title_checkin);
        context = this;
        userResult =  baseFragmentView.findViewById(R.id.userResultView);
        qrCodeReaderView = (QRCodeReaderView) baseFragmentView.findViewById(R.id.qrdecoderview);
        qrImage = baseFragmentView.findViewById(R.id.qr_code);
        adminPanel = baseFragmentView.findViewById(R.id.adminCheckIn);
        memberPanel = baseFragmentView.findViewById(R.id.memberCheckIn);
        emailView = baseFragmentView.findViewById(R.id.email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("clearance");
            userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue().toString().equals("admin")) {
                            memberPanel.setVisibility(View.GONE);
                            adminPanel.setVisibility(View.VISIBLE);
                            titleView.setText("Scan a member's qr code below.");
                            qrCodeReaderView.setOnQRCodeReadListener(context);

                            // Use this function to enable/disable decoding
                            qrCodeReaderView.setQRDecodingEnabled(true);

                            // Use this function to change the autofocus interval (default is 5 secs)
                            qrCodeReaderView.setAutofocusInterval(2000L);

                            // Use this function to enable/disable Torch
                            qrCodeReaderView.setTorchEnabled(true);

                            // Use this function to set front camera preview
                            qrCodeReaderView.setFrontCamera();

                            // Use this function to set back camera preview
                            qrCodeReaderView.setBackCamera();
                        } else {
                            memberPanel.setVisibility(View.VISIBLE);
                            adminPanel.setVisibility(View.GONE);
                            titleView.setText("Show a board member this code or tell them your email to check in to an event.");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null) {
                                QRGEncoder qrgEncoder = new QRGEncoder("1", null, QRGContents.Type.TEXT, 1);
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
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        userResult.setText(text);
    }
}
