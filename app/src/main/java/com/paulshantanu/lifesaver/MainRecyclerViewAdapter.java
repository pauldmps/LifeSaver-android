package com.paulshantanu.lifesaver;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shantanu Paul on 31-10-2015.
 */


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>{

    public List<User> users;
    OnItemClickListener onItemClickListener;


    MainRecyclerViewAdapter(List<User> users){
        this.users = users;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView name;
        TextView address;
        TextView bloodgroup;
        TextView distance;

        MainViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView)itemView.findViewById(R.id.rv_main);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            address = (TextView)itemView.findViewById(R.id.tv_address);
            bloodgroup = (TextView)itemView.findViewById(R.id.tv_bloodgroup);
            distance = (TextView)itemView.findViewById(R.id.tv_distance);
        }
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainViewHolder mainViewHolder, final int position) {

        mainViewHolder.name.setText(users.get(position).name);
        mainViewHolder.address.setText(users.get(position).address);
        mainViewHolder.bloodgroup.setText(users.get(position).bloodgroup);
        mainViewHolder.distance.setText(users.get(position).distance);

        mainViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnItemClickListener {
        void onClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}


