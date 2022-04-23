package com.example.pangeaappproduccion.Model.Registro;

import java.util.List;

public class Language {
    private String goalLearning;
    private String nativee;
    private String[] other;
    private String[]  pretendToLearn;

    public Language() {
    }

    public Language(String goalLearning, String nativee) {
        this.goalLearning = goalLearning;
        this.nativee = nativee;
    }
    public Language(String goalLearning, String nativee, String[] other, String[] pretendToLearn) {
        this.goalLearning = goalLearning;
        this.nativee = nativee;
        this.other = other;
        this.pretendToLearn = pretendToLearn;
    }

    public String getGoalLearning() {
        return goalLearning;
    }

    public void setGoalLearning(String goalLearning) {
        this.goalLearning = goalLearning;
    }

    public String getNativee() {
        return nativee;
    }

    public void setNativee(String nativee) {
        this.nativee = nativee;
    }

    public String[] getOther() {
        return other;
    }

    public void setOther(String[] other) {
        this.other = other;
    }

    public String[] getPretendToLearn() {
        return pretendToLearn;
    }

    public void setPretendToLearn(String[] pretendToLearn) {
        this.pretendToLearn = pretendToLearn;
    }
}
