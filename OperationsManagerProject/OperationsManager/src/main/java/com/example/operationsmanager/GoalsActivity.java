package com.example.operationsmanager;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class GoalsActivity extends  Activity implements OnItemClickListener,OnItemLongClickListener {

    int PID;
    int GID;
    DatabaseHelper db;
    List<Goals> goals;
    int pos;
    String name,posi;
    CustomGoalAdapter adapter;
    ListView listView;
    Context c,dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals_main);

        Bundle b = getIntent().getExtras();
        PID = b.getInt("val");
        name = b.getString("name");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.goals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.goals_main, container, false);
            return rootView;
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.goals_main);


        db = new DatabaseHelper(this);


        try {

            goals= db.getGoals(PID);
            listView = (ListView) findViewById(R.id.listView1);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);

            c= this;


            adapter = new CustomGoalAdapter(this, R.layout.listview_goal_row, goals);
            View header = (View)getLayoutInflater().inflate(R.layout.listview_goal_header, null);



            listView.addHeaderView(header,null,false);
            listView.setAdapter(adapter);

            db.close();

        } catch (Exception e){ db.close();  }


    }


    // ------------------------------------------------------------------------ onclick listeners


    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        RelativeLayout listItem = (RelativeLayout) v;
        TextView clickedItemView = (TextView) listItem.findViewById(R.id.txtTitle);
        name = clickedItemView.getText().toString();
        pos = position-1;

        openGoal(v, goals.get(pos).getGoalID());
        Toast.makeText(getBaseContext(), name+" selected", Toast.LENGTH_LONG).show();


    }




    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

        RelativeLayout listItem = (RelativeLayout) v;
        TextView clickedItemView = (TextView) listItem.findViewById(R.id.txtTitle);
        name = clickedItemView.getText().toString();
        pos=position-1;

        new AlertDialog.Builder(c)
                .setTitle("What to do?")
                        //      .setMessage("Are you sure you want to delete this entry?")


                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateGoal(goals.get(pos).getGoalID()); //open update company
                    }
                })

                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDelete(); //open alert for delete
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();


        return false;

    }



    // -------------------------------------------------------------------------- open contacts for company

    public void openGoal(View view, int id) {
        Intent intent = new Intent(this, TargetsActivity.class); //to create

        Bundle bundle = new Bundle();
        bundle.putInt("val", id);
        bundle.putString("name", name);
        intent.putExtras(bundle);

        startActivity( intent);
    }


    // --------------------------------------------------------------------------- alert for delete

    public void alertDelete(){

        new AlertDialog.Builder(c)
                .setTitle("Delete entry")
                .setMessage("Delete '" + name + "' from list?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        db.delGoal(goals.get(pos).getGoalID());


                        Toast.makeText(getBaseContext(), name+" successfully deleted", Toast.LENGTH_LONG).show();

                        goals= db.getGoals(PID);

                        adapter = new CustomGoalAdapter(c, R.layout.listview_goal_row, goals);
                        adapter.notifyDataSetChanged();


                        listView.setAdapter(adapter);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }



    // ----------------------------------------------------------------------------------- create and update Project

    public void newGoal(View view) {

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Goal Name");


        layout.addView(Name);


        new AlertDialog.Builder(c)
                .setTitle("New Goal")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        //	db.delCompanyContact(proj.get(pos).getCompanyContactsID());

                        String value = Name.getText().toString();

                        db.addGoal(PID, value);

                        Toast.makeText(getBaseContext(), value+" successfully created", Toast.LENGTH_LONG).show();

                        goals= db.getGoals(PID);

                        adapter = new CustomGoalAdapter(c, R.layout.listview_goal_row, goals);
                        adapter.notifyDataSetChanged();

                        listView.setAdapter(adapter);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();


    }


    public void updateGoal(int id) {
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Goal Name");
        Name.setText(name);

        layout.addView(Name);


        new AlertDialog.Builder(c)
                .setTitle("Update Goal")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with update


                        String value = Name.getText().toString();

                        db.updateGoal(goals.get(pos).getGoalID(), value);


                        Toast.makeText(getBaseContext(), value+" successfully updated", Toast.LENGTH_LONG).show();

                        goals= db.getGoals(PID);

                        adapter = new CustomGoalAdapter(c, R.layout.listview_goal_row, goals);
                        adapter.notifyDataSetChanged();


                        listView.setAdapter(adapter);



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();


    }




}
