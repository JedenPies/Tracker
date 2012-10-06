package pl.jedenpies.android.tracker.list;

import pl.jedenpies.android.tracker.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class JPListItemFactory {

	public static class JPCheckBoxListItem extends JPListItem {

		@Override
		public int getItemViewType() {
			return R.layout.jp_list_item_checkbox;
		}
	}
	
	public static class JPIconListItem extends JPListItem {

		protected Integer iconId;

		@Override
		public int getItemViewType() {
			return R.layout.jp_list_item_icon;
		}		
	}
	
	public static class JPDialogListItem extends JPIconListItem {
		
		protected int dialogId;
		
		@Override
		public int getItemViewType() {
			return R.layout.jp_list_item_icon;
		}
		
		public int getDialogId() {
			return dialogId;
		}
	}
	
	public static JPListItem checkBoxListItem(String title, String details) {
		JPCheckBoxListItem item = new JPCheckBoxListItem();
		item.setTitle(title);
		item.setDetails(details);
		return item;
	}
	public static JPListItem iconListItem(String title, String details, Integer iconId) {
		JPIconListItem item = new JPIconListItem();		
		item.setTitle(title);
		item.setDetails(details);
		item.iconId = iconId;
		return item;
	}
	public static JPListItem dialogListItem(String title, String details, Integer iconId, int dialogId) {
		JPDialogListItem item = new JPDialogListItem();
		item.title = title;
		item.details = details;
		item.dialogId = dialogId;
		item.iconId = iconId;
		return item;
	}
	
	public static View createView(LayoutInflater inflater, JPListItem item, View convertView, ViewGroup parent) {
		
		if (item instanceof JPCheckBoxListItem) {
			return createView(inflater, (JPCheckBoxListItem) item, convertView, parent);
		}
		if (item instanceof JPIconListItem) {
			return createView(inflater, (JPIconListItem) item, convertView, parent);
		}
		if (item instanceof JPDialogListItem) {
			return createView(inflater, (JPDialogListItem) item, convertView, parent);
		}
		return null;
	}
	
	private static View createView(LayoutInflater inflater, JPCheckBoxListItem item, View convertView, ViewGroup parent) {
		
		View view;
		if (convertView == null) {
		} else {
			view = convertView;
		}
		view = inflater.inflate(item.getItemViewType(), parent, false);						
		TextView title = (TextView) view.findViewById(R.id.jp_list_item_checkbox_title);
		TextView details = (TextView) view.findViewById(R.id.jp_list_item_checkbox_details);
		title.setText(item.getTitle());
		details.setText(item.getDetails());
		return view;
	}
	
	private static View createView(LayoutInflater inflater, JPIconListItem item, View convertView, ViewGroup parent) {
		
		View view;
		if (convertView == null) {
		} else {
			view = convertView;
		}
		view = inflater.inflate(item.getItemViewType(), parent, false);						
		TextView title = (TextView) view.findViewById(R.id.jp_list_item_icon_title);
		TextView details = (TextView) view.findViewById(R.id.jp_list_item_icon_details);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		title.setText(item.getTitle());
		details.setText(item.getDetails());
		if (item.iconId != null) icon.setImageResource(item.iconId);
		return view;
	}
	
private static View createView(LayoutInflater inflater, JPDialogListItem item, View convertView, ViewGroup parent) {
		
		View view;
		if (convertView == null) {
		} else {
			view = convertView;
		}
		view = inflater.inflate(item.getItemViewType(), parent, false);						
		TextView title = (TextView) view.findViewById(R.id.jp_list_item_icon_title);
		TextView details = (TextView) view.findViewById(R.id.jp_list_item_icon_details);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		title.setText(item.getTitle());
		details.setText(item.getDetails());
		if (item.iconId != null) icon.setImageResource(item.iconId);
		view.setClickable(false);
		return view;
	}
}
