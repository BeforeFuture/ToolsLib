package com.yy.toolslib.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/5/15.
 * Android 设备信息
 */

public class DeviceInfoModel implements Serializable {

    private static final String TAG = "DeviceInfoModel";


    /**
     * IMEI 1
     */
    private String IMEI_1 = "";

    /**
     * IMEI 2
     */
    private String IMEI_2 = "";

    /**
     * MEID
     */
    private String MEID = "";

    /**
     * MAC 地址
     */
    private String MAC = "";

    /**
     * ANDROID_ID
     */
    private String ANDROID_ID = "";

    /**
     * UUID
     * 当其他所有设备信息都为空时，默认生成一条uuid
     */
    private String UUID = "";


    /**
     * MD5_DEVICE_ID
     * 经过md5加密的设备id
     */
    private String MD5_DEVICE_ID = "";


    /**
     * OAID
     * Android 10以上设备的oaid
     */
    private String OAID = "";

    public static String getTAG() {
        return TAG;
    }

    public String getIMEI_1() {
        return IMEI_1;
    }

    public void setIMEI_1(String IMEI_1) {
        this.IMEI_1 = IMEI_1;
    }

    public String getIMEI_2() {
        return IMEI_2;
    }

    public void setIMEI_2(String IMEI_2) {
        this.IMEI_2 = IMEI_2;
    }

    public String getMEID() {
        return MEID;
    }

    public void setMEID(String MEID) {
        this.MEID = MEID;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getANDROID_ID() {
        return ANDROID_ID;
    }

    public void setANDROID_ID(String ANDROID_ID) {
        this.ANDROID_ID = ANDROID_ID;
    }

    public String getMD5_DEVICE_ID() {
        return MD5_DEVICE_ID;
    }

    public void setMD5_DEVICE_ID(String MD5_DEVICE_ID) {
        this.MD5_DEVICE_ID = MD5_DEVICE_ID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getOAID() {
        return OAID;
    }

    public void setOAID(String OAID) {
        this.OAID = OAID;
    }
}
