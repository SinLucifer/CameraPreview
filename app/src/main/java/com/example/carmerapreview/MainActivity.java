package com.example.carmerapreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private CameraPreview cameraView;
    private SensorManager mSensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private float Latitude;//纬度
    private float Longitude;//经度

    private TextView azimuthAngle;
    private TextView azimuthAngle2;
    private TextView pichAngle;
    private TextView RollAngle;
    private TextView tv_location;
    private TextView RA_value;
    private TextView name;

    private float anglenew;
    private double DecS;
    private double Dec;
    private double RAS;
    private double RAC;
    private double RAT;
    private double RA;
    private double RA1;
    private double HC;
    private double HS;
    private float wei;
    private float jin;
    private float JD;
    private float UT;
    private float MJD;
    private float GP;
    private float LST;
    private float LST1;
    private int JD1;
    private int JD2;
    private int JD3;
    private int JD4;

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private MySensorEventListener listener;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                calculateOrientation();
                handler.sendEmptyMessageDelayed(0, 500);
            }
        }
    };

    String json = "[[12.5731,-23.3968,\"\\u4e4c\\u9e26\\u5ea7 \\u03b2, 9 Crv\",\"\\u8f78\\u5bbf\\u56db\"],[12.2634,-17.5419,\"\\u4e4c\\u9e26\\u5ea7 \\u03b3, 4 Crv\",\"\\u8f78\\u5bbf\\u4e00\"],[12.4977,-16.5154,\"\\u4e4c\\u9e26\\u5ea7 \\u03b4, 7 Crv\",\"\\u8f78\\u5bbf\\u4e09\"],[18.0968,-30.4241,\"\\u4eba\\u9a6c\\u5ea7 \\u03b32, \\u03b3 Sgr\",\"\\u7b95\\u5bbf\\u4e00\"],[18.3499,-29.8281,\"\\u4eba\\u9a6c\\u5ea7 \\u03b4, 19 Sgr\",\"\\u7b95\\u5bbf\\u4e8c\"],[18.4029,-34.3846,\"\\u4eba\\u9a6c\\u5ea7 \\u03b5, 20 Sgr\",\"\\u7b95\\u5bbf\\u4e09\"],[19.0435,-29.8802,\"\\u4eba\\u9a6c\\u5ea7 \\u03b6, 38 Sgr\",\"\\u6597\\u5bbf\\u516d\"],[18.4662,-25.4217,\"\\u4eba\\u9a6c\\u5ea7 \\u03bb, 22 Sgr\",\"\\u6597\\u5bbf\\u4e8c\"],[19.1627,-21.0236,\"\\u4eba\\u9a6c\\u5ea7 \\u03c0, 41 Sgr\",\"\\u5efa\\u4e09\"],[18.9211,-26.2967,\"\\u4eba\\u9a6c\\u5ea7 \\u03c3, 34 Sgr\",\"\\u6597\\u5bbf\\u56db\"],[0.6751,56.5374,\"\\u4ed9\\u540e\\u5ea7 \\u03b1, 18 Cas\",\"\\u738b\\u826f\\u56db\"],[0.153,59.1498,\"\\u4ed9\\u540e\\u5ea7 \\u03b2, 11 Cas\",\"\\u738b\\u826f\\u4e00\"],[0.9451,60.7167,\"\\u4ed9\\u540e\\u5ea7 \\u03b3, 27 Cas\",\"\\u7b56\"],[1.4303,60.2353,\"\\u4ed9\\u540e\\u5ea7 \\u03b4, 37 Cas\",\"\\u9601\\u9053\\u4e09\"],[0.1398,29.0904,\"\\u4ed9\\u5973\\u5ea7 \\u03b1, 21 And; \\u98de\\u9a6c\\u5ea7 \\u03b4\",\"\\u58c1\\u5bbf\\u4e8c\"],[1.1622,35.6205,\"\\u4ed9\\u5973\\u5ea7 \\u03b2, 43 And\",\"\\u594e\\u5bbf\\u4e5d\"],[2.065,42.3297,\"\\u4ed9\\u5973\\u5ea7 \\u03b32, \\u03b31 And\",\"\\u5929\\u5927\\u5c06\\u519b\\u4e00\"],[21.3097,62.5856,\"\\u4ed9\\u738b\\u5ea7 \\u03b1, 5 Cep\",\"\\u5929\\u94a9\\u4e94\"],[0.4381,-42.3061,\"\\u51e4\\u51f0\\u5ea7 \\u03b1, \\u03b1 Phe\",\"\\u706b\\u9e1f\\u516d\"],[15.5781,26.7147,\"\\u5317\\u5195\\u5ea7 \\u03b1, 5 CrB\",\"\\u8d2f\\u7d22\\u56db\"],[14.66,-60.8354,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b11, \\u03b1 A Cen\",\"\\u5357\\u95e8\\u4e8c\"],[14.0637,-60.373,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b2, \\u03b2 Cen\",\"\\u9a6c\\u8179\\u4e00\"],[12.692,-48.9597,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b3, \\u03b3 Cen\",\"\\u5e93\\u697c\\u4e03\"],[12.1393,-50.7224,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b4, \\u03b4 Cen\",\"\\u9a6c\\u5c3e\\u4e09\"],[13.6648,-53.4664,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b5, \\u03b5 Cen\",\"\\u5357\\u95e8\\u4e00\"],[13.9257,-47.2884,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b6, \\u03b6 Cen\",\"\\u5e93\\u697c\\u4e00\"],[14.5918,-42.1579,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b7, \\u03b7 Cen\",\"\\u5e93\\u697c\\u4e8c\"],[14.1114,-36.37,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b8, 5 Cen\",\"\\u5e93\\u697c\\u4e09\"],[13.3433,-36.7123,\"\\u534a\\u4eba\\u9a6c\\u5ea7 \\u03b9, \\u03b9 Cen\",\"\\u67f1\\u5341\\u4e00(\\u89d2\\u5bbf)\"],[16.8111,-69.0277,\"\\u5357\\u4e09\\u89d2\\u5ea7 \\u03b1, \\u03b1 TrA\",\"\\u4e09\\u89d2\\u5f62\\u4e09\"],[15.919,-63.4307,\"\\u5357\\u4e09\\u89d2\\u5ea7 \\u03b2, \\u03b2 TrA\",\"\\u4e09\\u89d2\\u5f62\\u4e8c\"],[15.3152,-68.6796,\"\\u5357\\u4e09\\u89d2\\u5ea7 \\u03b3, \\u03b3 TrA\",\"\\u4e09\\u89d2\\u5f62\\u4e00\"],[12.4434,-63.0992,\"\\u5357\\u5341\\u5b57\\u5ea7 \\u03b12, \\u03b11 Cru\",\"\\u5341\\u5b57\\u67b6\\u4e8c\"],[12.7953,-59.6887,\"\\u5357\\u5341\\u5b57\\u5ea7 \\u03b2, \\u03b2 Cru\",\"\\u5341\\u5b57\\u67b6\\u4e09\"],[12.5194,-57.1132,\"\\u5357\\u5341\\u5b57\\u5ea7 \\u03b3, \\u03b3 Cru\",\"\\u5341\\u5b57\\u67b6\\u4e00\"],[12.2524,-58.7489,\"\\u5357\\u5341\\u5b57\\u5ea7 \\u03b4, \\u03b4 Cru\",\"\\u5341\\u5b57\\u67b6\\u56db\"],[22.9608,-29.6223,\"\\u5357\\u9c7c\\u5ea7 \\u03b1, 24 PsA\",\"\\u5317\\u843d\\u5e08\\u95e8\"],[7.5767,31.8885,\"\\u53cc\\u5b50\\u5ea7 \\u03b1, 66 Gem\",\"\\u5317\\u6cb3\\u4e8c\"],[7.7553,28.0262,\"\\u53cc\\u5b50\\u5ea7 \\u03b2, 78 Gem\",\"\\u5317\\u6cb3\\u4e09\"],[6.6285,16.3993,\"\\u53cc\\u5b50\\u5ea7 \\u03b3, 24 Gem\",\"\\u4e95\\u5bbf\\u4e09\"],[6.7322,25.1311,\"\\u53cc\\u5b50\\u5ea7 \\u03b5, 27 Gem\",\"\\u4e95\\u5bbf\\u4e94\"],[6.3827,22.5136,\"\\u53cc\\u5b50\\u5ea7 \\u03bc, 13 Gem\",\"\\u4e95\\u5bbf\\u4e00\"],[11.0621,61.7509,\"\\u5927\\u718a\\u5ea7 \\u03b1, 50 UMa\",\"\\u5317\\u6597\\u4e00,\\u5929\\u67a2\"],[11.0307,56.3824,\"\\u5927\\u718a\\u5ea7 \\u03b2, 48 UMa\",\"\\u5317\\u6597\\u4e8c,\\u5929\\u7487\"],[11.8972,53.6948,\"\\u5927\\u718a\\u5ea7 \\u03b3, 64 UMa\",\"\\u5317\\u6597\\u4e09,\\u5929\\u7391\"],[12.9005,55.9599,\"\\u5927\\u718a\\u5ea7 \\u03b5, 77 UMa\",\"\\u5317\\u6597\\u4e94,\\u7389\\u8861\"],[13.3988,54.9254,\"\\u5927\\u718a\\u5ea7 \\u03b6, 79 UMa\",\"\\u5317\\u6597\\u516d,\\u5f00\\u9633\"],[13.7923,49.3133,\"\\u5927\\u718a\\u5ea7 \\u03b7, 85 UMa\",\"\\u5317\\u6597\\u4e03,\\u6447\\u5149\"],[6.7525,-16.7161,\"\\u5927\\u72ac\\u5ea7 \\u03b1, 9 CMa\",\"\\u5929\\u72fc\"],[6.3783,-17.9559,\"\\u5927\\u72ac\\u5ea7 \\u03b2, 2 CMa\",\"\\u519b\\u5e02\\u4e00\"],[7.1399,-26.3932,\"\\u5927\\u72ac\\u5ea7 \\u03b4, 25 CMa\",\"\\u5f27\\u77e2\\u4e00\"],[6.9771,-28.9721,\"\\u5927\\u72ac\\u5ea7 \\u03b5, 21 CMa\",\"\\u5f27\\u77e2\\u4e03\"],[7.4016,-29.3031,\"\\u5927\\u72ac\\u5ea7 \\u03b7, 31 CMa\",\"\\u5f27\\u77e2\\u4e8c\"],[5.5455,-17.8223,\"\\u5929\\u5154\\u5ea7 \\u03b1, 11 Lep\",\"\\u5395\\u4e00\"],[5.4708,-20.7595,\"\\u5929\\u5154\\u5ea7 \\u03b2, 9 Lep\",\"\\u5395\\u4e8c\"],[17.5307,-49.8762,\"\\u5929\\u575b\\u5ea7 \\u03b1, \\u03b1 Ara\",\"\\u6775\\u4e8c(\\u7b95\\u5bbf)\"],[17.4217,-55.5299,\"\\u5929\\u575b\\u5ea7 \\u03b2, \\u03b2 Ara\",\"\\u6775\\u4e09(\\u7b95\\u5bbf)\"],[18.6156,38.7837,\"\\u5929\\u7434\\u5ea7 \\u03b1, 3 Lyr\",\"\\u7ec7\\u5973\\u4e00\"],[14.848,-16.0418,\"\\u5929\\u79e4\\u5ea7 \\u03b12, 9 Lib\",\"\"],[15.2835,-9.3829,\"\\u5929\\u79e4\\u5ea7 \\u03b2, 27 Lib\",\"\\u6c10\\u5bbf\\u56db\"],[16.4901,-26.432,\"\\u5929\\u874e\\u5ea7 \\u03b1, 21 Sco\",\"\\u5fc3\\u5bbf\\u4e8c,\\u5927\\u706b\"],[16.0906,-19.8054,\"\\u5929\\u874e\\u5ea7 \\u03b21, 8 A Sco\",\"\\u623f\\u5bbf\\u56db\"],[16.0056,-22.6217,\"\\u5929\\u874e\\u5ea7 \\u03b4, 7 Sco\",\"\\u623f\\u5bbf\\u4e09\"],[16.8361,-34.2933,\"\\u5929\\u874e\\u5ea7 \\u03b5, 26 Sco\",\"\\u5c3e\\u5bbf\\u4e8c\"],[17.622,-42.9978,\"\\u5929\\u874e\\u5ea7 \\u03b8, \\u03b8 Sco\",\"\\u5c3e\\u5bbf\\u4e94\"],[17.7081,-39.03,\"\\u5929\\u874e\\u5ea7 \\u03ba, \\u03ba Sco\",\"\\u5c3e\\u5bbf\\u4e03\"],[17.5601,-37.1038,\"\\u5929\\u874e\\u5ea7 \\u03bb, 35 Sco\",\"\\u5c3e\\u5bbf\\u516b\"],[15.9809,-26.1141,\"\\u5929\\u874e\\u5ea7 \\u03c0, 6 Sco\",\"\\u623f\\u5bbf\\u4e00\"],[16.3531,-25.5928,\"\\u5929\\u874e\\u5ea7 \\u03c3, 20 Sco\",\"\\u5fc3\\u5bbf\\u4e00\"],[16.598,-28.216,\"\\u5929\\u874e\\u5ea7 \\u03c4, 23 Sco\",\"\\u5fc3\\u5bbf\\u4e09\"],[17.5127,-37.2958,\"\\u5929\\u874e\\u5ea7 \\u03c5, 34 Sco\",\"\\u5c3e\\u5bbf\\u4e5d\"],[5.6608,-34.0742,\"\\u5929\\u9e3d\\u5ea7 \\u03b1, \\u03b1 Col\",\"\\u4e08\\u4eba\\u4e00\"],[20.6905,45.2804,\"\\u5929\\u9e45\\u5ea7 \\u03b1, 50 Cyg\",\"\\u5929\\u6d25\\u56db\"],[20.3705,40.2567,\"\\u5929\\u9e45\\u5ea7 \\u03b3, 37 Cyg\",\"\\u5929\\u6d25\\u4e00\"],[19.7496,45.1307,\"\\u5929\\u9e45\\u5ea7 \\u03b4, 18 Cyg\",\"\\u5929\\u6d25\\u4e8c\"],[20.7702,33.9703,\"\\u5929\\u9e45\\u5ea7 \\u03b5, 53 Cyg\",\"\\u5929\\u6d25\\u4e5d\"],[22.1372,-46.961,\"\\u5929\\u9e64\\u5ea7 \\u03b1, \\u03b1 Gru\",\"\\u9e64\\u4e00\"],[22.7111,-46.8846,\"\\u5929\\u9e64\\u5ea7 \\u03b2, \\u03b2 Gru\",\"\\u9e64\\u4e8c\"],[19.8464,8.8683,\"\\u5929\\u9e70\\u5ea7 \\u03b1, 53 Aql\",\"\\u6cb3\\u9f13\\u4e8c\"],[19.771,10.6133,\"\\u5929\\u9e70\\u5ea7 \\u03b3, 50 Aql\",\"\\u6cb3\\u9f13\\u4e09\"],[19.0902,13.8635,\"\\u5929\\u9e70\\u5ea7 \\u03b6, 17 Aql\",\"\\u5929\\u5e02\\u5de6\\u57a3\\u516d,\\u5434\\u8d8a\"],[17.5072,52.3014,\"\\u5929\\u9f99\\u5ea7 \\u03b2, 23 Dra\",\"\\u5929\\u68d3\\u4e09\"],[17.9434,51.4889,\"\\u5929\\u9f99\\u5ea7 \\u03b3, 33 Dra\",\"\\u5929\\u68d3\\u56db\"],[16.3998,61.5144,\"\\u5929\\u9f99\\u5ea7 \\u03b7, 14 Dra\",\"\\u7d2b\\u5fae\\u5de6\\u57a3\\u4e09,\\u5c11\\u5bb0\"],[20.4275,-56.7351,\"\\u5b54\\u96c0\\u5ea7 \\u03b1, \\u03b1 Pav\",\"\\u5b54\\u96c0\\u5341\\u4e00\"],[22.0964,-0.3199,\"\\u5b9d\\u74f6\\u5ea7 \\u03b1, 34 Aqr\",\"\\u5371\\u5bbf\\u4e00\"],[21.526,-5.5712,\"\\u5b9d\\u74f6\\u5ea7 \\u03b2, 22 Aqr\",\"\\u865a\\u5bbf\\u4e00\"],[13.4199,-11.1613,\"\\u5ba4\\u5973\\u5ea7 \\u03b1, 67 Vir\",\"\\u89d2\\u5bbf\\u4e00\"],[12.6943,-1.4494,\"\\u5ba4\\u5973\\u5ea7 \\u03b3, 29 Vir\",\"\\u592a\\u5fae\\u5de6\\u57a3\\u4e8c,\\u4e1c\\u4e0a\\u76f8\"],[13.0363,10.9591,\"\\u5ba4\\u5973\\u5ea7 \\u03b5, 47 Vir\",\"\\u592a\\u5fae\\u5de6\\u57a3\\u56db,\\u4e1c\\u6b21\\u5c06\"],[2.5302,89.2641,\"\\u5c0f\\u718a\\u5ea7 \\u03b1, 1 UMi\",\"\\u52fe\\u9648\\u4e00,\\u5317\\u6781\\u661f\"],[14.8451,74.1555,\"\\u5c0f\\u718a\\u5ea7 \\u03b2, 7 UMi\",\"\\u5317\\u6781\\u4e8c,\\u5e1d\"],[7.655,5.225,\"\\u5c0f\\u72ac\\u5ea7 \\u03b1, 10 CMi\",\"\\u5357\\u6cb3\\u4e09\"],[7.4525,8.2893,\"\\u5c0f\\u72ac\\u5ea7 \\u03b2, 3 CMi\",\"\\u5357\\u6cb3\\u4e8c\"],[15.7378,6.4256,\"\\u5de8\\u86c7\\u5ea7 \\u03b1, 24 Ser\",\"\\u5929\\u5e02\\u53f3\\u57a3\\u4e03,\\u8700\"],[5.2782,45.998,\"\\u5fa1\\u592b\\u5ea7 \\u03b1, 13 Aur\",\"\\u4e94\\u8f66\\u4e8c\"],[5.9921,44.9474,\"\\u5fa1\\u592b\\u5ea7 \\u03b2, 34 Aur\",\"\\u4e94\\u8f66\\u4e09\"],[5.9953,37.2127,\"\\u5fa1\\u592b\\u5ea7 \\u03b8, 37 Aur\",\"\\u4e94\\u8f66\\u56db\"],[4.9499,33.1661,\"\\u5fa1\\u592b\\u5ea7 \\u03b9, 3 Aur\",\"\\u4e94\\u8f66\\u4e00\"],[21.784,-16.1273,\"\\u6469\\u7faf\\u5ea7 \\u03b4, 49 Cap\",\"\\u5792\\u58c1\\u9635\\u56db\"],[22.3084,-60.2596,\"\\u675c\\u9e43\\u5ea7 \\u03b1, \\u03b1 Tuc\",\"\\u9e1f\\u5599\\u4e00\"],[16.5037,21.4896,\"\\u6b66\\u4ed9\\u5ea7 \\u03b2, 27 Her\",\"\\u5929\\u5e02\\u53f3\\u57a3\\u4e00,\\u6cb3\\u4e2d\"],[16.6881,31.603,\"\\u6b66\\u4ed9\\u5ea7 \\u03b6, 40 Her\",\"\\u5929\\u7eaa\\u4e8c\"],[1.9795,-61.5698,\"\\u6c34\\u86c7\\u5ea7 \\u03b1, \\u03b1 Hyi\",\"\\u86c7\\u9996\\u4e00\"],[0.4292,-77.2543,\"\\u6c34\\u86c7\\u5ea7 \\u03b2, \\u03b2 Hyi\",\"\\u86c7\\u5c3e\\u4e00\"],[1.6286,-57.2367,\"\\u6ce2\\u6c5f\\u5ea7 \\u03b1, \\u03b1 Eri\",\"\\u6c34\\u59d4\\u4e00\"],[5.1308,-5.0865,\"\\u6ce2\\u6c5f\\u5ea7 \\u03b2, 67 Eri\",\"\\u7389\\u4e95\\u4e09\"],[3.9672,-13.5085,\"\\u6ce2\\u6c5f\\u5ea7 \\u03b3, 34 Eri\",\"\\u5929\\u82d1\\u4e00\"],[2.9711,-40.3048,\"\\u6ce2\\u6c5f\\u5ea7 \\u03b82, \\u03b81 Eri\",\"\\u5929\\u56ed\\u516d\"],[14.261,19.1824,\"\\u7267\\u592b\\u5ea7 \\u03b1, 16 Boo\",\"\\u5927\\u89d2\"],[14.7498,27.0742,\"\\u7267\\u592b\\u5ea7 \\u03b5, 36 Boo\",\"\\u6897\\u6cb3\\u4e00\"],[13.9114,18.3977,\"\\u7267\\u592b\\u5ea7 \\u03b7, 8 Boo\",\"\\u53f3\\u6444\\u63d0\\u4e00\"],[10.1395,11.9672,\"\\u72ee\\u5b50\\u5ea7 \\u03b1, 32 Leo\",\"\\u8f69\\u8f95\\u5341\\u56db\"],[11.8177,14.572,\"\\u72ee\\u5b50\\u5ea7 \\u03b2, 94 Leo\",\"\\u4e94\\u5e1d\\u5ea7\\u4e00\"],[10.3329,19.8414,\"\\u72ee\\u5b50\\u5ea7 \\u03b32, \\u03b31 Leo\",\"\\u8f69\\u8f95\\u5341\\u4e8c\"],[11.2351,20.5237,\"\\u72ee\\u5b50\\u5ea7 \\u03b4, 68 Leo\",\"\\u592a\\u5fae\\u53f3\\u57a3\\u4e94,\\u897f\\u4e0a\\u76f8\"],[9.7642,23.7742,\"\\u72ee\\u5b50\\u5ea7 \\u03b5, 17 Leo\",\"\\u8f69\\u8f95\\u4e5d\"],[5.9195,7.407,\"\\u730e\\u6237\\u5ea7 \\u03b1, 58 Ori\",\"\\u53c2\\u5bbf\\u56db\"],[5.2423,-8.2017,\"\\u730e\\u6237\\u5ea7 \\u03b2, 19 Ori\",\"\\u53c2\\u5bbf\\u4e03\"],[5.4188,6.3497,\"\\u730e\\u6237\\u5ea7 \\u03b3, 24 Ori\",\"\\u53c2\\u5bbf\\u4e94\"],[5.5334,-0.2991,\"\\u730e\\u6237\\u5ea7 \\u03b4, 34 Ori\",\"\\u53c2\\u5bbf\\u4e09\"],[5.6036,-1.202,\"\\u730e\\u6237\\u5ea7 \\u03b5, 46 Ori\",\"\\u53c2\\u5bbf\\u4e8c\"],[5.6793,-1.9426,\"\\u730e\\u6237\\u5ea7 \\u03b6, 50 Ori\",\"\\u53c2\\u5bbf\\u4e00\"],[5.5905,-5.9099,\"\\u730e\\u6237\\u5ea7 \\u03b9, 44 Ori\",\"\\u4f10\\u4e09\"],[5.7959,-9.6697,\"\\u730e\\u6237\\u5ea7 \\u03ba, 53 Ori\",\"\\u53c2\\u5bbf\\u516d\"],[12.9338,38.3184,\"\\u730e\\u72ac\\u5ea7 \\u03b12, 12 A CVn\",\"\\u5e38\\u9648\\u4e00\"],[2.1196,23.4624,\"\\u767d\\u7f8a\\u5ea7 \\u03b1, 13 Ari\",\"\\u5a04\\u5bbf\\u4e09\"],[1.9107,20.808,\"\\u767d\\u7f8a\\u5ea7 \\u03b2, 6 Ari\",\"\\u5a04\\u5bbf\\u4e00\"],[8.0597,-40.0032,\"\\u8239\\u5c3e\\u5ea7 \\u03b6, \\u03b6 PuP\",\"\\u5f27\\u77e2\\u589e\\u4e8c\\u5341\\u4e8c\"],[7.2857,-37.0975,\"\\u8239\\u5c3e\\u5ea7 \\u03c0, \\u03c0 PuP\",\"\\u5f27\\u77e2\\u4e5d\"],[8.1257,-24.3043,\"\\u8239\\u5c3e\\u5ea7 \\u03c1, 15 PuP\",\"\\u5f27\\u77e2\\u589e\\u4e09\\u5341\\u4e8c\"],[6.8323,-50.6146,\"\\u8239\\u5c3e\\u5ea7 \\u03c4, \\u03c4 PuP\",\"\\u8001\\u4eba\\u589e\\u4e00\"],[8.1589,-47.3366,\"\\u8239\\u5e06\\u5ea7 \\u03b32, \\u03b3 Vel\",\"\\u5929\\u793e\\u4e00\"],[8.7451,-54.7084,\"\\u8239\\u5e06\\u5ea7 \\u03b4, \\u03b4 Vel\",\"\\u5929\\u793e\\u4e09\"],[9.3686,-55.0107,\"\\u8239\\u5e06\\u5ea7 \\u03ba, \\u03ba Vel\",\"\\u5929\\u793e\\u4e94\"],[9.1333,-43.4326,\"\\u8239\\u5e06\\u5ea7 \\u03bb, \\u03bb Vel\",\"\\u5929\\u8bb0\"],[10.7795,-49.4201,\"\\u8239\\u5e06\\u5ea7 \\u03bc, \\u03bc Vel\",\"\\u6d77\\u5c71\\u589e\\u4e8c\"],[6.3992,-52.6957,\"\\u8239\\u5e95\\u5ea7 \\u03b1, \\u03b1 Car\",\"\\u8001\\u4eba\"],[9.22,-69.7172,\"\\u8239\\u5e95\\u5ea7 \\u03b2, \\u03b2 Car\",\"\\u5357\\u8239\\u4e94\"],[8.3752,-59.5096,\"\\u8239\\u5e95\\u5ea7 \\u03b5, \\u03b5 Car\",\"\\u6d77\\u77f3\\u4e00\"],[10.7159,-64.3945,\"\\u8239\\u5e95\\u5ea7 \\u03b8, \\u03b8 Car\",\"\\u5357\\u8239\\u4e09\"],[9.2848,-59.2753,\"\\u8239\\u5e95\\u5ea7 \\u03b9, \\u03b9 Car\",\"\\u6d77\\u77f3\\u4e8c\"],[9.785,-65.0719,\"\\u8239\\u5e95\\u5ea7 \\u03c5, \\u03c5 Car\",\"\\u6d77\\u77f3\\u4e94\"],[12.6197,-69.1355,\"\\u82cd\\u8747\\u5ea7 \\u03b1, \\u03b1 Mus\",\"\\u871c\\u8702\\u4e09\"],[3.4054,49.8612,\"\\u82f1\\u4ed9\\u5ea7 \\u03b1, 33 Per\",\"\\u5929\\u8239\\u4e09\"],[3.1361,40.9557,\"\\u82f1\\u4ed9\\u5ea7 \\u03b2, 26 Per\",\"\\u5927\\u9675\\u4e94\"],[3.0799,53.5065,\"\\u82f1\\u4ed9\\u5ea7 \\u03b3, 23 Per\",\"\\u5929\\u8239\\u4e8c\"],[3.9642,40.0102,\"\\u82f1\\u4ed9\\u5ea7 \\u03b5, 45 Per\",\"\\u5377\\u820c\\u4e8c\"],[3.9022,31.8836,\"\\u82f1\\u4ed9\\u5ea7 \\u03b6, 44 Per\",\"\\u5377\\u820c\\u56db\"],[17.5822,12.56,\"\\u86c7\\u592b\\u5ea7 \\u03b1, 55 Oph\",\"\\u5019\"],[17.7245,4.5673,\"\\u86c7\\u592b\\u5ea7 \\u03b2, 60 Oph\",\"\\u5b97\\u6b63\\u4e00\"],[16.2391,-3.6943,\"\\u86c7\\u592b\\u5ea7 \\u03b4, 1 Oph\",\"\\u5929\\u5e02\\u53f3\\u57a3\\u4e5d,\\u6881\"],[16.6193,-10.5671,\"\\u86c7\\u592b\\u5ea7 \\u03b6, 13 Oph\",\"\\u5929\\u5e02\\u53f3\\u57a3\\u5341\\u4e00,\\u97e9\"],[17.173,-15.7249,\"\\u86c7\\u592b\\u5ea7 \\u03b7, 35 Oph\",\"\\u5929\\u5e02\\u5de6\\u57a3\\u5341\\u4e00,\\u5b8b\"],[14.6988,-47.3882,\"\\u8c7a\\u72fc\\u5ea7 \\u03b1, \\u03b1 Lup\",\"\\u9a91\\u5b98\\u5341\"],[14.9755,-43.134,\"\\u8c7a\\u72fc\\u5ea7 \\u03b2, \\u03b2 Lup\",\"\\u9a91\\u5b98\\u56db\"],[15.5857,-41.1668,\"\\u8c7a\\u72fc\\u5ea7 \\u03b3, \\u03b3 Lup\",\"\\u9a91\\u5b98\\u4e00\"],[4.5987,16.5093,\"\\u91d1\\u725b\\u5ea7 \\u03b1, 87 Tau\",\"\\u6bd5\\u5bbf\\u4e94\"],[5.4382,28.6074,\"\\u91d1\\u725b\\u5ea7 \\u03b2, 112 Tau\",\"\\u4e94\\u8f66\\u4e94\"],[3.7914,24.1051,\"\\u91d1\\u725b\\u5ea7 \\u03b7, 25 Tau\",\"\\u6634\\u5bbf\\u516d\"],[9.4598,-8.6587,\"\\u957f\\u86c7\\u5ea7 \\u03b1, 30 Hya\",\"\\u661f\\u5bbf\\u4e00\"],[13.3154,-23.1716,\"\\u957f\\u86c7\\u5ea7 \\u03b3, 46 Hya\",\"\\u5e73\\u4e00\"],[23.0793,15.2053,\"\\u98de\\u9a6c\\u5ea7 \\u03b1, 54 Peg\",\"\\u5ba4\\u5bbf\\u4e00\"],[23.0629,28.0828,\"\\u98de\\u9a6c\\u5ea7 \\u03b2, 53 Peg\",\"\\u5ba4\\u5bbf\\u4e8c\"],[0.2206,15.1836,\"\\u98de\\u9a6c\\u5ea7 \\u03b3, 88 Peg\",\"\\u58c1\\u5bbf\\u4e00\"],[21.7364,9.875,\"\\u98de\\u9a6c\\u5ea7 \\u03b5, 8 Peg\",\"\\u5371\\u5bbf\\u4e09\"],[22.7167,30.2213,\"\\u98de\\u9a6c\\u5ea7 \\u03b7, 44 Peg\",\"\\u79bb\\u5bab\\u56db\"],[3.038,4.0897,\"\\u9cb8\\u9c7c\\u5ea7 \\u03b1, 92 Cet\",\"\\u5929\\u56f7\\u4e00\"],[0.7265,-17.9866,\"\\u9cb8\\u9c7c\\u5ea7 \\u03b2, 16 Cet\",\"\\u571f\\u53f8\\u7a7a\"]]";

    private JSONArray array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        // 实例化传感器管理者
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        azimuthAngle = (TextView) findViewById(R.id.azimuth_angle_value);
        pichAngle=(TextView) findViewById(R.id.pich_angle_value);
        RollAngle=(TextView) findViewById(R.id.Roll_angle_value);
        azimuthAngle2 = (TextView) findViewById(R.id.azimuth_angle_value2);
        tv_location = (TextView) findViewById(R.id.tv_location);
        RA_value=(TextView) findViewById(R.id.RA);
        cameraView = findViewById(R.id.textureView);
        name = findViewById(R.id.tv_name);
        listener = new MySensorEventListener();
        array = JSON.parseArray(json);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();

        handler.sendEmptyMessage(0);
        mSensorManager.registerListener(listener,
                accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(listener,
                magnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
        // 解除注册
        handler.removeMessages(0);
        handler.sendEmptyMessage(1);
        mSensorManager.unregisterListener(listener);
    }

    // 计算方向
    @SuppressLint("SetTextI18n")
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];

        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);

        //获取时间
        Calendar calendar = Calendar.getInstance();

        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        values[1] = (float) Math.toDegrees(values[1]);
        values[1] = values[1] - 90;
        if (values[1] < -90) {
            values[1] = values[1] + 180;
        }
        //方位角转化
        values[0] = (float) Math.toDegrees(values[0]);
        if(values[1]>0 )
        {
            values[0]=values[0]-180;
            values[0]=values[0]%360;
        }

        values[0]=(float)(values[0]*(3.14/180));
        values[1] = (float) (values[1] * (3.14 / 180));


        //将地平坐标转化成天体坐标
        wei= (float) (39.05*(3.14/180));//将纬度转化为弧度
        DecS=(Math.sin(wei)*Math.sin(values[1]))+(Math.cos(wei)*Math.cos(values[1])*Math.cos(values[0]));//赤纬的sin值
        Dec=(float)Math.toDegrees(Math.asin(DecS));

        jin=(float)(117.13*(3.14/180));
        RAC=(float)((-Math.cos(jin))*Math.sin(values[1]))+(Math.sin(jin)*Math.cos(values[1])*Math.cos(values[0]));
        RAS=(float)Math.sin(values[0])*Math.cos(values[1]);
        HC=RAC/Math.cos(Math.asin(DecS));
        HS=RAS/Math.cos(Math.asin(DecS));
        //RA=(float)Math.toDegrees(Math.acos(HC));
        RA1=(float)Math.toDegrees(Math.asin(HS));
        if(RA1<0) {
            RAT = RA1 + 180;
        }
        RA1=RA1/15;
        //计算恒星时
        UT=hour-8;
        JD1=(int)(365.25*year);
        JD2= (int) (year/400.0);
        JD3=(int)(year/100.0);
        JD4=(int)(30.59*(month-2));
        JD= (float) (JD1+ JD2-JD3+JD4+day+1721088.5+(UT/24)+(minute/1440)+(second/86400));
        MJD= (float) (JD-2400000.5);
        GP= (float) ((float) 24*(0.671262+(1.0027379094*MJD)));
        LST= (float) (GP*(117.13/15.0));

        // LST= (float) 690445.06814;
        if(LST>24||LST<0)
        {
            LST=LST%24;

        }


        //计算赤经
        RA= LST-RA1;
        DecimalFormat myformat=new DecimalFormat("0.00");
        String str = myformat.format(RA);

        //将弧度转化为角度
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        //将度数输出

        if(values[0]<0){
            anglenew= values[0]+360;
            azimuthAngle2.setText( " "+(int)anglenew)  ;
        }else{
            azimuthAngle2.setText( (int) values[0]+" ");}

        pichAngle.setText( (int) values[1]+" ");
        RollAngle.setText( (int) values[2]+" ");
        tv_location.setText((int)Dec +" ");
        RA_value.setText(str+" ");

        if (values[0] >= -5 && values[0] < 5) {
            azimuthAngle.setText("正北");
        } else if (values[0] >= 5 && values[0] < 85) {
            // Log.i(TAG, "东北");
            azimuthAngle.setText("东北");
        } else if (values[0] >= 85 && values[0] <= 95) {
            // Log.i(TAG, "正东");
            azimuthAngle.setText("正东");
        } else if (values[0] >= 95 && values[0] < 175) {
            // Log.i(TAG, "东南");
            azimuthAngle.setText("东南");
        } else if ((values[0] >= 175 && values[0] <= 180)
                || (values[0]) >= -180 && values[0] < -175) {
            // Log.i(TAG, "正南");
            azimuthAngle.setText("正南");
        } else if (values[0] >= -175 && values[0] < -95) {
            // Log.i(TAG, "西南");
            azimuthAngle.setText("西南");
        } else if (values[0] >= -95 && values[0] < -85) {
            // Log.i(TAG, "正西");
            azimuthAngle.setText("正西");
        } else if (values[0] >= -85 && values[0] < -5) {
            // Log.i(TAG, "西北");
            azimuthAngle.setText("西北");
        }

        int length = array.size();
        double min = Integer.MAX_VALUE;
        String engName = "";
        String subName = "";
        String finalName = "";
        for (int i = 0; i < length; i++) {
            double ra = array.getJSONArray(i).getDouble(0);
            double dec = array.getJSONArray(i).getDouble(1);
            double d2 = (ra - RA)*(ra - RA) + (dec - Dec)*(dec - Dec);
            if (d2 < min) {
                min = d2;
                engName = array.getJSONArray(i).getString(2);
                subName = array.getJSONArray(i).getString(3);
                if (!TextUtils.isEmpty(subName)) {
                    finalName = engName + "(" + subName +")";
                } else {
                    finalName = engName;
                }
            }
        }

        name.setText(finalName);
    }

    class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }

//            // startLocaion();
//            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    }
}
