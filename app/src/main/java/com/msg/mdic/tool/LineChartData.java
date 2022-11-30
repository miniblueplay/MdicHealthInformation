package com.msg.mdic.tool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.msg.mdic.R;

import java.util.ArrayList;
import java.util.List;

public class LineChartData {
    Context context;
    LineChart lineChart;
    private Handler mHandler = new Handler();

    public LineChartData(LineChart lineChart, Context context){
        this.context = context;
        this.lineChart = lineChart;
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
    }

    /**轴线的绘制 */
    private void drawAxis(AxisBase mAxis){
        //设置是否启用轴线：如果关闭那么就默认没有轴线/标签/网格线
        mAxis.setEnabled(true);
        //设置是否开启绘制轴的标签
        mAxis.setDrawLabels(true);
        //是否绘制轴线
        mAxis.setDrawAxisLine(true);
        //是否绘制网格线
        //mAxis.setDrawGridLines(true);
    }

    /**限制線*/
    public void LimitLine(int color, int high, String name){
        LimitLine ll1 = new LimitLine(high, name + " " + high);
        ll1.setLineWidth(2f);//線寬
        ll1.enableDashedLine(10f, 10f, 0f);//虛線
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);//位置
        ll1.setTextSize(10f);
        ll1.setTextColor(color);
        ll1.setLineColor(color);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.addLimitLine(ll1);
    }

    public void initDataSet(ArrayList<Entry> values_sys, ArrayList<Entry> values_dia, ArrayList<Entry> values_hr, ArrayList<Entry> valuesend_sys, ArrayList<Entry> valuesend_dia, ArrayList<Entry> valuesend_hr, boolean show_hr) {
        if(values_sys.size()>0 && values_dia.size()>0){
            final LineDataSet set_sys, set_dia, set_hr, setend_sys, setend_dia, setend_hr;

            int textSize = 18;//座標點數字大小

            //sys yellow
            set_sys = new LineDataSet(values_sys, "");
            set_sys.setMode(LineDataSet.Mode.LINEAR); //類型為折線
            set_sys.setHighlightEnabled(true);//禁用點擊交點後顯示高亮線 (預設顯示)
            set_sys.setColor(ContextCompat.getColor(context, R.color.yellow));//線的顏色
            set_sys.setLineWidth(2f);//線寬
            //set_sys.setDrawCircles(false); //不顯示相應座標點的小圓圈(預設顯示)
            set_sys.setValueTextSize(textSize);//座標點數字大小
            set_sys.setValueTextColor(ContextCompat.getColor(context, R.color.yellow));//字的顏色
            set_sys.setDrawValues(true);//顯示座標點對應Y軸的數字(預設顯示)
            //sys yellow 最後的圓點
            setend_sys = new LineDataSet(valuesend_sys, "");
            setend_sys.setCircleColor(ContextCompat.getColor(context, R.color.yellow));//圓點顏色
            setend_sys.setColor(ContextCompat.getColor(context, R.color.yellow));//線的顏色
            setend_sys.setCircleRadius(4);//圓點大小
            setend_sys.setDrawCircleHole(true);//圓點false實心,true空心
            setend_sys.setDrawValues(false);//不顯示座標點對應Y軸的數字(預設顯示)

            //dia green
            set_dia = new LineDataSet(values_dia, "");
            set_dia.setMode(LineDataSet.Mode.LINEAR); //類型為折線
            set_dia.setHighlightEnabled(true);//禁用點擊交點後顯示高亮線 (預設顯示)
            set_dia.setColor(ContextCompat.getColor(context, R.color.green));//線的顏色
            set_dia.setLineWidth(2f);//線寬
            //set_dia.setDrawCircles(false); //不顯示相應座標點的小圓圈(預設顯示)
            set_dia.setValueTextSize(textSize);//座標點數字大小
            set_dia.setValueTextColor(ContextCompat.getColor(context, R.color.green));//字的顏色
            set_dia.setDrawValues(true);//顯示座標點對應Y軸的數字(預設顯示)
            //dia green 最後的圓點
            setend_dia = new LineDataSet(valuesend_dia, "");
            setend_dia.setCircleColor(ContextCompat.getColor(context, R.color.green));//圓點顏色
            setend_dia.setColor(ContextCompat.getColor(context, R.color.green));//線的顏色
            setend_dia.setCircleRadius(4);//圓點大小
            setend_dia.setDrawCircleHole(true);//圓點false實心,true空心
            setend_dia.setDrawValues(false);//不顯示座標點對應Y軸的數字(預設顯示)

            //hr red
            set_hr = new LineDataSet(values_hr, "");
            set_hr.setMode(LineDataSet.Mode.LINEAR); //類型為折線
            set_hr.setHighlightEnabled(true);//禁用點擊交點後顯示高亮線 (預設顯示)
            set_hr.setColor(ContextCompat.getColor(context, R.color.red));//線的顏色
            set_hr.setLineWidth(2f);//線寬
            //set_hr.setDrawCircles(false); //不顯示相應座標點的小圓圈(預設顯示)
            set_hr.setValueTextSize(textSize);//座標點數字大小
            set_hr.setValueTextColor(ContextCompat.getColor(context, R.color.red));//字的顏色
            set_hr.setDrawValues(true);//顯示座標點對應Y軸的數字(預設顯示)
            //hr red 最後的圓點
            setend_hr = new LineDataSet(valuesend_hr, "");
            setend_hr.setCircleColor(ContextCompat.getColor(context, R.color.red));//圓點顏色
            setend_hr.setColor(ContextCompat.getColor(context, R.color.red));//線的顏色
            setend_hr.setCircleRadius(4);//圓點大小
            setend_hr.setDrawCircleHole(true);//圓點false實心,true空心
            setend_hr.setDrawValues(false);//不顯示座標點對應Y軸的數字(預設顯示)

            LineData data;

            //是否顯示心率
            if(show_hr){
                //多條線的集合
                data = new LineData(set_sys, set_dia, set_hr, setend_sys, setend_dia, setend_hr);
            }else{
                //多條線的集合
                data = new LineData(set_sys, set_dia, setend_sys, setend_dia);
            }

            //右下方description label：設置圖表資訊
            Description description = lineChart.getDescription();
            description.setEnabled(false);//不顯示Description Label (預設顯示)

            //左下方Legend：圖例數據資料
            Legend legend = lineChart.getLegend();
            legend.setEnabled(false);//不顯示圖例 (預設顯示)

            lineChart.setData(data);//一定要放在最後
        }else{
            lineChart.setNoDataText("暫時沒有數據");
            lineChart.setNoDataTextColor(Color.RED);//文字顏色
        }
        lineChart.invalidate();//繪製圖表

        //通过选中监听,来实现不点击图表后1秒,定位线自动消失
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mHandler.removeCallbacks(hideHighLight);
                mHandler.postDelayed(hideHighLight,1000);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void initX(ArrayList dateList) {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
        xAxis.setTextSize(10);//X軸標籤大小

        xAxis.setLabelCount(dateList.size());//X軸標籤個數
        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離

        xAxis.setDrawGridLines(false);//不顯示每個座標點對應X軸的線 (預設顯示)

        xAxis.setGranularity(1f);//座標最小間距(文字不重疊)

        //設置一頁最大顯示個數爲6，超出部分就滑動
        float ratio = (float) dateList.size() / (float) 5;
        //顯示的時候是按照多大的比率縮放顯示,1f表示不放大縮小
        lineChart.zoom(ratio, 0f, 0, 0);
        if (dateList.size()>0){
            xAxis.setValueFormatter(new StringAxisValueFormatter(dateList));
        }

        //xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
    }

    //自定義X軸數據
    public class StringAxisValueFormatter implements IAxisValueFormatter {

        private List<String> mTimeList;

        public StringAxisValueFormatter(List<String> mTimeList) {
            this.mTimeList = mTimeList;
        }

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            if (v < 0 || v > (mTimeList.size() - 1)) {//使得兩側柱子完全顯示
                return "";
            }
            return mTimeList.get((int) v);
        }
    }

    public void initY(int min, int max) {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        //rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        //左側的軸線
        leftAxis.setLabelCount(5);//Y軸標籤個數
        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小
        leftAxis.setAxisMinimum(min);//Y軸標籤最小值
        leftAxis.setAxisMaximum(max);//Y軸標籤最大值
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        //右側的軸線
        rightAxis.setLabelCount(5);//Y軸標籤個數
        rightAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        rightAxis.setTextSize(12);//Y軸標籤大小
        rightAxis.setAxisMinimum(min);//Y軸標籤最小值
        rightAxis.setAxisMaximum(max);//Y軸標籤最大值
        rightAxis.setValueFormatter(new MyYAxisValueFormatter());
    }

    Runnable hideHighLight = new Runnable() {
        @Override
        public void run() {
            lineChart.highlightValue(null);
        }
    };

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