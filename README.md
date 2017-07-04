# CBEmotionsKeyBoard
开源表情键盘

## 说明

> 该库是基于[w446108264](https://github.com/w446108264)提供的开源表情键盘解决方案[XhsEmoticonsKeyboard](https://github.com/w446108264/XhsEmoticonsKeyboard)修改的

## gif

![gif](/screenshot/showGif.gif)

## screen

![1](/screenshot/1.png "1")
![2](/screenshot/2.png "2")
![3](/screenshot/3.png "3")
![4](/screenshot/4.png "4")
![5](/screenshot/5.png "5")
![6](/screenshot/6.png "6")
![7](/screenshot/7.png "7")

## Gradle

* 添加以下代码到项目的build.gradle里:
```xml  
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
* 加上以下依赖
```xml
dependencies {
    compile 'com.codebear.keyboard:emoticons-keyboard:1.0.3'
}
```
* 库中需要解压表情包，需要用到存储权限，所以在项目的AndroidManifest.xml中还需要加入以下权限(6.0以上需要申请)
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## 使用
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.codebear.keyboard.CBEmoticonsKeyBoard
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/ekb_emoticons_keyboard"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/rcv_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </LinearLayout>

</com.codebear.keyboard.CBEmoticonsKeyBoard>
```

### activity

```java
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
                    String text = "bigEmoticon : " + " - [" + emoticon.getName() + "] - " + emoticon.getParentId() + " - " + emoticon.getId() + "." + emoticon.getIconType();
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
```

## 其他

 * 该库把显示表情包的view跟显示功能view跟表情键盘分开来，可以实现自定义的视图
 1. 通过调用以下方法可以设置显示表情包的view
       ```java
        setEmoticonFuncView(IEmoticonsView emoticonView)
       ```
    IEmoticonsView是表情包view统一的接口
       ```java
        public interface IEmoticonsView {
            /**
             * 获取view视图
             */
            View getView();
            /**
             * 通知view被打开了(即要显示出来)
             */
            void openView();
        }
       ```

 2. 通过调用以下方法可以设置显示app功能的biew
       ```java
        setAppFuncView(View appFuncView)
       ```

 * 该库提供了默认的表情包view:CBEmoticonsView和默认的app功能的view:CBAppFuncView
  #### 1. CBEmoticonsView的使用
 ```java
    //初始化CBEmoticonsView
    CBEmoticonsView cbEmoticonsView = new CBEmoticonsView(this);
    //内部使用了FragmentStatePagerAdapter，所以需要传入FragmentManager
    cbEmoticonsView.init(getSupportFragmentManager());
    //为keyboard添加显示表情包的view
    cbEmoticonsKeyBoard.setEmoticonFuncView(cbEmoticonsView);

    //添加表情包数据
    //通过表情包的名字
    //default是本库提供的默认的emoji表情
    //其他的需要自行添加在项目的assets->emoticon目录下，并且命名为xxx.zip
    //以下传入的名称就是xxx
    cbEmoticonsView.addEmoticonsWithName(new String[]{"default", "ali1", "ali2", "ali3","sibi","jinguanzhang"});
    //添加表情的点击事件
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
                String text = "bigEmoticon : " + " - [" + emoticon.getName() + "] - " + emoticon.getParentId() + " - " + emoticon.getId() + "." + emoticon.getIconType();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.i("onEmoticonClick", text);
            }
        }
    }
```
  ##### 1.1 添加表情包的方式
 表情包放在项目的assets->emoticon目录下，以ali1表情包为例：
 新建文件夹ali1，在文件夹ali1下新建文件夹命名为2968(表示ali1表情包的id，可以根据需要改别的),2968这个文件夹下就放表情包图片，可以是gif、png、jpg等，在ali1目录下新建ali1.xml(命名一定要跟文件夹一致，这里为ali1)，然后编写ali1.xml表情包配置文件，可以参考app例子
 ```xml
 <EmoticonSet>
    <id>2968</id>
    <name>阿狸1</name>
    <iconUri>01.gif</iconUri>
    <iconType>gif</iconType>
    <showName>0</showName>
    <bigEmoticon>1</bigEmoticon>
    <rol>4</rol>
    <row>2</row>
    <showDel>0</showDel>
    <Emoticon>
        <id>01</id>
        <iconUri>01.gif</iconUri>
        <iconType>gif</iconType>
    </Emoticon>
    <Emoticon>
        <id>02</id>
        <iconUri>02.gif</iconUri>
        <iconType>gif</iconType>
    </Emoticon>
 <EmoticonSet>
 ```
 ##### EmoticonSet 下各参数介绍：
   * id : 表情包的id
   * name : 表情包名称
   * iconUri : 表情包tab显示的图标
   * iconType : 图标的格式
   * showName : 表情包是否显示名称(0表示不显示，1表示显示)
   * bigEmoticon : 是否是大表情(0表示不显示，1表示显示)
   * rol : 表情包显示的列数
   * row : 表情包显示的行数
   * showDel : 是否显示删除键(0表示不显示，1表示显示)

 ##### Emoticon 下各参数介绍：
   * id : 表情的id
   * name : 表情名称
   * iconUri : 表情图标
   * iconType : 图标的格式

  #### 2. CBAppFuncView的使用
 ```java
    //初始化CBAppFuncView
    CBAppFuncView cbAppFuncView = new CBAppFuncView(this);
    //为keyboard添加显示显示app功能的view
    cbEmoticonsKeyBoard.setAppFuncView(cbAppFuncView);
    //添加数据
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
 ```
 相对表情包显示，CBAppFuncView使用比较简单，只需要初始化并传入AppFuncBean列表即可，AppFuncBean有两个变量，分别是显示的图标跟名称

 3. 使用以下方法更改发送按钮的背景
       ```java
        cbEmoticonsKeyBoard.getBtnSend().setBackgroundResource(R.drawable.btn_send);
       ```
