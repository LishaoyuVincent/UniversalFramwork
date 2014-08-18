package com.universal.framwork.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * �����жϵ�ǰ�Ƿ������磬������������
 * com.universal.framwork.net.util.NetWorkUtil
 * @author yuanzeyao <br/>
 * create at 2014��5��24�� ����10:15:42
 */
public class NetWorkUtil
{
  private static final String TAG = "NetWorkUtil";
  //��������������
  public enum NetType
  {
    WIFI,//wifi����
    MOBILE,//�ֻ�����
    NONET//û������
  }
  
  /**
   * �жϵ�ǰ�Ƿ����������ʹ��
   * @param context
   * @return true ��ʾ��  false ��ʾû��
   */
  public static boolean isNetWorkAvailable(Context context)
  {
    ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] infos=manager.getAllNetworkInfo();
    if(infos!=null)
    {
      for(NetworkInfo info:infos)
      {
        if(info.getState()==NetworkInfo.State.CONNECTED)
        {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * �ж������Ƿ�����
   * @param context
   * @return
   */
  public static boolean isNetWorkConnect(Context context)
  {
    ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info=manager.getActiveNetworkInfo();
    if(info!=null)
    {
      return info.isAvailable();
    }
    return false;
  }
  
  /**
   * �ж�wifi�����Ƿ�����
   * @param context
   * @return
   */
  public static boolean isWifiConnect(Context context)
  {
    ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo wifiInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if(wifiInfo!=null)
    {
      return wifiInfo.isAvailable();
    }
    return false;
  }
  
  /**
   * �ж��ֻ������Ƿ�����
   * @param context
   * @return
   */
  public static boolean isMobileConnect(Context context)
  {
    ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo infoMobile=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    if(infoMobile!=null)
    {
      return infoMobile.isAvailable();
    }
    return false;
  }
  
  /**
   * ��ȡ��ǰ��������
   * @param context
   * @return
   */
  public static NetType getNetType(Context context)
  {
    if(!isNetWorkAvailable(context))
      return NetType.NONET;
    else
    {
      ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info=manager.getActiveNetworkInfo();
      if(info!=null)
      {
        if(info.getType()==ConnectivityManager.TYPE_WIFI)
        {
          return NetType.WIFI;
        }else if(info.getType()==ConnectivityManager.TYPE_MOBILE)
        {
          return NetType.MOBILE;
        }else
          return NetType.NONET;
      }
      else
        return NetType.NONET;
    }
  }
  
  
}


