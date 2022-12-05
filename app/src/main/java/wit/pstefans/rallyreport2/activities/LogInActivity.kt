package wit.pstefans.rallyreport2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber.i
import com.google.firebase.auth.FirebaseAuth
import wit.pstefans.rallyreport2.databinding.ActivityLoginBinding

class LogInActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i("Login Activity started...")


        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginSignupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, PostListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }
    }
}