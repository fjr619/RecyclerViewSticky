package com.example.recyclerviewsticky;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import library.StickyHeadersBuilder;
import library.StickyHeadersItemDecoration;

import com.example.recyclerviewsticky.adapter.BigramHeaderAdapter;
import com.example.recyclerviewsticky.adapter.PersonAdapter;
import com.example.recyclerviewsticky.data.Person;
import com.example.recyclerviewsticky.data.PersonDataProvider;
import com.example.recyclerviewsticky.listener.OnRemoveListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnRemoveListener, ActionMode.Callback {

	private PersonDataProvider personDataProvider;
	private PersonAdapter personAdapter;
	private RecyclerView list;
	private int countAdd = 0;
	private ActionMode actionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (RecyclerView) findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

		personDataProvider = new PersonDataProvider();
		personAdapter = new PersonAdapter(personDataProvider, this);

		StickyHeadersItemDecoration top = new StickyHeadersBuilder().setAdapter(personAdapter).setRecyclerView(list).setStickyHeadersAdapter(new BigramHeaderAdapter(personDataProvider.getItems()))
				.build();
		list.setAdapter(personAdapter);
		list.addItemDecoration(top);
		list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

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
			countAdd++;
			Person p = new Person();
			p.setName("new name " + countAdd);
			p.setAge(30);
			personAdapter.addChild(p);

			int posAfterInsert = personDataProvider.getItems().indexOf(p);
			list.scrollToPosition(posAfterInsert);

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

		personAdapter.removeChild(position);

	}

	@Override
	public void onLongPressRemove(int position) {
		actionMode = startActionMode(this);
		myToggleSelection(position);
	}

	private void myToggleSelection(int position) {
		personAdapter.toggleSelection(position);
		String title = getString(R.string.selected_count, personAdapter.getSelectedItemCount());
		actionMode.setTitle(title);
	}

	@Override
	
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		MenuInflater inflater = actionMode.getMenuInflater();
		inflater.inflate(R.menu.delete_menu, menu);
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
			List<Integer> selectedItemPositions = personAdapter.getSelectedItems();
			int currPos;

//			selectedItemPositions = a(selectedItemPositions);
			
			for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
				currPos = selectedItemPositions.get(i);
				Log.i("TAG", "currPos delete =" + currPos);
				personAdapter.removeChild(currPos);
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
		personAdapter.clearSelections();
	}
}
