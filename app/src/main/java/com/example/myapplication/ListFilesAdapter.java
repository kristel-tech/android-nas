package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class ListFilesAdapter extends RecyclerView.Adapter<ListFilesAdapter.MyViewHolder> {

    private List<fileObject> mData;
    private ArrayList<fileObject> filesList;
    private LayoutInflater mInflater;
    private int itemCount;
    private ListItemListener mListitemListener;
//    private ItemClickListener mClickListener;
    private Context context;

    public ListFilesAdapter(List<fileObject> filesList,ListItemListener listitemlistener) {
        this.mData = filesList;
        this.itemCount = filesList.size();
        this.mListitemListener = listitemlistener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView fileNameText;
        private TextView fileSizeText;
        ListItemListener listItemListener;

        public MyViewHolder(final View view, ListItemListener listItemListener) {
            super(view);
            fileNameText = view.findViewById(R.id.textView9);
            fileSizeText = view.findViewById(R.id.textView11);
            this.listItemListener = listItemListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        listItemListener.onListItemCLick(getAdapterPosition());
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ListFilesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(itemView, mListitemListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        fileObject fileName = mData.get(position);
        holder.fileNameText.setText(fileName.getFileName());
        holder.fileSizeText.setText(fileName.getFileSize());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    public interface ListItemListener{
        void onListItemCLick(int position);
    }
}

    // stores and recycles views as they are scrolled off screen
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView myTextView;
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            myTextView = itemView.findViewById(R.id.fileName);
//            itemView.setOnClickListener(this);
//        }

//        @Override
//        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//        }
//    }

//     convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
//    void setClickListener(Callback<fileObject> itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }

    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
//}


