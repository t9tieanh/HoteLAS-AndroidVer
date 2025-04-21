package com.example.hotelas;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityStartBinding;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PrefManager prefManager = new PrefManager(this);
        AuthenticationResponse user = prefManager.getAuthResponse();
        if (user != null) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        binding.continueEmail.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginEmailActivity.class);
            startActivity(intent);
        });


//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso);

//        binding.loginGoogle.setOnClickListener(v -> signInGoogle());
//
//        binding.loginFacebook.setOnClickListener(v -> {
//            Toast.makeText(StartActivity.this, "This feature will be implemented soon.", Toast.LENGTH_SHORT).show();
//        });
    }

    private void signInGoogle() {
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        launcher.launch(signInIntent);
    }

    private final ActivityResultCallback<androidx.activity.result.ActivityResult> launcher =
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleResults(task);
                }
            };

    private void handleResults(Task<GoogleSignInAccount> task) {
        if (task.isSuccessful()) {
            GoogleSignInAccount account = task.getResult();
            if (account != null) {
                updateUI(account);
            }
        } else {
            Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        if (idToken != null) {
            FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            intent.putExtra("email", account.getEmail());
                            intent.putExtra("name", account.getDisplayName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(StartActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}