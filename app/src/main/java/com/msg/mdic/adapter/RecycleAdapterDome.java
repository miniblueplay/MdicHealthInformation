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

public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
    private Context context;
    private List<String> list_date;
    private List<String> list_sys;
    private List<String> list_dia;
    private List<String> list_hr;
    private List<String> list_Hypertension;
    private List<String> list_Medicine;
    private List<Drawable> list_IV;
    private View inflater;

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView_date;
        TextView textView_sys;
        TextView textView_dia;
        TextView textView_hr;
        ImageView imageView_IV;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView_date = (TextView) itemView.findViewById(R.id.text_date);
            textView_sys = (TextView) itemView.findViewById(R.id.text_sys);
            textView_dia = (TextView) itemView.findViewById(R.id.text_dia);
            textView_hr = (TextView) itemView.findViewById(R.id.text_hr);
            imageView_IV = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    //構造方法，傳入數據
    public RecycleAdapterDome(Context context, List<String> list_date, List<String> list_sys, List<String> list_dia, List<String> list_hr, List<String>list_Hypertension, List<String> list_Medicine, List<Drawable> list_IV){
        this.context = context;
        this.list_date = list_date;
        this.list_sys = list_sys;
        this.list_dia = list_dia;
        this.list_hr = list_hr;
        this.list_Hypertension = list_Hypertension;
        this.list_Medicine = list_Medicine;
        this.list_IV = list_IV;
    }

    @NonNull
    @Override
    public RecycleAdapterDome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //創建ViewHolder，返回每一項的佈局
        inflater = LayoutInflater.from(context).inflate(R.layout.result_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterDome.MyViewHolder holder, int position) {

        int sys = Integer.parseInt(list_sys.get(position));
        int dia = Integer.parseInt(list_dia.get(position));
        int hr = Integer.parseInt(list_hr.get(position));

        //將數據和控件綁定
        holder.textView_date.setText(list_date.get(position));
        holder.textView_sys.setText(list_sys.get(position));
        holder.textView_dia.setText(list_dia.get(position));
        holder.textView_hr.setText(list_hr.get(position));
        holder.imageView_IV.setImageDrawable(list_IV.get(position));

        holder.textView_sys.setTextColor(Color.argb(255,66,66,66));
        holder.textView_dia.setTextColor(Color.argb(255,66,66,66));
        holder.textView_hr.setTextColor(Color.argb(255,66,66,66));

        //有無高血壓
        if(list_Hypertension.get(position).equals("有")){
            //有無藥物控制
            if(list_Medicine.get(position).equals("有")){
                //安全值 140/90 mmHg 100bpm
                if(sys > 140)holder.textView_sys.setTextColor(Color.RED);
                if(dia > 90)holder.textView_dia.setTextColor(Color.RED);
                if(hr > 100)holder.textView_hr.setTextColor(Color.RED);
            }else{
                //安全值 180/110 mmHg 100bpm
                if(sys > 180)holder.textView_sys.setTextColor(Color.RED);
                if(dia > 110)holder.textView_dia.setTextColor(Color.RED);
                if(hr > 100)holder.textView_hr.setTextColor(Color.RED);
            }
        }else{
            //安全值 140/90 mmHg 100bpm
            if(sys > 140)holder.textView_sys.setTextColor(Color.RED);
            if(dia > 90)holder.textView_dia.setTextColor(Color.RED);
            if(hr > 100)holder.textView_hr.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return list_date.size();
    }

}
