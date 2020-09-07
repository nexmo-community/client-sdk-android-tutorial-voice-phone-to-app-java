package com.igorwojda.client_sdk_android_tutorial_voice_phone_to_app_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.nexmo.client.NexmoCall;
import com.nexmo.client.NexmoClient;

public class MainActivity extends AppCompatActivity {

    TextView connectionStatusTextView;
    Button answerCallButton;
    Button rejectCallButton;
    Button endCallButton;

    NexmoCall call;
    Boolean incomingCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionStatusTextView = findViewById(R.id.connectionStatus);
        answerCallButton = findViewById(R.id.answerCallButton);
        rejectCallButton = findViewById(R.id.rejectCallButton);
        endCallButton = findViewById(R.id.endCallButton);

        // Init Nexmo client
        NexmoClient client = new NexmoClient.Builder().build(this);

        // set Connection status listener
        client.setConnectionListener((connectionStatus, connectionStatusReason) -> runOnUiThread(() -> connectionStatusTextView.setText(connectionStatus.toString())));

        // login client
        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTk0NzM3ODMsImp0aSI6IjJlYTlmMWIwLWYwZjMtMTFlYS05Mjg2LTRkNDkyMjg5ZmIyOSIsImV4cCI6MTU5OTQ5NTM4MywiYWNsIjp7InBhdGhzIjp7Ii8qL3VzZXJzLyoqIjp7fSwiLyovY29udmVyc2F0aW9ucy8qKiI6e30sIi8qL3Nlc3Npb25zLyoqIjp7fSwiLyovZGV2aWNlcy8qKiI6e30sIi8qL2ltYWdlLyoqIjp7fSwiLyovbWVkaWEvKioiOnt9LCIvKi9hcHBsaWNhdGlvbnMvKioiOnt9LCIvKi9wdXNoLyoqIjp7fSwiLyova25vY2tpbmcvKioiOnt9fX0sInN1YiI6IkFsaWNlIiwiYXBwbGljYXRpb25faWQiOiJjNWJmNmNmZi02N2ExLTQ4ZmQtYjM3Yi0zZWM5N2Q0MjY4NjkifQ.d_aqCFuBb9eYni1kEXDk9Hgwuf-TlSmQddCxLrr6DbyJ5G3mus9WW1QBeICXsvIH1A8bgSIzROUE0hXOQceChvap00xEmtpljvEGtG7y-89sgPfTHWO9ZjcTfQkfaSnhcS57FpmhvmRgszQZpW34Xi1rMqzEwhwpxATpnaqTAlUJ9R6kebukkjJeGLEkDXGs6BysamoWba-3Ve2Kw2zMxLb56q_g_6IBAoRuc7Yyw-67wnZ2ShztKFOvo7ZfA2AtnqfNGMPzNLJd6_BkehR95w7K0C7KCxJjYutAr32pxu3jNBqcxAjO7TYEMkhuDFPsWpudXZa6AOEypqzqMRY1oQ");

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