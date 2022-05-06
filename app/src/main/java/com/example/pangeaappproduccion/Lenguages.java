package com.example.pangeaappproduccion;
import java.util.List;
import java.util.Map;

public class Lenguages {

    private String goalLearning = "Ingles";
    private String nativee;
    private String[] other;
    private String[]  pretendToLearn;

    public Lenguages(String s) {
    }

    public Lenguages(String goalLearning, String nativee) {
        this.goalLearning = goalLearning;
        this.nativee = nativee;
    }
    public Lenguages(String goalLearning, String nativee, String[] other, String[] pretendToLearn) {
        this.goalLearning = goalLearning;
        this.nativee = nativee;
        this.other = other;
        this.pretendToLearn = pretendToLearn;
    }

    public Lenguages() {

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
