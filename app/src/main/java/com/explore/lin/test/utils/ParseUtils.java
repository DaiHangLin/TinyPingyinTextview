package com.explore.lin.test.utils;

import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.explore.lin.test.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lin
 * @date 18/4/3
 * @license Copyright (c) 2016 那镁克
 */

public class ParseUtils {

    //标点符号替换
    private static String[] puncsCh = new String[]{"，", "。", "\"", "：", "！", "\t", "!", ",",  ":", "？"};
    private static String[] puncsEn = new String[]{"， ", "。 ", "\" ", "： ", "！ ", " ", "! ", ", ",  ": ", "? "};
    //    private String[] puncsEn = new String[]{", ", ". ", "\" ", ": ", "! ", " ", "! ", ", ", ": ", "? "};
    private final static String NEW_LINE_PLACE_HOLDER = "newLine";

    private static final String TAG = ParseUtils.class.getSimpleName();

    public static Pair<List<String>, List<String>> readDataFormFile(String fileName) {
        String path = Environment.getExternalStorageDirectory() + File.separator + fileName;
        File file = new File(path);
        if (file.exists()) {
            return parseFile(file);

        } else {
            Log.e(TAG, "openFile: failed file not found");
        }
        return null;
    }

    public static Pair<List<String>, List<String>> readDataFormResource(Resources resources) {
        return parseResource(resources);
    }

    private static Pair<List<String>, List<String>> parseResource(Resources resources) {
        List<String> hanziList = new ArrayList<>();
        List<String> pinyinList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resources.openRawResource(R.raw.baixuegongzhu)));

            // do reading, usually loop until end of file reading
            int lineIndex = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineIndex ++;
                int index = line.indexOf(']');
                line = line.substring(index + 1, line.length());
                if (lineIndex % 2 == 0) {
                    hanziList.addAll(new ArrayList<>(Arrays.asList(formatData(formatHanziLine(line)))));
                } else {
                    pinyinList.addAll(new ArrayList<>(Arrays.asList(formatPinyinData(formatPinYinLine(line)))));
                }
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return new Pair<>(hanziList, pinyinList);
    }

    private static Pair<List<String>, List<String>> parseFile(File file) {
        List<String> hanziList = new ArrayList<>();
        List<String> pinyinList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            int lineIndex = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineIndex ++;
                if (lineIndex > 3) {
                    int index = line.indexOf(']');
                    line = line.substring(index + 1, line.length());
                    if (lineIndex % 2 == 0) {
                        pinyinList.addAll(new ArrayList<>(Arrays.asList(formatPinyinData(formatPinYinLine(line)))));
                    } else {
                        hanziList.addAll(new ArrayList<>(Arrays.asList(formatData(formatHanziLine(line)))));
                    }
                }
            }
            scanner.close();
            return new Pair<>(hanziList, pinyinList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String formatHanziLine(String line) {
        for (String punc: puncsCh) {
            // format 拼音中的标点符号需要与中文的一致， 在标点符号前不能有空格
            line = line.replaceAll("\\s+ " + punc + "\\s+", punc);
        }
        // format  不允许存在个空格
        line = line.replaceAll("\\s+", "");
        // 添加换行标志-placeHolder
        line = line + " ";
        return line;
    }

    private static String formatPinYinLine(String line) {
        // 拼音不能以空格 ' '开头
        if (line.startsWith(" ")) {
            line = line.replaceFirst("\\s+", "");
        }
        for (int i = 0; i < puncsCh.length; i++) {
            // format 用 ',' + ' ' 代替中文的 '，'     // 中文 '，' 后面不是空格，解析会出错
            line = line.replaceAll(puncsCh[i], puncsEn[i]);
        }
        // 拼音两行拼接时没有 ' '，会导致与下一行的第一个拼音错误的合成一个
        // 添加换行标志-placeHolder
        line = line + " " + NEW_LINE_PLACE_HOLDER + " ";
        // format 将 '    ' replace 为 ' '， 不允许存在连续空格
        line = line.replaceAll("\\s+", " ");
        return line;
    }

    public static String[] formatData(String hanzi) {
        if (hanzi != null && hanzi.length() > 0) {
            char[] c = hanzi.toCharArray();
            String[] result = new String[c.length];
            for (int index = 0; index < c.length; index++) {
                result[index] = c[index] + "";
            }
            return result;
        } else {
            return null;
        }
    }

    public static String[] formatPinyinData(String pinyin) {
        String[] result = pinyin.split("\\s+");
        return result;
    }

    public static Set<Integer> parsePinYinLineIndex(List<String> pinyin) {
        Set<Integer> newLineIndex = new HashSet<>();
        for (int i = 0; i < pinyin.size(); i ++) {
            if (pinyin.get(i).equalsIgnoreCase(NEW_LINE_PLACE_HOLDER)) {
                newLineIndex.add(i);
            }
        }
        return newLineIndex;
    }
}
