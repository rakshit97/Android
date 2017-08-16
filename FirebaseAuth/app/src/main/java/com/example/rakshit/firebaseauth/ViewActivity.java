package com.example.rakshit.firebaseauth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity
{
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    String currUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference currentUserRef = reference.child("users").child(currUID);
    DatabaseReference expensesRef = reference.child("expenses");
    ArrayList<Object> adapterList = new ArrayList<>();
    ArrayList<String> eidList = new ArrayList<>();
    ListView listView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.lv_expenses);
        adapter = new ListAdapter(this, adapterList);
        listView.setAdapter(adapter);


        Class c = POJOExpense.class;

        Class[] fields1 = c.getDeclaredConstructors()[0].getParameterTypes();
        try
        {
            Object o = c.getDeclaredConstructors()[0].newInstance("abc", 22, 1, 1, 1213);
            adapter.add(o);
        }
        catch (Exception e)
        {

        }

        for(Class clazz : fields1)
            Log.e("viewww", clazz.getSimpleName());

        fetchData1();
    }

    private void fetchData1()
    {
        currentUserRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Log.e("view", "in child added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Log.e("view", "in child changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Log.e("view", "in child removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
                Log.e("view", "in child moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e("view", "in child cancelled");
            }
        });
    }

    private void fetchData()
    {
        currentUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for(DataSnapshot child : iter)
                {
                    Log.e("view", child.getKey() + " " + child.getValue());

                    if (!eidList.contains(child.getKey()))
                    {
                        eidList.add(child.getKey());

                        DatabaseReference childRef = expensesRef.child(child.getKey());
                        childRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                                String name = (String)dataSnapshot.child("name").getValue();
                                double cost = Double.valueOf(dataSnapshot.child("cost").getValue().toString());
                                int category = Integer.valueOf(dataSnapshot.child("category").getValue().toString());
                                int payment = Integer.valueOf(dataSnapshot.child("payment").getValue().toString());
                                long timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());

                                adapterList.add(new POJOExpense(name, cost, category, payment, timestamp));
                                adapter.notifyDataSetChanged();

                                for (DataSnapshot snapshot : iterable)
                                {
                                    //Log.e("view", snapshot.getKey() + " " + snapshot.getValue());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
