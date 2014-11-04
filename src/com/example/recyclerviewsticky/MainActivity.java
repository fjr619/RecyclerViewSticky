package com.example.recyclerviewsticky;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import library.StickyHeadersBuilder;
import library.StickyHeadersItemDecoration;
import library2.StickyRecyclerHeadersDecoration;

import com.example.recyclerviewsticky.adapter.BigramHeaderAdapter;
import com.example.recyclerviewsticky.adapter.PersonAdapter;
import com.example.recyclerviewsticky.animator.ScaleInOutItemAnimator;
import com.example.recyclerviewsticky.animator.SlideInOutBottomItemAnimator;
import com.example.recyclerviewsticky.data.Person;
import com.example.recyclerviewsticky.data.PersonDataProvider;
import com.example.recyclerviewsticky.listener.OnRemoveListener;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator;
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

	private PersonDataProvider personDataProvider;
	private PersonAdapter personAdapter;
	private RecyclerView list;
	private int countAdd = 0;
	private ActionMode actionMode;
	private ImageView iv;
	private StickyHeadersItemDecoration top;
	private StickyRecyclerHeadersDecoration decors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (RecyclerView) findViewById(R.id.list);

		setupSpinner();

		personDataProvider = new PersonDataProvider();
		personAdapter = new PersonAdapter(personDataProvider, this);
		list.setHasFixedSize(true);

		int orientation;
		if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			orientation = LinearLayoutManager.VERTICAL;
		} else {
			orientation = LinearLayoutManager.HORIZONTAL;
		}

		list.setLayoutManager(new LinearLayoutManager(MainActivity.this, orientation, false));
		// list.setLayoutManager(new LinearLayoutManager(this));
		// top = new
		// StickyHeadersBuilder().setAdapter(personAdapter).setRecyclerView(list).setStickyHeadersAdapter(new
		// BigramHeaderAdapter(personDataProvider.getItems()))
		// .build();
		// list.addItemDecoration(top);

		decors = new StickyRecyclerHeadersDecoration(personAdapter);
		list.addItemDecoration(decors);

//		list.addItemDecoration(new DividerItemDecoration(this));
		list.setItemAnimator(new DefaultItemAnimator());
//		list.setItemAnimator(new ScaleInOutItemAnimator(list));
		list.setAdapter(personAdapter);

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
			List<Integer> selectedItemPositions = personAdapter.getSelectedItems();
			int currPos;

			for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
				currPos = selectedItemPositions.get(i);
				Log.i("TAG", "currPos delete =" + currPos);
				personDataProvider.remove(currPos);
				personAdapter.notifyItemRemoved(currPos);
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
		// iv.setVisibility(View.VISIBLE);
	}

	private void add() {
		countAdd++;
		Person p = new Person();
		p.setName("new name " + countAdd);
		p.setAge(30);
		personAdapter.addChild(p);

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
				switch (position) {
				case 0:
					list.removeItemDecoration(decors);
					list.addItemDecoration(decors);
					personAdapter.notifyDataSetChanged();
					break;
				case 1:
					list.removeItemDecoration(decors);
					personAdapter.notifyDataSetChanged();
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	}
}
