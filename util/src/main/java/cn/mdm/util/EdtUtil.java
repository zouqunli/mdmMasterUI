package cn.mdm.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @date 创建时间 2019/12/23
 * @author qlzou
 * @Description 输入框监听刷卡工具类
 * @Version 1.0
 */
public class EdtUtil {

    private static EditText mEtCardNo;
    /**
     * 卡号的输入框获取焦点
     */
    public static void focusCardNoEt() {
        if (mEtCardNo != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEtCardNo.setFocusable(true);
                    mEtCardNo.setFocusableInTouchMode(true);
                    mEtCardNo.findFocus();
                    mEtCardNo.requestFocus();
                    Log.i("tag", "focusCardNoEt");
                }
            }, 500);
        }
    }

    /**
     * 更改刷卡的输入框
     */
    public static void changeCardEdit(EditText et, IEditScanCardListener listener) {
        mEtCardNo = et;
//        if (mEtCardNo != null) {
        mEtCardNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    hideSoftInput(et.getContext(),et);
                    String cardNo = mEtCardNo.getText().toString().trim();
                    mEtCardNo.getText().clear();
                    if (TextUtils.isEmpty(cardNo)) return false;
                    focusCardNoEt();
                    if (listener != null) listener.scanCard(cardNo);
                }
                return false;
            }
        });
//        }
        focusCardNoEt();
    }

    public interface IEditScanCardListener {
        void scanCard(String cardNo);
    }

    /**
     * 隐藏软键盘
     *
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 隐藏软键盘
        if(imm!=null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
