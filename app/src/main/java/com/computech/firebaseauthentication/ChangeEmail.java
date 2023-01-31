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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity implements View.OnClickListener {
	EditText mail,newMail;
	FirebaseUser user;
	private void changeEmail(@NonNull String email){

		if(email.equals(""))

			Toast.makeText(ChangeEmail.this,
					getString(R.string.empty_email)
					,
					Toast.LENGTH_SHORT).show();
		else user.updateEmail(email)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						mail.setText(user.getEmail());
                         user.sendEmailVerification();
						 Toast.makeText(ChangeEmail.this,
								getString(R.string.email_update_successfull)
								,
								Toast.LENGTH_SHORT).show();
					}
					else {
						Exception exception=task.getException();
						if(exception instanceof FirebaseNetworkException)
							Toast.makeText(ChangeEmail.this,
									getString(R.string.network_not_available)
									,
									Toast.LENGTH_SHORT).show();
						else if(exception instanceof FirebaseAuthInvalidCredentialsException)
							Toast.makeText(ChangeEmail.this,
									getString(R.string.invalid_mail)
									,
									Toast.LENGTH_SHORT).show();
						else if(exception instanceof FirebaseAuthUserCollisionException)
							Toast.makeText(ChangeEmail.this,
									getString(R.string.email_used)
									,
									Toast.LENGTH_SHORT).show();

						else if(exception instanceof FirebaseAuthRecentLoginRequiredException)
							Toast.makeText(ChangeEmail.this,
									getString(R.string.recent_login_required)
									,
									Toast.LENGTH_SHORT).show();
					}

				});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_email);
		findViewById(R.id.change).setOnClickListener(this);
		mail=findViewById(R.id.old_mail);
		user = FirebaseAuth.getInstance().getCurrentUser();
		//if(user==null)
		assert user != null;
		mail.setText(user.getEmail());
		newMail=findViewById(R.id.new_email);
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
		if(v.getId()==R.id.change) {
			changeEmail(newMail.getText().toString().trim());
			newMail.getText().clear();
			onRestart();
		}

	}
}