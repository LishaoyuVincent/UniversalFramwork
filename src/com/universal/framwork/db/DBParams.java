package com.universal.framwork.db;
/**
 * �������ݿ����Ĳ�����
 * com.universal.framwork.db.DBParams
 * @author yuanzeyao <br/>
 * create at 2014��5��30�� ����11:46:48
 */
public class DBParams
{
  private static final String TAG = "DBParams";
  //Ĭ�����ݿ�����
  private static final String DB_NAME="gavin.db";
  
  //Ĭ�����ݿ�汾
  private static final int DB_VERSION=1;
  
  
  private String db_name=DB_NAME;
  private int db_version=DB_VERSION;
  public DBParams()
  {
    
  }
  
  public DBParams(String db_name,int db_version)
  {
    this.db_name=db_name;
    this.db_version=db_version;
  }

  public String getDb_name()
  {
    return db_name;
  }

  public void setDb_name(String db_name)
  {
    this.db_name = db_name;
  }

  public int getDb_version()
  {
    return db_version;
  }

  public void setDb_version(int db_version)
  {
    this.db_version = db_version;
  }
  
  
}
