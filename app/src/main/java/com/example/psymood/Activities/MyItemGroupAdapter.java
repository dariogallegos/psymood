package com.example.psymood.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.psymood.Models.ItemData;
import com.example.psymood.Models.ItemGroup;
import com.example.psymood.R;

import java.util.List;

public class MyItemGroupAdapter extends RecyclerView.Adapter<MyItemGroupAdapter.MyViewHolder> {

    private Context context;
    private List<ItemGroup> itemGroupList;

    public MyItemGroupAdapter(Context context, List<ItemGroup> itemGroupList) {
        this.context = context;
        this.itemGroupList = itemGroupList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_group, viewGroup , false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String item_tittle = itemGroupList.get(i).getTitle();
        myViewHolder.item_title.setText(item_tittle);

        List<ItemData> itemDataList = itemGroupList.get(i).getItemList();

        MyItemListAdapter itemListAdapter =  new MyItemListAdapter(context,itemDataList);
        myViewHolder.recyclerView_itemList.setHasFixedSize(true);
        myViewHolder.recyclerView_itemList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        myViewHolder.recyclerView_itemList.setAdapter(itemListAdapter);
        myViewHolder.recyclerView_itemList.setNestedScrollingEnabled(false);//Importante, para que no se pisen las vistas

    }

    @Override
    public int getItemCount() {
        return (itemGroupList != null ? itemGroupList.size() : 0) ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        RecyclerView recyclerView_itemList;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            recyclerView_itemList = itemView.findViewById(R.id.recyclerView_itemGrop);

        }
    }
}