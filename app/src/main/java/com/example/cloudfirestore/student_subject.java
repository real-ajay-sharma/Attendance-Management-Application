package com.example.cloudfirestore;

public class student_subject {

    String status,percentage,subject;
    Long total_class,attended_class;

    public student_subject(String s,String p,Long tc,Long ac,String subjec){

        status=s;
        percentage=p;
        total_class=tc;
        attended_class=ac;
        subject = subjec;
    }
    public String getStatus(){
        return status;
    }
    public String getPercentage(){
        return percentage;
    }
    public Long getTotal_class(){
        return total_class;
    }
    public Long getAttended_class(){
        return attended_class;
    }
    public String getSubject() {
        return subject;
    }
}
