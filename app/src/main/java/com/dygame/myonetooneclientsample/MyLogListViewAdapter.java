package com.dygame.myonetooneclientsample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/25.
 * It s a tool to use ListViewAdapter for print LogCat.
 * 用法:
 * protected MyLogListViewAdapter pListViewAdapter ;
 * //set adapter
 * pListViewAdapter.getInstance().create(this);
 * //set listView
 * pListView.setAdapter(pListViewAdapter.getInstance());
 */
public class MyLogListViewAdapter extends BaseAdapter
{
    protected LayoutInflater inflater;
    protected Context mContext ;
    protected ArrayList<String> LogList = new ArrayList<String>();
    protected static MyLogListViewAdapter _instance = null;

    public MyLogListViewAdapter()
    {

    }
    //get self
    public static MyLogListViewAdapter getInstance()
    {
        if(_instance != null)
        {
            return _instance;
        }
        synchronized (MyLogListViewAdapter.class)
        {
            if(_instance == null)
            {
                _instance = new MyLogListViewAdapter();
            }
        }
        return _instance;
    }

    @Override
    public int getCount() { return LogList.size(); }

    @Override
    public Object getItem(int position) { return LogList.get(position); }

    @Override
    public long getItemId(int position) { return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_item, null);
        }
        TextView tv = (TextView)convertView.findViewById(R.id.textViewItem);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageViewLogo);
        tv.setText((String) LogList.get(position));
//          iv.setImageDrawable(arrayAppinfo.get(position).getAppIcon());
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        return convertView;
    }

    public void create(Context context)
    {
        mContext = context ;
        inflater = LayoutInflater.from(context);
    }

    public void addLog(String sLog)
    {
        LogList.add(sLog) ;
        notifyDataSetChanged() ;
    }
}
