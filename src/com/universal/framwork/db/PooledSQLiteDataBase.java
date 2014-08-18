package com.universal.framwork.db;
/**
 * ��{@link XSQLiteDataBase} ��ķ�װ�����ڷ��뵽���ӳ��е�XSQLiteDataBase����¼
 * ���Ƿ���æµ״̬
 * com.universal.framwork.db.PooledSQLiteDataBase
 * @author yuanzeyao <br/>
 * create at 2014��5��30�� ����1:21:21
 */
public class PooledSQLiteDataBase
{
  private static final String TAG = "PooledSQLiteDataBase";
  //���ݿ�ʵ��
  private XSQLiteDataBase mSQLiteDataBase;
  
  //�Ƿ���æµ״̬
  private boolean isBusy;
  
  public PooledSQLiteDataBase(XSQLiteDataBase db,boolean isBusy)
  {
    this.mSQLiteDataBase=db;
    this.isBusy=isBusy;
  }

  public XSQLiteDataBase getmSQLiteDataBase()
  {
    return mSQLiteDataBase;
  }

  public void setmSQLiteDataBase(XSQLiteDataBase mSQLiteDataBase)
  {
    this.mSQLiteDataBase = mSQLiteDataBase;
  }

  public boolean isBusy()
  {
    return isBusy;
  }

  public void setBusy(boolean isBusy)
  {
    this.isBusy = isBusy;
  }
  
  
}
