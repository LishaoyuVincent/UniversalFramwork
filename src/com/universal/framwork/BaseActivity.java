package com.universal.framwork;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.universal.framwork.annotation.Injector;
import com.universal.framwork.manager.AppManager;
import com.universal.framwork.util.LogUtil;
import com.universal.framwork.util.NetWorkUtil.NetType;
import com.universal.framwork.util.ToastUtil;
/**
 * ����еĻ����࣬ʹ�øÿ�ܵ�Activity ��Ҫ�̳д�Activity
 * com.universal.framwork.BaseActivity
 * @author yuanzeyao <br/>
 * create at 2014��5��24�� ����11:03:14
 */
public class BaseActivity extends FragmentActivity
{
  private static final String TAG = "BaseActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onCreate");
    super.onCreate(savedInstanceState);
    AppManager.getInstance().addActivity(this);
  }
  
  
  
  @Override
  public void setContentView(int layoutResID)
  {
    super.setContentView(layoutResID);
    Injector.initInJectViewId(this,this.getWindow().getDecorView());
  }



  @Override
  public void setContentView(View view, LayoutParams params)
  {
    super.setContentView(view, params);
    Injector.initInJectViewId(this,this.getWindow().getDecorView());
  }



  @Override
  public void setContentView(View view)
  {
    super.setContentView(view);
    Injector.initInJectViewId(this,this.getWindow().getDecorView());
  }



  @Override
  protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onNewIntent");
  }
  
  @Override
  protected void onResume()
  {
    super.onResume();
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onResume");
  }
  
  @Override
  protected void onStart()
  {
    super.onStart();
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onStart");
  }
  
  @Override
  protected void onPause()
  {
    super.onPause();
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onPause");
  }
  
  @Override
  protected void onStop()
  {
    super.onStop();
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onStop");
  }
  
  
  
  @Override
  protected void onDestroy()
  {
    AppManager.getInstance().removeActivity(this);
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onDestroy");
    super.onDestroy();
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onSaveInstanceState");
  }
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
    LogUtil.d(TAG, this.getClass().getSimpleName()+"-->onConfigurationChanged");
  }
  
  
  public void onConnect(NetType netType)
  {
    ToastUtil.AlertMessageInCenter("��������");
  }
  
  
  public void onDisConnect()
  {
    ToastUtil.AlertMessageInCenter("����Ͽ�");
  }
  
  
  
  
  
  
}
