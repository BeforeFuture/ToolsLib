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
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.yy.toolslib.oaid.IDeviceId;
import com.yy.toolslib.oaid.IGetter;
import com.yy.toolslib.oaid.Utils;
import com.yy.toolslib.utils.LogUtils;

import java.util.Objects;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class VivoDeviceIdImpl implements IDeviceId {
    private Context context;
    private String TAG = "VivoDeviceIdImpl";

    public VivoDeviceIdImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supportOAID() {
        return Utils.sysProperty("persist.sys.identifierid.supported", "0").equals("1");
    }

    @Override
    public void doGet(@NonNull IGetter getter) {
        Uri uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            Objects.requireNonNull(cursor).moveToFirst();
            String ret = cursor.getString(cursor.getColumnIndex("value"));
            if (ret != null && ret.length() > 0) {
                 LogUtils.i(TAG, "oaid from provider: " + uri);
                getter.onDeviceIdGetComplete(ret);
            } else {
                getter.onDeviceIdGetError(new RuntimeException("OAID query failed"));
            }
        } catch (Exception e) {
             LogUtils.i(TAG, e.toString());
            getter.onDeviceIdGetError(e);
        }
    }

}
