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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SetName extends AppCompatActivity implements View.OnClickListener {

FirebaseUser user;
EditText oldEdit, newEdit;
	UserProfileChangeRequest profileUpdates;
	private  void setName(String newName){

		 profileUpdates = new UserProfileChangeRequest.Builder()
				.setDisplayName(newName)

				.build();


		user.updateProfile(profileUpdates)
				.addOnCompleteListener(task -> {
					if(task.isSuccessful())
					Toast.makeText(	SetName.this,
							getString(R.string.name_updated)
							,
							Toast.LENGTH_SHORT).show();
					else {
						Exception exception=task.getException();
						if(exception instanceof FirebaseNetworkException)
							Toast.makeText(SetName.this,
									getString(R.string.network_not_available)
									,
									Toast.LENGTH_SHORT).show();

						else if(exception instanceof FirebaseAuthInvalidUserException)
							Toast.makeText(SetName.this,
									getString(R.string.invalid_user)
									,
									Toast.LENGTH_SHORT).show();

					}
				});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_name);
		oldEdit=findViewById(R.id.old_name);
		newEdit=findViewById(R.id.new_name);
		findViewById(R.id.change).setOnClickListener(this);
		user = FirebaseAuth.getInstance().getCurrentUser();

		assert user != null;
		oldEdit.setText(user.getDisplayName());
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
	public void onClick(View v) {

		{
			setName(newEdit.getText().toString().trim());
			newEdit.getText().clear();

			oldEdit.setText(profileUpdates.getDisplayName());
			onRestart();
		}

	}
}