package com.zitech.premissiondemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView)findViewById(R.id.tv);
        textView.setText("MainActivity");
        final String permission_call_phone = Manifest.permission.CALL_PHONE;
        final String permission_read_contacts = Manifest.permission.READ_CONTACTS;
        final String permission_camear = Manifest.permission.CAMERA;
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("pepe", "Main"+"当前版本大于等于23");
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission_call_phone);
            int checkReadContactsPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission_read_contacts);
            int checkCamearPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission_camear);
            //如果其中有未授权的
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED||checkReadContactsPermission!= PackageManager.PERMISSION_GRANTED||checkCamearPermission!= PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission_call_phone)) {
                    Log.d("pepe", "Main"+"需要弹出解释对话框,为什么一定需要这个权限");
                    //TODO:之前申请被拒绝，需要弹出解释对话框,为什么一定需要这个权限
                    showMessageOKCancel("You need to allow access to Contacts",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission_call_phone}, 1);
                                }
                            });
                } else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission_read_contacts)){
                    showMessageOKCancel("You need to allow access to Contacts",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission_read_contacts}, 2);
                                }
                            });
                } else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission_camear)) {
                    showMessageOKCancel("You need to allow access to Contacts",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission_camear}, 3);
                                }
                            });
                }else{
                    Log.d("pepe", "Main"+"无需解释，直接申请授权");
                    //TODO:之前没有申请过，无需解释，直接申请授权
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission_call_phone,permission_read_contacts,permission_camear}, 0);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    return;
                }
            } else if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED) {
                //TODO:已授权，不用申请
                Log.d("pepe", "Main"+"已授权，不用申请");
//                callPhone();
            }
        } else {
            //TODO:当前版本低于23，直接使用
            Log.d("pepe", "Main"+"当前版本低于23，直接使用");
            callPhone();
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("pepe","Main"+ "授权成功，做你想做的吧！");
                    //TODO:授权成功，做你想做的吧！
                    callPhone();

                } else {
                    Log.d("pepe", "Main"+"用户拒绝授权！");
                    //TODO:用户拒绝授权！
                    startActivity(new Intent(MainActivity.this,BActivity.class));
                    Toast.makeText(MainActivity.this,"你已拒绝授予该权限，如需更改请于设置中设置！",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void readContact() {
        ContentResolver cr = getContentResolver();
        String str[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, str, null,
                null, null);
        int count = cur.getCount();
        cur.close();

        Toast.makeText(MainActivity.this, String.format("发现%s条", count), Toast.LENGTH_SHORT)
                .show();
    }
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }
}
