package com.universal.framwork.net;

import com.universal.framwork.util.NetWorkUtil.NetType;


/**
 * �������Ӽ���
 * com.universal.framwork.net.INetObserver
 * @author yuanzeyao <br/>
 * create at 2014��5��24�� ����9:56:32
 */
public interface INetObserver
{
  /**
   * �ɹ����ӵ�����
   * @param netType wifi/mobile/no net
   */
  public void onConnect(NetType netType);
  
  /**
   * ����Ͽ�
   */
  public void onDisConnect();
}
