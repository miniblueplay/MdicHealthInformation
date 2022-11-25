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
import java.util.List;
import java.util.Map;

public class MysqlCon {
    private static final String TAG = "MysqlCon";

    //連線IP定義
    private String mysql_ip = "https://health.mdic.ncku.edu.tw";
    //資料庫IP  南台："http://120.117.53.18" 成大："https://health.mdic.ncku.edu.tw"

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

    public String getData(String php, String name,  String id, String lad) {
        Log.v(TAG+" php", php + " ID = " + id);
        userdata resultUserdata = new userdata();
        try {
            URL url = new URL(mysql_ip + "/" + php + ".php?" + name + "=" + id);
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
                    if(!lad.equals("height") && !lad.equals("weight"))
                    {
                        JSONObject result1 = jobject.getJSONObject("result1");
                        try {
                            String da = result1.getString(lad);
                            Log.d(TAG+" getData",da);
                            return da;
                        } catch (Exception e) {
                            Log.d(TAG+" getData","查無資料");
                            return "null";
                        }
                    } else {
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

                        if(!resultUserdata.test.get("後測").get("height").equals("null"))
                            resultUserdata.Height = resultUserdata.test.get("後測").get("height");
                        else
                            resultUserdata.Height = resultUserdata.test.get("前測").get("height");
                        if(!resultUserdata.test.get("後測").get("weight").equals("null"))
                            resultUserdata.Weight = resultUserdata.test.get("後測").get("weight");
                        else
                            resultUserdata.Weight = resultUserdata.test.get("前測").get("weight");

                        if(lad.equals("height"))return resultUserdata.Height;
                        else return resultUserdata.Weight;
                    }
                }
                inputStream.close(); // 關閉輸入串流
            }
            Log.d(TAG+" getData","查無資料");
            return "null";
        } catch (Exception e) {
            Log.d(TAG+" getData","取得資料失敗");
            Log.d(TAG+" DB", e.toString());
            return "null";
        }
    }

    public Map<String,String> getDataArray(String php, String lab) {
        Log.v(TAG+" php", php);
        Map<String,String> ret = new HashMap<>();
        try {
            URL url = new URL(mysql_ip + "/" + php + ".php?");
            if(lab.length() == 10) //場域
                url = new URL(mysql_ip + "/" + php + ".php?field_id=" + lab);
            else if(lab.length() == 8) //卡片ID
                url = new URL(mysql_ip + "/" + php + ".php?CardID=" + lab);
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

                    if(php.equals("Field_id")) {
                        JSONArray result1 = jobject.getJSONArray("result1");
                        for(int i=0;i<result1.length();i++){
                            JSONObject data = result1.getJSONObject(i);
                            String field_id = data.getString("field_id");
                            String field_name = data.getString("field_name");
                            if(field_name.length() != 0 && field_id.length() != 0){
                                ret.put(field_id,field_name);
                                Log.i(TAG+" field_name", field_name + " field_id : " + field_id);
                            }
                        }
                    }
                    if(php.equals("Field_GetUserName")){
                        JSONArray result = jobject.getJSONArray("result2");
                        for(int i=0;i<result.length();i++){
                            JSONObject data = result.getJSONObject(i);
                            String Birthday = data.getString("birthday");
                            String CardID = data.getString("CardID");
                            String Cname = data.getString("cname");
                            if(CardID.length() != 0 && Cname.length() != 0){
                                ret.put(CardID, Cname + " " + Birthday);
                                Log.i(TAG+" User ", Cname + " CardID : " + CardID + " Birthday : " + Birthday);
                            }
                        }
                    }
                    if(php.equals("Field_GetUserData")){
                        JSONArray result1 = jobject.getJSONArray("result1");
                        JSONObject result2 = jobject.getJSONObject("result2");
                        for(int i=0;i<result1.length();i++){
                            JSONObject data1 = result1.getJSONObject(i);
                            String Date = data1.getString("Date");
                            if(Date.length() != 0){
                                String Data = data1.getString("SYS") + " " +data1.getString("DIA") + " " +data1.getString("HR") + " " + data1.getString("Res");
                                Data = Data + " " + result2.getString("Hypertension") + " " + result2.getString("Medicine");
                                ret.put(Date, Data);
                                Log.i(TAG+" Time ", Date + " Data : " + Data);
                            }
                        }
                    }
                }
                inputStream.close(); // 關閉輸入串流
            }
            return ret;
        } catch (Exception e) {
            Log.d(TAG+" getData","取得資料失敗");
            Log.d(TAG+" DB", e.toString());
            return ret;
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

