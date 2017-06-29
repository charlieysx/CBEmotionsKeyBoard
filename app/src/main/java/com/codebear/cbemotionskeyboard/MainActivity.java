package com.codebear.cbemotionskeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.codebear.keyboard.CBEmoticonsKeyBoard;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.widget.CBEmoticonsView;
import com.codebear.keyboard.widget.FuncLayout;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private CBEmoticonsKeyBoard cbEmoticonsKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbEmoticonsKeyBoard = (CBEmoticonsKeyBoard) findViewById(R.id.ekb_emoticons_keyboard);

        CBEmoticonsView cbEmoticonsView = new CBEmoticonsView(this, getSupportFragmentManager());
        cbEmoticonsKeyBoard.setEmoticonFuncView(cbEmoticonsView);


        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);

        for(int i = 0;i < 3;++i) {
            EmoticonsBean emoticonsBean = new EmoticonsBean();
            emoticonsBean.setName(emojiArray.get(0).emoji);
            emoticonsBean.setIconUri(emojiArray.get(0).icon);
            emoticonsBean.setRol(7);
            emoticonsBean.setRow(3);
            for (EmojiBean emojiBean : emojiArray) {
                EmoticonsBean bean = new EmoticonsBean();
                bean.setName(emojiBean.emoji);
                bean.setIconUri(emojiBean.icon);
                emoticonsBean.getEmoticonsBeanList().add(bean);
            }
            cbEmoticonsView.addEmoticons(emoticonsBean);
        }

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
}
