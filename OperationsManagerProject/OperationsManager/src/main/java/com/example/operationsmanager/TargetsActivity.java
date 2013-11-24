package com.example.operationsmanager;

import android.content.Context;
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


public class TargetsActivity extends Activity implements OnItemClickListener,OnItemLongClickListener {

    int GID,TID;
    DatabaseHelper db;
    List<Targets> targets;
    int pos;
    String name,posi,remarks;
    int t,a;
    CustomTargetsAdapter adapter;
    ListView listView;
    Context c,dc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.targets_main);

        Bundle b = getIntent().getExtras();
        GID = b.getInt("val");
        name = b.getString("name");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.targets, menu);
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
            View rootView = inflater.inflate(R.layout.targets_main, container, false);
            return rootView;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.targets_main);


        db = new DatabaseHelper(this);

        try {

            targets= db.getTargets(GID);
            listView = (ListView) findViewById(R.id.listView1);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);

            c= this;


            adapter = new CustomTargetsAdapter(this, R.layout.listview_targets_row, targets);
            View header = (View)getLayoutInflater().inflate(R.layout.listview_targets_header, null);


            listView.addHeaderView(header,null,false);
            listView.setAdapter(adapter);

            db.close();

        } catch (Exception e){ db.close();  }


    }


    // ------------------------------------------------------------------------ onclick listeners


    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        RelativeLayout listItem = (RelativeLayout) v;
        TextView clickedItemView = (TextView) listItem.findViewById(R.id.txtTitle);
        TextView clickedItemView2 = (TextView) listItem.findViewById(R.id.txtTarget);
        TextView clickedItemView3 = (TextView) listItem.findViewById(R.id.txtActual);
        TextView clickedItemView4 = (TextView) listItem.findViewById(R.id.txtRemarks);

        name = clickedItemView.getText().toString();
        pos = position-1;
        t = Integer.parseInt(clickedItemView2.getText().toString().substring(10));
        a = Integer.parseInt(clickedItemView3.getText().toString().substring(10));
        remarks = clickedItemView4.getText().toString();

        Toast.makeText(getBaseContext(), name+" selected", Toast.LENGTH_LONG).show();

        updateTarget(targets.get(pos).getTargetID());

    }




    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

        RelativeLayout listItem = (RelativeLayout) v;
        TextView clickedItemView = (TextView) listItem.findViewById(R.id.txtTitle);

        name = clickedItemView.getText().toString();
        pos=position-1;


        new AlertDialog.Builder(c)
                .setTitle("Delete '" + name + "' from list?")
                        //      .setMessage("Are you sure you want to delete this entry?")


                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDelete(); //open update company
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();


        return false;

    }



    // -------------------------------------------------------------------------- open contacts for company

    public void openGoal(View view, int id) {
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
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        db.delTarget(targets.get(pos).getTargetID());


                        Toast.makeText(getBaseContext(), name+" successfully deleted", Toast.LENGTH_LONG).show();

                        targets= db.getTargets(GID);

                        adapter = new CustomTargetsAdapter(c, R.layout.listview_targets_row, targets);
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

    public void newTarget(View view) {

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);
        final TextView T = new TextView(this);
        final EditText Target = new EditText(this);
        final TextView A = new TextView(this);
        final EditText Actual = new EditText(this);
        final EditText Remarks = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Target Name");

        T.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.4f));
        T.setText("Target Quantity");
        T.setEnabled(false);

        Target.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.6f));
        Target.setInputType(InputType.TYPE_CLASS_NUMBER);
        Target.setHint("Target Quantity");

        A.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.4f));
        A.setText("Actual Quantity");

        Actual.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.6f));
        Actual.setInputType(InputType.TYPE_CLASS_NUMBER);
        Actual.setHint("Actual Quantity");

        Remarks.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Remarks.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Remarks.setHint("Remarks");

        layout.addView(Name);
        layout.addView(T);
        layout.addView(Target);
        layout.addView(A);
        layout.addView(Actual);
        layout.addView(Remarks);

        new AlertDialog.Builder(c)
                .setTitle("New Target")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        //	db.delCompanyContact(proj.get(pos).getCompanyContactsID());
                        if (Target.getText().length() < 1 ) Target.setText("1");
                        if (Actual.getText().length() < 1 ) Actual.setText("0");

                        String value = Name.getText().toString();
                        int value2 = Integer.parseInt(Target.getText().toString());
                        int value3 = Integer.parseInt(Actual.getText().toString());
                        String value4 = Remarks.getText().toString();

                        db.addTarget(GID, value,value2,value3,value4);

                        Toast.makeText(getBaseContext(), value+" successfully created", Toast.LENGTH_LONG).show();

                        targets= db.getTargets(GID);

                        adapter = new CustomTargetsAdapter(c, R.layout.listview_targets_row, targets);
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


    public void updateTarget(int id) {
        TID = id;

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText Name = new EditText(this);
        final TextView T = new TextView(this);
        final EditText Target = new EditText(this);
        final TextView A = new TextView(this);
        final EditText Actual = new EditText(this);
        final EditText Remarks = new EditText(this);

        Name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Name.setInputType(InputType.TYPE_CLASS_TEXT);
        Name.setHint("Target Name");
        Name.setText(name);

        T.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.4f));
        T.setText("Target");

        Target.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.6f));
        Target.setInputType(InputType.TYPE_CLASS_NUMBER);
        Target.setHint("Target");
        Target.setText(""+t);

        A.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.4f));
        A.setText("Actual");

        Actual.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.6f));
        Actual.setInputType(InputType.TYPE_CLASS_NUMBER);
        Actual.setHint("Actual");
        Actual.setText(""+a);

        Remarks.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Remarks.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Remarks.setHint("Remarks");
        Remarks.setText(remarks);

        layout.addView(Name);
        layout.addView(T);
        layout.addView(Target);
        layout.addView(A);
        layout.addView(Actual);
        layout.addView(Remarks);

        new AlertDialog.Builder(c)
                .setTitle("Update Target")

                .setView(layout)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with update

                        if (Target.getText().length() < 1 ) Target.setText("1");
                        if (Actual.getText().length() < 1 ) Actual.setText("0");

                        String value = Name.getText().toString();
                        int value2 = Integer.parseInt(Target.getText().toString());
                        int value3 = Integer.parseInt(Actual.getText().toString());
                        String value4 = Remarks.getText().toString();


                        db.updateTarget(TID, value,value2, value3, value4);


                        Toast.makeText(getBaseContext(), value+" successfully updated", Toast.LENGTH_LONG).show();

                        targets= db.getTargets(GID);

                        adapter = new CustomTargetsAdapter(c, R.layout.listview_targets_row, targets);
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
