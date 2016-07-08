package com.zitech.premissiondemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pepe on 2016/7/8.
 * E_mail: 494778200@qq.com
 * Company:小知科技 http://www.zizizizizi.com/
 */
public class BActivity extends Activity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String permission = Manifest.permission.CALL_PHONE;
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("pepe", "B"+"当前版本大于等于23");
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission == PackageManager.PERMISSION_DENIED) {
                Log.d("pepe", "B"+"弹出授权申请对话框");
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(BActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    //TODO:需要弹出解释对话框
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(BActivity.this, new String[]{permission}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    return;
                }
            } else if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED) {
                //TODO:已授权，不用申请
                Log.d("pepe", "B"+"已授权，不用申请");
                callPhone();
            }
        } else {
            //TODO:当前版本低于23，直接使用
            Log.d("pepe", "B"+"当前版本低于23，直接使用");
            callPhone();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("pepe", "B"+"授权成功，做你想做的吧！");
                    callPhone();
                    //TODO:授权成功

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.d("pepe", "B"+"用户拒绝授权！");
                    //TODO:用户拒绝
                    // Permission Denied
                    Toast.makeText(BActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

//                    //读取联系人
//                    ContentResolver resolver = BActivity.this.getContentResolver();
//                    // 获取手机联系人
//                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null, null, null);

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
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
