package pl.jedenpies.android.tracker.listold;

import java.util.ArrayList;
import java.util.List;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.list.JPListAdapter;
import pl.jedenpies.android.tracker.list.JPListItem;
import pl.jedenpies.android.tracker.list.JPListItemFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MyListActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_list);
        
        List<JPListItem> items = new ArrayList<JPListItem>();
        items.add(JPListItemFactory.checkBoxListItem("Tytul 1", "Opis 1"));
//        items.add(JPListItemFactory.iconListItem("Tytul 4", "Opis 4"));
        items.add(JPListItemFactory.checkBoxListItem("Tytul 2", "Opis 2"));
//        items.add(JPListItemFactory.iconListItem("Tytul 3", "Opis 3"));
//        
//        JPListAdapter adapter = new JPListAdapter(this, items);
//        ListView listView1 = (ListView) findViewById(R.id.my_list_view);
//        listView1.setAdapter(adapter);
        
//        List<MyListItem> items = new ArrayList<MyListItem>();
//        items.add(new MyListItem("Tytul 1", "Opis 1"));
//        items.add(new MyListItem("Tytul 2", "Opis 2"));
        
        MyListAdapter adapter = new MyListAdapter(this, R.layout.my_list_row, items);
        ListView listView1 = (ListView) findViewById(R.id.my_list_view);
        listView1.setAdapter(adapter);
    }
}
