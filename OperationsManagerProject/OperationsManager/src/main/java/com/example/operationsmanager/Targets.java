package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */
public class Targets {

    int _TargetID;
    int _GoalID;
    String _TargetName;
    int _Target;
    int _Actual;
    String _Remarks;

    // Empty constructor
    public Targets(){

    }


    public Targets(int id,int GID, String name,int Target, int Actual, String Remarks ){
        this._TargetID = id;
        this._GoalID=GID;
        this._TargetName = name;
        this._Target = Target;
        this._Actual=Actual;
        this._Remarks = Remarks;
    }


    // getting ID
    public int getTargetID(){
        return this._TargetID;
    }

    // setting id
    public void setTargetID(int id){
        this._TargetID = id;
    }

    // getting ID
    public int getGoalID(){
        return this._GoalID;
    }

    // setting id
    public void setGoalID(int id){
        this._GoalID = id;
    }

    public String getTargetName(){
        return this._TargetName;
    }

    // setting id
    public void setTargetName(String TargetName){ this._TargetName = TargetName; }

    // getting name
    public int getTarget(){ return this._Target; }

    // setting name
    public void setTarget(int Target){
        this._Target = Target;
    }

    public int getActual(){ return this._Actual; }

    // setting name
    public void setActual(int Actual){
        this._Actual = Actual;
    }

    public String getRemarks(){
        return this._Remarks;
    }

    // setting id
    public void setRemarks(String Remarks){ this._Remarks = Remarks; }


}
