/*
 * MIT License
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yy.toolslib.oaid.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

import com.yy.toolslib.oaid.IDeviceId;
import com.yy.toolslib.oaid.IGetter;
import com.yy.toolslib.utils.Logger;

import java.util.concurrent.Executors;

/**
 * 随机生成一个全局唯一标识，通过{@link SharedPreferences}及{@link Environment#getExternalStoragePublicDirectory(String)}进行永久化存储。
 * 注：APP无法获得{@link android.Manifest.permission#WRITE_SETTINGS}权限，故放弃使用{@link android.provider.Settings.System}进行存储。
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DefaultDeviceIdImpl implements IDeviceId {
    private Context context;
    private static String TAG = "DefaultDeviceIdImpl";

    public DefaultDeviceIdImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supportOAID() {
        return false;
    }

    @Override
    public void doGet(@NonNull IGetter getter) {
        String guid = readFromSharedPreferences();
        if (TextUtils.isEmpty(guid)) {
            guid = "";//如果获取不到，直接传递空值
            getter.onDeviceIdGetComplete(guid);

        } else {
            getter.onDeviceIdGetComplete(guid);
            final String finalGuid = guid;
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    writeToSharedPreferences(finalGuid);
                }
            });
        }
    }


    private String readFromSharedPreferences() {
        SharedPreferences sp = context.getSharedPreferences(".OAID", Context.MODE_PRIVATE);
        return sp.getString("l__y__j", "");
    }

    private void writeToSharedPreferences(String guid) {
        Logger.i(TAG, "write guid to SharedPreferences: " + guid);
        SharedPreferences sp = context.getSharedPreferences(".OAID", Context.MODE_PRIVATE);
        sp.edit().putString("l__y__j", guid).apply();
    }

}
