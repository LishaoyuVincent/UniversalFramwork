package com.universal.framwork.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.text.TextUtils;

import com.universal.framwork.annotation.Column;
import com.universal.framwork.annotation.PrimaryKey;
import com.universal.framwork.annotation.Table;
import com.universal.framwork.annotation.Transient;
import com.universal.framwork.db.entity.PropertyEntity;
import com.universal.framwork.db.entity.TableInfoEntity;
import com.universal.framwork.util.LogUtil;

public class DBUtils
{
  private static final String TAG = "DBUtils";
  /**
   * ���ĳ�����Ӧ�ı���
   * @param clazz
   * @return
   */
  public static String getTableName(Class<?> clazz)
  {
    Table table=clazz.getAnnotation(Table.class);
    //��ø����Tableע�⣬���ע�ⲻΪnull,��ôʹ��ע����Ϊ����������ʹ������
    if(table!=null && !TextUtils.isEmpty(table.name()))
    {
      return table.name();
    }else
    {
      return clazz.getSimpleName();
    }
  }
  
  /**
   * ��ȡһ�������е������ֶ�
   * @param clazz
   * @return
   */
  public static Field getPrimaryKeyField(Class<?> clazz)
  {
    Field primaryField=null;
    Field [] fields=clazz.getDeclaredFields();
    for(Field tmp :fields)
    {
      if(tmp.isAnnotationPresent(PrimaryKey.class))
      {
        primaryField=tmp;
        break;
      }
    }
    //û��ʹ��PrimaryKeyע��,Ѱ���ֶ���Ϊ��_id�����ֶ���Ϊ����
    if(primaryField==null)
    {
      for(Field tmp:fields)
      {
        if(tmp.getName().equals("_id"))
        {
          primaryField=tmp;
        }
      }
    }
    //û���ֶ���Ϊ"_id"���ֶΣ���ô��Ѱ�ҡ�id���ֶ�
    if(primaryField==null)
    {
      for(Field tmp:fields)
      {
        if(tmp.getName().equals("id"))
        {
          primaryField=tmp;
        }
      }
    }
    
    if(primaryField==null)
    {
      throw new RuntimeException(clazz.getSimpleName()+" not set primary key");
    }
    return primaryField;
  }
  
  /**
   * ��ȡ�����ֶ����ƣ����û���������򷵻ؿ�
   * @param clazz
   *          ��Ҫ����ȡ�������Ƶ����Class
   * @return 
   *          ���������ֶ����� ����û������ʱ������Null
   */
  public static String getPrimaryKeyName(Class<?> clazz)
  {
    Field field=getPrimaryKeyField(clazz);
    if(field!=null)
    {
      return field.getName();
    }else
    {
      return null;
    }
  }
  
  /**
   * ��ȡ��ĳһ���Ӧ�Ķ������������
   * @param clazz
   *          ��ĳ�����Ӧ�Ķ���
   * @return
   *          �ö�����������Զ�Ӧ����
   */
  public static List<PropertyEntity> getPropertyList(Class<?> clazz)
  {
    List<PropertyEntity> propertys=new ArrayList<PropertyEntity>();
    try
    {
      //��ȡ��������
      String pkname=getPrimaryKeyName(clazz);
      //�õ����е��ֶ�
      Field[] fields=clazz.getDeclaredFields();
      for(Field tmp:fields)
      {
        if(!isTransient(tmp) && isBaseDateType(tmp))
        {
          if(tmp.getName().equals(pkname))
            continue;
          PropertyEntity entity=new PropertyEntity();
          entity.setColumnName(getColumnNameByField(tmp));
          entity.setName(tmp.getName());
          entity.setDefaultValue(getPropertyDefaultValue(tmp));
          entity.setClazz(tmp.getType());
          propertys.add(entity);
        }
      }
    }catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return propertys;
  }
  
  /**
   * �ж�ĳһ�ֶ��Ƿ�Ϊ͸���ֶΣ����ĳһ���ֶα�{@link Transient }ע�⣬��ô����
   * ͸���ֶ�
   * @param field
   *         ��Ҫ�����ֶ�
   * @return
   *         true ����Transientע�⣬false û��Transientע��
   */
  public static boolean isTransient(Field field)
  {
    if(field!=null)
    {
      if(field.isAnnotationPresent(Transient.class))
      {
        return true;
      }else
      {
        return false;
      }
    }else
    {
      throw new RuntimeException("������ֶβ���Ϊ�գ�");
    }
  }
  
  /**
   * �Ƿ�Ϊ��������������
   * 
   * @param field
   * @return
   */
  public static boolean isBaseDateType(Field field)
  {
    Class<?> clazz = field.getType();
    return clazz.equals(String.class) || clazz.equals(Integer.class)
        || clazz.equals(Byte.class) || clazz.equals(Long.class)
        || clazz.equals(Double.class) || clazz.equals(Float.class)
        || clazz.equals(Character.class) || clazz.equals(Short.class)
        || clazz.equals(Boolean.class) || clazz.equals(Date.class)
        || clazz.equals(java.util.Date.class)
        || clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
  }
  
  
  /**
   * ����ĳһ���ֶΣ���ȡ���ݿ������
   * @param field
   *          Ҫ��ȡ�������ֶΣ���Ҫ����Null,������׳�{@link NullPointerException}
   * @return
   *          �����{@link Column} ��ô�ͷ���Column��ֵ�������{@link PrimaryKey} ��ô�ͷ���PrimaryKey��ֵ
   */
  public static String getColumnNameByField(Field field)
  {
    if(field!=null)
    {
      Column column=field.getAnnotation(Column.class);
      if(column!=null && column.name().trim().length()!=0)
      {
        return column.name();
      }
      
      PrimaryKey primayrKey=field.getAnnotation(PrimaryKey.class);
      if(primayrKey!=null && primayrKey.name().trim().length()!=0)
      {
        return primayrKey.name();
      }
      return field.getName();
    }else
    {
      throw new NullPointerException("field is null");
    }
  }
  
  /**
   * ���Ĭ��ֵ
   * 
   * @param field
   * @return
   */
  public static String getPropertyDefaultValue(Field field)
  {
    Column column = field.getAnnotation(Column.class);
    if (column != null && column.defaultValue().trim().length()!=0)
    {
      return column.defaultValue();
    }
    return null;
  }
  
  
  /**
   * ����Ƿ�����
   * 
   * @param field
   * @return
   */
  public static boolean isAutoIncrement(Field field)
  {
    PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
    if (null != primaryKey)
    {
      LogUtil.v("sql", "is auto -->"+primaryKey.autoIncrement());
      return primaryKey.autoIncrement();
    }
      LogUtil.v("sql", "is auto -->false");
    return false;
  }
  
  public static String createTableSQL(Class clazz) 
  {
    //����clazz����õ���Ӧ���һЩ����
    TableInfoEntity tableInfoEntity=TableEntityFactory.getInstance().getTableInfoEntity(clazz);
    PropertyEntity pk=tableInfoEntity.getmPkEntity();
    StringBuilder sb=new StringBuilder("create table if not exists '").append(tableInfoEntity.getTableName()).append("'(");
    //����ñ�������
    if(pk!=null)
    {
        if(pk.getClazz()==int.class || pk.getClazz()==Integer.class)
        {
          //����ʱint����
          if(pk.isAutoIncrement())
          {
            //�����Զ�����
            sb.append(pk.getColumnName()).append(" ").append("integer primary key autoincrement,");
          }else
          {
            //�������Զ�����
            sb.append(pk.getColumnName()).append(" ").append("integer primary key,");
          }
          
        }else
        {
          //������int���ͣ������Զ�����
          sb.append(pk.getColumnName()).append(" ").append("text primary key,");
        }
    }else
    {
      //���û���������������Ѹ��ñ����һ������id
      sb.append("id integer primary key autoincrement,");
    }
    
    Collection<PropertyEntity> otherProperty=tableInfoEntity.getProperties();
    for(PropertyEntity entity:otherProperty)
    {
      sb.append(entity.getColumnName()).append(" integer").append(",");
    }
    
    sb.deleteCharAt(sb.length()-1);
    sb.append(")");
    LogUtil.d("sql", sb.toString());
    return sb.toString();
  }
  
  /**
   * ��ĳһ��Cursor�е����ݴ���һ��HashMap��
   * @param cursor
   *           ��Ҫ�������ݵ�Cursor
   * @return
   */
  public static HashMap<String,String> getRowData(Cursor cursor)
  {
    HashMap<String, String> result=null;
    if(cursor!=null && cursor.getColumnCount()>0)
    {
      result=new HashMap<String, String>();
      int count=cursor.getColumnCount();
      for(int i=0;i<count;i++)
      {
        result.put(cursor.getColumnName(i), cursor.getString(i));
      }
     
    }
    return result;
  }

}
