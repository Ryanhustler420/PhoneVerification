package io.raisehand.phoneverification;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.matesnetwork.Cognalys.VerifyMobile;

public class MainActivity extends AppCompatActivity {

    MainActivity context = null;
    Button verifyButton;
    EditText phone_number;
    EditText country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        verifyButton = findViewById(R.id.verifyButton);
        phone_number = findViewById(R.id.phone_number);
        country_code = findViewById(R.id.country_code);

        country_code.setText(VerifyMobile.getCountryCode(getApplicationContext()));

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*starts here*/
                Dexter.withActivity(context)
                        .withPermission(Manifest.permission.READ_PHONE_STATE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera

                                String mobile = country_code.getText().toString()
                                        + phone_number.getText().toString();

                                Intent in = new Intent(MainActivity.this, VerifyMobile.class);
                                in.putExtra("app_id", "");
                                in.putExtra("access_token", "");
                                in.putExtra("mobile", mobile);

                                startActivityForResult(in, VerifyMobile.REQUEST_CODE);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {
                                    // navigate user to app settings
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

                /*ends here*/

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VerifyMobile.REQUEST_CODE) {
            assert data != null;
            String message = data.getStringExtra("message");
            int result = data.getIntExtra("result", 0);
            Log.d("XXX", message);
            Log.d("XXX", "" + result);
        }
    }
}
