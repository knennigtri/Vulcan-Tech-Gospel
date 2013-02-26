package com.nennig.vulcan.tech.gospel;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;

public class SearchListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ArrayList<String> listItems=new ArrayList<String>();
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.id.search_results_list, listItems);
        setListAdapter(aa);
        
      //This creates a listener for the Files listed
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	String memFolder = aa.getItem(position);
              
            }
        });
        
        //This allows for an optional deletion of the folder
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				
				return false;
			}
        	
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_list, menu);
        return true;
    }
}
