## 1.0.4
 * 更新图标，替换为更清晰的图标
 * 修复图标显示大小不正常的bug
 * 添加录音监听

    ### gif
    ![gif](/update/1.0.4/1.0.4.gif)
 * 录音使用

    ```java

    private void initRecordButton() {
        recordIndicator = new RecordIndicator(this);
        cbEmoticonsKeyBoard.setRecordIndicator(recordIndicator);
        recordIndicator.setOnRecordListener(new RecordIndicator.OnRecordListener() {
            @Override
            public void recordStart() {
                Log.i("record", "开始录音");
            }

            @Override
            public void recordFinish() {
                Log.i("record", "录音结束");
            }

            @Override
            public void recordCancel() {
                Log.i("record", "录音取消");
            }
        });
    }
    ```

## 1.0.3
 * 更新UI，调整小部分布局
 * 优化表情包的加载
## 1.0.2
 * 表情键盘最初版本
 * 可插入表情包
 * 可添加app功能模块
 * 输入框显示默认表情包
 * 提供默认的显示表情包的view跟显示app功能的view