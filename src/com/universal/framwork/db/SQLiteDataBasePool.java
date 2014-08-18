package com.universal.framwork.db;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import com.universal.framwork.db.XSQLiteDataBase.XDBUpdateListener;

import android.content.Context;

/**
 * ���ݿ����ӳع�����
 * com.universal.framwork.db.SQLiteDataBasePool
 * @author yuanzeyao <br/>
 * create at 2014��5��30�� ����11:05:50
 */
public class SQLiteDataBasePool 
{
  private static final String TAG = "SQLiteDataBasePool";
  //����һ�����ݿ����ӳص�Ĭ�ϴ�С��2
  private int initDbConnectionSize=2;
  //���ݿ����ӳص�������С��2
  private int dbConnectionIncreSize=2;
  //������ӳصĴ�С
  private int maxConnectionSize=10;
  //��¼�����п���ʹ�õ����ݿ����
  private Vector<PooledSQLiteDataBase> pDataBases;
  //�����Ķ���
  private Context context;
  //���ݿ����������ӿ�
  private XDBUpdateListener updateListener;
  //���ݿ����Ӷ��������
  private DBParams params;
  private boolean isWrite=false;
  //������е����ӳأ�һ�����ӳض�Ӧһ�����ݿ�
  private static HashMap<String,SQLiteDataBasePool> allPool=new HashMap<String, SQLiteDataBasePool>();
  
  //����ģʽ
  public synchronized static SQLiteDataBasePool getInstance(Context context,DBParams params,boolean isWrite)
  {
    String dbName=params.getDb_name();
    SQLiteDataBasePool pool=allPool.get(dbName);
    if(pool==null)
    {
      pool=new SQLiteDataBasePool(context, params, isWrite);
      allPool.put(dbName, pool);
    }
    return pool;
  }
  
  //����ģʽ
  public static SQLiteDataBasePool getInstance(Context context,boolean isWrite)
  {
    return getInstance(context,new DBParams(),isWrite);
  }
  
  /**
   * ���캯��
   * @param context
   *            �����Ķ���
   * @param params
   *            �������ݿ����Ӷ���Ĳ���
   * @param isWrite
   *            �Ƿ��д
   */           
  public SQLiteDataBasePool(Context context,DBParams params,boolean isWrite)
  {
    this.context=context;
    this.params=params;
    this.isWrite=isWrite;
  }
  
  /**
   * ������������
   * @param updateListener
   */
  public void setOnUpdateListener(XDBUpdateListener updateListener)
  {
    this.updateListener=updateListener;
  }
  
  /**
   * �������ӳص�Ĭ�ϴ�С
   */
  public int getInitPoolSize()
  {
    return initDbConnectionSize;
  }

  /**
   * ��ȡÿ���������ӵĴ�С
   * @return
   */
  public int getDbConnectionIncreSize()
  {
    return dbConnectionIncreSize;
  }

  /**
   * ����ÿ�����ӵĴ�С
   * @param dbConnectionIncreSize
   */
  public void setDbConnectionIncreSize(int dbConnectionIncreSize)
  {
    this.dbConnectionIncreSize = dbConnectionIncreSize;
  }

  /**
   * ��ȡ���ӳ����������
   * @return
   */
  public int getMaxConnectionSize()
  {
    return maxConnectionSize;
  }

  /**
   * �������ӳ����������
   * @param maxConnectionSize
   */
  public void setMaxConnectionSize(int maxConnectionSize)
  {
    this.maxConnectionSize = maxConnectionSize;
  }
  
  /**
   * ����һ�����ݿ����ӳأ�Ĭ�ϴ�С
   */
  public synchronized void createDBPool()
  {
    if(pDataBases!=null)
    {
      //pDataBases��Ϊ�գ���ô���ӳ��Ѿ�������ֱ�ӷ���
      return;
    }
    if(this.initDbConnectionSize>0 && this.maxConnectionSize>0 && this.initDbConnectionSize<=this.maxConnectionSize)
    {
      //����initDbConnectionSize�����ݿ�����
      createDBPool(this.initDbConnectionSize);
    }
  }
  
  /**
   * ����ָ����С�����ݿ����ӳ�
   * @param size
   */
  private void createDBPool(int size)
  {
    pDataBases=new Vector<PooledSQLiteDataBase>();
    for(int i=0;i<size;i++)
    {
      pDataBases.addElement(new PooledSQLiteDataBase(newSQLiteDataBase(), false));
    }
    
  }
  
  /**
   * �½�һ�����ݿ����Ӷ���
   * @return
   */
  private XSQLiteDataBase newSQLiteDataBase()
  {
    XSQLiteDataBase xdb=new XSQLiteDataBase(context, params);
    xdb.openDataBase(updateListener, isWrite);
    return xdb;
  }
  
  /**
   * �����ӳ���������ݿ�����
   */
  private void addNumOfDataBasePool()
  {
    if(pDataBases==null)
    {
      createDBPool();
    }else
    {
      for(int i=0;i<dbConnectionIncreSize;i++)
      {
        if(pDataBases.size()>=maxConnectionSize)
          return;
        pDataBases.addElement(new PooledSQLiteDataBase(newSQLiteDataBase(), false));
      }
    }
    
    
  }
  
  /**
   * ��ȡһ�����е�SQLiteDataBase���������һ�λ�ȡΪ�գ���ô�����ӳ������dbConnectionIncreSize�����ӣ�
   * Ȼ���ٴλ�ȡ�������ȡ�ɹ��򷵻أ����򷵻�null
   * @return
   */
  private XSQLiteDataBase getFreeSQLiteDataBase()
  {
    XSQLiteDataBase xdb=findFreeSQLiteDataBase();
    if(xdb==null)
    {
      addNumOfDataBasePool();
      xdb=findFreeSQLiteDataBase();
    }
    return xdb;
  }
  
  /**
   * �����ӳ�����һ�����е����ӣ�����������Ѿ�ʧЧ�������´���������
   * @return  һ���������ӣ����û�п��������򷵻�null
   */
  private XSQLiteDataBase findFreeSQLiteDataBase()
  {
    XSQLiteDataBase db=null;
    PooledSQLiteDataBase pdb=null;
    
    if(pDataBases==null)
    {
      //���ݿ����ӳػ�û�н���
      return null;
    }
    Enumeration<PooledSQLiteDataBase> pooledDbs=pDataBases.elements();
    while(pooledDbs.hasMoreElements())
    {
      pdb=pooledDbs.nextElement();
      if(!pdb.isBusy())
      {
        db=pdb.getmSQLiteDataBase();
        if(!db.isUseable())
        {
          db=newSQLiteDataBase();
          pdb.setmSQLiteDataBase(db);
        }
        break;
      }
    }
    return db;
  }
  
  /**
   * ��ȡһ�����ݿ����Ӷ������û�л�ȡ�����ӣ���ô�ȴ�250 ms,Ȼ�����Ѱ�ҿ������ӣ�֪���ɹ���ȡ
   * @return һ����Ч�����ݿ�����
   * @throws InterruptedException  wait�����б����
   */
  public synchronized XSQLiteDataBase getSQLiteDataBase() 
  {
    XSQLiteDataBase xdb=null;
    if(pDataBases==null)
    {
      createDBPool();
    }
    xdb=getFreeSQLiteDataBase();
    while(xdb==null)
    {
      wait(250);
      xdb=getFreeSQLiteDataBase();
    }
    return xdb;
  }
  
  /**
   * �ͷ�ָ�����ݿ����Ӷ���
   * @param sqliteDatabase  Ҫ���ͷŵ����ݿ����Ӷ���
   */
  public void releaseSQLiteDataBase(XSQLiteDataBase sqliteDatabase)
  {
    if(sqliteDatabase==null)
    {
      return;
    }
    Enumeration<PooledSQLiteDataBase> poolDatabase=pDataBases.elements();
    PooledSQLiteDataBase pdb=null;
    while(poolDatabase.hasMoreElements())
    {
      pdb=poolDatabase.nextElement();
      if(sqliteDatabase==pdb.getmSQLiteDataBase())
      {
        pdb.setBusy(false);
        break;
      }
    }
  }
  
  
  public void closeSQLiteDataBase(XSQLiteDataBase xdb)
  {
    if(xdb!=null)
    {
      xdb.close();
    }
  }
  
  public synchronized void cloaseAllSQLiteDataBase()
  {
    //ȷ�����ӳش���
    if(pDataBases==null)
    {
      //��û�д������ӳأ�����Ҫ�ر�
      return;
    }
    Enumeration<PooledSQLiteDataBase> poolDataBase=pDataBases.elements();
    PooledSQLiteDataBase pdb=null;
    //�������ӳ�������������ӣ�������Ӵ���æµ״̬����ô�ȴ�5s,����ֱ�ӹر�����
    while(poolDataBase.hasMoreElements())
    {
      pdb=poolDataBase.nextElement();
      if(pdb.isBusy())
      {
        //�ȴ�5s��
        wait(5000);
      }
      closeSQLiteDataBase(pdb.getmSQLiteDataBase());
      //�����ӳ������Ƴ��ö���
      pDataBases.removeElement(pdb);
      
    }
    //�����ӳ��ÿ�
    pDataBases=null;
  }
  
  public synchronized void refreshAllDaseBase()
  {
    //ȷ�����ӳ��Ѿ�����
    if(pDataBases==null)
    {
      return;
    }
    Enumeration<PooledSQLiteDataBase> poolDataBase=pDataBases.elements();
    PooledSQLiteDataBase pdb=null;
    //�������ӳ�������������ӣ�������Ӵ���æµ״̬����ô�ȴ�5s,����ֱ�ӹرգ�Ȼ���ؽ�
    while(poolDataBase.hasMoreElements())
    {
      pdb=poolDataBase.nextElement();
      if(pdb.isBusy())
      {
        //�ȴ�5s
        wait(5000);
      }
      closeSQLiteDataBase(pdb.getmSQLiteDataBase());
      pdb.setmSQLiteDataBase(newSQLiteDataBase());
      pdb.setBusy(false);
    }
  }
  
  /**
   * ʹ����ȴ������ĺ�����
   * 
   * @param �����ĺ�����
   */

  private void wait(int mSeconds)
  {
    try
    {
      Thread.sleep(mSeconds);
    } catch (InterruptedException e)
    {
    }
  }
  
  
  
}
