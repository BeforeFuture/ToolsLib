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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.yy.toolslib.oaid.IDeviceId;
import com.yy.toolslib.oaid.IGetter;
import com.yy.toolslib.utils.LogUtils;

import java.lang.reflect.Method;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class XiaomiDeviceIdImpl implements IDeviceId {
    private Context context;
    private Class idProvider;
    private String TAG = "XiaomiDeviceIdImpl";

    public XiaomiDeviceIdImpl(Context context) {
        this.context = context;
    }

    @SuppressLint("PrivateApi")
    @Override
    public boolean supportOAID() {
        try {
            idProvider = Class.forName("com.android.id.impl.IdProviderImpl");
            return true;
        } catch (Exception e) {
            LogUtils.i(TAG, e.toString());
            return false;
        }
    }

    @SuppressLint("PrivateApi")
    @SuppressWarnings("unchecked")
    @Override
    public void doGet(@NonNull IGetter getter) {
        if (idProvider == null) {
            try {
                idProvider = Class.forName("com.android.id.impl.IdProviderImpl");
            } catch (Exception e) {
                LogUtils.i(TAG, e.toString());
            }
        }
        String did = null;
        try {
            Method udidMethod = idProvider.getMethod("getDefaultUDID", Context.class);
            did = invokeMethod(udidMethod);
        } catch (Exception e) {
            LogUtils.i(TAG, e.toString());
        }
        if (did != null && did.length() > 0) {
            getter.onDeviceIdGetComplete(did);
            return;
        }
        try {
            Method oaidMethod = idProvider.getMethod("getOAID", Context.class);
            did = invokeMethod(oaidMethod);
            if (did != null && did.length() > 0) {
                getter.onDeviceIdGetComplete(did);
            } else {
                getter.onDeviceIdGetError(new RuntimeException("Xiaomi OAID get failed"));
            }
        } catch (Exception e) {
            LogUtils.i(TAG, e.toString());
            getter.onDeviceIdGetError(e);
        }
    }

    private String invokeMethod(Method method) {
        String result = null;
        if (method != null) {
            try {
                result = (String) method.invoke(idProvider.newInstance(), context);
            } catch (Exception e) {
                LogUtils.i(TAG, e.toString());
            }
        }
        return result;
    }

}
