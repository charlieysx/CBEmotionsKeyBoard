package com.codebear.keyboard.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.codebear.keyboard.R;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/7/4.
 */

public class RecordIndicator {

    public enum RecordView {
        START_VIEW, CANCEL_VIEW, SHORT_VIEW
    }

    private Context mContext;

    private static int[] amps = {R.mipmap.amp1, R.mipmap.amp2, R.mipmap.amp3, R.mipmap.amp4, R.mipmap.amp5, R.mipmap
            .amp6, R.mipmap.amp7};

    private Dialog recordDialog;
    private ViewFlipper viewFlipper;
    private ImageView volumeAnim;

    private boolean cancel_record = false;
    private boolean start_record = false;
    /**
     * 最短录音时长为1s
     */
    private int minRecordTime = 1000;
    private Button recordButton;

    public RecordIndicator(Context mContext) {
        this.mContext = mContext;
        initDialog();
    }

    private void initDialog() {
        View view = View.inflate(mContext, R.layout.dialog_record_indicator, null);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.vf_record);
        volumeAnim = (ImageView) view.findViewById(R.id.iv_record_amp);
        viewFlipper.setDisplayedChild(0);

        recordDialog = new Dialog(mContext, R.style.preview_dialog_style);
        recordDialog.setContentView(view);
    }

    private void show() {
        if (recordDialog != null && !recordDialog.isShowing()) {
            recordDialog.show();

        }
    }

    private void dismiss() {
        if (recordDialog != null && recordDialog.isShowing()) {
            recordDialog.dismiss();
        }
    }

    private void showView(RecordView recordView) {
        switch (recordView) {
            case START_VIEW:
                viewFlipper.setDisplayedChild(0);
                break;
            case CANCEL_VIEW:
                viewFlipper.setDisplayedChild(1);
                break;
            case SHORT_VIEW:
                viewFlipper.setDisplayedChild(2);
                break;
        }
    }

    public void setRecordDecibel(int decibelRank) {
        if (decibelRank < 0 || decibelRank > 6) {
            decibelRank = 0;
        }
        volumeAnim.setImageResource(amps[decibelRank]);
    }

    public void setMinRecordTime(int minRecordTime) {
        this.minRecordTime = minRecordTime;
    }

    public void setRecordButton(Button recordButton) {
        this.recordButton = recordButton;
        listenerVoiceBtn();
    }

    private void listenerVoiceBtn() {
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onRecordListener != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startRecord(true);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (start_record) {
                                float x = event.getX();
                                float y = event.getY();
                                boolean cancelY;
                                boolean cancelX;
                                if (y < 0) {
                                    cancelY = (-y > recordButton.getHeight() * 4);
                                } else {
                                    cancelY = (y > recordButton.getHeight() * 1.5);
                                }
                                cancelX = (x < 0 || x > recordButton.getWidth());

                                cancel_record = cancelX || cancelY;

                                if (cancel_record) {
                                    recordButton.setText("松开手指，结束录音");
                                    cancelRecord(false);
                                } else {
                                    recordButton.setText("松开结束");
                                    startRecord(false);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            start_record = false;
                            recordButton.setText("按住录音");
                            long intervalTime = onRecordListener.getRecordTime();
                            if (cancel_record) {
                                cancelRecord(true);
                            } else if (intervalTime < minRecordTime) {
                                recordTooShort();
                            } else {
                                finishRecord();
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    private DecibelThread decibelThread;

    private void startRecord(boolean realStart) {
        showView(RecordIndicator.RecordView.START_VIEW);
        if (realStart) {
            cancel_record = false;
            start_record = true;
            recordButton.setBackgroundResource(R.drawable.btn_voice_press);
            recordButton.setText("松开结束");
            onRecordListener.recordStart();
            show();
            decibelThread = new DecibelThread();
            decibelThread.start();
        }
    }

    private void cancelRecord(boolean dismiss) {
        if (decibelThread != null) {
            decibelThread.exit();
            decibelThread = null;
        }
        showView(RecordIndicator.RecordView.CANCEL_VIEW);
        if (dismiss) {
            onRecordListener.recordCancel();
            dismissRecordIndicator(600);
        }
    }

    private void finishRecord() {
        if (decibelThread != null) {
            decibelThread.exit();
            decibelThread = null;
        }
        onRecordListener.recordFinish();
        dismissRecordIndicator(200);
    }

    private void recordTooShort() {
        showView(RecordIndicator.RecordView.SHORT_VIEW);
        onRecordListener.recordCancel();
        dismissRecordIndicator(600);
    }

    private void dismissRecordIndicator(long time) {
        recordButton.setBackgroundResource(R.drawable.btn_voice_normal);
        recordButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, time);
    }


    private OnRecordListener onRecordListener;

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    /**
     * 录音接口
     */
    public interface OnRecordListener {
        /**
         * 开始录音
         */
        void recordStart();

        /**
         * 结束录音
         */
        void recordFinish();

        /**
         * 取消录音
         */
        void recordCancel();

        /**
         * 获取录音时长
         *
         * @return
         */
        long getRecordTime();

        /**
         * 获取分贝等级
         *
         * @return
         */
        int getRecordDecibel();
    }

    private class DecibelThread extends Thread {
        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            super.run();
            while (running) {
                if (onRecordListener == null || !running) {
                    break;
                }
                viewFlipper.post(new Runnable() {
                    @Override
                    public void run() {
                        setRecordDecibel(onRecordListener.getRecordDecibel());
                    }
                });
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
