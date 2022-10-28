package com.example.display;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.display.data.LikeDao;
import com.example.display.data.LikeData;
import com.example.display.data.RoomDB;
import com.example.display.databinding.ListitemLikeBinding;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeHolder>{
    ListitemLikeBinding binding;
    private List<LikeData> dataList;
    private Activity context;
    private RoomDB database;
    private LikeDao likeDao;
    private OnItemClickListener itemClickListener;
    int mPosition ;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    public LikeAdapter(List<LikeData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
    }


    @NonNull
    @Override
    public LikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ListitemLikeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LikeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeHolder holder, @SuppressLint("RecyclerView") int position) {
        final LikeData data = dataList.get(holder.getAdapterPosition());
        database = RoomDB.getInstance(context);
        binding.tvLikeList.setText(position + " ) " + data.getText());
        binding.tvLikeList.setTextColor(data.getTextColor());
        binding.tvLikeList.setBackgroundColor(data.getBackColor());
        binding.ivDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LikeData likeData = dataList.get(holder.getAdapterPosition());
                database.likeDao().delete(likeData);
                mPosition = holder.getAdapterPosition();
                dataList.remove(mPosition);
                notifyItemRemoved(mPosition);
                notifyItemRangeChanged(mPosition, dataList.size());
            }
        });

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "okCheck", Toast.LENGTH_SHORT).show();
                int pos = holder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    //동작 호출 (onItemClick 함수 호출)
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(view, pos);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private LikeDao getLikeDao() {
        if (likeDao == null) {
            likeDao = RoomDB.getInstance(context.getApplicationContext()).likeDao();
        }
        return likeDao;
    }
}
