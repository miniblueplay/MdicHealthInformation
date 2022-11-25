package com.msg.mdic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msg.mdic.R;

import java.util.List;

public class RecycleAdapterDomeUser extends RecyclerView.Adapter<RecycleAdapterDomeUser.MyViewHolder> {
    private Context context;
    private List<String> CardID;
    private List<String> cname;
    private List<String> Birthday;
    private View inflater;

    // 宣告interface
    private OnItemClickHandler mClickHandler;

    // 建立interface，命名為OnItemClickHandler，並在裡面寫好我們要發生的事件
    public interface OnItemClickHandler {
        // 提供onItemClick方法作為點擊事件，括號內為接受的參數
        void onItemClick(String text);
        // 提供onItemRemove做為移除項目的事件
        void onItemRemove(int position, String text);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView_birthday;
        TextView textView_cname;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView_birthday = (TextView) itemView.findViewById(R.id.birthday);
            textView_cname = (TextView) itemView.findViewById(R.id.name);

            // 點擊項目時
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = CardID.get(getAdapterPosition());
                    // 呼叫interface的method
                    mClickHandler.onItemClick(msg);
                }
            });
        }
    }

    //構造方法，傳入數據
    public RecycleAdapterDomeUser(Context context, List<String> CardID, List<String> Cname, List<String> Birthday, OnItemClickHandler clickHandler){
        this.context = context;
        this.CardID = CardID;
        this.cname = Cname;
        this.Birthday = Birthday;
        mClickHandler = clickHandler;
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
        holder.textView_birthday.setText(Birthday.get(position));
        holder.textView_cname.setText(cname.get(position));
    }

    @Override
    public int getItemCount() {
        return CardID.size();
    }

}
