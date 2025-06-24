package com.example.demo2;

public class Scores {
    private Integer stuID;
    private Integer corID;
    private String corName;
    private String stuName;
    private Integer score;

    @Override
    public String toString() {
        return "Scores{" +
                "stuID=" + stuID +
                ", corID=" + corID +
                ", corName='" + corName + '\'' +
                ", stuName='" + stuName + '\'' +
                ", score=" + score +
                '}';
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStuID() {
        return stuID;
    }

    public void setStuID(Integer stuID) {
        this.stuID = stuID;
    }

    public Integer getCorID() {
        return corID;
    }

    public void setCorID(Integer corID) {
        this.corID = corID;
    }

    public String getCorName() {
        return corName;
    }

    public void setCorName(String corName) {
        this.corName = corName;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Scores(Integer stuID, Integer corID, String corName, String stuName, Integer score) {
        this.stuID = stuID;
        this.corID = corID;
        this.corName = corName;
        this.stuName = stuName;
        this.score = score;
    }
}
