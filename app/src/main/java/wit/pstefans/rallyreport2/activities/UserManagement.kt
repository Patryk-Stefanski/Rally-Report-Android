package wit.pstefans.rallyreport2.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import timber.log.Timber.i
import com.google.firebase.auth.FirebaseAuth
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityUserManagementBinding

class UserManagement : AppCompatActivity() {
    private lateinit var binding: ActivityUserManagementBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        i("User Management Activity started...")


        firebaseAuth = FirebaseAuth.getInstance()

        binding.changePasswordButton.setOnClickListener {
            val pass = binding.currentPasswordInput.text
            val newPass = binding.newPasswordInput.text
            val newPassRepeat = binding.newPasswordInput2.text

            i(" pass = " + pass + " pass2 = " + newPass + " pass3 = " + newPassRepeat)

            val credential = EmailAuthProvider.getCredential(
                firebaseAuth.currentUser!!.email.toString(), pass.toString()
            )

            if (pass.isNotEmpty() && newPass.isNotEmpty() && newPassRepeat.isNotEmpty() && (newPass.toString() == newPassRepeat.toString())) {
                i("Got here")
                firebaseAuth.currentUser!!.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser!!.updatePassword(newPass.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        i("User password updated.")
                                        Toast.makeText(
                                            this, "Password Has Been Updated", Toast.LENGTH_SHORT
                                        ).show()
                                        firebaseAuth.signOut()
                                        val intent = Intent(this, LogInActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                        } else {
                            i("Failed to authenticate")
                            Toast.makeText(this, "Failed to Authenticate User", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


            }
        }

        binding.deleteUserButton.setOnClickListener {
            val pass = binding.deleteUserPassword.text

            val credential = EmailAuthProvider.getCredential(
                firebaseAuth.currentUser!!.email.toString(), pass.toString()
            )

            if (pass.isNotEmpty()) {
                firebaseAuth.currentUser!!.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser!!.delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    i("User has been Deleted")
                                    Toast.makeText(
                                        this, "User has been deleted", Toast.LENGTH_SHORT
                                    ).show()

                                    firebaseAuth.signOut()
                                    val intent = Intent(this, LogInActivity::class.java)
                                    startActivity(intent)

                                    //to-do add post cleanup on user deletion
                                }
                            }
                        } else {
                            Toast.makeText(this, "Failed to Authenticate User", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_managment_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.return_home -> {
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}