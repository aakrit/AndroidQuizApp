package edu.uchicago.cs.quiz.aakrit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aakritprasad on 7/21/13.
 */
public class LanguageQuestion implements Serializable
{
    private String mForeign;
    private String mEnglish;
    private String mLanguage;
    private Set<String> wrongAnswers = new HashSet<String>();


    public LanguageQuestion(String foreign, String english, String language) {

        mForeign = foreign;
        mEnglish = english;
        mLanguage = language;
    }

    public  LanguageQuestion (String[] strArray)
    {
        if(strArray.length == 3)
        {
            this.mForeign = strArray[0];
            this.mEnglish = strArray[1];
            this.mLanguage = strArray[2];
        }
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public void setEnglish(String english) {
        mEnglish = english;
    }

    public String getForeign() {
        return mForeign;
    }

    public void setForeign(String foreign) {
        mForeign = foreign;
    }
    public Set<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public boolean addWrongAnswer(String wrongAnswer){
        return wrongAnswers.add(wrongAnswer);
    }

    public String getQuestionText(String strFormat){

        return String.format(strFormat, mEnglish);
    }

}
