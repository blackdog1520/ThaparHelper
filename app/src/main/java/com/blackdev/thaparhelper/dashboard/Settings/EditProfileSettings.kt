package com.blackdev.thaparhelper.dashboard.Settings

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.blackdev.thaparhelper.R
import com.blackdev.thaparhelper.UserFacultyModelClass
import com.blackdev.thaparhelper.UserPersonalData
import com.blackdev.thaparhelper.allutils.Constants
import com.blackdev.thaparhelper.allutils.CustomButtonWithPD
import com.blackdev.thaparhelper.allutils.MySharedPref
import com.blackdev.thaparhelper.allutils.Utils
import com.blackdev.thaparhelper.dashboard.DashBoardActivity
import com.blackdev.thaparhelper.dashboard.dashboardFrag.DashboardFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideExperiments
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mikhaellopez.circularimageview.CircularImageView
import java.util.*
import kotlin.collections.HashMap

class EditProfileSettings : AppCompatActivity() {

    private lateinit var profilePic : CircularImageView
    private lateinit var nameEt : EditText
    private lateinit var bioEt : EditText
    private lateinit var deptEt : EditText
    private lateinit var mobEt : EditText
    private lateinit var rootLayout : LinearLayout
    private lateinit var resetPasswordEt : CustomButtonWithPD
    private lateinit var mRef : DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var saveButton : ImageButton
    private lateinit var closeButton : ImageButton
    private lateinit var currName : String
    private lateinit var currDept : String
    private lateinit var currBio : String
    private lateinit var currMob : String
    private lateinit var deptKey : String
    private lateinit var sharedPref : MySharedPref
    private lateinit var otherUser : UserFacultyModelClass
    private lateinit var studentUser : UserPersonalData
    private var isStudent = false
    private lateinit var pd : ProgressDialog
    private var userType : Int = 0
    private lateinit var uri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_settings)

        mAuth = FirebaseAuth.getInstance()
        mRef = Utils.getRefForBasicData(Utils.getCurrentUserType(this, mAuth.uid), mAuth.uid)
        init()

        when(Utils.getCurrentUserType(this, mAuth.uid)){
            Constants.USER_STUDENT -> setStudentData()
            else -> setOtherUserData()
        }
        saveButton.setOnClickListener { saveInfo() }
        resetPasswordEt.setOnClickListener {
            resetPasswordEt.isClickable = false
            resetPasswordEt.showLoading()
            mAuth.sendPasswordResetEmail(mAuth.currentUser?.email.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Snackbar.make(rootLayout, "Reset link sent to your email", Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(rootLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                    resetPasswordEt.isClickable = true
                }
                resetPasswordEt.hideLoading()
            }
        }
        closeButton.setOnClickListener { finish() }
        profilePic.setOnClickListener { setProfilePic() }
    }

    private fun init() {
        profilePic = findViewById(R.id.profileImageEditView)
        nameEt = findViewById(R.id.name_edit_profile)
        bioEt = findViewById(R.id.bio_edit_profile)
        deptEt = findViewById(R.id.dept_edit_profile)
        mobEt = findViewById(R.id.mob_edit_profile)
        rootLayout = findViewById(R.id.rootLayoutEditProfile)
        resetPasswordEt = findViewById(R.id.resetPasswordEditProfile)
        saveButton = findViewById(R.id.tick_button_edit_profile)
        closeButton = findViewById(R.id.cross_button_edit_profile)
        sharedPref = MySharedPref(this, Utils.getStringPref(mAuth.uid),Constants.TYPE_SHARED_PREF)
        pd = ProgressDialog(this)
        userType = sharedPref.userType
    }

    private fun setProfilePic() {
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1920, 1080).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            uri = data?.data!!
            showUploadDialog()
            val storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture").child(mAuth.uid.toString())
            storageReference.putFile(uri).addOnCompleteListener {
                if(it.isSuccessful){
                    val uriTask : Task<Uri> = it.result.storage.downloadUrl
                    var done = false
                    while (!uriTask.isComplete) {
                        if(uriTask.isComplete) {
                            done = true
                            postDataInDB(uriTask.result.toString(), uri)
                            break
                        }
                    }
                    if(!done) {
                        postDataInDB(uriTask.result.toString(), uri)
                    }
                }
            }.addOnSuccessListener {
                Log.i("Post", "Success")
            }.addOnFailureListener {
                Snackbar.make(rootLayout,"Failed to upload post. Try again!", Snackbar.LENGTH_SHORT).show()
                pd.hide()
            }
        }
        else if(resultCode == ImagePicker.RESULT_ERROR){
            Snackbar.make(rootLayout, ImagePicker.getError(data), Snackbar.LENGTH_SHORT).show()
        }
        else{
            Snackbar.make(rootLayout, "Task cancelled", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun postDataInDB(downloadUrl: String, uri: Uri) {
        val dbRef = Utils.getRefForBasicData(userType,FirebaseAuth.getInstance().uid)
        val map = HashMap<String, Any>()
        map.put(getString(R.string.profileImageLink),downloadUrl)
        dbRef.updateChildren(map).addOnSuccessListener {
            pd.hide()
            Glide.with(this).load(downloadUrl).centerCrop().into(profilePic)
            Snackbar.make(rootLayout,"Profile updated Successfully!",Snackbar.LENGTH_SHORT).show()
        }.addOnFailureListener{
            pd.hide()
            Snackbar.make(rootLayout,"Something went wrong",Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun showUploadDialog() {
        pd.setTitle("Set your profile")
        pd.setMessage("Please wait while we are setting your profile")
        pd.show()
    }

    private fun saveInfo() {
        saveButton.isClickable = false
        var map = HashMap<String, Any>()
        if(currName != nameEt.text.toString()){
            map.put(getString(R.string.namedb), nameEt.text.toString())
            if(isStudent){
                studentUser.name = nameEt.text.toString()
            }
            else{
                otherUser.name = nameEt.text.toString()
            }
        }
        if(currDept != deptEt.text.toString()){
            map.put(deptKey, deptEt.text.toString())
            if(isStudent){
                studentUser.branch = deptEt.text.toString()
            }
            else{
                otherUser.department = deptEt.text.toString()
            }
        }
        if(currBio != bioEt.text.toString()){
            map.put(getString(R.string.biodb), bioEt.text.toString())
            if(isStudent){
                studentUser.bio = bioEt.text.toString()
            }
            else{
                otherUser.bio = bioEt.text.toString()
            }
        }
        if(currMob != mobEt.text.toString()){
            map.put(getString(R.string.mobdb), mobEt.text.toString())
            if(isStudent){
                studentUser.mobNumber = mobEt.text.toString()
            }
            else{
                otherUser.mobNumber = mobEt.text.toString()
            }
        }
        if(map.size != 0){
            mRef.updateChildren(map).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    saveOffline()
                    finish()
                }
                else{
                    Snackbar.make(rootLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveOffline() {
        if(isStudent){
            sharedPref.saveUser(studentUser)
        }
        else{
            sharedPref.saveUser(otherUser)
        }
    }

    private fun setOtherUserData() {
        otherUser = Utils.getCurrentUserDataF(this, mAuth.uid)
        nameEt.setText(otherUser.name)
        bioEt.setText(otherUser.bio)
        deptEt.setText(otherUser.department)
        mobEt.setText(otherUser.mobNumber)

        currName = otherUser.name
        currDept = otherUser.department
        currMob = otherUser.mobNumber
        if(otherUser.bio == null){
            currBio = ""
        }
        else{
            currBio = otherUser.bio
        }
        currBio = otherUser.mobNumber

        Glide.with(this).load(otherUser.profileImageLink).into(profilePic)

        deptKey = getString(R.string.deptdb)
        sharedPref = MySharedPref(this, Utils.getStringPref(mAuth.uid), otherUser.userType)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DashBoardActivity::class.java))
        finish()
    }

    private fun setStudentData() {
        isStudent = true
        studentUser = Utils.getCurrentUserData(this, mAuth.uid)
        nameEt.setText(studentUser.name)
        bioEt.setText(studentUser.bio)
        deptEt.setText(studentUser.branch)
        mobEt.setText(studentUser.mobNumber)

        currName = studentUser.name
        currDept = studentUser.branch
        currMob = studentUser.mobNumber
        if (studentUser.bio == null){
            currBio = ""
        }
        else{
            currBio = studentUser.bio
        }
        currBio = studentUser.mobNumber
        Glide.with(this).load(studentUser.profileImageLink).into(profilePic)

        deptKey = getString(R.string.branchdb)
        sharedPref = MySharedPref(this, Utils.getStringPref(mAuth.uid), Constants.USER_STUDENT)
    }
}