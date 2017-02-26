package com.aamir.schoolme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aamir on 25/02/17.
 */

public class ListViewCustomAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	ArrayList< Course > cources;
	Context context;

	public ListViewCustomAdapter( AllClassesList allClassesList, ArrayList< Course > courses ) {

		this.cources = courses;
		context = allClassesList;
		inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

	}

	@Override
	public int getCount() {
		return cources.size();
	}

	@Override
	public Object getItem( int position ) {
		return cources.get( position );
	}

	@Override
	public long getItemId( int position ) {
		return position;
	}

	@Override
	public View getView( final int position, View convertView, ViewGroup parent ) {
		ViewHolder viewHolder = new ViewHolder();
		View row;

		row = inflater.inflate( R.layout.course_list_item, null );

		viewHolder.name = (TextView) row.findViewById( R.id.courseName );
		viewHolder.code = (TextView) row.findViewById( R.id.courseCode );

		viewHolder.name.setText( cources.get( position ).getName() );
		viewHolder.code.setText( cources.get( position ).getCode() );
		row.setTag( cources.get( position ).getName() );
		return row;
	}

	public class ViewHolder {
		TextView name, code;
	}

}