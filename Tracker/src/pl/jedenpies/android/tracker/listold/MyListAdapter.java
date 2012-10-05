package pl.jedenpies.android.tracker.listold;

import java.util.List;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.list.JPListItem;
import pl.jedenpies.android.tracker.list.JPListItemFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<JPListItem> {

	private int layoutResourceId;
	private Context context;
	
	public MyListAdapter(Context context, int layoutResourceId, List<JPListItem> objects) {
		super(context, 0, objects);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		JPListItem item = getItem(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            		
		return JPListItemFactory.createView(inflater, item, convertView, parent);
		
//		View row = convertView;
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            
//		row = inflater.inflate(R.layout.my_list_row, parent, false);
//		TextView tx1 = (TextView) row.findViewById(R.id.txtTitle1);
//		TextView tx2 = (TextView) row.findViewById(R.id.txtTitle2);
//		tx1.setText(getItem(position).getTitle());
//		tx2.setText(getItem(position).getDescription());
//		return row;
    }

}
