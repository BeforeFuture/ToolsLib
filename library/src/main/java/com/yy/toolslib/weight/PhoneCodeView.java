package com.yy.toolslib.weight;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.toolslib.utils.YyInflaterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证码输入框
 */
public class PhoneCodeView extends RelativeLayout {
    private Context context;
    private TextView tv_code1;
    private TextView tv_code2;
    private TextView tv_code3;
    private TextView tv_code4;
    private TextView tv_code5;
    private TextView tv_code6;
    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private View v5;
    private View v6;
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;

    public PhoneCodeView(Context context) {
        super(context);
        this.context = context;
        loadView();
    }

    public PhoneCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    private void loadView() {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(YyInflaterUtils.getLayout(context, "phone_code"), this);
        initView(view);
        initEvent();
    }

    private void initView(View view) {
        tv_code1 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code1"));
        tv_code2 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code2"));
        tv_code3 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code3"));
        tv_code4 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code4"));
        tv_code5 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code5"));
        tv_code6 = (TextView) view.findViewById(YyInflaterUtils.getControl(context, "tv_code6"));
        et_code = (EditText) view.findViewById(YyInflaterUtils.getControl(context, "et_code"));
        v1 = view.findViewById(YyInflaterUtils.getControl(context, "v1"));
        v2 = view.findViewById(YyInflaterUtils.getControl(context, "v2"));
        v3 = view.findViewById(YyInflaterUtils.getControl(context, "v3"));
        v4 = view.findViewById(YyInflaterUtils.getControl(context, "v4"));
        v5 = view.findViewById(YyInflaterUtils.getControl(context, "v5"));
        v6 = view.findViewById(YyInflaterUtils.getControl(context, "v6"));
    }

    private void initEvent() {
        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    et_code.setText("");
                    if (codes.size() < 6) {
                        codes.add(editable.toString());
                        showCode();
                    }
                }
            }
        });

        // 监听验证码删除按键
        et_code.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                codes.remove(codes.size() - 1);
                showCode();
                return true;
            }
            return false;
        });
    }

    /**
     * 显示输入的验证码
     */
    private void showCode() {
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        String code5 = "";
        String code6 = "";
        if (codes.size() >= 1) {
            code1 = codes.get(0);
        }
        if (codes.size() >= 2) {
            code2 = codes.get(1);
        }
        if (codes.size() >= 3) {
            code3 = codes.get(2);
        }
        if (codes.size() >= 4) {
            code4 = codes.get(3);
        }
        if (codes.size() >= 5) {
            code5 = codes.get(4);
        }
        if (codes.size() >= 6) {
            code6 = codes.get(5);
        }
        tv_code1.setText(code1);
        tv_code2.setText(code2);
        tv_code3.setText(code3);
        tv_code4.setText(code4);
        tv_code5.setText(code5);
        tv_code6.setText(code6);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    /**
     * 设置高亮颜色
     */
    private void setColor() {
        int color_default = Color.parseColor("#999999");
        int color_focus = Color.parseColor("#3F8EED");
        v1.setBackgroundColor(color_default);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
        v5.setBackgroundColor(color_default);
        v6.setBackgroundColor(color_default);
        if (codes.size() == 0) {
            v1.setBackgroundColor(color_focus);
        }
        if (codes.size() == 1) {
            v2.setBackgroundColor(color_focus);
        }
        if (codes.size() == 2) {
            v3.setBackgroundColor(color_focus);
        }
        if (codes.size() == 3) {
            v4.setBackgroundColor(color_focus);
        }
        if (codes.size() == 4) {
            v5.setBackgroundColor(color_focus);
        }
        if (codes.size() >= 5) {
            v6.setBackgroundColor(color_focus);
        }
    }

    /**
     * 回调
     */
    private void callBack() {
        if (onInputListener == null) {
            return;
        }
        if (codes.size() == 6) {

            if(null != imm && imm.isActive()){
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

            onInputListener.onSucess(getPhoneCode());
        } else {
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener {
        void onSucess(String code);

        void onInput();
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput() {
        //显示软键盘
        if (imm != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(et_code, 0);
                }
            }, 200);
        }
    }



    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }
}