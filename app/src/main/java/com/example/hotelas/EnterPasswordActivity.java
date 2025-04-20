package com.example.hotelas;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotelas.databinding.ActivityEnterPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class EnterPasswordActivity extends AppCompatActivity {

    private ActivityEnterPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityEnterPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get email from intent
        String email = getIntent().getStringExtra("email");

        // Set the information text with the email
        if (email != null) {
            String informationText = "Please enter your password for " + email;
            binding.informationEmailText.setText(informationText);
        }

        // Set click listener for the login button
        binding.loginButton.setOnClickListener(v -> {
            String password = binding.passwordEditText.getText().toString();

            // Call the login method
            if (email != null) {
                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
    }
}
