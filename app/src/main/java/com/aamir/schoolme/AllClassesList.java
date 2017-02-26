package com.aamir.schoolme;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class AllClassesList extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

	ListView courcesListView;

	@RequiresApi ( api = Build.VERSION_CODES.M )
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_all_classes_list );
		Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
		setSupportActionBar( toolbar );

		FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById( R.id.fab );
		floatingActionButton.setImageResource( R.mipmap.ic_add_white_24dp );
		floatingActionButton.setBackgroundColor( getColor( R.color.colorPrimary ) );
		floatingActionButton.setOnClickListener( this );

		//AUTO GENERATED ONCE DB IS INTERGRATED
		ArrayList< Course > course = new ArrayList<>();
		course.add( new Course( "Programming Software Studio", "CS 126", 4, new HashMap< String, String >(), 3.0 ) );
		course.add( new Course( "Computer Systems and Programming", "ECE 220", 3, new HashMap< String, String >(), 2.5 ) );

		courcesListView = (ListView) findViewById( R.id.LV_courses );
		courcesListView.setAdapter( new ListViewCustomAdapter( this, course ) );

		courcesListView.setOnItemClickListener( this );

	}

	@Override
	public void onClick( View view ) {
		switch ( view.getId() ) {
			case R.id.fab:
				//Add Indent to Add Course
				//Log.d("Testing","ONCLICK");
				break;
			default:
				//Log.d("Testing","THIS DOESNT WORK! :(");
		}

	}

	@Override
	public void onItemClick( AdapterView< ? > parent, View view, int position, long id ) {
		//Log.d("Testing","ONITEMCLICK WAS CALLED:" + parent.getChildAt( position ).getTag().toString());
		Intent intent = new Intent( this, CourseDescription.class );
		intent.putExtra( "current_course", parent.getChildAt( position ).getTag().toString() );
		startActivity( intent );

	}
}
