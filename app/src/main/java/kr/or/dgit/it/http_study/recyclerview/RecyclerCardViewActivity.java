package kr.or.dgit.it.http_study.recyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.it.http_study.R;

public class RecyclerCardViewActivity extends AppCompatActivity implements RecyclerAdapter.OnSelectedItem, ItemFragment.OnUpdateItem, ItemFragment.OnInsertItem{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<Item> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_card_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
//      layoutManager = new GridLayoutManager(this, 2);
//      layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        lists = getItemLists();
        adapter = new RecyclerAdapter(lists, this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                ItemFragment newItem = ItemFragment.getInstance(null, position);
                newItem.show(getSupportFragmentManager(), "new");
            }
        });

        FloatingActionButton fab02 = (FloatingActionButton) findViewById(R.id.fab02);
        fab02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=lists.size()-1; i>-1;i--){
                    if (lists.get(i).isChecked()){
                        lists.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, lists.size());
                    }
                }
            }
        });
    }

    private List<Item> getItemLists() {
        String[] titles = {
                "Chapter One", "Chapter Two",
                "Chapter Three", "Chapter Four",
                "Chapter Five", "Chapter Six",
                "Chapter Seven", "Chapter Eight"};

        String[] details = {
                "Item one details",  "Item two details", "Item three details",
                "Item four details", "Item file details", "Item six details",
                "Item seven details","Item eight details"};

        int[] images = {
                R.drawable.android_image_1,  R.drawable.android_image_2,
                R.drawable.android_image_3,  R.drawable.android_image_4,
                R.drawable.android_image_5,  R.drawable.android_image_6,
                R.drawable.android_image_7,  R.drawable.android_image_8 };

        List<Item> items = new ArrayList<>();
        for(int i=0; i<titles.length; i++){
            items.add(new Item(titles[i], details[i], images[i]));
        }
        return items;
    }
    @Override
    public void onUpdateItem(Item item, int position) {
        lists.set(position, item);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onInsertItem(Item item, int position) {
        lists.add(position, item);
        adapter.notifyItemInserted(position);
        adapter.notifyItemRangeChanged(0, lists.size());
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void onItemSelected(Item item, int position) {
        ItemFragment fr = ItemFragment.getInstance(item, position);
        fr.show(getSupportFragmentManager(), "updateItem");
    }
}
