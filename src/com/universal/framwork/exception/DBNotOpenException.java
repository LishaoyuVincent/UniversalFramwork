package com.universal.framwork.exception;
/**
 * ���ݿ�û�д��쳣
 * com.universal.framwork.exception.DBNotOpenException
 * @author yuanzeyao <br/>
 * create at 2014��6��2�� ����5:18:18
 */
public class DBNotOpenException extends Exception
{
  private static final String TAG = "DBNotOpenException";
  public DBNotOpenException(String message)
  {
    super(message);
  }
}
