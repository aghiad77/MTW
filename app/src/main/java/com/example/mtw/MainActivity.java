package com.example.mtw;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kodmap.app.library.PopopDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    private EditText text;
    private ImageView send, uploadImage, uploadFile, setTime,contact,images;
    private static String timePeriod = "", columnName = "", filePath = "";
    private static List<File> imagesList=new ArrayList<>();
    private ProgressDialog mProgressDialog;
    public static ArrayList<String> numberList=new ArrayList<>();
    List<String> url_images_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializ();
        startMTWservice();
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // load the dialog_promt_user.xml layout and inflate to view
                LayoutInflater layoutinflater = LayoutInflater.from(MainActivity.this);
                View promptUserView = layoutinflater.inflate(R.layout.dialog_prompt_file, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(promptUserView);
                final EditText column = (EditText) promptUserView.findViewById(R.id.name);
                if (!columnName.equals(""))
                    column.setText(columnName);
                alertDialogBuilder.setTitle("What is the column name of the phone number field?");
                alertDialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (column.getText().toString().equals("")) {
                            //Toast.makeText(getBaseContext(), "Please insert the column name", Toast.LENGTH_LONG).show();
                            uploadFile.setBackgroundResource(R.drawable.upload_file);
                            columnName = "";
                        } else {
                            // Toast.makeText(getBaseContext(), column.getText().toString(), Toast.LENGTH_LONG).show();
                            columnName = column.getText().toString();
                            dialog.cancel();
                            //uploadFile.setBackgroundResource(R.drawable.upload_file_);
                            //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            //intent.setType("*/*");
                            //startActivityForResult(intent, 7);
                            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseFile.setType("*/*");
                            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                            startActivityForResult(chooseFile, 7);
                        }
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // load the dialog_promt_user.xml layout and inflate to view
                LayoutInflater layoutinflater = LayoutInflater.from(MainActivity.this);
                View promptUserView = layoutinflater.inflate(R.layout.dialog_prompt_time, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(promptUserView);
                final EditText time = (EditText) promptUserView.findViewById(R.id.name);
                if (!timePeriod.equals(""))
                    time.setText(timePeriod);
                alertDialogBuilder.setTitle("Please enter the time between each transmission, in seconds");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (time.getText().toString().equals("")) {
                            //Toast.makeText(getBaseContext(),"Please enter the time",Toast.LENGTH_LONG).show();
                            setTime.setBackgroundResource(R.drawable.set_time);
                            timePeriod = "";
                        } else {
                            // Toast.makeText(getBaseContext(), time.getText().toString()+ " seconds", Toast.LENGTH_LONG).show();
                            timePeriod = time.getText().toString();
                            dialog.cancel();
                            setTime.setBackgroundResource(R.drawable.set_time_);
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent("Send_Msg_To_Wahtsapp");
                intent.putExtra("images", url_images_list.get(0));
                intent.putExtra("text", text.getText().toString());
                intent.putExtra("period", timePeriod);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showNumbersDialog();
            }
        });


        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                 showimagesDialog();
            }
        });
    }


    public void initializ() {
        text = (EditText) findViewById(R.id.editText);
        send = (ImageView) findViewById(R.id.send);
        uploadImage = (ImageView) findViewById(R.id.upload_image);
        uploadFile = (ImageView) findViewById(R.id.upload_file);
        setTime = (ImageView) findViewById(R.id.set_time);
        contact = (ImageView) findViewById(R.id.contact);
        images = (ImageView) findViewById(R.id.images);
    }
    private void startMTWservice() {
        try {
            //todo handle no internet connect with other way
            if (!IsMyServiceRunning(this, MTWservice.class)) {
                Intent i = new Intent(this, MTWservice.class);
                startService(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean IsMyServiceRunning(Context context , Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private String imagesPathes() {
        String pathes="";
        for(String path : url_images_list)
            pathes=pathes+path+";";
        return pathes;
    }
    private void callReadExcelAsync(){
        ArrayList<String> list=new ArrayList<>();
        list.add(filePath);
        list.add(columnName);
        new ReadExcelFileAsync().execute(list);
    }
    private void showNumbersDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(getTitleName());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line);
        for(String number: numberList){
            arrayAdapter.add(number);
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected number is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();*/
            }
        });
        builderSingle.show();
    }
    private void showimagesDialog() {
        Dialog dialog = new PopopDialogBuilder(MainActivity.this)
                .setList(url_images_list)
                .setHeaderBackgroundColor(R.color.color_dialog_bg)
                .setDialogBackgroundColor(R.color.color_dialog_bg)
                .setCloseDrawable(R.drawable.ic_close_white_24dp)
                .setLoadingView(R.layout.loading_view)
                .setDialogStyle(R.style.DialogStyle)
                .showThumbSlider(true)
                .setSliderImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();

        dialog.show();
    }
    private String getTitleName() {

        if(numberList.size()==1)
            return "Phone number";
        else
            return numberList.size() +"  Phone numbers";
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
    public void getImages(){
        /*for (int l = 0; l < mArrayUri.size(); l++) {
            try {
                String path = RealPathUtil.getRealPathFromURI_API19(this, mArrayUri.get(l));
                imagesList.add(createFile(path));
                                        /*Bitmap bitmap = ImageLoader.init().from(path).requestSize(512, 512).getBitmap();
                                        ImageView imageView = new ImageView(getActivity());
                                        imageView.setImageBitmap(bitmap);
                                        imageView.setAdjustViewBounds(true);
                                        layout.addView(imageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
    }
    private File createFile(String path) {
        File image = new File(path);
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    //Uri uri = data.getData();
                    //filePath = getPath(uri);
                    filePath = data.getData().getPath();
                    filePath = filePath.split(":")[1];
                    //Toast.makeText(MainActivity.this, filePath, Toast.LENGTH_LONG).show();
                    uploadFile.setBackgroundResource(R.drawable.upload_file_);
                    callReadExcelAsync();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    // Get the Image from data
                    if (data.getClipData() != null) {
                        int cout = data.getClipData().getItemCount();
                        for (int i = 0; i < cout; i++) {
                            Uri imageurl = data.getClipData().getItemAt(i).getUri();
                            url_images_list.add(imageurl.toString());

                        }
                    } else {
                        Uri imageurl = data.getData();
                        url_images_list.add(imageurl.toString());
                    }
                    uploadImage.setBackgroundResource(R.drawable.upload_photo_);
                    images.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                    uploadImage.setBackgroundResource(R.drawable.upload_photo);
                }
        }
    }

    class ReadExcelFileAsync extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Initialize the progress dialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setIndeterminate(false);
            // Progress dialog horizontal style
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Progress dialog title
            mProgressDialog.setTitle("Importing phone numbers");
            // Progress dialog message
            mProgressDialog.setMessage("Please wait, we are importing phones numbers...");
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... list) {

            ArrayList<String> resultSet = new ArrayList<String>();
            //File sdDir = Environment.getExternalStorageDirectory();
            File inputWorkbook = new File(String.valueOf(list[0].get(0)));
            if(inputWorkbook.exists()){
                Workbook w;
                try {
                    w = Workbook.getWorkbook(inputWorkbook);
                    // Get the first sheet
                    Sheet sheet = w.getSheet(0);
                    int rows=sheet.getRows();
                    // Loop over column and lines
                    for (int j = 0; j < sheet.getRows(); j++) {
                        Cell cell = sheet.getCell(0, j);
                        if(cell.getContents().equals(list[0].get(1))){
                            int count=sheet.getColumns();
                            for (int i = 1; i < sheet.getRows(); i++) {
                                Cell cel = sheet.getCell(j, i);
                                if(!cel.getContents().equals(""))
                                    resultSet.add(cel.getContents());
                                publishProgress((int) (((i+1) / (float) count) * 100));
                            }
                            break;
                        }
                        continue;
                    }
                    if(resultSet.isEmpty()){
                        resultSet.add("Column not found..!");
                    }
                } catch (BiffException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                resultSet.add("File not found..!");
            }
            if(resultSet.size()==0){
                resultSet.add("Data not found..!");
            }
            return resultSet;

        }
        @Override
        protected void onProgressUpdate(Integer... progress) {

            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }
        @Override
        protected void onPostExecute(ArrayList<String> numbersList) {
            mProgressDialog.dismiss();
            numberList=numbersList;
            contact.setVisibility(View.VISIBLE);
        }
    }
}
