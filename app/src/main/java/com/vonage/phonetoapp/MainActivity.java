package com.vonage.phonetoapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.nexmo.client.NexmoCall;
import com.nexmo.client.NexmoClient;

@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {

    private TextView connectionStatusTextView;
    private Button answerCallButton;
    private Button rejectCallButton;
    private Button endCallButton;

    private NexmoCall call;
    private Boolean incomingCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve views
        connectionStatusTextView = findViewById(R.id.connectionStatusTextView);
        answerCallButton = findViewById(R.id.answerCallButton);
        rejectCallButton = findViewById(R.id.rejectCallButton);
        endCallButton = findViewById(R.id.endCallButton);

        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 123);
        }

        // Init Nexmo client
        NexmoClient client = new NexmoClient.Builder().build(this);

        // set Connection status listener
        client.setConnectionListener((connectionStatus, connectionStatusReason) -> runOnUiThread(() -> connectionStatusTextView.setText(connectionStatus.toString())));

        // login client
        client.login("ALICE_TOKEN");

        // listen for incoming calls
        client.addIncomingCallListener(it -> {
            call = it;

            incomingCall = true;
            updateUI();
        });

        // add buttons listeners
        answerCallButton.setOnClickListener(view -> {
            incomingCall = false;
            updateUI();
            call.answer(null);

        });

        rejectCallButton.setOnClickListener(view -> {
            incomingCall = false;
            call = null;
            updateUI();

            call.hangup(null);
        });

        endCallButton.setOnClickListener(view -> {
            call.hangup(null);

            call = null;
            updateUI();
        });
    }

    private void updateUI() {
        answerCallButton.setVisibility(View.GONE);
        rejectCallButton.setVisibility(View.GONE);
        endCallButton.setVisibility(View.GONE);

        if (incomingCall) {
            answerCallButton.setVisibility(View.VISIBLE);
            rejectCallButton.setVisibility(View.VISIBLE);
        } else if (call != null) {
            endCallButton.setVisibility(View.VISIBLE);
        }
    }
}