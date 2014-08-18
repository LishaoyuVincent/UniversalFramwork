package com.universal.framwork.util;

import android.view.Gravity;
import android.widget.Toast;

import com.universal.framwork.XApplication;

/**
 * ��װToast,���ڲ���
 * com.universal.framwork.util.ToastUtil
 * @author yuanzeyao <br/>
 * create at 2014��5��24�� ����11:05:36
 */
public class ToastUtil
{
  private static final String TAG = "ToastUtil";
  
  /**
   * ������ʾ�������ַ���id
   * @param msg �ַ���id
   */
  public static final void AlertMessageInCenter(int msg)
  {
    XApplication application=XApplication.getInstance();
    String str_msg=application.getResources().getString(msg);
    Toast toast=Toast.makeText(application, str_msg, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }
  
  /**
   * ������ʾ
   * @param msg  ֱ�Ӵ���Ҫ��ʾ���ַ���
   */
  public static final void AlertMessageInCenter(String msg)
  {
    XApplication application=XApplication.getInstance();
    Toast toast=Toast.makeText(application, msg, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }
  
  
  /**
   * �ײ���ʾ
   * @param msg Ҫ��ʾ�ַ�����id
   */
  public static final void AlertMessageInBottom(int msg)
  {
    XApplication application=XApplication.getInstance();
    String str_msg=application.getResources().getString(msg);
    Toast.makeText(application, str_msg, Toast.LENGTH_SHORT).show();;
  }
  
  /**
   * �ײ���ʾ
   * @param msg Ҫ��ʾ���ַ���
   */
  public static final void AlertMessageInBottom(String msg)
  {
    XApplication application=XApplication.getInstance();
    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();;
  }
}
