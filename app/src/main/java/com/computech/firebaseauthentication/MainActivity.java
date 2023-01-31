package com.computech.firebaseauthentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private FirebaseAuth mAuth;
	Uri photoUrl;
	private EditText email,password;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 actionBar = getSupportActionBar();
// showing the back button in action bar
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);


		findViewById(R.id.login).setOnClickListener(this);
				findViewById(R.id.create_account).setOnClickListener(this);
		findViewById(R.id.forgotten_password).setOnClickListener(this);

		email=findViewById(R.id.mail);
		password=findViewById(R.id.password);
		// Initialize Firebase Auth
		mAuth = FirebaseAuth.getInstance();



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId())
		{
		case  R.id.setting:
			startActivity(new Intent(this,Settings.class));
			break;
			case android.R.id.home:
				onBackPressed();
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void login(@NonNull String email, @NonNull String password){

	if(email.equals(""))

		Toast.makeText(MainActivity.this,
				getString(R.string.empty_email)
				,
				Toast.LENGTH_SHORT).show();
	else if(password.equals(""))

		Toast.makeText(MainActivity.this,
				getString(R.string.empty_password)
				,
				Toast.LENGTH_SHORT).show();


 else
	 mAuth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(this, task -> {


				 if (task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information

               FirebaseUser user=mAuth.getCurrentUser();

					 Toast.makeText(MainActivity.this,
								 getString(R.string.successful)
								 ,
								 Toast.LENGTH_SHORT).show();
					 /*assert user != null;
					 photoUrl = user.getPhotoUrl();
					 try {
						 InputStream inputStream = getContentResolver().
								 openInputStream(photoUrl);
						 Drawable drawable = Drawable.createFromStream
								 (inputStream, photoUrl.toString());
						 actionBar.setLogo(drawable);
					 } catch (FileNotFoundException e) {
						 e.printStackTrace();
					 }
*/

				 } else {
					// If sign in fails, display a message to the user.

					Exception exception = task.getException();
					if (exception instanceof FirebaseAuthInvalidCredentialsException) {


						Toast.makeText(MainActivity.this,getString(R.string.incorrect_email_or_password),
								Toast.LENGTH_SHORT).show();

					} else if (exception instanceof FirebaseAuthInvalidUserException) {
						Toast.makeText(MainActivity.this,getString
										(R.string.no_such_account)
								,
								Toast.LENGTH_SHORT).show();

					}
					else if(exception instanceof FirebaseNetworkException)
						Toast.makeText(MainActivity.this,
								getString(R.string.network_not_available)
								,
								Toast.LENGTH_SHORT).show();
					else {

						/*We have blocked all requests from this device due to unusual activity.
						 Try again later. [ Access to this account has been
						  temporarily disabled due to many failed login attempts.
						   You can immediately restore it by resetting your password or you
						   can try again later. ]
						 */
						assert exception != null;

						//exception.printStackTrace();
						Toast.makeText(MainActivity.this,exception.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

				}
			});

}


	private void createAccount(@NonNull String email, @NonNull String password)
	{

		if(email.equals(""))

			Toast.makeText(MainActivity.this,
					getString(R.string.empty_email)
					,
					Toast.LENGTH_SHORT).show();
		else if(password.equals(""))

			Toast.makeText(MainActivity.this,
					getString(R.string.empty_password)
					,
					Toast.LENGTH_SHORT).show();
		else
			mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						// Sign in success, update UI with the signed-in user's information

						FirebaseUser user = mAuth.getCurrentUser();
						assert user != null;
						user.sendEmailVerification();
						//R.string.account_creation_message
						Toast.makeText(this,"Account created" ,Toast.LENGTH_SHORT).show();
						
						//updateUI(user);
					} else {
						try {
							throw Objects.requireNonNull(task.getException());


						}
						catch ( FirebaseNetworkException e) {
							Toast.makeText(MainActivity.this,
									getString(R.string.network_not_available)
									,
									Toast.LENGTH_SHORT).show();
						}
						catch (FirebaseAuthUserCollisionException e) {


							// Email address already in use
							Toast.makeText(this,getString(R.string.email_used),
									Toast.LENGTH_SHORT).show();
							Toast.makeText(this,getString(R.string.click_on_login),
									Toast.LENGTH_SHORT).show();

						}
						catch (FirebaseAuthWeakPasswordException e) {
							// Password is too weak
							Toast.makeText(this,getString(R.string.password_weak) ,Toast.LENGTH_SHORT).show();
						}
						 catch (FirebaseAuthInvalidCredentialsException e) {
							// Invalid email address
							Toast.makeText(this,getString(R.string.invalid_mail),Toast.LENGTH_SHORT).show();
						}


						catch (Exception e) {
							e.printStackTrace();
							// Handle other exceptions, such as network error, etc.
							Toast.makeText(this,getString(R.string.others),
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}
	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in (non-null) and update UI accordingly.
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if(currentUser != null){
			currentUser.reload();
		}
	}

	@Override
	public void onClick(@NonNull View v) {

if(v.getId()==R.id.create_account)
	createAccount(email.getText().toString().trim(),
			password.getText().toString().trim());
else  if(v.getId()==R.id.login)
	login(email.getText().toString().trim(),
			password.getText().toString().trim());
else if(v.getId()==R.id.forgotten_password)
	startActivity(new Intent(this,ResetPassword.class));



	}

}