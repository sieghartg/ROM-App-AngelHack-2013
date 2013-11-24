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

public class CustomTargetsAdapter extends ArrayAdapter<Targets> {

    Targets proj;
    DatabaseHelper db;

    Context context;
    int layoutResourceId;
    List<Targets> data = null;

    public CustomTargetsAdapter(Context context, int layoutResourceId, List<Targets> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    public void setList(List<Targets> data){
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TargetHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TargetHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtTarget = (TextView)row.findViewById(R.id.txtTarget);
            holder.txtActual = (TextView)row.findViewById(R.id.txtActual);
            holder.txtRemarks = (TextView)row.findViewById(R.id.txtRemarks);
            row.setTag(holder);
        }
        else
        {
            holder = (TargetHolder)row.getTag();
        }

        //   final View view = super.getView(position, convertView, parent);

        db = new DatabaseHelper(context);


        proj = data.get(position);


        holder.txtTitle.setText(""+proj._TargetName);
        holder.txtTarget.setText("Target:   "+proj._Target);
        holder.txtActual.setText("Actual:   "+proj._Actual);
        holder.txtRemarks.setText("         "+proj._Remarks);

        return row;
    }


    static class TargetHolder
    {
        TextView txtTitle;
        TextView txtTarget;
        TextView txtActual;
        TextView txtRemarks;
    }


}

