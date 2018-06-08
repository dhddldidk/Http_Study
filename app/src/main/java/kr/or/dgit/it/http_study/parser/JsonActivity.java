package kr.or.dgit.it.http_study.parser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.or.dgit.it.http_study.R;
import kr.or.dgit.it.http_study.volley.RequestQueueSingleton;

public class JsonActivity extends AppCompatActivity {

    public static final String REQ_TAG = "JsonActivity";

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        setTitle(getIntent().getStringExtra("title"));

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

        getJsonArrayData();
    }

    private void getJsonArrayData() {
        String url = "http://192.168.0.69:8080/androidHTTPTest/order.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, getResponseJsonArrayListener(), errorListener);
        jsonArrayRequest.setTag(REQ_TAG);
        requestQueue.add(jsonArrayRequest);
    }

    private Response.Listener<JSONArray> getResponseJsonArrayListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Item> items = new ArrayList<>();


                    try {
                        for(int i = 0; i<response.length(); i++) {
                            JSONObject jsonItem = response.getJSONObject(i);
                            Item item = new Item();
                            item.setMakerName(jsonItem.getString("Maker"));
                            item.setItemName(jsonItem.getString("Product"));
                            item.setItemPrice(jsonItem.getInt("Price"));
                            items.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                recyclerView.setAdapter(new RecyclerAdapter(items));
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue !=null){
            requestQueue.cancelAll(REQ_TAG);
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {}
    };
}
