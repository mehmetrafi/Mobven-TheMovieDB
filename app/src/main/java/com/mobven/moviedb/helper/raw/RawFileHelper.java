package com.mobven.moviedb.helper.raw;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import mjson.Json;

public class RawFileHelper {

    private static final int defaultBufferSize = 1024 * 3;
    private static final String defaultTextEncoding = "UTF-8";


    private RawFileHelper() { }
    private static RawFileHelper INSTANCE;

    private Context context;

    public static void init(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new RawFileHelper();
            INSTANCE.setContext(context.getApplicationContext());
        }
    }

    public static RawFileHelper getInstance() {
        if(INSTANCE == null || INSTANCE.context == null) {
            return null;
        }
        return INSTANCE;
    }

    private InputStream getRawFileInputStream(int rawResId) {
        InputStream inputStream = null;
        try {
            inputStream = getContext().getResources().openRawResource(rawResId);
        }catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private String getStringFromInputStream(InputStream inputSream, String textEncoding, Integer bufferSize) throws IOException {
        String stringFromInputStrem = null;
        if(inputSream != null) {
            String lineFromStream;
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            textEncoding = (textEncoding != null) ? textEncoding : defaultTextEncoding;
            bufferSize = (bufferSize != null) ? bufferSize : defaultBufferSize;
            bufferedReader = new BufferedReader(new InputStreamReader(inputSream, textEncoding), bufferSize);
            while ((lineFromStream = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineFromStream);
            }
            stringFromInputStrem = stringBuilder.toString();
            bufferedReader.close();
            inputSream.close();
        }
        return stringFromInputStrem;
    }

    public String getStringFromRawResource(int rawResId)  {
        String stringFromRawResource = null;
        try {
            InputStream rawResourceInputStream = getRawFileInputStream(rawResId);
            if(rawResourceInputStream != null) {
                stringFromRawResource = getStringFromInputStream(rawResourceInputStream, defaultTextEncoding,  defaultBufferSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringFromRawResource;
    }

    public Json getJsonFromRawResource(int rawResId) {
        Json jsonFromRawResource = null;
        String stringFromRawResource;
        if((stringFromRawResource = getStringFromRawResource(rawResId)) != null) {
            jsonFromRawResource = Json.read(stringFromRawResource);
        }
        return jsonFromRawResource;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
