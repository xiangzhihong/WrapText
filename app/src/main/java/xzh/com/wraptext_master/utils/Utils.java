package xzh.com.wraptext_master.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.InputStream;

/**
 * Created by xiangzhihong on 2016/6/2 on 15:53.
 */
public class Utils {

    public static String getAssertData(Context context, String fileName){
        String resultString="";
        try {
            InputStream inputStream=context.getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) (context.getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
