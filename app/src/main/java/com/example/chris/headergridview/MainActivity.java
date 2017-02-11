package com.example.chris.headergridview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private HeaderGridView mGv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGv = (HeaderGridView) findViewById(R.id.headerGridView);

        GvAdapter adapter = new GvAdapter();

//        ImageView imageView = new ImageView(this);
//        imageView.setBackgroundResource(R.drawable.newscar);
//        ImageView.LayoutParams params = imageView.getLayoutParams();
//        params.height=200;
//        params.width =100;
//        imageView.setLayoutParams(params);

        View headerView = View.inflate(this,R.layout.header,null);

        mGv.addHeaderView(headerView);

        mGv.setAdapter(adapter);

    }

    public class GvAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = View.inflate(MainActivity.this,R.layout.item,null);

            return v;
        }
    }
}
