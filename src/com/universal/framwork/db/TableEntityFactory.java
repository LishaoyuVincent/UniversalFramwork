package com.universal.framwork.db;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.universal.framwork.db.entity.PropertyEntity;
import com.universal.framwork.db.entity.TableInfoEntity;

/**
 * һ������TableEntity��ʵ��
 * com.universal.framwork.db.TableEntityFactory
 * @author yuanzeyao <br/>
 * create at 2014��6��1�� ����11:22:13
 */
public class TableEntityFactory
{
  private static final String TAG = "TableEntityFactory";
  //���ڱ����Ѿ���������TableInfoEntity,���Լ���TableInfoEntity�Ĵ���
  public static final HashMap<String,TableInfoEntity> tableEntitys=new HashMap<String,TableInfoEntity>();
  
  //����ģʽ
  public static TableEntityFactory instance;
  public static TableEntityFactory getInstance()
  {
    if(instance==null)
    {
      instance=new TableEntityFactory();
    }
    return instance;
  }
  
  /**
   * 
   * @param clazz
   * @return
   */
  public TableInfoEntity getTableInfoEntity(Class clazz)
  {
    if(clazz == null)
    {
      throw new NullPointerException("class is null");
    }
    
    TableInfoEntity tableInfo=tableEntitys.get(clazz.getName());
    
    if(tableInfo==null)
    {
      tableInfo=new TableInfoEntity();
      tableInfo.setClassName(clazz.getName());
      tableInfo.setTableName(DBUtils.getTableName(clazz));
      //�ҵ������ֶ�
      Field field=DBUtils.getPrimaryKeyField(clazz);
      Log.v("sql", "field name-->"+field.getName());
      
      if(field!=null)
      {
        //����������һЩ����
        PropertyEntity pk=new PropertyEntity();
        //������Ϊ��
        pk.setAllowNull(false);
        //�Ƿ��Զ�����
        pk.setAutoIncrement(DBUtils.isAutoIncrement(field));
        //������������
        pk.setClazz(field.getType());
        //�����ֶ�����
        pk.setColumnName(DBUtils.getColumnNameByField(field));
        //����Ĭ��ֵ
        pk.setDefaultValue(DBUtils.getPropertyDefaultValue(field));
        pk.setPrimaryKey(true);
        //������������
        pk.setName(field.getName());
        tableInfo.setmPkEntity(pk);
      }else
      {
        //û������
        tableInfo.setmPkEntity(null);
      }
      //��ȡ�ñ�ķ���������
      List<PropertyEntity> otherProperty=DBUtils.getPropertyList(clazz);
      tableInfo.setProperties(otherProperty);
      tableEntitys.put(clazz.getName(), tableInfo);
     
    }
    return tableInfo;
  }
}
