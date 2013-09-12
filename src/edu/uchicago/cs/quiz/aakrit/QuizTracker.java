package edu.uchicago.cs.quiz.aakrit;


//QuizTracker follows the singleton pattern. Here's what you need to do to create a singleton
public class QuizTracker {
    private int mCorrectAnswers = 0;
    private int mIncorrectAnswers = 0;
    private String name;
    private int mQuestionNum;
    private byte mLanguage;

    public static final byte LATIN = 0;
    public static final byte GREEK = 1;
    public static final byte MIXED = 2;
    private  static QuizTracker sQuizTracker;

    private QuizTracker(){}
    public static QuizTracker getInstance(){
        if (sQuizTracker == null){
            sQuizTracker = new QuizTracker();
            return sQuizTracker;
        }
        else {
            return sQuizTracker;
        }
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        mIncorrectAnswers = incorrectAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        mCorrectAnswers = correctAnswers;
    }

    public void reset(){
        setName("Hey");
        initialize();
    }

    public void again()
    {
        initialize();
    }
    private void initialize()
    {
        setQuestionNum(1);
        setIncorrectAnswers(0);
        setCorrectAnswers(0);
    }


    public byte getLanguage() {
        return mLanguage;
    }

    public void setLanguage(byte language) {
        mLanguage = language;
    }

    public int getQuestionNum() {
        return mQuestionNum;
    }

    public void setQuestionNum(int questionNum) {
        mQuestionNum = questionNum;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void answeredWrong(){
        mIncorrectAnswers++;
    }

    public void answeredRight(){
        mCorrectAnswers++;
    }

    public void incrementQuestionNumber(){
        mQuestionNum++;
    }

    public int getCorrectAnswers(){
        return mCorrectAnswers;
    }

    public int getIncorrectAnswers(){
        return mIncorrectAnswers;
    }

    public int getTotalAnswers(){
        return mCorrectAnswers + mIncorrectAnswers;
    }


}
