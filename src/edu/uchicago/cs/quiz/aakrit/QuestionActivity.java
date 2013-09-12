package edu.uchicago.cs.quiz.aakrit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuestionActivity extends Activity {
    public static final String QUESTION = "edu.uchicago.cs.quiz.aakrit.QUESTION";
    public static final String RADS = "edu.uchicago.cs.quiz.aakrit.RADS";

    private static final String DELIMITER = "\\|";//used to read the array-list
    private static final int NUM_ANSWERS = 5;//total number of choices


    Random mRandom;

    private RadioButton[] mRadioButtons;
    private LanguageQuestion mQuestion;
    private String[] mWords;
    private boolean mItemSelected = false;
    private int mRandomPosition;
    //make these members
    TextView mQuestionNumberTextView;
    RadioGroup mQuestionRadioGroup;
    TextView mQuestionTextView;
    Button mSubmitButton;
    Button mQuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //generate a question
        mWords = getResources().getStringArray(R.array.word_classics);
        //get refs to inflated members
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumber);
        mQuestionTextView = (TextView) findViewById(R.id.questionText);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        //init the random
        mRandom = new Random();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        //set quit button action
        mQuitButton = (Button) findViewById(R.id.quitButton);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayResult();
            }
        });
        mQuestionRadioGroup = (RadioGroup) findViewById(R.id.radioAnswers);
        //disallow submitting until an answer is selected
        mQuestionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mSubmitButton.setEnabled(true);//enable submit only once a radiogroup is checked
                mItemSelected = true;
            }
        });
        fireQuestion(savedInstanceState);//ask question
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {//only invoked when the user changes/rotates the screen
        super.onSaveInstanceState(outState);
        //pass the question into the bundle when I have a config change

        outState.putSerializable(QuestionActivity.QUESTION, mQuestion);
        mQuestionRadioGroup.removeAllViews();//use this to set all view to null so they can be reloaded when the screen orientation is changed
        outState.putSerializable(QuestionActivity.RADS, mRadioButtons);//pass radio buttons into the bundle
    }


    private void fireQuestion(){
        populateUserInterface();
    }

    //overloaded to take savedInstanceState
    private void fireQuestion(Bundle savedInstanceState){

        if (savedInstanceState == null ){
           populateUserInterface();
        } else {
            populateUserInterface(savedInstanceState);
        }

    }

    private void populateUserInterface() {
        //get a question from model and set the submit button feature
        mQuestion = generateGoodQuestion(QuizTracker.getInstance().getLanguage());
        mSubmitButton.setEnabled(false);
        mItemSelected = false;

        //populate the QuestionNumber textview
        String questionNumberText = getResources().getString(R.string.questionNumberText);
        int number = QuizTracker.getInstance().getQuestionNum();
        mQuestionNumberTextView.setText(String.format(questionNumberText, number));

        //set question text
        mQuestionTextView.setText(mQuestion.getQuestionText(getResources().getString(R.string.question_string)));

        //will generate a number 0-4 inclusive
        mRandomPosition = mRandom.nextInt(NUM_ANSWERS);
        int counter = 0;
        mRadioButtons = new RadioButton[NUM_ANSWERS];
        mQuestionRadioGroup.removeAllViews();
        //for each of the 5 wrong answers
        for (String wrongAnswer : mQuestion.getWrongAnswers()) {
            if (counter == mRandomPosition) {
                //insert one correct one answer
                mRadioButtons[counter] = addRadioButton(mQuestionRadioGroup, mQuestion.getForeign());
            } else {
                mRadioButtons[counter] = addRadioButton(mQuestionRadioGroup, wrongAnswer);
            }
            counter++;
        }
    }
    //overloaded to take bundle for when the orientation of screen is changed
    private void populateUserInterface(Bundle savedInstanceState) {
        mQuestion = (LanguageQuestion) savedInstanceState.getSerializable(QuestionActivity.QUESTION);//cast serializable obj to question and return its ref
        //take care of button first
        mRadioButtons = (RadioButton[]) savedInstanceState.getSerializable(QuestionActivity.RADS);//get the radio button
        //populate the QuestionNumber textview
        String questionNumberText = getResources().getString(R.string.questionNumberText);//
        int number = QuizTracker.getInstance().getQuestionNum();//get the question number
        mQuestionNumberTextView.setText(String.format(questionNumberText, number));//display the question with number

        //set question text
        mQuestionTextView.setText(mQuestion.getQuestionText(getResources().getString(R.string.question_string)));//get the question
        //will generate a number 0-4 inclusive
        for(int nC = 0; nC < mRadioButtons.length; nC++)
        {
            try {
                mQuestionRadioGroup.addView(mRadioButtons[nC]);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void submit() {

        Button checkedButton = (Button) findViewById(mQuestionRadioGroup.getCheckedRadioButtonId());
        String guess = checkedButton.getText().toString();
        //see if they guessed right
        if (mQuestion.getForeign().equals(guess)) {
            QuizTracker.getInstance().answeredRight();
            Toast.makeText(getApplicationContext(),"Correct!", Toast.LENGTH_SHORT).show();
        } else {
            QuizTracker.getInstance().answeredWrong();
            Toast.makeText(getApplicationContext(),"Wrong Answer!", Toast.LENGTH_SHORT).show();
        }
        if (QuizTracker.getInstance().getTotalAnswers() < Integer.MAX_VALUE) {
            //increment the question number
            QuizTracker.getInstance().incrementQuestionNumber();
            fireQuestion();
        } else {
            displayResult();
        }

     }

    private void displayResult(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuQuit:
                displayResult();
                return true;
            case R.id.menuSubmit:
                if(mItemSelected){
                submit();
                }
                else{
                    Toast toast = Toast.makeText(this, getResources().getText(R.string.pleaseSelectAnswer), Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RadioButton addRadioButton(RadioGroup questionGroup, String text) {
        RadioButton button = new RadioButton(this);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        questionGroup.addView(button);
        return button;
    }

    private LanguageQuestion generateGoodQuestion(byte yLanguage)
    {
        mQuestion = getRandomQuestion(yLanguage);
        while(mQuestion.getWrongAnswers().size() < NUM_ANSWERS)
        {
            LanguageQuestion wrongQuestion;
            do {
                wrongQuestion = getRandomQuestion(yLanguage);

            }while (mQuestion.getEnglish().equals(wrongQuestion.getEnglish()));//if its the same Q or trick question, discard it
            mQuestion.addWrongAnswer(wrongQuestion.getForeign());//
        }
        return mQuestion;
    }

    private LanguageQuestion getRandomQuestion(byte yLanguage)
    {
        int index;
        String[] strForEngLans;
        LanguageQuestion question;

        switch (yLanguage)
        {
            case QuizTracker.LATIN:
                do{
                    index = mRandom.nextInt(mWords.length);
                    strForEngLans = mWords[index].split(DELIMITER);
                    question = new LanguageQuestion(strForEngLans);
                }while(!question.getLanguage().equals("LAT"));
                return question;

            case QuizTracker.GREEK:
                do{
                    index = mRandom.nextInt(mWords.length);
                    strForEngLans = mWords[index].split(DELIMITER);
                    question = new LanguageQuestion(strForEngLans);
                }while(!question.getLanguage().equals("GRK"));
                return question;

            case QuizTracker.MIXED:
                default:
                    index = mRandom.nextInt(mWords.length);
                    return new LanguageQuestion(mWords[index].split(DELIMITER));
        }
    }
}
