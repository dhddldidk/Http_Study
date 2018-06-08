package kr.or.dgit.it.http_study.parser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import kr.or.dgit.it.http_study.R;
import kr.or.dgit.it.http_study.volley.RequestQueueSingleton;

public class XmlPullActivity extends AppCompatActivity {

    public static final String REQ_TAG = "XmlPullActivity";

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

        getStringRequest();
    }

    private void getStringRequest() {
        String url = "http://192.168.0.69:8080/androidHTTPTest/order.xml";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        stringRequest.setTag(REQ_TAG);
        requestQueue.add(stringRequest);
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            recyclerView.setAdapter(new RecyclerAdapter(parsingXml(response)));
        }
    };

    public ArrayList<Item> parsingXml(String xml) {
        ArrayList<Item> arItem = new ArrayList<Item>();
        Item itemClass = null;
        boolean initem = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.TEXT:
                        if (initem) {
                            itemClass.setItemName(parser.getText());
                            initem = false;
                        }
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            initem = true;
                            itemClass = new Item();
                        }
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equalsIgnoreCase("price")) {
                                itemClass.setItemPrice(Integer.parseInt(parser.getAttributeValue(i)));
                            } else {
                                itemClass.setMakerName(parser.getAttributeValue(i));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            arItem.add(itemClass);
                        }
                        break;
                }// end of switch
                eventType = parser.next();
            }// end of while
        } catch (Exception e) {
            Log.i("Pull_Parser", e.getMessage());
        }
        return arItem;
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
