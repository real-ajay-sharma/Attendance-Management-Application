package com.example.cloudfirestore.faculty;

public class student {

    String name,attendance;
    int imageId;

    public student(String name,String att)
    {
        this.name = name;
        attendance = att;
    }

    public student(String name,int imageId)
    {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId()
    {
        return imageId;
    }
    public String getName()
    {
        return name;
    }
    public String getAttendance()
    {
        return attendance;
    }
}
