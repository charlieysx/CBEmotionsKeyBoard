package com.codebear.cbemotionskeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.codebear.keyboard.CBEmoticonsKeyBoard;
import com.codebear.keyboard.data.AppFuncBean;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.utils.RecordUtil;
import com.codebear.keyboard.widget.CBAppFuncView;
import com.codebear.keyboard.widget.CBEmoticonsView;
import com.codebear.keyboard.widget.FuncLayout;
import com.codebear.keyboard.widget.RecordIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CBEmoticonsKeyBoard cbEmoticonsKeyBoard;
    private RecyclerView rcvContent;
    private RecordIndicator recordIndicator;

    private RecordUtil recordUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycleView();
        initKeyBoard();
        initRecordButton();
        initEmoticonsView();
        initAppFuncView();
    }

    private void initRecycleView() {
        rcvContent = (RecyclerView) findViewById(R.id.rcv_content);
        rcvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rcvContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (null != cbEmoticonsKeyBoard) {
                    cbEmoticonsKeyBoard.reset();
                }
                return false;
            }
        });
    }

    private void initKeyBoard() {
        cbEmoticonsKeyBoard = (CBEmoticonsKeyBoard) findViewById(R.id.ekb_emoticons_keyboard);

        cbEmoticonsKeyBoard.addOnFuncKeyBoardListener(new FuncLayout.OnFuncKeyBoardListener() {
            @Override
            public void OnFuncPop(int height) {

            }

            @Override
            public void OnFuncClose() {

            }
        });

        cbEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, cbEmoticonsKeyBoard.getEtChat().getText().toString(), Toast
                        .LENGTH_SHORT).show();
                cbEmoticonsKeyBoard.getEtChat().setText("");
            }
        });

        cbEmoticonsKeyBoard.getBtnSend().setBackgroundResource(R.drawable.btn_send);
    }

    private void initRecordButton() {
        recordIndicator = new RecordIndicator(this);
        cbEmoticonsKeyBoard.setRecordIndicator(recordIndicator);
        recordIndicator.setOnRecordListener(new RecordIndicator.OnRecordListener() {
            @Override
            public void recordStart() {
                startRecord();
            }

            @Override
            public void recordFinish() {
                finishRecord();
            }

            @Override
            public void recordCancel() {
                cancelRecord();
            }

            @Override
            public long getRecordTime() {
                if(null == recordUtil) {
                    return 0;
                }
                return recordUtil.getVoiceDuration() * 1000;
            }

            @Override
            public int getRecordDecibel() {
                if(null == recordUtil) {
                    return 0;
                }
                return recordUtil.getRecordDecibel();
            }
        });
    }

    private void initEmoticonsView() {
        CBEmoticonsView cbEmoticonsView = new CBEmoticonsView(this);
        cbEmoticonsView.init(getSupportFragmentManager());
        cbEmoticonsKeyBoard.setEmoticonFuncView(cbEmoticonsView);

        cbEmoticonsView.addEmoticonsWithName(new String[]{"default", "ali1", "ali2", "ali3", "sibi", "jinguanzhang"});

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
                        String text = "bigEmoticon : " + " - [" + emoticon.getName() + "] - " + emoticon.getParentId
                                () + " - " + emoticon.getId() + "." + emoticon.getIconType();
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                        Log.i("onEmoticonClick", text);
                    }

                }
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
        cbAppFuncView.setOnAppFuncClickListener(new CBAppFuncView.OnAppFuncClickListener() {
            @Override
            public void onAppFunClick(AppFuncBean emoticon) {
                String text = emoticon.getTitle();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.i("onAppFunClick", text);
            }
        });
    }

    private void startRecord() {
        if (null == recordUtil) {
            recordUtil = new RecordUtil(this);
        }
        recordUtil.start();
    }

    private void finishRecord() {
        recordUtil.finish();
        Toast.makeText(this, "发送了" + recordUtil.getVoiceDuration() + "s的录音", Toast.LENGTH_SHORT).show();
    }

    private void cancelRecord() {
        recordUtil.cancel();
        Toast.makeText(this, "取消录音", Toast.LENGTH_SHORT).show();
    }
}
