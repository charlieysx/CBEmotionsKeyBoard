package com.codebear.cbemotionskeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.codebear.keyboard.CBEmoticonsKeyBoard;
import com.codebear.keyboard.data.AppFuncBean;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.widget.CBAppFuncView;
import com.codebear.keyboard.widget.CBEmoticonsView;
import com.codebear.keyboard.widget.FuncLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CBEmoticonsKeyBoard cbEmoticonsKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbEmoticonsKeyBoard = (CBEmoticonsKeyBoard) findViewById(R.id.ekb_emoticons_keyboard);

        initEmoticonsView();
        initAppFuncView();
    }

    private void initEmoticonsView() {
        CBEmoticonsView cbEmoticonsView = new CBEmoticonsView(this);
        cbEmoticonsView.init(getSupportFragmentManager());
        cbEmoticonsKeyBoard.setEmoticonFuncView(cbEmoticonsView);

        cbEmoticonsView.addEmoticonsWithName(new String[]{"default", "xd_emoticon", "jinguanzhang"});

        cbEmoticonsView.setOnEmoticonClickListener(new CBEmoticonsView.OnEmoticonClickListener() {
            @Override
            public void onEmoticonClick(EmoticonsBean emoticon, boolean isDel) {
                if (isDel) {
                    Log.i("onEmoticonClick", "delete");
                    cbEmoticonsKeyBoard.delClick();
                } else {
                    if ("default".equals(emoticon.getParentTag())) {
                        String content = emoticon.getName();
                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        int index = cbEmoticonsKeyBoard.getEtChat().getSelectionStart();
                        Editable editable = cbEmoticonsKeyBoard.getEtChat().getText();
                        editable.insert(index, content);
                    } else {
                        Log.i("onEmoticonClick", "bigEmoticon : " + " - [" + emoticon.getName() + "] - " + emoticon
                                .getParentId() + " - " + emoticon.getId() + "." + emoticon.getIconType());
                    }

                }
            }
        });

        cbEmoticonsKeyBoard.addOnFuncKeyBoardListener(new FuncLayout.OnFuncKeyBoardListener() {
            @Override
            public void OnFuncPop(int height) {

            }

            @Override
            public void OnFuncClose() {

            }
        });

        cbEmoticonsKeyBoard.getBtnVoice().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        cbEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbEmoticonsKeyBoard.getEtChat().setText("");
            }
        });
    }

    private void initAppFuncView() {
        CBAppFuncView cbAppFuncView = new CBAppFuncView(this);
        cbEmoticonsKeyBoard.setAppFuncView(cbAppFuncView);
        List<AppFuncBean> appFuncBeanList = new ArrayList<>();
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_photo, "图片"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_ptjob, "兼职"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_reply, "快捷回复"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_location, "定位"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_photo, "图片"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_ptjob, "兼职"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_chat_reply, "快捷回复"));
        appFuncBeanList.add(new AppFuncBean(R.mipmap.ic_location, "定位"));
        cbAppFuncView.setAppFuncBeanList(appFuncBeanList);
    }
}
