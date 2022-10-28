package com.example.display;

import androidx.recyclerview.widget.RecyclerView;

import com.example.display.databinding.ListitemLikeBinding;

public class LikeHolder extends RecyclerView.ViewHolder {
    ListitemLikeBinding binding;
    public LikeHolder(ListitemLikeBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
