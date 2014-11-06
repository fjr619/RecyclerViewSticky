package com.example.recyclerviewsticky;

import java.util.List;

import library.StickyHeadersBuilder;
import library.StickyHeadersItemDecoration;
import library2.StickyRecyclerHeadersDecoration;

import com.example.recyclerviewsticky.adapter.lib1.BigramHeaderAdapter;
import com.example.recyclerviewsticky.adapter.lib1.PersonAdapter_Library1;
import com.example.recyclerviewsticky.adapter.lib2.PersonAdapter_Library2;
import com.example.recyclerviewsticky.data.Person;
import com.example.recyclerviewsticky.data.PersonDataProvider;
import com.example.recyclerviewsticky.listener.OnRemoveListener;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnRemoveListener, ActionMode.Callback {

	private boolean USE_LIB1 = true;
	
	private PersonDataProvider personDataProvider;
//	private PersonAdapter personAdapter;
	private RecyclerView list;
	private int countAdd = 0;
	private ActionMode actionMode;
	private ImageView iv;
	
	private PersonAdapter_Library1 adapter_Library1;
	private PersonAdapter_Library2 adapter_Library2;
	
	private StickyHeadersItemDecoration topHeader_lib1;
	private StickyRecyclerHeadersDecoration topHeader_lib2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (RecyclerView) findViewById(R.id.list);

		setupSpinner();

		personDataProvider = new PersonDataProvider();
//		personAdapter = new PersonAdapter(personDataProvider, this);
		adapter_Library1 = new PersonAdapter_Library1(personDataProvider, this);
		adapter_Library2 = new PersonAdapter_Library2(personDataProvider, this);
		list.setHasFixedSize(true);

		int orientation;
		if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			orientation = LinearLayoutManager.VERTICAL;
		} else {
			orientation = LinearLayoutManager.HORIZONTAL;
		}

		list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
		
		topHeader_lib1 = new StickyHeadersBuilder().setAdapter(adapter_Library1).setRecyclerView(list).setStickyHeadersAdapter(new BigramHeaderAdapter(personDataProvider.getItems())).build();
		topHeader_lib2 = new StickyRecyclerHeadersDecoration(adapter_Library2);

		list.setItemAnimator(new DefaultItemAnimator());
		
		if(USE_LIB1){
			list.addItemDecoration(topHeader_lib1);
			list.setAdapter(adapter_Library1);
		}else{
			list.addItemDecoration(topHeader_lib2);
			list.setAdapter(adapter_Library2);
		}
//		list.setAdapter(personAdapter);

		iv = (ImageView) findViewById(R.id.fab);
		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add_item) {
			add();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRemove(int position) {
		if (actionMode != null) {
			myToggleSelection(position);
			return;
		}

		if(USE_LIB1){
			adapter_Library1.removeChild(position);
		}else{
			adapter_Library2.removeChild(position);
		}
//		personAdapter.removeChild(position);
	}

	@Override
	public void onLongPressRemove(int position) {

		actionMode = startActionMode(this);
		myToggleSelection(position);

	}

	private void myToggleSelection(int position) {
		String title = "";
		if(USE_LIB1){
			adapter_Library1.toggleSelection(position);
			title = getString(R.string.selected_count, adapter_Library1.getSelectedItemCount());
		}else{
			adapter_Library2.toggleSelection(position);
			title = getString(R.string.selected_count, adapter_Library2.getSelectedItemCount());
		}
		
//		personAdapter.toggleSelection(position);
//		String title = getString(R.string.selected_count, personAdapter.getSelectedItemCount());
		actionMode.setTitle(title);
	}

	@Override
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		MenuInflater inflater = actionMode.getMenuInflater();
		inflater.inflate(R.menu.delete_menu, menu);
		// iv.setVisibility(View.GONE);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.menu_delete:
			List<Integer> selectedItemPositions = null;
			if(USE_LIB1){
				selectedItemPositions = adapter_Library1.getSelectedItems();
			}else{
				selectedItemPositions = adapter_Library2.getSelectedItems();
			}
			
//			List<Integer> selectedItemPositions = personAdapter.getSelectedItems();
			int currPos;

			for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
				currPos = selectedItemPositions.get(i);
				Log.i("TAG", "currPos delete =" + currPos);
				personDataProvider.remove(currPos);
				if(USE_LIB1){
					adapter_Library1.notifyItemRemoved(currPos);
				}else{
					adapter_Library2.notifyItemRemoved(currPos);
				}
//				personAdapter.notifyItemRemoved(currPos);
			}

			actionMode.finish();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		this.actionMode = null;
		if(USE_LIB1){
			adapter_Library1.clearSelections();
		}else{
			adapter_Library2.clearSelections();
		}
//		personAdapter.clearSelections();
	}

	private void add() {
		countAdd++;
		Person p = new Person();
		p.setName("new name " + countAdd);
		p.setAge(30);
		
		if(USE_LIB1){
			adapter_Library1.addChild(p);
		}else{
			adapter_Library2.addChild(p);
		}
//		personAdapter.addChild(p);

		int posAfterInsert = personDataProvider.getItems().indexOf(p);
		list.scrollToPosition(posAfterInsert);
	}

	private void setupSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				list.removeItemDecoration(topHeader_lib1);
				list.removeItemDecoration(topHeader_lib2);
				switch (position) {
				case 0:
					USE_LIB1 = true;
					list.addItemDecoration(topHeader_lib1);
					list.setAdapter(adapter_Library1);
					break;
				case 1:
					USE_LIB1 = false;
					list.addItemDecoration(topHeader_lib2);
					list.setAdapter(adapter_Library2);
					break;
				}
				adapter_Library1.notifyDataSetChanged();
				adapter_Library2.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	}
}
