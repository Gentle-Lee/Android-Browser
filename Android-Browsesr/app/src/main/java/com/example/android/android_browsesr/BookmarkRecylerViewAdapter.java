package com.example.android.android_browsesr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tommy on 2017/6/27.
 */

public class BookmarkRecylerViewAdapter extends RecyclerView.Adapter<BookmarkRecylerViewAdapter.MyViewHolder> {
    private final String TAG = "BookmarkRecylerViewAdapter";
    private static ArrayList<String> mDataset;
    TextViewListener tvListener;
    interface TextViewListener{
        void clickListen(String urlString);
    }
    TextViewListener textViewListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView deleteButton;
        public TextView mTextView;
        public MyViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            deleteButton = (ImageView)v.findViewById(R.id.delete_btn);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteOpenHelper dbHelper = new BrowserDBHelper(view.getContext(),"webDB",null,2);
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    sqLiteDatabase.delete("BOOKMARK_DB","BookmarkUrl = ?",new String[]{mTextView.getText().toString()});
                    sqLiteDatabase.close();
                    Toast.makeText(view.getContext(),"Delete~",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookmarkRecylerViewAdapter(Context context,TextViewListener tvListener) {
        mDataset = new ArrayList<>();
        this.tvListener = tvListener;
        BrowserDBHelper dbHelper = new BrowserDBHelper(context,"webDB",null,2);

        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("BOOKMARK_DB",null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String BookmarkUrl = cursor.getString(cursor.getColumnIndex("BookmarkUrl"));
            mDataset.add(BookmarkUrl);
        }
        cursor.close();
        //关闭数据库
        sqliteDatabase.close();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookmarkRecylerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final MyViewHolder vh = new MyViewHolder(v);
        vh.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvListener.clickListen(vh.mTextView.getText().toString());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
