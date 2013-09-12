package edu.uchicago.cs.quiz.aakrit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class QuizActivity extends Activity {

    public static final byte MIXED = 2, GREEK = 1, LATIN = 0;

    private Button mExitButton, mLatinButton, mGreekButton, mMixedButton;
    private String mName;
    EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//once the activity is created
        super.onCreate(savedInstanceState);//save instancestate in superclass -> Activity
        setContentView(R.layout.activity_start);//Set the view for the activity
        mExitButton = (Button) findViewById(R.id.exitButton);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLatinButton = (Button) findViewById(R.id.latin_button);//get the button from resources view
        mLatinButton.setOnClickListener(new View.OnClickListener(){//add a click listener for the button
            @Override
            public void onClick(View view)//what to do when button is clicked
            {
                startMe(QuizTracker.LATIN);
            }

        });
        mGreekButton = (Button) findViewById(R.id.greek_button);
        mGreekButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startMe(QuizTracker.GREEK);
            }

        });
        mMixedButton = (Button) findViewById(R.id.mixed_button);
        mMixedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startMe(QuizTracker.MIXED);
            }

        });
    }

    private void startMe(byte yLanguage) {
        QuizTracker.getInstance().setLanguage(yLanguage);
        mNameEditText = (EditText) findViewById(R.id.editName);
        mName = mNameEditText.getText().toString();
        QuizTracker.getInstance().setName(mName);
        askQuestion(1);
    }

    private void askQuestion(int number) {
        QuizTracker.getInstance().setQuestionNum(number);
        Intent intent = new Intent(QuizActivity.this, QuestionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuExit:
                finish();
                return true;
            case R.id.menuLatin:
                startMe(QuizTracker.LATIN);
                return true;
            case R.id.menuGreek:
                startMe(QuizTracker.GREEK);
                return true;
            case R.id.menuMixed:
                startMe(QuizTracker.MIXED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
