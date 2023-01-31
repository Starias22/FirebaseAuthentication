package com.computech.firebaseauthentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
// showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.delete_account).setOnClickListener(this);
        findViewById(R.id.add_a_name).setOnClickListener(this);
        findViewById(R.id.add_a_picture).setOnClickListener(this);
        findViewById(R.id.change_password).setOnClickListener(this);
        findViewById(R.id.change_email).setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.removeItem(R.id.setting);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }
    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        selectedImageUri = data.getData();
                      //  Bitmap selectedImageBitmap;
                       /* try {
                             MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);

                       */


                    }
                }
            });
    private void deleteAccount(){


    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(getString(R.string.exit_confirmation_message))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
            .setPositiveButton(getString(R.string.yes), (dialog, which) ->
                    user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Settings.this,
                                    getString(R.string.delete_successfull)
                                    ,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Exception exception=task.getException();

                            if(exception instanceof FirebaseNetworkException)
                                Toast.makeText(Settings.this,
                                        getString(R.string.network_not_available)
                                        ,
                                        Toast.LENGTH_SHORT).show();
                            else if (exception instanceof
                                    FirebaseAuthRecentLoginRequiredException)

                                Toast.makeText(Settings.this,
                                                getString(R.string.recent_login_required),Toast.LENGTH_SHORT).
                                        show();
                                //the current user's account has been disabled, deleted, or
                                // its credentials are no longer valid

                            else  if(exception instanceof FirebaseAuthInvalidUserException)

                                Toast.makeText(Settings.this,
                                        getString(R.string.invalid_mail),Toast.LENGTH_SHORT).show();


                        }

                    }));
    AlertDialog alert = builder.create();
    alert.setTitle(getString(R.string.exit_confirmation));
    alert.show();
}


private void setPicture(){
        imageChooser();
    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(String.valueOf(selectedImageUri)))
            .build();

    user.updateProfile(profileUpdates)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(Settings.this,
                                getString(R.string.picture_updated), Toast.LENGTH_SHORT).
                        show();
                else if (task.getException() instanceof  FirebaseAuthInvalidUserException) {
                    Toast.makeText(Settings.this,
                                    getString(R.string.invalid_user), Toast.LENGTH_SHORT).
                            show();

                }
            });

}

    @Override
    public void onClick(@NonNull View v) {



        if(user==null)
            Toast.makeText(Settings.this,
                            getString(R.string.unknown_account), Toast.LENGTH_SHORT).
                    show();


     else   if(v.getId()==R.id.delete_account)
            deleteAccount();
        else  if(v.getId()==R.id.add_a_name)
            startActivity(new Intent(this,SetName.class));
        else  if(v.getId()==R.id.add_a_picture)
            setPicture();
        else  if(v.getId()==R.id.change_email)
            startActivity(new Intent(this,ChangeEmail.class));
        else  if(v.getId()==R.id.change_password)
            startActivity(new Intent(this,ChangePassword.class));
    }

}