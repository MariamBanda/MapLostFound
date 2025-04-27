package com.prac.lostfound;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewItemsActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Advert> advertList;
    ArrayAdapter<String> adapter;
    ArrayList<String> advertNames;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        listView = findViewById(R.id.listViewItems);

        db = new DatabaseHelper(this);

        advertList = db.getAllAdverts();
        advertNames = new ArrayList<>();
        for (Advert a : advertList) {
            advertNames.add(a.getPostType() + " " + a.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, advertNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("id", advertList.get(i).getId());
            startActivityForResult(intent, 1);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            reloadList();
        }
    }

    private void reloadList() {
        advertList = db.getAllAdverts();
        advertNames.clear();
        for (Advert a : advertList) {
            advertNames.add(a.getPostType() + " " + a.getName());
        }
        adapter.notifyDataSetChanged();
    }

}
