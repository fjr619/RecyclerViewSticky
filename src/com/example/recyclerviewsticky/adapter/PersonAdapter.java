package com.example.recyclerviewsticky.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.recyclerviewsticky.R;
import com.example.recyclerviewsticky.data.Person;
import com.example.recyclerviewsticky.data.PersonDataProvider;
import com.example.recyclerviewsticky.listener.OnRemoveListener;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> /*
																				 * implements
																				 * OnRemoveListener
																				 */{

	private List<Person> items;
	private PersonDataProvider personDataProvider;
	private OnRemoveListener onRemoveListener;
	private SparseBooleanArray selectedItems;

	public PersonAdapter(PersonDataProvider personDataProvider, OnRemoveListener onRemoveListener) {
		this.personDataProvider = personDataProvider;
		this.items = personDataProvider.getItems();
		this.onRemoveListener = onRemoveListener;

		setHasStableIds(true);
		selectedItems = new SparseBooleanArray();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

		return new ViewHolder(itemView/* , this */);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getName().hashCode();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		viewHolder.label.setText(items.get(position).getName() + " " + items.get(position).getAge());
		viewHolder.itemView.setTag(viewHolder);
		viewHolder.itemView.setOnClickListener(childOnClick);
		viewHolder.itemView.setOnLongClickListener(childLongClick);
		viewHolder.itemView.setActivated(selectedItems.get(position,false));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private OnClickListener childOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewHolder vh = (ViewHolder) v.getTag();
			int position = vh.getPosition();
			onRemoveListener.onRemove(position);
		}
	};

	private OnLongClickListener childLongClick = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			ViewHolder vh = (ViewHolder) v.getTag();
			int position = vh.getPosition();
			onRemoveListener.onLongPressRemove(position);
			return true;
		}
	};

	public void removeChild(int position) {
		personDataProvider.remove(position);
		notifyItemRemoved(position);
	}

	public void addChild(Person p) {
		personDataProvider.add(p);
		int newChildPos = items.indexOf(p);
		notifyItemInserted(newChildPos);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder /*
																	 * implements
																	 * View.
																	 * OnClickListener
																	 */{

		private TextView label;

		public ViewHolder(View itemView/* , OnRemoveListener listener */) {
			super(itemView);
			this.label = (TextView) itemView.findViewById(R.id.name);

		}
	}

	public void toggleSelection(int pos) {
		if (selectedItems.get(pos, false)) {
			selectedItems.delete(pos);
		} else {
			selectedItems.put(pos, true);
		}
		notifyItemChanged(pos);
	}

	public void clearSelections() {
		selectedItems.clear();
		notifyDataSetChanged();
	}

	public int getSelectedItemCount() {
		return selectedItems.size();
	}

	public List<Integer> getSelectedItems() {
		List<Integer> items = new ArrayList<Integer>(selectedItems.size());
		for (int i = 0; i < selectedItems.size(); i++) {
			items.add(selectedItems.keyAt(i));
		}
		return items;
	}

}
