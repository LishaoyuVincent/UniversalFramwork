package com.universal.framwork.manager;

import java.util.Stack;

import com.universal.framwork.BaseActivity;

import android.app.Activity;

/**
 * Ӧ�ó��������
 * com.universal.framwork.manager.AppManager
 * @author yuanzeyao <br/>
 * create at 2014��5��23�� ����10:26:31
 */
public class AppManager
{
  private static final String TAG = "AppManager";
  
  //*********ʵ�ֵ���ģʽ**********************
  private AppManager()
  {
    
  }
  public static class SingleHolder
  {
    public static final AppManager instance=new AppManager();
  }
  
  public static AppManager getInstance()
  {
    return SingleHolder.instance;
  }
  //**************��������**********************
  /**
   * ���ڱ��浱ǰӦ���е����е�Activity
   */
  public Stack<BaseActivity> allActiivtys;
  public BaseActivity currentActivity=null;
  
  public void addActivity(BaseActivity activity)
  {
    if(allActiivtys==null)
    {
      allActiivtys=new Stack<BaseActivity>();
    }
    allActiivtys.push(activity);
    currentActivity=allActiivtys.peek();
  }
  
  /**
   * �Ƴ���Ӧ��Activity
   * @param activity
   */
  public void removeActivity(BaseActivity activity)
  {
    if(allActiivtys!=null && allActiivtys.size()>0)
    {
      for(Activity tmp:allActiivtys)
      {
        if(tmp==activity)
        {
          allActiivtys.remove(activity);
        }
      }
      //���õ�ǰ��Actiivty
      if(allActiivtys.size()==0)
      {
        //������е�Activity�����Ƴ��ˣ���ô��ǰActivity����ΪNull
        currentActivity=null;
      }else
      {
        //�����Ϊ�գ���ô����Ϊջ����Activity
        currentActivity=allActiivtys.peek();
      }
    }else
    {
      currentActivity=null;
    }
    
  }
  
  /**
   * �˳�Ӧ�ó���
   * @param runback �Ƿ��̨����
   */
  public void exitApp(boolean runback)
  {
    for(Activity activity:allActiivtys)
    {
      activity.finish();
    }
    //����Ҫ�ٺ�ִ̨��
    if(!runback)
    {
      System.exit(0);
    }
  }
  
  
}
