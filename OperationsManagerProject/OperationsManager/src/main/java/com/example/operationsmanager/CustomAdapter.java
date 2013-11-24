package com.example.operationsmanager;

/**
 * Created by FRANCIS on 11/23/13.
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Project> {

    Project proj;

    Context context;
    int layoutResourceId;
    List<Project> data = null;

    public CustomAdapter(Context context, int layoutResourceId, List<Project> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    public void setList(List<Project> data){
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProjectHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ProjectHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtDesc = (TextView)row.findViewById(R.id.desc);


            row.setTag(holder);
        }
        else
        {
            holder = (ProjectHolder)row.getTag();
        }

        //   final View view = super.getView(position, convertView, parent);


        proj = data.get(position);
        holder.txtTitle.setText(""+proj._ProjectName);
        holder.txtDesc.setText(""+proj._ProjectDetails);

        return row;
    }


    static class ProjectHolder
    {
        TextView txtTitle;
        TextView txtDesc;
    }





}