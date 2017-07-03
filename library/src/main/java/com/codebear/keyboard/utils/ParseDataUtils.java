package com.codebear.keyboard.utils;

import android.content.Context;
import android.util.Log;

import com.codebear.keyboard.data.EmoticonsBean;

import java.io.File;
import java.io.IOException;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/30.
 */

public class ParseDataUtils {
    public static EmoticonsBean parseDataFromFile(Context context, String zipFileName) {
        String filePath = FileUtils.getFolderPath("emoticon");
        String xmlFilePath = filePath + "/" + zipFileName + "/" + zipFileName + ".xml";
        File file = new File(xmlFilePath);
        if (!file.exists()) {
            Log.i("upzip", "upzip : " + zipFileName);
            try {
                FileUtils.unzip(context.getAssets().open("emoticon/" + zipFileName + ".zip"), filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        file = new File(xmlFilePath);
        if (file.exists()) {
            XmlUtil xmlUtil = new XmlUtil(context);
            return xmlUtil.ParserXml(filePath + "/" + zipFileName, xmlUtil.getXmlFromSD(xmlFilePath));
        }
        return null;
    }
}
