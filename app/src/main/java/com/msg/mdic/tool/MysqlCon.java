/**
 * 引用網址：https://mnya.tw/cc/word/1480.html
 */
package com.msg.mdic.tool;

import android.util.Log;
import com.msg.mdic.category.userdata;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MysqlCon {
    private static final String TAG = "MysqlCon";

    //連線IP定義
    private String mysql_ip = "http://120.117.53.18";    //資料庫IP  南台："http://120.117.53.18" 成大："https://health.mdic.ncku.edu.tw"

    //AES加密金鑰
    private static  final String mstrTestKey = "vkdogkgmbcpabfvm"; //密碼
    private static  final String mstrIvParameter = "2274125221369975"; //密碼偏移量

    //連線狀態
    boolean connect = false;


    public void run() {
        Log.i(TAG+" DB","連接中");
        try {
            URL url = new URL(mysql_ip + "/Connectinfo.php");
            // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
            Log.d(TAG+" url:", String.valueOf(url));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 建立 Google 比較挺的 HttpURLConnection 物件
            connection.setRequestMethod("POST");
            // 設定連線方式為 POST
            connection.setDoOutput(true); // 允許輸出
            connection.setDoInput(true); // 允許讀入
            connection.setUseCaches(false); // 不使用快取
            connection.connect(); // 開始連線

            int responseCode = connection.getResponseCode();
            // 建立取得回應的物件
            Log.d(TAG+" debug:", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.v("DB","遠端連接成功");
                connect =true;
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG+" DB","遠端連接失敗");
            Log.d(TAG+" DB", e.toString());
            connect = false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.d(TAG+" DB","遠端連接失敗");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG+" DB","遠端連接失敗");
        }
    }

    public void getData(String input_id) {
        Log.v(TAG+" ID",input_id);
        userdata resultUserdata = new userdata();
        try {
            URL url = new URL(mysql_ip + "/Login.php?CardID=" + input_id);
            // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
            Log.d(TAG+" URL", String.valueOf(url));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 建立 Google 比較挺的 HttpURLConnection 物件
            connection.setRequestMethod("POST");
            // 設定連線方式為 POST
            connection.setDoOutput(true); // 允許輸出
            connection.setDoInput(true); // 允許讀入
            connection.setUseCaches(false); // 不使用快取
            connection.connect(); // 開始連線

            int responseCode = connection.getResponseCode();
            // 建立取得回應的物件
            Log.d(TAG+" DB", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 如果 HTTP 回傳狀態是 OK ，而不是 Error
                InputStream inputStream = connection.getInputStream();
                // 取得輸入串流
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                // 讀取輸入串流的資料
                String line = null; // 宣告讀取用的字串
                while ((line = bufReader.readLine()) != null) {
                    Log.d(TAG+" LINE",line);
                    JSONObject jobject = new JSONObject(line);
                    JSONObject result1 = jobject.getJSONObject("result1");
                    resultUserdata.Name = result1.getString("cname");
                    resultUserdata.CardID = result1.getString("CardID");
                    resultUserdata.Gender = result1.getString("gender");
                    resultUserdata.Hypertension = result1.getString("Hypertension");
                    resultUserdata.Medicine = result1.getString("Medicine");
                    resultUserdata.birthday = result1.getString("birthday");

                    JSONArray result2 = jobject.getJSONArray("result2");
                    for(int i=0;i<result2.length();i++){
                        JSONObject data = result2.getJSONObject(i);
                        Map<String,String> test = new HashMap<String,String>();
                        String type = data.getString("type");
                        String height = data.getString("height");
                        String weight = data.getString("weight");
                        test.put("type",type);
                        test.put("height",height);
                        test.put("weight",weight);
                        resultUserdata.test.put(type,test);
                    }
                }
                inputStream.close(); // 關閉輸入串流
            }

            if(!resultUserdata.test.get("後測").get("height").equals("null"))
                resultUserdata.Height = resultUserdata.test.get("後測").get("height");
            else
                resultUserdata.Height = resultUserdata.test.get("前測").get("height");
            if(!resultUserdata.test.get("後測").get("weight").equals("null"))
                resultUserdata.Weight = resultUserdata.test.get("後測").get("weight");
            else
                resultUserdata.Weight = resultUserdata.test.get("前測").get("weight");

            // 讀取輸入串流並存到字串的部分
            // 取得資料後想用不同的格式
            // 例如 Json 等等，都是在這一段做處理
        } catch (Exception e) {
            Log.d(TAG+" getData","取得資料失敗");
            Log.d(TAG+" DB", e.toString());
        }
    }

    public void insertData(String name, String ID, String Date, int SYS, int DIA, int HR, String Res) {
        Log.i(TAG+" insertData","寫入資料");
        try {
            String CNAME = URLEncoder.encode(name, "utf-8");
            String data = ("cname="+CNAME+"&CardID="+ID+"&Date="+Date+"&SYS="+SYS+"&DIA="+DIA+"&HR="+HR+"&Res="+Res);
            String AesData = AESCBCUtils.encrypt_AES(mstrTestKey, data, mstrIvParameter);
            new Thread(() -> {
                try {
                    Log.v(TAG+" aesEncrypt", AesData);
                    Log.v(TAG+" decrypt", AESCBCUtils.decrypt(mstrTestKey, AesData, mstrIvParameter));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            URL url = new URL( mysql_ip +"/AddData.php?data="+AesData);
            Log.d(TAG+" url:", String.valueOf(url));
            // 開始宣告 HTTP 連線需要的物件
            // 建立 HttpURLConnection 物件
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 設定連線方式為 POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // 允許輸出
            connection.setDoInput(true); // 允許讀入
            connection.setUseCaches(false); // 不使用快取
            connection.connect(); // 開始連線
            // 建立取得回應的物件
            int responseCode = connection.getResponseCode();

            Log.d(TAG+" DB", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.v(TAG+" 寫入資料完成", "Name="+name+"CardID="+ID+"Date="+Date+"SYS="+SYS+"DIA="+DIA+"HR="+HR+"Res="+Res);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG+" DB", "寫入資料失敗");
            Log.e(TAG+" DB", e.toString());
        }
    }

    public void login_information(String ip){
        Log.i(TAG+" login_information","已設定連線IP");
        mysql_ip = ip;
    }

    public boolean getConnect(){
        Log.i(TAG+" getConnect","取得連線狀態");
        return connect;
    }
}

