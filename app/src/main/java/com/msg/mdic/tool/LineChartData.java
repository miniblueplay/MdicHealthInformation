package com.msg.mdic.tool;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class LineChartData {
    Context context;
    LineChart lineChart;

    public LineChartData(LineChart lineChart, Context context){
        this.context = context;
        this.lineChart = lineChart;
    }

    public void initDataSet(ArrayList<Entry> valuesY) {
        if(valuesY.size()>0){
            for(int i = 0 ;i<valuesY.size();i++){
                final LineDataSet set;
                set = new LineDataSet(valuesY, "");
                set.setMode(LineDataSet.Mode.LINEAR);//類型為折線
                //set.setColor(Color.GREEN);//線的顏色
                String str = valuesY.get(i).toString();
                Log.i("admin var", str.substring(str.indexOf("y: ")+3));
                //if(Float.parseFloat(str.substring(str.indexOf("y: ")+3)) > 130) {
                //    set.setCircleColor(Color.RED);//圓點顏色
                //    set.setColor(Color.RED);//線的顏色
                //    Log.i("admin color", "RED");
                //}else if(Float.parseFloat(str.substring(str.indexOf("y: ")+3)) < 120) {
                //    set.setCircleColor(Color.BLUE);//圓點顏色
                //    set.setColor(Color.BLUE);//線的顏色
                //    Log.i("admin color", "BLUE");
                //}else{
                //    set.setCircleColor(Color.GREEN);//圓點顏色
                //    set.setColor(Color.GREEN);//線的顏色
                //    Log.i("admin color", "GREEN");
                //}
                set.setCircleColor(Color.GREEN);//圓點顏色
                set.setColor(Color.GREEN);//線的顏色
                set.setCircleRadius(5);//圓點大小
                set.setDrawCircleHole(true);//圓點為實心(預設空心)
                set.setLineWidth(1.5f);//線寬
                set.setDrawValues(true);//顯示座標點對應Y軸的數字(預設顯示)
                set.setValueTextSize(12);//座標點數字大小
                set.setValueFormatter(new DefaultValueFormatter(0));//座標點數字的小數位數0位

                Legend legend = lineChart.getLegend();
                legend.setEnabled(false);//不顯示圖例
                Description description = lineChart.getDescription();
                description.setEnabled(false);//不顯示Description Label (預設顯示)

                LineData data = new LineData(set);
                lineChart.setData(data);//一定要放在最後
            }
        }else{
            lineChart.setNoDataText("暫時沒有數據");
            lineChart.setNoDataTextColor(Color.BLUE);//文字顏色
        }
        lineChart.invalidate();//繪製圖表
    }

    public void initX(ArrayList dateList) {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
        xAxis.setTextSize(12);//X軸標籤大小

        xAxis.setLabelCount(dateList.size());//X軸標籤個數
        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離

        xAxis.setDrawGridLines(false);//不顯示每個座標點對應X軸的線 (預設顯示)

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
    }

    public void initY(Float min, Float max) {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        leftAxis.setLabelCount(4);//Y軸標籤個數
        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

        leftAxis.setAxisMinimum(min-10);//Y軸標籤最小值
        leftAxis.setAxisMaximum(max+10);//Y軸標籤最大值

        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
    }

    class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###");//Y軸數值格式及小數點位數
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }
}