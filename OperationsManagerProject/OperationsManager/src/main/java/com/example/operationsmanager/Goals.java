package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */

public class Goals {

    int _GoalID;
    int _ProjectID;
    String _GoalName;

    // Empty constructor
    public Goals(){

    }


    public Goals(int id,int icon, String name){
        this._GoalID = id;
        this._GoalName = name;
    }

    // getting ID
    public int getGoalID(){
        return this._GoalID;
    }

    // setting id
    public void setGoalID(int id){
        this._GoalID = id;
    }

    public int getProjectID(){
        return this._ProjectID;
    }

    // setting id
    public void setProjectID(int id){
        this._ProjectID = id;
    }

    // getting name
    public String getGoalName(){
        return this._GoalName;
    }

    // setting name
    public void setGoalName(String name){
        this._GoalName = name;
    }

}

