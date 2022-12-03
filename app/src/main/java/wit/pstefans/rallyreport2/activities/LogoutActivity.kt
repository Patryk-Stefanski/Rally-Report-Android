package wit.pstefans.rallyreport2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import wit.pstefans.rallyreport2.databinding.ActivityLoginBinding

class LogoutActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.i("Logout Activity started...")

        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null){
            firebaseAuth.signOut()


            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}