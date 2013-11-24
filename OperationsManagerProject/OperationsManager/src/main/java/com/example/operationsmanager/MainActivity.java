package com.example.operationsmanager;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class MainActivity extends Activity implements OnItemClickListener,OnItemLongClickListener{

    int PID;
    DatabaseHelper db;
    List<Project> proj;
    int pos;
    String name,posi;
    CustomAdapter adapter;
    ListView listView;
    Context c,dc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);


        db = new DatabaseHelper(this);


        try {

            proj= db.getAllProject();
            listView = (ListView) findViewById(R.id.listView1);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);

            c= this;


            adapter = new CustomAdapter(this, R.layout.listview_project_row, proj);
            View header = (View)getLayoutInflater().inflate(R.layout.listview_project_header, null);



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

        openProject(v,proj.get(pos).getProjectID());
        Toast.makeText(getBaseContext(), name+" selected", Toast.LENGTH_LONG).show();


    }




    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

        RelativeLayout listItem = (RelativeLayout) v;
        TextView clickedItemView = (TextView) listItem.findViewById(R.id.txtTitle);
        TextView clickedItemView2 = (TextView) listItem.findViewById(R.id.desc);
        name = clickedItemView.getText().toString();
        posi = clickedItemView2.getText().toString();
        pos=position-1;


        new AlertDialog.Builder(c)
                .setTitle("What to do?")
                        //      .setMessage("Are you sure you want to delete this entry?")


                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateProject(proj.get(pos).getProjectID()); //open update company
                    }
                })

                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDelete(); //open alert for delete
                    }
                })

                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Calendar cal = Calendar.getInstance();

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Report for:   " + name + "\n" +
                                                                "Details:   " + posi + "\n\n" +
                                                                "As of:    " + dateFormat.format(cal.getTime()) + "\n\n" +
                                                                db.generateReport(proj.get(pos).getProjectID()));
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, name);

                        startActivity(Intent.createChooser(sendIntent,"Share using"));
                    }
                })
                .show();


        return false;

    }


    // -------------------------------------------------------------------------- open contacts for company

    public void openProject(View view, int id) {
        //Intent intent = new Intent(this, ProjectGoals.class); //to create
        Intent intent = new Intent(this, GoalsActivity.class); //to create

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

                        db.delProject(proj.get(pos).getProjectID(),c);


                        Toast.makeText(getBaseContext(), name+" successfully deleted", Toast.LENGTH_LONG).show();

                        proj= db.getAllProject();

                        adapter = new CustomAdapter(c, R.layout.listview_project_row, proj);
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

    public void newProject(View view) {

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);
        final EditText Desc = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Project Name");
        Desc.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Desc.setHint("Project Details");


        layout.addView(Name);
        layout.addView(Desc);


        new AlertDialog.Builder(c)
                .setTitle("New Project")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        //	db.delCompanyContact(proj.get(pos).getCompanyContactsID());

                        String value = Name.getText().toString();
                        String value2 = Desc.getText().toString();


                        db.addProject(value,value2);


                        Toast.makeText(getBaseContext(), value+" successfully created", Toast.LENGTH_LONG).show();

                        proj= db.getAllProject();

                        adapter = new CustomAdapter(c, R.layout.listview_project_row, proj);
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


    public void updateProject(int id) {
        PID=id;

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);
        final EditText Desc = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Project Name");
        Name.setText(name);
        Desc.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Desc.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Desc.setHint("Project Details");
        Desc.setText(posi);


        layout.addView(Name);
        layout.addView(Desc);




        new AlertDialog.Builder(c)
                .setTitle("Update Project")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with update


                        String value = Name.getText().toString();
                        String value2 = Desc.getText().toString();


                        db.updateProject(PID,value,value2);


                        Toast.makeText(getBaseContext(), value+" successfully updated", Toast.LENGTH_LONG).show();

                        proj= db.getAllProject();

                        adapter = new CustomAdapter(c, R.layout.listview_project_row, proj);
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
