package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    static final String dbName="OperationsDB";
    static final String ProjectTable="Projects";
    static final String GoalTable="Goals";


    public DatabaseHelper(Context context) {
        super(context, dbName, null,33);
    }



    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE Projects( ProjectID integer primary key autoincrement, ProjectName TEXT, ProjectDetails TEXT)");

        db.execSQL("CREATE TABLE Goals( GoalID integer primary key autoincrement, ProjectID INT,Goal TEXT," +
                "foreign KEY(ProjectID) references Projects(ProjectID) )");

        db.execSQL("CREATE TABLE Targets( TargetID integer primary key autoincrement, GoalID INT,TargetName TEXT,Target INT,Actual INT,Remarks TEXT," +
                "foreign KEY(GoalID) references Goals(GoalID) )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS "+ProjectTable);
        db.execSQL("DROP TABLE IF EXISTS "+GoalTable);

        onCreate(db);
    }


    // ------------------------- FOR Project ---------------------------------



    public void addProject(String Pname, String Details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ProjectName", Pname);
        values.put("ProjectDetails", Details);

        // Inserting Row
        db.insert("Projects",null, values);

        db.close(); // Closing database connection


    }

    public void delProject(int PID,Context c) {
        SQLiteDatabase db = this.getWritableDatabase();
        DatabaseHelper db2 = new DatabaseHelper(c);

        db2.delGoal(PID);

        db.delete(ProjectTable, " ProjectID ="+PID, null);

        db.close(); // Closing database connection
        db2.close();

    }




    public List<Project> getAllProject() {
        List<Project> ProjectList = new ArrayList<Project>();
        // Select All Query
        String selectQuery = "SELECT  ProjectID as _id,ProjectName,ProjectDetails FROM Projects order by ProjectName";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String [] {});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Project proj = new Project();
                proj.setProjectID(Integer.parseInt(cursor.getString(0)));
                proj.setProjectName(cursor.getString(1));
                proj.setProjectDetails(cursor.getString(2));

                // Adding contact to list
                ProjectList.add(proj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return contact list
        return ProjectList;
    }



    public void updateProject(int PID, String Pname, String Details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ProjectName", Pname);
        values.put("ProjectDetails", Details);


        // Inserting Row
        db.update("Projects", values, "ProjectID=" + PID, null);
        db.close(); // Closing database connection
    }



    //--------------------------------------- FOR Goals ------------------------------------




    public void addGoal(int PID,String Goal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ProjectID", PID);
        values.put("Goal", Goal);

        // Inserting Row
        db.insert("Goals",null, values);


        db.close(); // Closing database connection


    }



    public void delAllGoals(int PID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Goals", " ProjectID ="+PID, null);

        db.close(); // Closing database connection


    }



    public void  delGoal(int GID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Goals", " GoalID ="+GID, null);

        db.close(); // Closing database connection

    }




    public List<Goals> getGoals(int id) {
        List<Goals> GoalList = new ArrayList<Goals>();
        // Select All Query
        String selectQuery = "SELECT  GoalID as _id, ProjectID,Goal FROM Goals where ProjectID="+id +" order by Goal";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String [] {});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Goals proj = new Goals();
                proj.setGoalID(Integer.parseInt(cursor.getString(0)));
                proj.setProjectID(Integer.parseInt(cursor.getString(1)));
                proj.setGoalName(cursor.getString(2));

                // add material to list
                GoalList.add(proj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return contact list
        return GoalList;

    }

    public String getGoalDetail(int GoalsID) {

        // Select All Query
        String selectQuery = "SELECT  TargetName,(((cast(Actual as float)/cast(Target as float)))*100) FROM Targets where GoalID="+GoalsID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String [] {});
        String tempstr = "";

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tempstr+="   " + cursor.getString(0) + "\t -->\t " + cursor.getString(1) + "%\n";
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return contact list
        return tempstr;
    }


    public void updateGoal(int GoalsID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("Goal", name);

        // Inserting Row
        db.update("Goals", values, "GoalID=" + GoalsID, null);
        db.close(); // Closing database connection
    }


    //--------------------------------------- FOR Target ------------------------------------




    public void addTarget(int GID,String TargetName,int Target, int Actual,String Remarks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("GoalID", GID);
        values.put("TargetName", TargetName);
        values.put("Target", Target);
        values.put("Actual", Actual);
        values.put("Remarks", Remarks);
        // Inserting Row
        db.insert("Targets",null, values);

        db.close(); // Closing database connection

    }



    public void delAllTargets(int GID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Targets", " GoalID ="+GID, null);

        db.close(); // Closing database connection


    }



    public void  delTarget(int TID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Targets", " TargetID ="+TID, null);

        db.close(); // Closing database connection

    }




    public List<Targets> getTargets(int id) {
        List<Targets> TargetsList = new ArrayList<Targets>();
        // Select All Query
        String selectQuery = "SELECT  TargetID as _id, GoalID,TargetName,Target,Actual,Remarks  FROM Targets where GoalID="+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String [] {});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Targets proj = new Targets();
                proj.setTargetID(Integer.parseInt(cursor.getString(0)));
                proj.setGoalID(Integer.parseInt(cursor.getString(1)));
                proj.setTargetName(cursor.getString(2));
                proj.setTarget(Integer.parseInt(cursor.getString(3)));
                proj.setActual(Integer.parseInt(cursor.getString(4)));
                proj.setRemarks(cursor.getString(5));

                // add material to list
                TargetsList.add(proj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return contact list
        return TargetsList;

    }

    public void updateTarget(int TID, String name, int target, int actual, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("TargetName", name);
        values.put("Target",target);
        values.put("Actual",actual);
        values.put("Remarks",remarks);

        // Inserting Row
        db.update("Targets", values, "TargetID=" + TID, null);
        db.close(); // Closing database connection
    }


    public String generateReport(int ProjectID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String GoalQuery,TargetQuery;
        Cursor GoalCursor,TargetCursor;
        String tempstr = "";

        // Select All Query
        GoalQuery = "SELECT  GoalID,Goal FROM Goals where ProjectID="+ProjectID;
        GoalCursor = db.rawQuery(GoalQuery, new String [] {});

        // looping through all rows and adding to list
        if (GoalCursor.moveToFirst()) {
            do {

                tempstr+="Goal:   " + GoalCursor.getString(1) + "\n\n";

                TargetQuery = "SELECT  TargetName,Target,Actual,Remarks FROM Targets where GoalID="+GoalCursor.getString(0);
                TargetCursor = db.rawQuery(TargetQuery, new String [] {});

                if (TargetCursor.moveToFirst()) {
                    do {

                tempstr+="    Target:   " + TargetCursor.getString(0) + "\n";
                tempstr+="          Target Quantity:   " + TargetCursor.getString(1) + "\n";
                tempstr+="          Actual Quantity:   " + TargetCursor.getString(2) + "\n";
                tempstr+="          Remarks:   " + TargetCursor.getString(3) + "\n\n";
                   } while (TargetCursor.moveToNext());
                }



            } while (GoalCursor.moveToNext());
        }

        GoalCursor.close();

        db.close();

        // return contact list
        return tempstr;
    }



}
