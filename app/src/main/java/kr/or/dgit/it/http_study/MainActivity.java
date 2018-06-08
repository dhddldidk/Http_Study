package kr.or.dgit.it.http_study;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = findViewById(R.id.recyclerView);

        //전개 방법
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);

        //어댑터
        list.setLayoutManager(lm);

        String[] titles = getResources().getStringArray(R.array.titles);
        String[] clses = getResources().getStringArray(R.array.classes);

        List<Item> items = new ArrayList<>();
        for(int i=0; i<titles.length; i++){
            items.add(new Item(titles[i], clses[i]));
        }

        ListAdapter adapter = new ListAdapter(items);
        list.setAdapter(adapter);
    }

    class Item{

        String title;
        String className;

        public Item(String title, String className) {
            this.title = title;
            this.className = className;
        }
    }

    private class ListAdapter extends RecyclerView.Adapter{

        private class ViewHolder extends RecyclerView.ViewHolder{
            private TextView titleTv;

            public ViewHolder(View itemView) {
                super(itemView);
                titleTv = itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //포지션을 알아야 함
                        int position = getAdapterPosition();
                        Item item = items.get(position);//선택된 목록에 대한 포지션 가져오기


                        //이 intent 때문에 ViewHolder를 어댑터 안에 선언한거임
                        Intent intent = new Intent();
                        intent.setClassName(getPackageName(), getPackageName()+"."+item.className);
                        intent.putExtra("title", item.title);
                        startActivity(intent);
                    }
                });
            }
        }


        //item 담을
        List<Item> items;

        public ListAdapter(List<Item> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.titleTv.setText(items.get(position).title);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
