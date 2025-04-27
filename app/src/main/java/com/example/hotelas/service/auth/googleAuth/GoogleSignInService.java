package com.example.hotelas.service.auth.googleAuth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class GoogleSignInService {

    private static final int RC_SIGN_IN = 1001; // Request code tùy chỉnh
    private final GoogleSignInClient googleSignInClient;
    private final Activity activity;

    public GoogleSignInService(Activity activity) {
        this.activity = activity;
        this.googleSignInClient = buildGoogleSignInClient(activity);
    }

    private GoogleSignInClient buildGoogleSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("Điền client id vào đây nha anh sang")
                .build();

        return GoogleSignIn.getClient(context, gso);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        googleSignInClient.signOut();
    }

    public void revokeAccess() {
        googleSignInClient.revokeAccess();
    }

    public boolean isUserSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    public String getLastSignedInUserEmail() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return (account != null) ? account.getEmail() : null;
    }

    public String getLastServerAuthCode() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return (account != null) ? account.getServerAuthCode() : null;
    }

    public void handleSignInResult(@Nullable Intent data, SignInCallback callback) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                String authCode = account.getServerAuthCode();
                callback.onSuccess(authCode);
            } else {
                callback.onFailure("Account is null");
            }
        } catch (ApiException e) {
            callback.onFailure("SignIn failed: " + e.getStatusCode());
        }
    }

    public int getRequestCode() {
        return RC_SIGN_IN;
    }

    public interface SignInCallback {
        void onSuccess(String serverAuthCode);

        void onFailure(String errorMessage);
    }
}
