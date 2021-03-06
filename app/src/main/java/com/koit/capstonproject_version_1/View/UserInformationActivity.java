package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.InputController;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserInformationActivity extends AppCompatActivity {
    private EditText edDob,edEmail,edPhoneNumber,edFullname,edAddress,edStoreName;
    private RadioButton rbMale,rbFemale;
    private UserController userController;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        findViewById();
        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        setCurrentUserInfo();
        userController = new UserController(this);
    }


    private void setCurrentUserInfo(){
        edFullname.setText(currentUser.getFullName());
        edEmail.setText(currentUser.getEmail());
        edPhoneNumber.setText(currentUser.getPhoneNumber());
        edDob.setText(currentUser.getDateOfBirth());
        edAddress.setText(currentUser.getAddress());
        edStoreName.setText(currentUser.getStoreName());
        if (currentUser.isGender())  rbMale.setChecked(true); else rbFemale.setChecked(true);


    }
    private void findViewById(){
        edFullname = findViewById(R.id.edFullname);
        edEmail = findViewById(R.id.edEmail);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
       // radioGender = findViewById(R.id.radioGender);
        edDob = findViewById(R.id.edDob);
        edAddress = findViewById(R.id.edAddress);
        edStoreName = findViewById(R.id.edStoreName);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        //btnUpdateUserInfo = findViewById(R.id.btnUpdateUserInfo);
    }
    public void updateUserInfo(View view){
        userController.updateUserInformation(edFullname,edEmail,edPhoneNumber,edDob,rbMale,edAddress,edStoreName,currentUser);

    }
    public void getNewDate(View view) {
        DatePickerDialog.OnDateSetListener mListener = mListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i1 < 10 ? i + "-0" + i1 + "-" + i2 : i + "-" + i1 + "-" + i2;
               String updateDate = i1 + "/" + i2 + "/" + i;
                edDob.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, mListener, year, month, day);
        dialog.show();
    }
    public void backButton(View v){
        onBackPressed();
    }
    public void setErrorEditTxt(String mess, EditText textInputEditText){
        textInputEditText.requestFocus();
        textInputEditText.setError(mess);
    }

    public EditText getEdDob() {
        return edDob;
    }

    public void setEdDob(EditText edDob) {
        this.edDob = edDob;
    }

    public EditText getEdEmail() {
        return edEmail;
    }

    public void setEdEmail(EditText edEmail) {
        this.edEmail = edEmail;
    }

    public EditText getEdPhoneNumber() {
        return edPhoneNumber;
    }

    public void setEdPhoneNumber(EditText edPhoneNumber) {
        this.edPhoneNumber = edPhoneNumber;
    }

    public EditText getEdFullname() {
        return edFullname;
    }

    public void setEdFullname(EditText edFullname) {
        this.edFullname = edFullname;
    }

    public EditText getEdAddress() {
        return edAddress;
    }

    public void setEdAddress(EditText edAddress) {
        this.edAddress = edAddress;
    }

    public EditText getEdStoreName() {
        return edStoreName;
    }

    public void setEdStoreName(EditText edStoreName) {
        this.edStoreName = edStoreName;
    }
}