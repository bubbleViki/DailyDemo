package com.meritit.db;

import java.util.List;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 * Hbase数据库通用操作接口
 * @author Lenovo
 *
 */
public interface HbaseDAO
{
  public abstract boolean createTable(String paramString1, String paramString2)
    throws Exception;

  public abstract boolean createHTable(String paramString, String[] paramArrayOfString)
    throws Exception;

  public abstract HTable getHTable(String paramString)
    throws Exception;

  public abstract boolean deleteTable(String paramString)
    throws Exception;

  public abstract boolean save(Put paramPut, String paramString)
    throws Exception;

  public abstract boolean save(List<Put> paramList, String paramString)
    throws Exception;

  public abstract boolean isExists(String paramString1, String paramString2)
    throws Exception;

  public abstract String getCellValue(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;

  public abstract Result getResultByRowKey(String paramString1, String paramString2)
    throws Exception;

  public abstract List<Result> getResultByFilter(String paramString1, String paramString2)
    throws Exception;

  public abstract List<Result> getResultByValueFilter(String paramString1, String paramString2, String paramString3)
    throws Exception;

  public abstract List<Result> getResultByFilterList(String paramString1, String paramString2, String paramString3)
    throws Exception;

  public abstract List<Result> getResultByFilterList(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception;

  public abstract List<Result> getRangeResultList(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;

  public abstract List<Result> scaneByPrefixFilter(String paramString1, String paramString2)
    throws Exception;

  public abstract List<Result> getRangeResultWithFamily(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;

  public abstract List<Result> getRows(String paramString1, String paramString2, String paramString3, List<String> paramList)
    throws Exception;

  public abstract List<Result> getRows(String paramString1, String paramString2, String paramString3)
    throws Exception;

  public abstract boolean deleteByRowKey(String paramString1, String paramString2)
    throws Exception;
}