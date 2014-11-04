package com.example.recyclerviewsticky.adapter.lib1;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recyclerviewsticky.R;
import com.example.recyclerviewsticky.data.Person;

import library.StickyHeadersAdapter;

public class BigramHeaderAdapter implements StickyHeadersAdapter<BigramHeaderAdapter.ViewHolder> {

    private List<Person> items;

    public BigramHeaderAdapter(List<Person> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_header, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, final int position) {
        headerViewHolder.title.setText("Age "+items.get(position).getAge());
        headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("TAG","header Age"+items.get(position).getAge());
			}
		});
    }

    @Override
    public long getHeaderId(int position) {
        return items.get(position).getAge();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.letter);
        }
    }
}