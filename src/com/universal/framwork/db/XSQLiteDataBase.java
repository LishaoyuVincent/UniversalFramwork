package com.universal.framwork.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.universal.framwork.db.sqlbuilder.SQLBuilder;
import com.universal.framwork.db.sqlbuilder.SQLBuilderFactory;
import com.universal.framwork.exception.DBNotOpenException;
import com.universal.framwork.util.LogUtil;
import com.universal.framwork.util.ToastUtil;

/**
 * ���ݿ�����࣬ͨ����������ݿ���в���
 * com.universal.framwork.db.XSQLiteDataBase
 * @author yuanzeyao <br/>
 * create at 2014��5��30�� ����11:27:59
 */
public class XSQLiteDataBase
{
  private static final String TAG="XSQLiteDataBase";
  /**
   * ��Ų�ѯ���
   */
  private Cursor queryCursor;
  
  /**
   * �����жϵ�ǰ���ݿ�mSqliteDatabaseʵ���Ƿ����ʹ��
   */
  private boolean isConnect=false;
  
  /**
   * �������ݿ�ʵ��
   */
  private SQLiteDatabase mSqliteDatabase;
  private XDBHelper dbHelper;
  
  /**
   * ���ݿ����������ӿ�
   */
  private XDBUpdateListener updateListener;
  
  /**
   * ���캯��
   * @param context �����Ķ���
   */
  public XSQLiteDataBase(Context context)
  {
    this(context,new DBParams());
  }
  
  /**
   * ���캯��
   * @param context
   *            �����Ķ���
   * @param params
   *            �������ݿ�Ĳ����࣬�������ݿ����ơ��汾
   */
  public XSQLiteDataBase(Context context,DBParams params)
  {
    dbHelper=new XDBHelper(context, params.getDb_name(), null, params.getDb_version());
  }
  
  public void setOnUpdateListener(XDBUpdateListener updateListener)
  {
    this.updateListener=updateListener;
    if(dbHelper!=null)
    {
      dbHelper.setOnUpdateListener(updateListener);
    }
  }
  
  /**
   * ��һ�����ݿ����
   * @param updateListener
   *              ���ݿ����������ӿ�
   * @param isWrite
   *              ���ݿ��Ƿ��д
   * @return
   *              һ���������ݿ��ʵ��
   */
  public SQLiteDatabase openDataBase(XDBUpdateListener updateListener,boolean isWrite)
  {
    if(isWrite)
    {
      mSqliteDatabase=openWriteDataBase(updateListener);
    }else
    {
      mSqliteDatabase=openReadDataBase(updateListener);
    }
    return mSqliteDatabase;
  }
  
  /**
   * ����һ������д��SQLiteDatabase
   * @return
   */
  private SQLiteDatabase openWriteDataBase(XDBUpdateListener updateListener)
  {
    this.updateListener=updateListener;
    if(dbHelper!=null)
    {
      dbHelper.setOnUpdateListener(updateListener);
    }
    try
    {
      mSqliteDatabase=dbHelper.getWritableDatabase();
      isConnect=true;
    }catch(Exception e)
    {
      isConnect=false;
    }
    
    return mSqliteDatabase;
  }
  
  
  /**
   * ����һ��ֻ����SQLiteDatabase
   * @return
   */
  private SQLiteDatabase openReadDataBase(XDBUpdateListener updateListener)
  {
    this.updateListener=updateListener;
    if(dbHelper!=null)
    {
      dbHelper.setOnUpdateListener(updateListener);
    }
    try
    {
      mSqliteDatabase=dbHelper.getReadableDatabase();
      isConnect=true;
    }catch(Exception e)
    {
      isConnect=false;
    }
    
    return mSqliteDatabase;
  }
  
  /**
   * �ж�һ�����Ƿ����
   * @param clazz
   *        ���Ӧ�� ����
   * @return
   *        ������Ѿ����ڷ���true,���򷵻�false
   * @throws DBNotOpenException
   *        ������ݿ����ʵ��û�г�ʼ�������׳����쳣
   */
  public boolean hasTable(Class<?> clazz) throws DBNotOpenException
  {
    String name=DBUtils.getTableName(clazz);
    return hasTable(name);
  }
  
  /**
   * ͨ����������ж�һ�����Ƿ����
   * @param tableName
   *            ����
   * @return
   *            ���ڷ���true,���򷵻�false
   * @throws DBNotOpenException
   *             ������ݿ����ʵ��û�г�ʼ�������׳����쳣.
   */
  public boolean hasTable(String tableName) throws DBNotOpenException
  {
    if(!TextUtils.isEmpty(tableName))
    {
      if(isUseable())
      {
        tableName=tableName.trim();
        //sqlite �����ݿ��еı���Ϣ ���ڡ�sqlite_master����
        String queryStr="select count(*) as c from sqlite_master where type='table' and name='"+tableName+"'";
        free();
        queryCursor=mSqliteDatabase.rawQuery(queryStr, null);
        if(queryCursor.moveToNext())
        {
          int count=queryCursor.getInt(0);
          if(count>0)
            return true;
        }
      }else
      {
        throw new DBNotOpenException("���ݿ��Ѿ��رգ�");
      }
    }else
    {
      return false;
    }
    return false;
  }
  
  /**
   * ����һ�����ݿ��
   * @param clazz
   *        ���ݿ���Ӧ���� ����
   * @return
   *        �����ɹ�����true,����ʧ�ܷ���false
   */
  public boolean createTable(Class<?> clazz)
  {
    boolean success=false;
    String createSQL;
    try
    {
      createSQL = DBUtils.createTableSQL(clazz);
      if(createSQL!=null)
      {
       execute(createSQL,null);
       success=true;
      }
    } catch (Exception e)
    {
      success=false;
      Log.v("sql", e.getMessage());
    }
   
    return success;
  }
  
  /**
   * ͨ��mSqliteDatabaseʵ����ɶ����ݿ�Ĳ���
   * @param exeSql
   *          Ҫִ�е�sql��䣨������select��䣩�����в�������?ռλ
   * @param args
   *          sql�����Ҫ�Ĳ���
   * @throws DBNotOpenException
   */
  public void execute(String exeSql,Object[] args) throws DBNotOpenException
  {
    if(!TextUtils.isEmpty(exeSql))
    {
      if(isUseable())
      {
        if(args==null)
        {
          mSqliteDatabase.execSQL(exeSql);
        }else
        {
          mSqliteDatabase.execSQL(exeSql, args);
        }
        
      }
      else
      {
        throw new DBNotOpenException("���ݿ��Ѿ��ر�");
      }
    }
  }
  
  public boolean execute(SQLBuilder sqlBuilder)
  {
    boolean success=false;
    if(sqlBuilder!=null)
    {
      try
      {
        if(isUseable())
        {
          String sql=sqlBuilder.getSQLStatement();
          LogUtil.v("sql", "execute-->"+sql);
          mSqliteDatabase.execSQL(sql);
          success=true;
        }else
        {
          success=false;
          Log.v("sql", "notUseable");
        }
        
      } catch (IllegalArgumentException e)
      {
        success=false;
        ToastUtil.AlertMessageInBottom(e.getMessage());
      } catch (IllegalAccessException e)
      {
        success=false;
        ToastUtil.AlertMessageInBottom(e.getMessage());
      }
    }
    return success;
  }
  
  /**
   * ���ݲ�ѯ��䣬��ѯ����
   * @param sql
   *          һ��select ���
   * @param selectionArgs
   *          select���Ĳ���
   * @return
   *          ���ز�ѯ����������ѯ�쳣����᷵��null
   * @throws DBNotOpenException
   *     
   */
  public ArrayList<HashMap<String,String>> query(String sql,String[]selectionArgs) throws DBNotOpenException
  {
    if(isUseable())
    {
      if(sql!=null)
      {
        //�ͷ�Cursor
        free();
        queryCursor= mSqliteDatabase.rawQuery(sql, selectionArgs);
        return getResultFromCurosr(queryCursor);
      }
    }else
    {
      throw new DBNotOpenException("���ݿ��Ѿ��رգ�");
    }
    return null;
  }
  
  /**
   * ��Ҫ����ִ��select��䣬ͨ��SQLiteDataBase.queryʵ��
   * @param table
   *        ��ѯ�ı���
   * @param columns
   *        ��Ҫ���ص��У��������գ���ô�᷵��������
   * @param selection
   *        ������������������ʹ�ã�ռλ
   * @param selectionArgs
   *        ���������еĲ���
   * @param groupBy
   *        ��ĳ�з���
   * @param having
   *        having���
   * @param orderBy
   *        ��ĳһ�ֶ�������߽���
   * @return
   * @throws DBNotOpenException
   */
  public ArrayList<HashMap<String,String>> query(String table,String[]columns,String selection,
      String[] selectionArgs,String groupBy,String having,String orderBy) throws DBNotOpenException
  {
    if(isUseable())
    {
      free();
      queryCursor=mSqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
      return getResultFromCurosr(queryCursor);
    }else
    {
      throw new DBNotOpenException("���ݿ��Ѿ��ر�!");
    }
  }
  
  /**
   * 
   * @param distinct
   *        �Ƿ�ȥ����ͬ����
   * @param table
   *        �������൱��select���from�ؼ��ֺ���Ĳ��֡�����Ƕ�����ϲ�ѯ�������ö��Ž����������ֿ���
   * @param columns
   *        Ҫ��ѯ�������������൱��select���select�ؼ��ֺ���Ĳ��֡�
   * @param selection
   *        ��ѯ�����Ӿ䣬�൱��select���where�ؼ��ֺ���Ĳ��֣��������Ӿ�����ʹ��ռλ����?��
   * @param selectionArgs
   *        ��Ӧ��selection�����ռλ����ֵ��ֵ�������е�λ����ռλ��������е�λ�ñ���һ�£�����ͻ����쳣��
   * @param groupBy
   *        �൱��select���group by�ؼ��ֺ���Ĳ���
   * @param having
   *        �൱��select���having�ؼ��ֺ���Ĳ���
   * @param orderBy
   *        �൱��select���order by�ؼ��ֺ���Ĳ��֣��磺personid desc, age asc;
   * @param limit
   *        ָ��ƫ�����ͻ�ȡ�ļ�¼�����൱��select���limit�ؼ��ֺ���Ĳ��֡�
   * @return
   * @throws DBNotOpenException
   */
  public ArrayList<HashMap<String,String>> query(boolean distinct,String table,String[]columns,String selection,
      String[]selectionArgs,String groupBy,String having,String orderBy,String limit) throws DBNotOpenException
  {
    if(isUseable())
    {
      free();
      queryCursor=mSqliteDatabase.query(distinct,table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
      return getResultFromCurosr(queryCursor);
    }else
    {
      throw new DBNotOpenException("���ݿ��Ѿ��ر�!");
    }
  }
  
  /**
   * 
    @param table
   *        �������൱��select���from�ؼ��ֺ���Ĳ��֡�����Ƕ�����ϲ�ѯ�������ö��Ž����������ֿ���
   * @param columns
   *        Ҫ��ѯ�������������൱��select���select�ؼ��ֺ���Ĳ��֡�
   * @param selection
   *        ��ѯ�����Ӿ䣬�൱��select���where�ؼ��ֺ���Ĳ��֣��������Ӿ�����ʹ��ռλ����?��
   * @param selectionArgs
   *        ��Ӧ��selection�����ռλ����ֵ��ֵ�������е�λ����ռλ��������е�λ�ñ���һ�£�����ͻ����쳣��
   * @param groupBy
   *        �൱��select���group by�ؼ��ֺ���Ĳ���
   * @param having
   *        �൱��select���having�ؼ��ֺ���Ĳ���
   * @param orderBy
   *        �൱��select���order by�ؼ��ֺ���Ĳ��֣��磺personid desc, age asc;
   * @param limit
   *        ָ��ƫ�����ͻ�ȡ�ļ�¼�����൱��select���limit�ؼ��ֺ���Ĳ��֡�
   * @return
   * @throws DBNotOpenException
   */
  public ArrayList<HashMap<String,String>> query(String table,String[]columns,String selection,
      String[]selectionArgs,String groupBy,String having,String orderBy,String limit) throws DBNotOpenException
  {
    if(isUseable())
    {
      free();
      queryCursor=mSqliteDatabase.query(table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
      return getResultFromCurosr(queryCursor);
    }else
    {
      throw new DBNotOpenException("���ݿ��Ѿ��ر�!");
    }
  }
  
  /**
   * ��Cursor�л�ȡ����
   * @param cursor
   *        ��Ҫ��ȡ���ݵ�Cursor
   * @return
   */
  private ArrayList<HashMap<String,String>> getResultFromCurosr(Cursor cursor)
  {
    ArrayList<HashMap<String,String>> results=new ArrayList<HashMap<String,String>>();
    if(cursor!=null)
    {
      while(cursor.moveToNext())
      {
        HashMap<String,String> map=DBUtils.getRowData(cursor);
        results.add(map);
      }
      free();
    }
    return results;
  }
  
  /**
   * ɾ��һ����
   * @param clazz
   *        Ҫɾ���ı��Ӧ��Class����
   * @return
   *        true��ɾ���ɹ�   false��ɾ��ʧ��
   * @throws DBNotOpenException
   *        ������ݿ�رգ����׳����쳣
   */
  public boolean dropTable(Class<?> clazz) throws DBNotOpenException
  {
    String name=DBUtils.getTableName(clazz);
    return dropTable(name);
  }
  
  /**
   * ���ݱ���ɾ�����ݿ��
   * @param tableName
   *        Ҫɾ�����ݿ�ı���
   * @return
   *        true ɾ���ɹ�  false ɾ��ʧ��
   * @throws DBNotOpenException
   *        ������ݿ�رգ����׳����쳣
   */
  public boolean dropTable(String tableName) throws DBNotOpenException
  {
    boolean success=false;
    if(isUseable())
    {
      if(!TextUtils.isEmpty(tableName))
      {
        String sql="drop table "+tableName;
        execute(sql, null);
        success=true;
      }
    }else
    {
      success=false;
      throw new DBNotOpenException("���ݿ��Ѿ��رգ�");
    }
    return success;
  }
  
  /**
   * �жϸ����ݿ������Ƿ����ʹ��
   * @return
   *      true ����ʹ��  false ����ʹ��
   */
  public boolean isUseable()
  {
    if(isConnect)
    {
      if(mSqliteDatabase!=null && mSqliteDatabase.isOpen())
      {
        return true;
      }else
      {
        return false;
      }
    }else
    {
      return false;
    }
  }
  
  /**
   * �رո����ݿ�����
   */
  public void close()
  {
    if(mSqliteDatabase!=null)
    {
      mSqliteDatabase.close();
      mSqliteDatabase=null;
    }
  }
  
  /**
   * �ͷŲ�ѯ���
   */
  public void free()
  {
    if (queryCursor != null)
    {
      try
      {
        this.queryCursor.close();
      } catch (Exception e)
      {
        // TODO: handle exception
      }
    }

  }
  
  /**
   * �����ݿ��������һ����¼������ö��������ֶ�
   * @param entity
   *        Ҫ����Ķ���
   * @return
   *        true �ɹ�    false ʧ��
   */
  public boolean insert(Object entity)
  {
    return insert(entity,null);
  }
  
  /**
   * �����ݿ��������һ����¼����������ֶ�
   * @param entity
   *          Ҫ�����ʵ�����
   * @param pairs
   *          ��Ҫ����ļ�ֵ��
   * @return
   *          true �ɹ�  false ʧ��
   */
  public boolean insert(Object entity,ArrayList<NameValuePair> pairs)
  {
    SQLBuilder builder=SQLBuilderFactory.getInsance().getSQLBuilder(SQLBuilderFactory.INSERT);
    builder.setEntity(entity);
    builder.setFields(pairs);
    return execute(builder);
  }
  
  /**
   * �����ݿ��������һ����¼
   * @param entity
   * @param nullColumn
   * @param values
   * @return
   */
  public boolean insert(Object entity,String nullColumn,ContentValues values)
  {
    if(isUseable())
    {
     return  mSqliteDatabase.insert(DBUtils.getTableName(entity.getClass()), nullColumn, values)>0;
    }else
    {
      return false;
    }
    
  }
  
  
  /**
   * ���ݿ���ɾ��һ����¼
   * @param entity
   *        Ҫ��ɾ���ļ�¼
   * @param where
   *        where ���� ʹ��?��Ϊռλ��
   * @param whereArgs
   *        ռλ���еĲ���
   * @return
   *        true ɾ���ɹ�    false  ɾ��ʧ��
   */
  public boolean delete(Object entity,String where,String[]whereArgs)
  {
    if(isUseable())
    {
      return mSqliteDatabase.delete(DBUtils.getTableName(entity.getClass()), where, whereArgs)>0;
    }else
    {
      return false;
    }
  }
  
  /**
   * ɾ��һ����¼
   * @param clazz
   *        ��Ҫɾ�����ݱ��Ӧ��Class��
   * @param where
   *        where���
   * @return
   */
  public boolean delete(Class<?> clazz,String where)
  {
    if(isUseable())
    {
      SQLBuilder builder=SQLBuilderFactory.getInsance().getSQLBuilder(SQLBuilderFactory.DELETE);
      builder.setClazz(clazz);
      builder.setCondition(false, where, null, null, null, null);
      return execute(builder);
    }else
    {
      return false;
    }
  }
  
  /**
   * ɾ��һ����¼
   * @param entity
   *        Ҫɾ��������ʵ��
   * @return
   *        true ɾ���ɹ�   false  ɾ��ʧ��
   */
  public boolean delete(Object entity)
  {
    if(isUseable())
    {
      SQLBuilder builder=SQLBuilderFactory.getInsance().getSQLBuilder(SQLBuilderFactory.DELETE);
      builder.setEntity(entity);
      return execute(builder);
    }else
    {
      return false;
    }
  }
  
  /**
   * ����ĳ����¼
   * @param table
   *        ��Ҫ���µı���
   * @param values
   *        ��Ҫ���µ��ֶ�
   * @param whereClause
   *        where��� ʹ��?ռλ
   * @param whereArgs
   *        where����
   * @return
   */
  public boolean update(String table,ContentValues values,String whereClause,String[] whereArgs)
  {
    if(isUseable())
    {
      return mSqliteDatabase.update(table, values, whereClause, whereArgs)>0;
    }else
    {
      return false;
    }
  }
  
  /**
   * ����һ����¼
   * @param entity
   *        ��Ҫ���µ�ʵ�壬Ĭ�ϸ��������ֶ�
   * @return
   */
  public boolean update(Object entity)
  {
    return update(entity,null);
  }
  
  /**
   * ����һ����¼
   * @param entity
   *        ��Ҫ���µ�ʵ�壬Ĭ�ϸ��������ֶ�
   * @param where
   *        where���
   * @return
   *    
   */
  public boolean update(Object entity,String where)
  {
    if(isUseable())
    {
      SQLBuilder builder=SQLBuilderFactory.getInsance().getSQLBuilder(SQLBuilderFactory.UPDATE);
      builder.setCondition(false, where, null, null, null, null);
      builder.setEntity(entity);
      return execute(builder);
    }else
    {
      Log.v("sql","sql-->not use");
      return false;
    }
  }
  
  
  
  
  
  
  /**
   * Interface ���ݿ������ص�
   */
  public interface XDBUpdateListener
  {
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
  }
}
