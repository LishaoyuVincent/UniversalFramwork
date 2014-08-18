package com.universal.framwork.db.entity;
/**
 * ��������ʵ����
 * com.universal.framwork.db.entity.PropertyEntity
 * @author yuanzeyao <br/>
 * create at 2014��5��30�� ����5:53:03
 */
public class PropertyEntity
{
  private static final String TAG = "PropertyEntity";
  /**
   * �����������
   */
  private String name;
  
  /**
   * �ö����Ӧ�������
   */
  private String columnName;
  
  /**
   * ������������
   */
  private Class<?> clazz;
  
  /**
   * ���е�Ĭ��ֵ
   */
  private Object defaultValue;
  
  /**
   * �����Ƿ�����Ϊ��
   */
  private boolean isAllowNull;
  
  /**
   * �е�����
   */
  private int index;
  
  /**
   * �Ƿ�������
   */
  private boolean primaryKey;
  
  /**
   * �Ƿ��Զ�����
   */
  private boolean autoIncrement;
  
  /**
   * ����ʵ��Ĺ��캯��
   * @param name
   * @param clazz
   * @param defaultValue
   * @param primaryKey
   * @param isAllowNull
   * @param autoIncrement
   * @param columnName
   */
  public PropertyEntity(String name,Class<?> clazz,Object defaultValue,boolean primaryKey,
      boolean isAllowNull,boolean autoIncrement,String columnName)
  {
    this.name=name;
    this.clazz=clazz;
    this.defaultValue=defaultValue;
    this.primaryKey=primaryKey;
    this.autoIncrement=autoIncrement;
    this.isAllowNull=isAllowNull;
    this.columnName=columnName;
    
  }
  
  public PropertyEntity()
  {
    
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getColumnName()
  {
    return columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public Class<?> getClazz()
  {
    return clazz;
  }

  public void setClazz(Class<?> clazz)
  {
    this.clazz = clazz;
  }

  public Object getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue(Object defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public boolean isAllowNull()
  {
    return isAllowNull;
  }

  public void setAllowNull(boolean isAllowNull)
  {
    this.isAllowNull = isAllowNull;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  public boolean isPrimaryKey()
  {
    return primaryKey;
  }

  public void setPrimaryKey(boolean primaryKey)
  {
    this.primaryKey = primaryKey;
  }

  public boolean isAutoIncrement()
  {
    return autoIncrement;
  }

  public void setAutoIncrement(boolean autoIncrement)
  {
    this.autoIncrement = autoIncrement;
  }
  
  
}
