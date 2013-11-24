package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */
public class Project {

    int _ProjectID;
    int icon;
    String _ProjectName;
    String _ProjectDetails;

    // Empty constructor
    public Project(){

    }



    public Project(int id,int icon, String name, String details){
        this._ProjectID = id;
        this._ProjectName = name;
        this._ProjectDetails = details;
    }


    // getting ID
    public int getProjectID(){
        return this._ProjectID;
    }

    // setting id
    public void setProjectID(int id){
        this._ProjectID = id;
    }

    // getting name
    public String getProjectName(){
        return this._ProjectName;
    }

    // setting name
    public void setProjectName(String name){
        this._ProjectName = name;
    }


    public String getProjectDetails(){
        return this._ProjectDetails;
    }

    // setting name
    public void setProjectDetails(String details){
        this._ProjectDetails = details;
    }

}
