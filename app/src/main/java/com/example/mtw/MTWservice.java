package com.example.mtw;

import android.app.Notification;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class MTWservice extends Service {

    public static ArrayList<String> numberList=new ArrayList<>();
    private ProgressDialog mProgressDialog;

    Runnable runablesend;
    Handler mHandler = new Handler();

    private BroadcastReceiver sendMTW = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String images = intent.getStringExtra("images");
            final String text = intent.getStringExtra("text");
            final String timePeriod = intent.getStringExtra("period");
            final int[] index = {0};
            numberList=MainActivity.numberList;
            runablesend = new Runnable() {
                @Override
                public void run() {
                   if (index[0] <numberList.size()){
                       openWhatsAppchat(numberList.get(index[0]));
                       index[0] ++;
                    }else{
                       stopRunable();
                   }
                    mHandler.postDelayed(this, Long.parseLong(timePeriod)*1000);
                }
            };
            mHandler.postDelayed(runablesend, Long.parseLong(timePeriod)*1000);
            runablesend.run();
        }
    };

    private void stopRunable() {
        mHandler.removeCallbacks(runablesend);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //builder.setPriority(Notification.PRIORITY_MIN);
        //startForeground(1, builder.build());

        LocalBroadcastManager.getInstance(this).registerReceiver(sendMTW, new IntentFilter("Send_Msg_To_Wahtsapp"));


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(sendMTW);
        super.onDestroy();
    }

    public boolean sendMTW(String number, String text, String imagepath){

        try {
            number = number.replace("+", "").replace(" ", "");
            Uri image = Uri.parse(imagepath);
            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {

                /*PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_SENDTO);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode(text, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                   // }
                } catch (Exception e){
                    e.printStackTrace();
                }*/
                /*Intent sendIntent = new Intent(); // "android.intent.action.MAIN"
                sendIntent.setAction(Intent.ACTION_SENDTO);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");//phone number without "+" prefix
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_STREAM,image);
                sendIntent.setType("image/*");
                //sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_STREAM,image);
                sendIntent.setType("image/*");
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(this, "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }

            //You can read the image from external drove too
            //Intent intent = new Intent();
            //intent.setAction(Intent.ACTION_SEND);
            //intent.putExtra("jid", number + "@s.whatsapp.net");

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public void sendMsg(){
        String message = "Health worker uploaded a data";
        Context context_new=this.getApplicationContext();

        PackageManager packageManager = context_new.getPackageManager();
        Intent i = new Intent(Intent.ACTION_SEND);

        try {

            //only message and ability to send message to multiple nos.
            String url = "https://api.whatsapp.com/send?text=" + URLEncoder.encode(message, "UTF-8");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));

            if (i.resolveActivity(packageManager) != null) {
                context_new.startActivity(i);
                //Log.d(TAG, "Watsapp message successful"+ "passed");
            }
        } catch (Exception e){
            e.printStackTrace();
            //Log.d(TAG, "Watsapp message failed"+ "failed");
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    public void openWhatsAppchat(String number){
        try {
            String text = "This is a test";// Replace with your message.

            String toNumber = number; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void openWhatsappContact(String number) {

        try {
            /*Uri uri = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SEND, uri);
            i.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            i.setType("text/plain");
            i.setPackage("com.whatsapp");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);*/
            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
            String url = "https://chat.whatsapp.com/JCFRaEn4Ji05gi5XwOt0M4";
            intentWhatsapp.setData(Uri.parse(url));
            intentWhatsapp.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            //intentWhatsapp.setType("text/plain");
            intentWhatsapp.setPackage("com.whatsapp");
            intentWhatsapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentWhatsapp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void openWhatsApp(String number) {
        number = number.replace("+", "").replace(" ", "");
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.putExtra("jid", number + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(this.getPackageManager()) == null) {
            Toast.makeText(this, "Error/n" + false, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(sendIntent);
    }

}
