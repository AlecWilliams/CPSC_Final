package net.jsaistudios.cpsc.cpsc_app;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CheckInFragment extends Fragment {
    private View baseFragmentView;
    private ImageView qrImage;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.check_in_frag, container, false);
        QRGEncoder qrgEncoder = new QRGEncoder("1", null, QRGContents.Type.TEXT, 1);
        qrImage = baseFragmentView.findViewById(R.id.qr_code);
        Bitmap bitmap;
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));
        } catch (Exception e) {
        }
        return baseFragmentView;
    }
}
