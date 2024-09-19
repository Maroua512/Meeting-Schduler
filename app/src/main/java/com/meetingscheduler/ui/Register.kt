import com.meetingscheduler.ViewModels.RegisterViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.meetingscheduler.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val confirmPassword = binding.ConfirmPassword.editText?.text.toString()

            // Validation des champs
            if (validateFields(email, password, confirmPassword)) {
                viewModel.checkIfUserExists(email)
            }
        }

        observeViewModel()
    }

    private fun validateFields(email: String, password: String, confirmPassword: String): Boolean {
        // Ajouter la logique de validation ici (similaire à l'ancien code)
        return true
    }

    private fun observeViewModel() {
        // Observer les données du ViewModel
        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.userExists.observe(this, Observer { exists ->
            if (exists) {
                val email = binding.email.editText?.text.toString()
                val password = binding.password.editText?.text.toString()
                viewModel.createUser(email, password)
            } else {
                Toast.makeText(this, "L'utilisateur n'existe pas dans la base de données.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.emailVerified.observe(this, Observer { isVerified ->
            if (isVerified) {
                Toast.makeText(this, "Email vérifié avec succès", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.verificationTimeout.observe(this, Observer { timeout ->
            if (timeout) {
                Toast.makeText(this, "Échec de la vérification, délai dépassé.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
