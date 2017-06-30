package com.codebear.cbemotionskeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.codebear.keyboard.CBEmoticonsKeyBoard;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.widget.CBEmoticonsView;
import com.codebear.keyboard.widget.FuncLayout;

public class MainActivity extends AppCompatActivity {

    private CBEmoticonsKeyBoard cbEmoticonsKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbEmoticonsKeyBoard = (CBEmoticonsKeyBoard) findViewById(R.id.ekb_emoticons_keyboard);

        CBEmoticonsView cbEmoticonsView = new CBEmoticonsView(this, getSupportFragmentManager());
        cbEmoticonsKeyBoard.setEmoticonFuncView(cbEmoticonsView);

        cbEmoticonsView.addEmoticonsWithName(new String[]{"default", "xd_emoticon"});

        cbEmoticonsView.setOnEmoticonClickListener(new CBEmoticonsView.OnEmoticonClickListener() {
            @Override
            public void onEmoticonClick(EmoticonsBean emoticon, boolean isDel) {
                if (isDel) {
                    Log.i("onEmoticonClick", "delete");
                } else {
                    Log.i("onEmoticonClick", "bigEmoticon : " + emoticon.isBigEmoticon() + " - [" + emoticon.getName()
                            + "] - " + emoticon.getParentId() + " - " + emoticon.getId() + "." + emoticon.getIconType());

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
}
