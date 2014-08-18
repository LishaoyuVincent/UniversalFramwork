package com.universal.framwork.db;

import com.universal.framwork.db.XSQLiteDataBase.XDBUpdateListener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class XDBHelper extends SQLiteOpenHelper 
{
  private static final String TAG = "XDBHelper";
  //���ݿ����������ӿ�
  public XDBUpdateListener mUpdateListener;
  
  /**
   * 
   * @param context
   *              �����Ķ���
   * @param name
   *              ���ݿ�����
   * @param factory
   *              �α깤������ִ��һ����ѯ���ʱ���˴���������һ���α�
   * @param version
   *              ���ݿ�汾
   */
  public XDBHelper(Context context, String name, CursorFactory factory, int version) 
  {
    super(context, name, factory, version);
  }
  
  /**
   * 
   * @param context
   *              �����Ķ���
   * @param name  
   *              ���ݿ�����
   * @param factory
   *              �α깤������ִ��һ����ѯ���ʱ���˴���������һ���α�
   * @param version
   *              ���ݿ�汾
   * @param listener
   *              ���ݿ������ص��ӿ�
   */
  public XDBHelper(Context context, String name, CursorFactory factory, int version,XDBUpdateListener listener) 
  {
    super(context, name, factory, version);
    mUpdateListener=listener;
  }

  /**
   * ���ݿ��һ�δ�����ִ�д˷���
   */
  @Override
  public void onCreate(SQLiteDatabase db) 
  {
    
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
  {
    if(mUpdateListener!=null)
    {
      mUpdateListener.onUpgrade(db, oldVersion, newVersion);
    }
  }
  
  /**
   * ����һ�����ݿ�����������
   * @param updateListener  �����ݿ�����ʱ���ýӿڱ��ص�
   */
  public void setOnUpdateListener(XDBUpdateListener updateListener)
  {
    this.mUpdateListener=updateListener;
  }
}
