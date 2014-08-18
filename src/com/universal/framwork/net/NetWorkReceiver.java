package com.universal.framwork.net;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.universal.framwork.util.NetWorkUtil;
import com.universal.framwork.util.NetWorkUtil.NetType;
/**
 * �������ӵ�BroadcastReceiver
 * com.universal.framwork.net.NetWorkReceiver
 * @author yuanzeyao <br/>
 * create at 2014��5��24�� ����10:00:06
 */
public class NetWorkReceiver extends BroadcastReceiver
{
  private static final String TAG = "NetWorkReceiver";
  public static final String RECEIVE_ACTION_SYSTEM="android.net.conn.CONNECTIVITY_CHANGE";//ϵͳ����
  public static final String RECEIVE_ACITON_USER="user.net.conn.CONNECTIVITY_CHANGE";//�û��Լ�����
  //*******************����ʵ�ֿ�ʼ**********************
  private NetWorkReceiver()
  {
    
  }
  public static class SingleHolder{
    public static final NetWorkReceiver INSTANCE=new NetWorkReceiver();
  }
  
  public static NetWorkReceiver getInstance()
  {
    return SingleHolder.INSTANCE;
  }
  //*******************����ʵ�ֽ���**********************

  private static ArrayList<INetObserver> allObservers;
  
 
  //��ǰ����������
  public NetType currentNetType;
  //��ǰ�Ƿ�����������
  public boolean isnetconnect=true;
  
  @Override
  public void onReceive(Context context, Intent intent)
  {
    if(intent.getAction().equals(RECEIVE_ACITON_USER)|| intent.getAction().equals(RECEIVE_ACTION_SYSTEM))
    {
      if(NetWorkUtil.isNetWorkAvailable(context))
      {
        isnetconnect=true;
        currentNetType=NetWorkUtil.getNetType(context);
        Log.v("yzy", "has net work");
      }else
      {
        isnetconnect=false;
        currentNetType=NetType.NONET;
        Log.v("yzy", "not has net work");
      }
      
      notifyObserver();
     
    }
  }
  
  //������������
  public static void addObserver(INetObserver observer)
  {
    if(allObservers==null)
    {
      allObservers=new ArrayList<INetObserver>();
    }
    allObservers.add(observer);
  }
  
  //�Ƴ����������
  public static void removeObserver(INetObserver observer)
  {
    if(allObservers!=null && allObservers.size()>0)
    {
      allObservers.remove(observer);
    }
  }
  
  //֪ͨ�۲���
  private void notifyObserver()
  {
    if(allObservers!=null)
    {
      for(INetObserver observer:allObservers)
      {
        if(isnetconnect)
        {
          //֪ͨ�ɹ����ӣ����Ҹ�֪��������
          observer.onConnect(currentNetType);
        }else
        {
          //֪ͨ�Ͽ�����
          observer.onDisConnect();
        }
      }
    }
    
  }
  
  //����������Reciver
  public static void registerNetBroadcastReceiver(Context context)
  {
    IntentFilter filter=new IntentFilter();
    filter.addAction(RECEIVE_ACTION_SYSTEM);
    filter.addAction(RECEIVE_ACITON_USER);
    context.getApplicationContext().registerReceiver(NetWorkReceiver.getInstance(), filter);
  }
  
  //ȡ���������Receiver
  public static void unRegisNetBroadcastReceiver(Context context)
  {
    context.getApplicationContext().unregisterReceiver(NetWorkReceiver.getInstance());
  }
}
