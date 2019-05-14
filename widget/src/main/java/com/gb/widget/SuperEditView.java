package com.gb.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.gb.widget.util.Unit;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * Create by wgb on 2019/4/15.
 */
public class SuperEditView extends LinearLayout {

    private Context mContext;
    //左边Icon相关属性
    private AppCompatImageView leftIconIv;
    private Drawable leftIconSrc;
    private int leftIconMarginRight;

    //EditText相关属性
    private AppCompatEditText editText;
    private String editHint;
    private ColorStateList editHintTextColor;
    private Drawable editBackground;
    private ColorStateList editTextColor;
    private String editTextString;
    private int editTextSize;
    private boolean singleLine;
    private int inputType;
    private int maxLength;

    //清除按钮相关
    private AppCompatImageView clearIconIv;
    private boolean deletable;
    private Drawable deleteIconSrc;

    //*显示内容
    private AppCompatImageView hideShowSwitchIv;
    private boolean showSwitch;
    private Drawable hideIconSrc;
    private Drawable showIconSrc;

    //默认数据
    private int defaultSize = 14;
    private int defaultMargin = 15;
    private int defaultInputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    private int defaultMaxLength = 30;
    private int defaultTextColor = 0xFF888888;//文字默认颜色
    private int defaultHintTextColor = 0xFF888888;//提示文字默认颜色

    //画笔相关
    private Paint paint;

    public SuperEditView(Context context) {
        this(context, null);
    }

    public SuperEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        defaultSize = Unit.sp2px(context, defaultSize);
        defaultSize = Unit.sp2px(context, defaultMargin);
        initAttrs(attrs);
        initPaint();
        initView();
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.SuperEditView);

        ////////////////////
        leftIconSrc = ta.getDrawable(R.styleable.SuperEditView_sLeftIconSrc);
        leftIconMarginRight = ta.getDimensionPixelSize(R.styleable.SuperEditView_sLeftIconMarginRight, defaultMargin);

        editHint = ta.getString(R.styleable.SuperEditView_sEditHint);
        editHintTextColor = ta.getColorStateList(R.styleable.SuperEditView_sEditHintTextColor);
        editTextColor = ta.getColorStateList(R.styleable.SuperEditView_sEditTextColor);
        editBackground = ta.getDrawable(R.styleable.SuperEditView_sEditBackground);
        editTextSize = ta.getDimensionPixelSize(R.styleable.SuperEditView_sEditTextSize, defaultSize);
        editTextString = ta.getString(R.styleable.SuperEditView_sEditTextString);
        singleLine = ta.getBoolean(R.styleable.SuperEditView_android_singleLine, true);
        inputType = ta.getInt(R.styleable.SuperEditView_android_inputType, defaultInputType);
        maxLength = ta.getInt(R.styleable.SuperEditView_android_maxLength, defaultMaxLength);

        deletable = ta.getBoolean(R.styleable.SuperEditView_sDeletable, true);
        deleteIconSrc = ta.getDrawable(R.styleable.SuperEditView_sDeleteIconSrc);

        showSwitch = ta.getBoolean(R.styleable.SuperEditView_sShowSwitch, false);
        hideIconSrc = ta.getDrawable(R.styleable.SuperEditView_sHideIconSrc);
        showIconSrc = ta.getDrawable(R.styleable.SuperEditView_sShowIconSrc);
        ///////////////////
        ta.recycle();
    }

    private void initView() {
        initSuperEditText();
        initLeftIcon();
        initEdit();
        initClearIcon();
        initHideShowSwitch();
    }

    private void initSuperEditText() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Unit.dp2px(mContext, 55));
        setLayoutParams(params);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(Unit.dp2px(mContext, 13), 0, 0, 0);
    }

    private void initLeftIcon() {
        if (leftIconIv == null) {
            leftIconIv = new AppCompatImageView(mContext);
        }
        LayoutParams leftIconParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftIconParams.rightMargin = defaultMargin;
        leftIconIv.setId(R.id.sLeftIconIv);
        leftIconIv.setLayoutParams(leftIconParams);
        if (leftIconSrc != null) {
            leftIconIv.setImageDrawable(leftIconSrc);
        } else {
            leftIconIv.setVisibility(GONE);
        }
        addView(leftIconIv);
    }

    private void initEdit() {
        if (editText == null) {
            editText = new AppCompatEditText(mContext);
        }
        LayoutParams editTextParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        editTextParams.weight = 1;
        editText.setId(R.id.sEditText);
        editText.setLayoutParams(editTextParams);
        if (editBackground != null) {
            editText.setBackground(editBackground);
        } else {
            editText.setBackground(null);
        }
        editText.setHint(editHint);
//        editText.setMaxEms(maxLength);
        editText.setSingleLine(singleLine);
        editText.setText(editTextString);
        if (editTextColor == null) {
            editTextColor = ColorStateList.valueOf(defaultTextColor);
        }
        editText.setTextColor(editTextColor);
        if (editHintTextColor == null) {
            editHintTextColor = ColorStateList.valueOf(defaultHintTextColor);
        }
        editText.setHintTextColor(editHintTextColor);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
        editText.setInputType(inputType);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clearIconIv.getVisibility() == View.GONE && deletable) {
                    clearIconIv.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clearIconIv.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty()) {
                    return;
                }
                //TODO 添加检测代码
//                if (!s.toString().matches("[A-Za-z0-9]+")) {
//                    String temp = s.toString();
//                    Toast.makeText(mContext, "请输入数字或字母", Toast.LENGTH_SHORT).show();
//                    s.delete(temp.length() - 1, temp.length());
//                    editText.setSelection(s.length());
//                }
            }
        });
        addView(editText);
    }

    private void initClearIcon() {
        if (clearIconIv == null) {
            clearIconIv = new AppCompatImageView(mContext);
        }
        LayoutParams clearParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clearIconIv.setId(R.id.sClearIconIv);
        clearIconIv.setLayoutParams(clearParams);
        clearIconIv.setVisibility(GONE);
        if (deleteIconSrc == null) {
            deleteIconSrc = mContext.getResources().getDrawable(R.drawable.ic_clear);
        }
        clearIconIv.setImageDrawable(deleteIconSrc);
        clearIconIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        addView(clearIconIv);
    }

    private void initHideShowSwitch() {
        if (hideShowSwitchIv == null) {
            hideShowSwitchIv = new AppCompatImageView(mContext);
        }
        LayoutParams switchParams = new LinearLayout.LayoutParams(Unit.dp2px(mContext, 40), LayoutParams.WRAP_CONTENT);
        hideShowSwitchIv.setId(R.id.sHideShowSwitchIv);
        hideShowSwitchIv.setLayoutParams(switchParams);
        if (hideIconSrc == null) {
            hideIconSrc = mContext.getResources().getDrawable(R.drawable.ic_invisible);
        }
        if (showIconSrc == null) {
            showIconSrc = mContext.getResources().getDrawable(R.drawable.ic_visible);
        }
        if (inputType != (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            hideShowSwitchIv.setImageDrawable(showIconSrc);
        } else {
            hideShowSwitchIv.setImageDrawable(hideIconSrc);
        }
        if (!showSwitch) {
            hideShowSwitchIv.setVisibility(GONE);
        } else {
            hideShowSwitchIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        hideShowSwitchIv.setImageDrawable(showIconSrc);
                    } else {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        hideShowSwitchIv.setImageDrawable(hideIconSrc);
                    }
                    String pwd = editText.getText().toString();
                    if (!TextUtils.isEmpty(pwd))
                        editText.setSelection(pwd.length());
                }
            });
        }
        addView(hideShowSwitchIv);
    }


    private void initPaint() {
    }

    public String getString() {
        return editText.getText().toString();
    }
}
