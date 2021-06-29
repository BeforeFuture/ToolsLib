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
import com.yy.toolslib.utils.Logger;
import com.uodis.opendevice.aidl.OpenDeviceIdentifierService;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class HuaweiDeviceIdImpl implements IDeviceId {
    private Context context;
    private static String TAG = "HuaweiDeviceIdImpl";

    public HuaweiDeviceIdImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supportOAID() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.huawei.hwid", 0);
            return pi != null;
        } catch (Exception e) {
            Logger.i(TAG, e.toString());
            return false;
        }
    }

    @Override
    public void doGet(@NonNull final IGetter getter) {
        Intent intent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
        intent.setPackage("com.huawei.hwid");
        boolean isBinded = context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.i(TAG, "Huawei OPENIDS_SERVICE connected");
                try {
                    OpenDeviceIdentifierService anInterface = OpenDeviceIdentifierService.Stub.asInterface(service);
                    String IDs = anInterface.getIDs();
                    if (IDs == null || IDs.length() == 0) {
                        getter.onDeviceIdGetError(new RuntimeException("Huawei IDs get failed"));
                    } else {
                        getter.onDeviceIdGetComplete(IDs);
                    }
                } catch (Exception e) {
                    Logger.i(TAG, e.toString());
                    getter.onDeviceIdGetError(e);
                } finally {
                    context.unbindService(this);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Logger.i(TAG, "Huawei OPENIDS_SERVICE disconnected");
            }
        }, Context.BIND_AUTO_CREATE);
        if (!isBinded) {
            getter.onDeviceIdGetError(new RuntimeException("Huawei OPENIDS_SERVICE bind failed"));
        }
    }

}
