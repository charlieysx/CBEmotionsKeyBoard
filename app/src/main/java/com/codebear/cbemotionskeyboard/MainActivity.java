package com.codebear.cbemotionskeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.codebear.keyboard.CBEmoticonsKeyBoard;
import com.codebear.keyboard.widget.FuncLayout;

public class MainActivity extends AppCompatActivity {

    private CBEmoticonsKeyBoard cbEmoticonsKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbEmoticonsKeyBoard = (CBEmoticonsKeyBoard) findViewById(R.id.ekb_emoticons_keyboard);

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
