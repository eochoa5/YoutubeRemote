package com.example.edwin.youtuberemote;

/**
 * Created by Edwin on 6/3/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter{
    ArrayList<Video> videoList;
    Context context;

    private static LayoutInflater inflater=null;
    public ListAdapter(MainActivity mainActivity, ArrayList<Video> videoList) {
        // TODO Auto-generated constructor stub
        this.videoList = videoList;
        context = mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tvTitle;
        TextView tvUploaderDuration;
        TextView tvViews;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.videos_listview, null);
        holder.tvTitle=(TextView) rowView.findViewById(R.id.textViewTitle);
        holder.tvUploaderDuration=(TextView) rowView.findViewById(R.id.textViewUploaderDuration);
        holder.tvViews=(TextView) rowView.findViewById(R.id.textViewViews);

        holder.tvTitle.setText(videoList.get(position).getTitle());

        String uploader = videoList.get(position).getUploader();
        String duration = videoList.get(position).getDuration();

        holder.tvUploaderDuration.setText("Uploader: " + uploader + " " + duration);
        holder.tvViews.setText(videoList.get(position).getViews());
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ControllerActivity.class);
                i.putExtra("vidTitle", videoList.get(position).getTitle());
                i.putExtra("vidURL", videoList.get(position).getURL());
                context.startActivity(i);

            }
        });
        return rowView;
    }

}