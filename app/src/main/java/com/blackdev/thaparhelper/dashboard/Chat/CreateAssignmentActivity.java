package com.blackdev.thaparhelper.dashboard.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blackdev.thaparhelper.R;
import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.CustomButtonWithPD;
import com.blackdev.thaparhelper.dashboard.Chat.Models.ModelChatOneToOne;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateAssignmentActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ImageButton datePickerButton;
    TextInputEditText subjectET,descET,topicET,linkET,assignmentLinkET;
    TextView datePickedTextView;
    CustomButtonWithPD submitButton;
    private static final int SUBMIT_BUTTON = R.id.sendAssignment;
    private static final int DATE_BUTTON = R.id.pickDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        init();
        datePickerButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    private void init() {
        datePickerButton = findViewById(R.id.pickDateButton);
        datePickedTextView = findViewById(R.id.pickDateTextView);
        subjectET = findViewById(R.id.subjectAssignmentET);
        descET = findViewById(R.id.descAssignmentET);
        topicET = findViewById(R.id.topicAssignmentET);
        linkET = findViewById(R.id.submissionAssignmentET);
        submitButton = findViewById(R.id.sendAssignment);
        assignmentLinkET = findViewById(R.id.driveLinkAssignmentET);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case SUBMIT_BUTTON:
                Intent intent =  new Intent();
                intent.putExtra(getString(R.string.subjectName),subjectET.getText().toString().trim());
                intent.putExtra(getString(R.string.assignmentTopic),topicET.getText().toString().trim());
                intent.putExtra(getString(R.string.assignmentDesc),descET.getText().toString());
                intent.putExtra(getString(R.string.assignmentDeadline),datePickedTextView.getText().toString().trim());
                intent.putExtra(getString(R.string.submissionLink),linkET.getText().toString().trim());
                intent.putExtra(getString(R.string.fileLink),assignmentLinkET.getText().toString().trim());
                setResult(Constants.ASSIGNMENT_REQUEST_CODE,intent);
                finish();
                break;
            case DATE_BUTTON:
                com.blackdev.thaparhelper.allutils.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.blackdev.thaparhelper.allutils.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalender.getTime());
        datePickedTextView.setText(selectedDate);
    }
}