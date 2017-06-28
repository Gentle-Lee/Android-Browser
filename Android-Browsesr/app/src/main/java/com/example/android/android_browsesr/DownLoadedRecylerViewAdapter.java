package com.example.android.android_browsesr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tommy on 2017/6/28.
 */

public class DownLoadedRecylerViewAdapter extends RecyclerView.Adapter<DownLoadedRecylerViewAdapter.MyViewHolder> {
    private final String TAG = "DownLoadedRecylerViewAdapter";
    private static ArrayList<String> fileNameList;
    private static ArrayList<String> filePathList;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fileName,filePath;
        public MyViewHolder(View v) {
            super(v);
            fileName = (TextView) v.findViewById(R.id.fileName);
            filePath = v.findViewById(R.id.filePath);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DownLoadedRecylerViewAdapter(Context context) {
        fileNameList = new ArrayList<>();
        filePathList = new ArrayList<>();
        BrowserDBHelper dbHelper = new BrowserDBHelper(context,"DOWNLOADED_DB",null,2);

        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("DOWNLOADED_DB",null, null, null, null, null, null);

        if (cursor.getCount()<0)
            return ;
        while (cursor.moveToNext()) {
            String fileNameString = cursor.getString(cursor.getColumnIndex("FileName"));
            fileNameList.add(fileNameString);
            String filePathString = cursor.getString(cursor.getColumnIndex("FilePath"));
            filePathList.add(filePathString);
        }
        cursor.close();
        //关闭数据库
        sqliteDatabase.close();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DownLoadedRecylerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.downloaded_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.fileName.setText(fileNameList.get(position));
        holder.filePath.setText(filePathList.get(position));
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }
}
