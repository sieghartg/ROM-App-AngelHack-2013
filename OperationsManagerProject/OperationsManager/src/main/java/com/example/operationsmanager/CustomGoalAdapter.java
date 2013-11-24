package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomGoalAdapter extends ArrayAdapter<Goals> {

    Goals proj;
    DatabaseHelper db;

    Context context;
    int layoutResourceId;
    List<Goals> data = null;

    public CustomGoalAdapter(Context context, int layoutResourceId, List<Goals> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    public void setList(List<Goals> data){
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GoalHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GoalHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.listDesc = (TextView)row.findViewById(R.id.listDesc);
            row.setTag(holder);
        }
        else
        {
            holder = (GoalHolder)row.getTag();
        }

        //   final View view = super.getView(position, convertView, parent);

        db = new DatabaseHelper(context);



        proj = data.get(position);


        holder.txtTitle.setText(""+proj._GoalName);
        //holder.listDesc.setText(""+proj._GoalDetail);
        holder.listDesc.setText(""+db.getGoalDetail(proj._GoalID));

        return row;
    }


    static class GoalHolder
    {
        TextView txtTitle;
        TextView listDesc;
    }





}
