package com.universal.framwork.db.sqlbuilder;
/**
 * ��������SQLBuilder�Ĺ�����
 * com.universal.framwork.db.sqlbuilder.SQLBuilderFactory
 * @author yuanzeyao <br/>
 * create at 2014��6��3�� ����11:26:38
 */
public class SQLBuilderFactory
{
  private static final String TAG = "SQLBuilderFactory";
  /**
   * ����{@link SelectSQLBuilder}
   */
  public static final int SELECT=1;
  
  /**
   * ����{@link UpdateSQLBuilder}
   */
  public static final int UPDATE=2;
  
  /**
   * ����{@link DeleteSQLBuilder}
   */
  public static final int DELETE=3;
  
  /**
   * ����{@link InsertSQLBuilder}
   */
  public static final int INSERT=4;
  
  /**
   * ʹ�õ���ģʽ
   */
  public static SQLBuilderFactory instance;
  public static SQLBuilderFactory getInsance()
  {
    if(instance==null)
    {
      instance=new SQLBuilderFactory();
    }
    return instance;
  }
  
  /**
   * ��������������ͬ��SQLBuilder
   * @param type
   *        
   * @return
   */
  public SQLBuilder getSQLBuilder(int type)
  {
    switch(type)
    {
      case SELECT:
        return new SelectSQLBuilder();
      case UPDATE:
        return new UpdateSQLBuilder();
      case DELETE:
        return new DeleteSQLBuilder();
      case INSERT:
        return new InsertSQLBuilder();
      default:
        return null;
    }
  }
}
