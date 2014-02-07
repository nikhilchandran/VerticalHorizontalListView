package com.example.verticalhorizontallistview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.twoway.TwoWayView;
import com.example.twoway.TwoWayView.OnScrollListener;

public class MainActivity extends Activity {

	private ListView mListView;
	private List<POJOData> mVerticalList = new ArrayList<POJOData>();
	private List<String> mHorizontalList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		for (int j = 1; j <= 50; j++) {
			mHorizontalList = new ArrayList<String>();
			for (int i = 1; i <= 50; i++) {
				mHorizontalList.add("Item " + i);
			}
			mVerticalList.add(new POJOData(mHorizontalList, 0, 0));
		}

		mListView = (ListView) findViewById(R.id.mListView);
		MyAdapter adapter = new MyAdapter();
		mListView.setAdapter(adapter);
	}
	
	class MyAdapter extends BaseAdapter implements OnScrollListener
	{
		LayoutInflater inflater;
		
		public MyAdapter() {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return mVerticalList.size();
		}

		@Override
		public Object getItem(int position) {
			return mVerticalList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder{
			TwoWayView mTwoWayView;
			MyTwoWayAdapter adapter;
			TextView roomname;
			TextView roomcount;
			ImageView left,right;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder holder;
			if(convertView == null){
				
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.vertical_row, parent, false);
				holder.mTwoWayView = (TwoWayView) convertView.findViewById(R.id.mTwoWayView);
				holder.roomname = (TextView) convertView.findViewById(R.id.mylife_titles);
				holder.roomcount = (TextView) convertView.findViewById(R.id.mylife_titles_count);
				holder.left = (ImageView) convertView.findViewById(R.id.left_arrow_imageview);
				holder.right = (ImageView) convertView.findViewById(R.id.right_arrow_imageview);
				holder.mTwoWayView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(TwoWayView view, int scrollState) {
						// TODO Auto-generated method stub
						Log.d("Scroll State change", ""+scrollState);
					}
					
					@Override
					public void onScroll(TwoWayView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						//Log.d("Scroll", ""+visibleItemCount);
						Log.d("firstVisibleItem", ""+firstVisibleItem);
						if(firstVisibleItem==0)
						{
							holder.left.setVisibility(View.INVISIBLE);
							holder.right.setVisibility(View.VISIBLE);
						}else if(firstVisibleItem==(totalItemCount-4))
						{
							holder.left.setVisibility(View.VISIBLE);
							holder.right.setVisibility(View.INVISIBLE);
						}
						else{
							holder.left.setVisibility(View.VISIBLE);
							holder.right.setVisibility(View.VISIBLE);
						}
					}
				});
			
				holder.adapter = new MyTwoWayAdapter();
				holder.mTwoWayView.setAdapter(holder.adapter);
				
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.mTwoWayView.setTag(position);
			
			holder.adapter.setData(mVerticalList.get(position).getmList());// notify Adapter with data
			holder.mTwoWayView.setSelectionFromOffset(mVerticalList.get(position).getIndex(), mVerticalList.get(position).getOffSet()); // setting the exact state of ListView as scrolled previously
			holder.roomname.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alert = new AlertDialog.Builder(
							MainActivity.this);
					//alert.setTitle(status);
					alert.setMessage("Text Long Click");

					alert.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									
								}
									 
							});
				
					AlertDialog alertDialog = alert.create();
					alertDialog.setCancelable(true);
					alertDialog.show();
					return false;
				}
			});
			
			holder.roomname.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Room name", Toast.LENGTH_SHORT).show();
				}
			});
			
			
			return convertView;
		}

		@Override
		public void onScrollStateChanged(TwoWayView view, int scrollState) {
			
			if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
				// do nothing
			}
			else{
				// set the previous state of ListView as scrolled
				
				int index = view.getFirstVisiblePosition();
				View v = view.getChildAt(0);
				int left = (v == null) ? 0 : v.getLeft();
				
				mVerticalList.get((Integer)view.getTag()).setIndex(index);
				mVerticalList.get((Integer)view.getTag()).setOffSet(left);
			}
		}

		@Override
		public void onScroll(TwoWayView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Log.d("firstVisibleItem", ""+firstVisibleItem+":"+visibleItemCount+""+totalItemCount);
		}
	}
	
	class MyTwoWayAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		
		public MyTwoWayAdapter() {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		private void setData(List<String> mList) {
			mHorizontalList = mList;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return mHorizontalList.size();
		}

		@Override
		public Object getItem(int position) {
			return mHorizontalList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder{
			TextView mTextView;
			ImageView image;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView == null){
				
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.horizontal_row, parent, false);
				holder.mTextView = (TextView) convertView.findViewById(R.id.mTextView);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position % 2 == 0){
				holder.mTextView.setBackgroundColor(Color.BLUE);
				holder.mTextView.setTextColor(Color.YELLOW);
			}
			else{
				holder.mTextView.setBackgroundColor(Color.YELLOW);
				holder.mTextView.setTextColor(Color.BLUE);
			}
			holder.mTextView.setText(mHorizontalList.get(position).toString());
			holder.image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Image position: "+position, Toast.LENGTH_SHORT).show();
				}
			});
			holder.image.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alert = new AlertDialog.Builder(
							MainActivity.this);
					//alert.setTitle(status);
					alert.setMessage("Image Long Click");

					alert.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									
								}
									 
							});
				
					AlertDialog alertDialog = alert.create();
					alertDialog.setCancelable(true);
					alertDialog.show();
					return false;
					
				}
			});
          holder.mTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Text position: "+position, Toast.LENGTH_SHORT).show();
				}
			});
			return convertView;
		}
	}
	
	
}
