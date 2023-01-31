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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity  implements View.OnClickListener {
	FirebaseUser user;
	EditText mailAddress,newPassword;

	private void changePassword(String newPassword){
		if(Objects.equals(newPassword, ""))
			Toast.makeText(ChangePassword.this,
				getString(R.string.empty_password)
				,
				Toast.LENGTH_SHORT).show();
		else
			user.updatePassword(newPassword)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						Toast.makeText(ChangePassword.this,
								getString(R.string.password_update_successfull)
								,
								Toast.LENGTH_SHORT).show();
					}

					else {
						Exception exception=task.getException();
						if(exception instanceof FirebaseNetworkException)
							Toast.makeText(ChangePassword.this,
									getString(R.string.network_not_available)
									,
									Toast.LENGTH_SHORT).show();
						else if(exception instanceof FirebaseAuthWeakPasswordException)
							Toast.makeText(ChangePassword.this,
									getString(R.string.password_weak)
									,
									Toast.LENGTH_SHORT).show();
						else if(exception instanceof FirebaseAuthInvalidUserException)
							Toast.makeText(ChangePassword.this,
									getString(R.string.invalid_user)
									,
									Toast.LENGTH_SHORT).show();

						else if(exception instanceof FirebaseAuthRecentLoginRequiredException)
							Toast.makeText(ChangePassword.this,
									getString(R.string.recent_login_required)
									,
									Toast.LENGTH_SHORT).show();
					}

				});

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		mailAddress=findViewById(R.id.mail);
		newPassword=findViewById(R.id.new_password);
		findViewById(R.id.change).setOnClickListener(this);
		user = FirebaseAuth.getInstance().getCurrentUser();

		assert user != null;
		mailAddress.setText(user.getEmail());
		ActionBar actionBar = getSupportActionBar();
// showing the back button in action bar
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onClick(@NonNull View v) {
		if(v.getId()==R.id.change)
			changePassword(newPassword.getText().toString().trim());
		newPassword.getText().clear();
		onRestart();
	}
}