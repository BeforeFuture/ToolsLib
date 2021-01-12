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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.yy.toolslib.oaid.IDeviceId;
import com.yy.toolslib.oaid.IGetter;
import com.yy.toolslib.utils.LogUtils;
import com.zui.deviceidservice.IDeviceidInterface;

import java.lang.reflect.Method;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LenovoDeviceIdImpl implements IDeviceId {
    private Context context;
    private static String TAG = "LenovoDeviceIdImpl";

    public LenovoDeviceIdImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supportOAID() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.zui.deviceidservice", 0);
            return pi != null;
        } catch (Exception e) {
             LogUtils.i(TAG, e.toString());
            return false;
        }
    }

    @Override
    public void doGet(@NonNull final IGetter getter) {
        Intent intent = new Intent();
        intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService");
        boolean isBinded = context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                 LogUtils.i(TAG, "Lenovo DeviceidService connected");
                try {
                    //IDeviceidInterface anInterface = new IDeviceidInterface.Stub.asInterface(service);
                    Method asInterface = IDeviceidInterface.Stub.class.getDeclaredMethod("asInterface", IBinder.class);
                    IDeviceidInterface anInterface = (IDeviceidInterface) asInterface.invoke(null, service);
                    String deviceId = anInterface.a();
                    if (deviceId == null || deviceId.length() == 0) {
                        getter.onDeviceIdGetError(new RuntimeException("Lenovo deviceId get failed"));
                    } else {
                        getter.onDeviceIdGetComplete(deviceId);
                    }
                } catch (Exception e) {
                     LogUtils.i(TAG, e.toString());
                    getter.onDeviceIdGetError(e);
                } finally {
                    context.unbindService(this);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                 LogUtils.i(TAG, "Lenovo DeviceidService disconnected");
            }
        }, Context.BIND_AUTO_CREATE);
        if (!isBinded) {
            getter.onDeviceIdGetError(new RuntimeException("Lenovo DeviceidService bind failed"));
        }
    }

}
