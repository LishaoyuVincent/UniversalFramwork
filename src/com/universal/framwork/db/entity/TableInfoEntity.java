package com.universal.framwork.db.entity;

import java.util.ArrayList;
import java.util.List;
/**
 * ���ʵ����
 * com.universal.framwork.db.entity.TableInfoEntity
 * @author yuanzeyao <br/>
 * create at 2014��6��1�� ����10:57:25
 */
public class TableInfoEntity
{
  private static final String TAG = "TableInfoEntity";
  /**
   * ���Ӧ������
   */
  private String className;
  
  /**
   * ����
   */
  private String tableName;
  
  /**
   * ������Ӧ����
   */
  private PropertyEntity mPkEntity;
  
  /**
   * ��������Ӧ����
   */
  private List<PropertyEntity> properties=new ArrayList<PropertyEntity>();

  public String getClassName()
  {
    return className;
  }

  public void setClassName(String className)
  {
    this.className = className;
  }

  public String getTableName()
  {
    return tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  public PropertyEntity getmPkEntity()
  {
    return mPkEntity;
  }

  public void setmPkEntity(PropertyEntity mPkEntity)
  {
    this.mPkEntity = mPkEntity;
  }

  public List<PropertyEntity> getProperties()
  {
    return properties;
  }

  public void setProperties(List<PropertyEntity> properties)
  {
    this.properties = properties;
  }
  
  
}
