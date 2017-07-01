package com.codebear.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codebear.keyboard.emoji.DefaultEmojiFilter;
import com.codebear.keyboard.utils.EmoticonsKeyboardUtils;
import com.codebear.keyboard.widget.AutoHeightLayout;
import com.codebear.keyboard.widget.EmoticonsEditText;
import com.codebear.keyboard.widget.FuncLayout;

/**
 * description:
 * <p>
 * 参照w446108264提供的XhsEmoticonsKeyboard开源键盘解决方案
 * github:https://github.com/w446108264/XhsEmoticonsKeyboard
 * <p>
 * Created by CodeBear on 2017/6/28.
 */

public class CBEmoticonsKeyBoard extends AutoHeightLayout implements View.OnClickListener, EmoticonsEditText
        .OnBackKeyClickListener, FuncLayout.OnFuncChangeListener {
    public static final int FUNC_TYPE_EMOTION = -1;
    public static final int FUNC_TYPE_APPS = -2;

    protected LayoutInflater mInflater;

    protected ImageView mBtnVoiceOrText;
    protected AppCompatButton mBtnVoice;
    protected EmoticonsEditText mEtChat;
    protected ImageView mBtnFace;
    protected RelativeLayout mRlInput;
    protected ImageView mBtnMultimedia;
    protected AppCompatButton mBtnSend;
    protected FuncLayout funFunction;

    protected boolean mDispatchKeyEventPreImeLock = false;

    /**
     * 用于记录软键盘关闭是手动关闭还是点击功能按钮
     */
    private int clickFunc = 0;

    public CBEmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateKeyboardBar();
        initView();
        initFuncView();
    }

    protected void inflateKeyboardBar() {
        mInflater.inflate(R.layout.cb_view_keyboard, this);
    }

    protected View inflateFunc() {
        return mInflater.inflate(R.layout.cb_view_app_func_default, null);
    }

    protected void initView() {
        mBtnVoiceOrText = findViewById(R.id.iv_voice_or_text);
        mBtnVoice = findViewById(R.id.btn_voice);
        mEtChat = findViewById(R.id.et_chat);
        mBtnFace = findViewById(R.id.iv_face);
        mRlInput = findViewById(R.id.rl_input);
        mBtnMultimedia = findViewById(R.id.iv_multimedia);
        mBtnSend = findViewById(R.id.btn_send);
        funFunction = findViewById(R.id.fun_function);

        mBtnVoiceOrText.setOnClickListener(this);
        mBtnFace.setOnClickListener(this);
        mBtnMultimedia.setOnClickListener(this);
        mEtChat.setOnBackKeyClickListener(this);
        funFunction.setOnFuncChangeListener(this);
    }

    protected void initFuncView() {
        initEmoticonFuncView();
        initAppFuncView();
        initEditView();
    }

    protected void initEmoticonFuncView() {
        View keyboardView = inflateFunc();
        funFunction.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
    }

    protected void initAppFuncView() {
        View keyboardView = inflateFunc();
        funFunction.addFuncView(FUNC_TYPE_APPS, keyboardView);
    }

    protected void initEditView() {
        mEtChat.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mEtChat.isFocused()) {
                    mEtChat.setFocusable(true);
                    mEtChat.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        mEtChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnSend.setVisibility(VISIBLE);
                    mBtnMultimedia.setVisibility(GONE);
                    mBtnSend.setBackgroundResource(R.drawable.btn_send_bg);
                } else {
                    mBtnMultimedia.setVisibility(VISIBLE);
                    mBtnSend.setVisibility(GONE);
                }
            }
        });
        mEtChat.addEmoticonFilter(new DefaultEmojiFilter());
    }

    public void setEmoticonFuncView(View emoticonFuncView) {
        if(null != emoticonFuncView) {
            funFunction.addFuncView(FUNC_TYPE_EMOTION, emoticonFuncView);
        }
    }

    public void setAppFuncView(View appFuncView) {
        if(null != appFuncView) {
            funFunction.addFuncView(FUNC_TYPE_APPS, appFuncView);
        }
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        funFunction.hideAllFuncView();
        mBtnFace.setImageResource(R.drawable.icon_face_nomal);
    }

    protected void showVoice() {
        mRlInput.setVisibility(GONE);
        mBtnFace.setVisibility(GONE);
        mBtnVoice.setVisibility(VISIBLE);
        reset();
    }

    protected void checkVoice() {
        if (mBtnVoice.isShown()) {
            mBtnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text_keyboard);
        } else {
            mBtnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text);
        }
    }

    protected void showText() {
        mRlInput.setVisibility(VISIBLE);
        mBtnFace.setVisibility(VISIBLE);
        mBtnVoice.setVisibility(GONE);
    }

    protected void toggleFuncView(int key) {
        showText();
        funFunction.toggleFuncView(key, isSoftKeyboardPop(), mEtChat);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
            mBtnFace.setImageResource(R.drawable.icon_softkeyboard_nomal);
        } else {
            mBtnFace.setImageResource(R.drawable.icon_face_nomal);
        }
        checkVoice();
    }

    protected void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) funFunction.getLayoutParams();
        params.height = height;
        funFunction.setLayoutParams(params);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        funFunction.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        funFunction.setVisibility(true);
        onFuncChange(funFunction.DEF_KEY);
        clickFunc = 0;
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (clickFunc == 0) {
            reset();
        } else {
            onFuncChange(funFunction.getCurrentFuncKey());
        }
    }

    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener l) {
        funFunction.addOnKeyBoardListener(l);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_voice_or_text) {
            if (mRlInput.isShown()) {
                mBtnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text_keyboard);
                showVoice();
            } else {
                showText();
                mBtnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text);
                funFunction.setVisibility(true);
                EmoticonsKeyboardUtils.openSoftKeyboard(mEtChat);
            }
        } else if (i == R.id.iv_face) {
            EmoticonsKeyboardUtils.openSoftKeyboard(mEtChat);
            EmoticonsKeyboardUtils.closeSoftKeyboard(mEtChat);
            clickFunc = FUNC_TYPE_EMOTION;
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == R.id.iv_multimedia) {
            clickFunc = FUNC_TYPE_APPS;
            toggleFuncView(FUNC_TYPE_APPS);
        }
    }

    @Override
    public void onBackKeyClick() {
        if (funFunction.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reset();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (funFunction.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && funFunction.isShown()) {
                    reset();
                    return true;
                }
            default:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    boolean isFocused;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        isFocused = mEtChat.getShowSoftInputOnFocus();
                    } else {
                        isFocused = mEtChat.isFocused();
                    }
                    if (isFocused) {
                        mEtChat.onKeyDown(event.getKeyCode(), event);
                    }
                }
                return false;
        }
    }

    public EmoticonsEditText getEtChat() {
        return mEtChat;
    }

    public AppCompatButton getBtnVoice() {
        return mBtnVoice;
    }

    public AppCompatButton getBtnSend() {
        return mBtnSend;
    }


    public void delClick() {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mEtChat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }
}
