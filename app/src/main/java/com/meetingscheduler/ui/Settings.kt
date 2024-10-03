package com.meetingscheduler.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.meetingscheduler.R
import com.meetingscheduler.Utils.ConfirmPasswordValidator
import com.meetingscheduler.Utils.PasswordValidation
import com.meetingscheduler.Utils.ValidateService
import com.meetingscheduler.app.utils.Utils
import java.io.ByteArrayOutputStream
import java.util.Locale

class Settings : Fragment() {
    //Firebase instances for authentication and  database
    val db = Firebase.firestore
    val auth = Firebase.auth
    val curentUser = auth.currentUser

    //UI components
    lateinit var btnlogout: TextView
    lateinit var btnEditPhoto: ImageView
    lateinit var btnEditPassword: TextView
    lateinit var btnNotification: SwitchMaterial
    lateinit var btnChangeLaunguage: TextView
    lateinit var btnDeleteAccount: TextView
    lateinit var imageProfile: ShapeableImageView
    lateinit var username: TextView

    // Dialog components
    private lateinit var oldPassword: TextInputLayout
    private lateinit var newPassword: TextInputLayout
    private lateinit var confirmPassword: TextInputLayout
    private lateinit var confirmBtn: AppCompatButton
    private lateinit var cancelBtn: AppCompatButton

    //Variables for handling image  selection and camera
    private var chosenImageUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 101
    private val PERMISSION_REQUEST_CODE = 102
    private var ischanged = false
    private lateinit var progressDialog: AlertDialog
    private var onImageSelectedCallback: (() -> Unit)? = null

    //Activity  result launcher for  selecting an image  from the gallery
    var imageChoisit = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri.let {
            chosenImageUri = it
            imageProfile.let { imageView ->
                Glide.with(requireContext()).load(uri).placeholder(R.drawable.profil_vide)
                    .into(imageView)
                ischanged = true
                onImageSelectedCallback!!.invoke()//Call the callback when an image is selected
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize UI components
        initComponents(view)

        // Set up click listener for the editing  profile photo
        btnEditPhoto.setOnClickListener {
            showDialogPicture {
                if (chosenImageUri != null) {
                    Log.d("Settings", "Image choisie: $chosenImageUri")
                    progressDialog = Utils.showProgressBar(requireActivity(), "Chargement en cours")
                    //
                    uploadImageToFirebase(curentUser!!.uid)
                } else {
                    Log.d("Settings", "No image selected")
                }
            }
        }

        // Set up click listener for the logout button
        btnNotification.setOnClickListener {

        }
        btnEditPassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Set up click listener for the logout button
        btnDeleteAccount.setOnClickListener {
            showDeleteDialog()
        }
        // Set up click listener for the logout button
        btnChangeLaunguage.setOnClickListener {
            showDialogChangeLanguage()
        }

        // Set up click listener for the logout button
        btnlogout.setOnClickListener {
            showDialogLogout()
        }
        return view
    }

    private fun showChangePasswordDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.update_password)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        initDialog(dialog)
        confirmBtn.setOnClickListener {
            if (VerifierChamps(oldPassword, newPassword, confirmPassword)) {
                progressDialog = Utils.showProgressBar(requireActivity(), "Changement en cours")
                changePassword(
                    oldPassword.editText?.text.toString(),
                    newPassword.editText?.text.toString()
                )
            }
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun changePassword(oldPassword: String, newPassword: String) {

        val credentials =
            EmailAuthProvider.getCredential(curentUser!!.email.toString(), oldPassword)
        curentUser.reauthenticate(credentials).addOnSuccessListener {
            curentUser.updatePassword(newPassword).addOnSuccessListener {
                Utils.hideProgressBar(progressDialog)
                Toast.makeText(
                    requireContext(),
                    "Mot de passe modifié avec succès",
                    Toast.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Erreur lors de modification du mot de passe",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "l'anncienne mot de passe est inccorect",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setLanguage(selectedLocale: String) {
        val newLocale = Locale(selectedLocale)
        resources.configuration.setLocale(newLocale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        val defaultLocale =
            Locale.getDefault() // si on met juste recreate sans tous ca va bloker( boucle infifni)
        /*if (defaultLocale.language != selectedLocale){
            Locale.setDefault(newLocale)
            PreferenceManager.setSelectedLanguage(requireActivity(), selectedLocale)
            (requireActivity().application as MyApp).setAppLocale(selectedLocale)
            (activity as? LanguageChangeListener)?.onLanguageChanged()
            Toast.makeText(context, "Change language to $selectedLocale", Toast.LENGTH_SHORT).show() // Enregistrer la langue sélectionnée dans les préférences partagées
        }*/
    }

    private fun showDialogChangeLanguage() {
        val dialogBinding = layoutInflater.inflate(R.layout.message_langage, null)

        Dialog(requireContext()).apply {
            setContentView(dialogBinding)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            val btnCancel = dialogBinding.findViewById<AppCompatButton>(R.id.btnCancel)
            val btnOk = dialogBinding.findViewById<AppCompatButton>(R.id.btnOk)
            val langRadioGroup = dialogBinding.findViewById<RadioGroup>(R.id.langRadioGroup)

            btnOk.setOnClickListener {
                val radio =
                    dialogBinding.findViewById<RadioButton>(langRadioGroup.checkedRadioButtonId)
                var langResult = radio.text.toString()
                when (langResult) {
                    "English" -> setLanguage("en")
                    "Français" -> setLanguage("fr")
                    "العربية" -> setLanguage("ar")
                }
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    /**
     * Show a dialog to delete account
     */

    private fun showDeleteDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_delete_account)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        // Initialize buttons for the cancel and confirm options
        val cancel = dialog.findViewById<LinearLayout>(R.id.btncancel)
        val delete = dialog.findViewById<LinearLayout>(R.id.btnDelete)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        delete.setOnClickListener {
            auth.currentUser!!.delete().addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Votre compte a ete supprimer avec success",
                    Toast.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Erreur lors de suppression de votre compte",
                    Toast.LENGTH_LONG
                ).show()
            }
            dialog.dismiss()

        }
        dialog.show()
    }

    /**
     * Show  a  dialog  to select a picture,either from the camera or  the gallery
     * @param onImageSelected A callback function trigger when an image is  selected
     */
    private fun showDialogPicture(onImageSelected: () -> Unit) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.activity_message_profile_photo)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        //Asign the callback function
        onImageSelectedCallback = onImageSelected

        // Initialize buttons for the camera and gallery options
        val camera = dialog.findViewById<LinearLayout>(R.id.camera)
        val gallery = dialog.findViewById<LinearLayout>(R.id.picture)

        //Action when the camera option is clicked
        camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                //Launch the camera to take a picture
                disptachTakenPicture()
            }
            dialog.dismiss()
        }
        //Action when the gallery option is clicked
        gallery.setOnClickListener {
            imageChoisit.launch("image/*")// Open galley  to select an image
            dialog.dismiss()
        }
        dialog.show() // Show  the dialog
    }

    /**
     * Dispatches  an  intent  to capture  an image  using  the camera
     */
    private fun disptachTakenPicture() {
        //Capturer l'image avec la camera
        val takenPictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //Lancer l'application de la camera et attendre une resultat
        startActivityForResult(takenPictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * Handle the result  of an activity(camera  or  gallery) .
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageProfile.setImageBitmap(imageBitmap) // Display the captured image
            ischanged = true
            chosenImageUri = data.data  // Get  the  Uri for uploading

        }
    }

    /**
     * Show  a  logout confirmation dialog .
     */
    private fun showDialogLogout() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.activity_message_logout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

        // Buttons for cancelling or confirming logout
        val cancelButton = dialog.findViewById<AppCompatButton>(R.id.btnNo)
        val confirmButton = dialog.findViewById<AppCompatButton>(R.id.btnYes)
        // Action for cancel button
        cancelButton.setOnClickListener {

            dialog.dismiss() // Close dialog without logging out
        }

        // Action for confirm button (log out)
        confirmButton.setOnClickListener {
            auth.signOut() // Sign out from firebase
            Intent(requireActivity(), Authentification::class.java).also {
                startActivity(it)
            }
            requireActivity().finish() // Close  current activity

            dialog.dismiss()
        }

        dialog.show()  // Show  the dialog
    }

    /**
     * Initialize UI commponents.
     */
    private fun initComponents(view: View) {
        btnlogout = view.findViewById(R.id.btnLogout)
        btnEditPhoto = view.findViewById(R.id.btnEditPhoto)
        imageProfile = view.findViewById(R.id.imageProfile)
        username = view.findViewById(R.id.username)
        btnEditPassword = view.findViewById(R.id.btnEditPassword)
        btnNotification = view.findViewById(R.id.btnNotification)
        btnChangeLaunguage = view.findViewById(R.id.txtLanguage)
        btnDeleteAccount = view.findViewById(R.id.deleteAccount)
    }

    private fun initDialog(dialog: Dialog) {
        oldPassword = dialog.findViewById(R.id.oldPassword)
        newPassword = dialog.findViewById(R.id.newPassword)
        confirmPassword = dialog.findViewById(R.id.confirmPassword)
        cancelBtn = dialog.findViewById(R.id.btnCancel)
        confirmBtn = dialog.findViewById(R.id.btnConfirm)
    }

    private fun uploadImageToFirebase(id: String) {
        // Check if an image has been selected
        if (chosenImageUri == null) {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            return
        }

        // Getting a reference to Firebase storage
        val storage = Firebase.storage.reference
        val imageRef = storage.child("ImageProfile/$id")

        try {
            // Converting the image to a bitmap from the chosen URI
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, chosenImageUri)

            // Compressing the bitmap to JPEG format
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)

            // Converting the compressed image into a byte array for uploading
            val data = bos.toByteArray()

            // Uploading the image to Firebase storage
            val uploadTask = imageRef.putBytes(data)

            // Handling upload success
            uploadTask.addOnSuccessListener {
                // Once uploaded, retrieve the download URL of the image
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Convert the download URL to a string
                    val photoProfile = downloadUri.toString()

                    // Create a hashmap object to update the image path in Firestore
                    val updatePhoto = hashMapOf<String, Any>(
                        "photo_profile" to photoProfile
                    )

                    // Update the user's photo profile path in Firestore
                    db.collection("Utilisateur").document(id).update(updatePhoto)
                        .addOnSuccessListener {
                            Log.d("Image", "Image successfully uploaded and path updated")
                            // Hide the progress bar once the operation is complete
                            Utils.hideProgressBar(progressDialog)
                        }
                        .addOnFailureListener { e ->
                            // Log any errors that occur during the update of Firestore
                            Log.e("Firestore", "Error updating image path: $e")
                        }
                }.addOnFailureListener { e ->
                    // Log any errors that occur while obtaining the download URL
                    Log.e("Storage", "Error getting download URL: $e")
                }
            }.addOnFailureListener { e ->
                // Log any errors that occur during the image upload
                Log.e("Storage", "Error uploading image: $e")
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur while processing the image
            Log.e("ImageProcessing", "Error processing image: ${e.message}")
        }
    }

    private fun VerifierChamps(
        oldPassword: TextInputLayout,
        newPassword: TextInputLayout,
        confirmPassword: TextInputLayout
    ): Boolean {
        val validator = ValidateService(
            listOf(
                oldPassword to PasswordValidation(),
                newPassword to PasswordValidation(),
                confirmPassword to ConfirmPasswordValidator(newPassword)
            )
        )
        if (validator.validete()) {
            return true
        }
        return false
    }
}




