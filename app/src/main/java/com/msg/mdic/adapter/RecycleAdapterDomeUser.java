package com.msg.mdic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msg.mdic.R;

import java.util.List;

public class RecycleAdapterDomeUser extends RecyclerView.Adapter<RecycleAdapterDomeUser.MyViewHolder> {
    private Context context;
    private List<String> CardID;
    private List<String> cname;
    private View inflater;

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView_CardID;
        TextView textView_cname;
        public MyViewHolder(View itemView) {
            super(itemView);
            //textView_CardID = (TextView) itemView.findViewById(R.id.text_date);
            textView_cname = (TextView) itemView.findViewById(R.id.name);
        }
    }

    //構造方法，傳入數據
    public RecycleAdapterDomeUser(Context context, List<String> CardID, List<String> list_sys){
        this.context = context;
        this.CardID = CardID;
        this.cname = list_sys;
    }

    @NonNull
    @Override
    public RecycleAdapterDomeUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //創建ViewHolder，返回每一項的佈局
        inflater = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterDomeUser.MyViewHolder holder, int position) {
        //將數據和控件綁定
        //holder.textView_date.setText(CardID.get(position));
        holder.textView_cname.setText(cname.get(position));
    }

    @Override
    public int getItemCount() {
        return CardID.size();
    }

}
