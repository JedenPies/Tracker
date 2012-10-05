package pl.jedenpies.android.tracker.list;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class JPListAdapter extends ArrayAdapter<JPListItem> {

	private List<JPListItem> items;
	private Context context;
	
	public JPListAdapter(Context context, List<JPListItem> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
	}
	
	@Override
    public int getItemViewType(int position) {
        return items.get(position).getItemViewType();
    }
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public JPListItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return JPListItemFactory.createView(li, getItem(position), convertView, parent);
	}
	
	@Override
    public int getViewTypeCount() {
		Set<Integer> types = new HashSet<Integer>();
    	for (JPListItem item : items) {
    		types.add(item.getItemViewType());
    	}
        return types.size();
    }
    
    public boolean isEmpty() {
        return getCount() == 0;
    }

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

    
}
