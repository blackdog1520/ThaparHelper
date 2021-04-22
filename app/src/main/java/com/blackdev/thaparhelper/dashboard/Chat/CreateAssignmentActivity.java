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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateAssignmentActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ImageButton datePickerButton;
    TextInputEditText subject,desc,topic,link;
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
        subject = findViewById(R.id.subjectAssignmentET);
        desc = findViewById(R.id.descAssignmentET);
        topic = findViewById(R.id.topicAssignmentET);
        link = findViewById(R.id.submissionAssignmentET);
        submitButton = findViewById(R.id.sendAssignment);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case SUBMIT_BUTTON:
                Intent intent =  new Intent();
                intent.putExtra("subjectName",subject.getText().toString().trim());
                intent.putExtra("topic",topic.getText().toString().trim());
                intent.putExtra("desc",desc.getText().toString());
                intent.putExtra("deadline",link.getText().toString().trim());
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