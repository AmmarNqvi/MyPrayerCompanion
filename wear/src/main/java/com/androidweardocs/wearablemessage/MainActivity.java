package com.androidweardocs.wearablemessage;


//Class implementing the MainActivity of the app
//This is the entry point of the app which displays a listwiew
//Selections lead to 3 main classes of the app: MessageActivity(nearest mosque), prayers(Prayer Timings) & Qibla(Qibla Direction)


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements WearableListView.ClickListener{

    private List<ListViewItem> viewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);


        WearableListView wearableListView = (WearableListView) findViewById(R.id.wearable_list_view);

        viewItemList.add(new ListViewItem(R.drawable.mosque, "Nearest Mosque"));
        viewItemList.add(new ListViewItem(R.drawable.timing, "Prayer Timings"));
        viewItemList.add(new ListViewItem(R.drawable.qibs, "Qibla Direction"));

        wearableListView.setAdapter(new ListViewAdapter(this, viewItemList));
        wearableListView.setClickListener(this);

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        String Selection= viewItemList.get(viewHolder.getPosition()).text;

        if(Selection=="Prayer Timings"){
            Context c=getApplicationContext();
            Intent myIntent = new Intent();
            myIntent.setClass(c, prayers.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            c.startActivity(myIntent);
        }
        if(Selection=="Nearest Mosque"){
            Context c=getApplicationContext();
            Intent myIntent = new Intent();
            myIntent.setClass(c,MessageActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            c.startActivity(myIntent);
        }
        if(Selection=="Qibla Direction"){
            Context c=getApplicationContext();
            Intent myIntent = new Intent();
            myIntent.setClass(c,Qibla.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            c.startActivity(myIntent);
        }

    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}

