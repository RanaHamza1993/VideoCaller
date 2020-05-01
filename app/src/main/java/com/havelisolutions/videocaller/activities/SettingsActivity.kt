package com.havelisolutions.videocaller.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.SettingsBinding
import com.havelisolutions.videocaller.extensions.showErrorMessage
import com.havelisolutions.videocaller.extensions.showSuccessMessage
import com.havelisolutions.videocaller.utils.ActivityNavigator
import com.havelisolutions.videocaller.utils.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: SettingsBinding
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    lateinit var firebaseRef: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var downloadUri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        init()
        setListeners()
    }

    private fun init() {
        firebaseRef = FirebaseDatabase.getInstance().reference.child("Users")
        storageReference = FirebaseStorage.getInstance().reference.child("Profile Images")
        retrieveUserInfo()
    }

    private fun setListeners() {
        binding.run {
            settingImage.setOnClickListener {
                if(!checkPermissions()){
                    askPermissions {
                        chooseImage()
                    }
                }
            }
            saveSettings.setOnClickListener {
                if (validateFields())
                    saveData()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (filePath == null) {
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(FirebaseAuth.getInstance().currentUser!!.uid).hasChild("image")) {
                        saveInfo()
                    }

                }

            })

            return false
        } else if (binding.usernameSettings.text.toString().isEmpty()) {
            showErrorMessage("Enter your name")
            return false
        } else if (binding.bioSettings.text.toString().isEmpty()) {
            showErrorMessage("Enter your bio")
            return false
        }
        return true
    }

    private fun chooseImage() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent, PICK_IMAGE_REQUEST
        )
    }

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode, resultCode, data
        )
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.settingImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile(filePath: Uri?, key: DatabaseReference, map: HashMap<String, String?>) {
        //  filePath?.let {
        //val progressDialog = ProgressDialog(injectedContext)
        //progressDialog.setTitle("Uploading")
        //progressDialog.show()
//            val imageRef = storageReference.child("images/").child(key.key.toString()+"jpg")
//            imageRef.putFile(filePath).addOnSuccessListener {
//                // if the upload is successfull
//                //hiding the progress dialog
//                val result = it.metadata?.reference?.downloadUrl
//                result?.addOnSuccessListener {
//                    map["image"] = it.toString()
//                    //    injectedContext.showSuccessMessage("Image uri is $it")
//                    key.updateChildren(map as Map<String, Any?>)
//                        .addOnSuccessListener {
////                            setNotificationLiveData.value =
////                                SafeApiResponse.Success(
////                                    "Main nai btao ga",
////                                    200
////                                )
//                        }.addOnFailureListener {
////                            setNotificationLiveData.value=SafeApiResponse.Error(null,"Failed to postData",400)
//                        }
//
//                }
//                // progressDialog.dismiss()
//                ///injectedContext.showSuccessMessage("Image uploaded successfully")
//
//                //and displaying a success toast
//            }.addOnFailureListener {
//                //if the upload is not successfull
//                //hiding the progress dialog
//                //  progressDialog.dismiss();
////                setNotificationLiveData.value=SafeApiResponse.Error(null,"Image not uploaded",400)
////                injectedContext.showErrorMessage("Image not uploaded")
//                //and displaying error message
//            }.addOnProgressListener {
//                //calculating progress percentage
////                val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
////
////              //  displaying percentage in progress dialog
////                progressDialog.setMessage("Uploaded " + (progress).toInt() + "%...")
//            }
//            return
//        }
        //injectedContext.showErrorMessage("Please select a file")
    }

    private fun saveData() {
        val filePathRef = storageReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
        val uploadTask = filePath?.let { filePathRef.putFile(it) }
        uploadTask?.continueWithTask(object :
            Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                if (!task.isSuccessful) {
                    throw task.exception!!
                } else {
                    downloadUri = filePathRef.downloadUrl.toString()
                    return filePathRef.downloadUrl
                }
            }

        })?.addOnCompleteListener {
            if (it.isSuccessful) {
                downloadUri = it.result.toString()
                val profileMap = HashMap<String, Any>()
                profileMap.put("uid", FirebaseAuth.getInstance().currentUser!!.uid)
                profileMap.put("name", binding.usernameSettings.text.toString())
                profileMap.put("bio", binding.bioSettings.text.toString())
                profileMap.put("image", downloadUri)
                firebaseRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .updateChildren(profileMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            ActivityNavigator(this, ContactsActivity::class.java)
                            showSuccessMessage("Settings has been updated")
                        }
                    }
            }
        }

    }

    private fun saveInfo() {
        if (binding.usernameSettings.text.toString().isEmpty()) {
            showErrorMessage("Enter your name")
            return
        } else if (binding.bioSettings.text.toString().isEmpty()) {
            showErrorMessage("Enter your bio")
            return
        }
        val profileMap = HashMap<String, Any>()
        profileMap.put("name", binding.usernameSettings.text.toString())
        profileMap.put("bio", binding.bioSettings.text.toString())
        firebaseRef.child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(profileMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    ActivityNavigator(this, ContactsActivity::class.java)
                    showSuccessMessage("Settings has been updated")
                }
            }

    }

    private fun retrieveUserInfo() {
        binding.run {
            firebaseRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            usernameSettings.setText(p0.child("name").value.toString())
                            bioSettings.setText(p0.child("bio").value.toString())
                            Constants.loadImage(settingImage,p0.child("image").value.toString())
                        }
                    }

                })
        }
    }
    fun askPermissions(dowork: () -> Unit) {
        if (!checkPermissions()) {
            Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object :
                MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) if (report.areAllPermissionsGranted()) dowork.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?,
                                                                token: PermissionToken?) {
                    token?.continuePermissionRequest();
                }

            }).check()
        }
    }

}
