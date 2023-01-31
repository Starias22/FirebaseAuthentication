package com.computech.firebaseauthentication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
	private FirebaseAuth mAuth;
	private EditText email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		findViewById(R.id.receive_email).setOnClickListener(this);
		email=findViewById(R.id.mail);
		mAuth=FirebaseAuth.getInstance();
		ActionBar actionBar = getSupportActionBar();
// showing the back button in action bar
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
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



	@Override
	public void onClick(@NonNull View v) {
if(v.getId()==R.id.receive_email)
	sendResetPassword(email.getText().toString().trim());

	}
	private void sendResetPassword(String email){
		mAuth.setLanguageCode("en");
		FirebaseUser user =mAuth.getCurrentUser();

		assert user != null;
		String url = "https://myfirebaseauthentication.page.link/tobR?uid=" + user.getUid();
		ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
				.setUrl(url)
				// The default for this is populated with the current android package name.
				.setAndroidPackageName(getPackageName(),
						false, null)
				.build();

		if(Objects.equals(email, ""))
			Toast.makeText(ResetPassword.this,
					getString(R.string.empty_email)
					,
					Toast.LENGTH_SHORT).show();
		else
			mAuth.sendPasswordResetEmail(email,actionCodeSettings)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						Toast.makeText(ResetPassword.this,
									getString(R.string.reset_mail_sent)+email
									,
									Toast.LENGTH_SHORT).show();
					}
					else
					{
					Exception exception=task.getException();
					if(exception instanceof FirebaseNetworkException)
						Toast.makeText(ResetPassword.this,
								getString(R.string.network_not_available)
								,
								Toast.LENGTH_SHORT).show();
					else if(exception instanceof FirebaseTooManyRequestsException)
						Toast.makeText(ResetPassword.this,
								getString(R.string.too_many_requests)
								,
								Toast.LENGTH_SHORT).show();
					else if (exception instanceof FirebaseAuthInvalidCredentialsException)

						// Invalid email address
						Toast.makeText(this,getString(R.string.invalid_mail),Toast.LENGTH_SHORT).show();

					else if(exception instanceof FirebaseAuthInvalidUserException)
						Toast.makeText(ResetPassword.this,
								getString(R.string.no_such_account)
								,
								Toast.LENGTH_SHORT).show();
					else

						Toast.makeText(ResetPassword.this,
								getString(R.string.others)
								,
								Toast.LENGTH_SHORT).show();
						assert exception != null;
						exception.printStackTrace();
					}
				});
	}

}