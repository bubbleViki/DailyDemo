package com.meritit.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.meritit.util.PropUtils;

/**
 * hbase通用操作实现类
 * 
 * @author Lenovo
 * 
 */

@Repository("db.hbaseDAOImp")
public class HbaseDAOImp implements HbaseDAO {
	/**
	 * 用来写日志
	 */
	private static Logger logger = Logger.getLogger(HbaseDAOImp.class);
	private static final String COLENDCHAR = String.valueOf(':');
	/**
	 * hadoop configuration对象
	 */
	private static Configuration conf = null;

	private static HBaseAdmin admin;
	public static Properties prop;
	private static HConnection connection;
	
	public static HConnection getConnection(){
		return connection;
	}
	
	static {
		try {
			prop = PropUtils.getProp("hbaseconfig.properties");
			
			conf = new Configuration();
			conf.set("hbase.zookeeper.quorum",
					prop.getProperty("hbase.zookeeper.quorum"));
			conf.set("hbase.rootdir", prop.getProperty("hbase.rootdir"));
			
			admin = new HBaseAdmin(conf);
			connection = HConnectionManager.createConnection(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建表，只有一个列簇
	 */
	public boolean createTable(String tablename, String columnFamily)
			throws Exception {
		boolean b = false;
		     try {
		       if (admin.tableExists(tablename)) {
		        logger.info(tablename + "表已存在.");
		         return b;
		       }
		       HTableDescriptor htdesc = createHTDesc(tablename);
		       admin.createTable(htdesc);
		       b = true;
		     } catch (Exception e) {
		       logger.error(e);
		       throw e;
		     }
		   return b;
		
	}
	
	/**
	 * 创建表，传入多个列簇
	 */
	public boolean createHTable(String tableName, String[] columns)
			throws Exception {
		 boolean b = false;
		    try {
		      if (admin.tableExists(tableName)) {
		         logger.info(tableName + "表已存在.");
		         return b;
		       }
		      HTableDescriptor htdesc = createHTDesc(tableName);
		       for (int i = 0; i < columns.length; i++) {
		         String colName = columns[i];
		         addFamily(htdesc, colName, false);
		       }
		        admin.createTable(htdesc);
		       b = true;
		      } catch (Exception e) {
		      logger.error(e);
		       throw e;
		     }
		      return b;
	}

	/**
	 * 获取hbase表的操作句柄
	 */
	public HTable getHTable(String tableName) throws Exception {
		 try {
		       return new HTable(conf, tableName);
		       
			  } catch (IOException e) {
			     e.printStackTrace();
			         throw e;
			 }
	}

	/**
	 * 删除指定表的所有数据
	 */
	public boolean deleteTable(String tablename) throws Exception {
		 boolean b = false;
		    if (admin.tableExists(tablename)) {
		      try {
		         admin.disableTable(tablename);
		         admin.deleteTable(tablename);
		         b = true;
		       } catch (Exception e) {
		         logger.error(e);
		         throw e;
		       }
		      }
		  return b;
	}

	public boolean save(Put put, String tableName) throws Exception {
		 boolean b = false;
		     HTable table = getHTable(tableName);
		     try {
		       table.put(put);
		       b = true;
		       logger.info("***************插入成功***************");
		    } catch (Exception e) {
		       logger.error(e);
		       throw e;
		     } finally {
		       try {
		         table.close();
		       } catch (IOException e) {
		         logger.error(e);
		          throw e;
		       }
		     }
		    return b;
	}

	public boolean save(List<Put> Puts, String tableName)
			throws Exception {
		     boolean b = false;
		      HTable table = getHTable(tableName);
		         try {
		       table.put(Puts);
		        b = true;
		        logger.info("***************批量插入成功***************");
		         } catch (Exception e) {
		        logger.error(e);
		       throw e;
		         } finally {
		           try {
		         table.close();
		           } catch (IOException e) {
		         logger.error(e);
		          throw e;
		           }
		         }
		      return b;
	}

	public boolean isExists(String tableName, String rowKey)
			throws Exception {
		boolean b = false;
		     HTable table = getHTable(tableName);
		     try {
		     Get get = new Get(rowKey.getBytes());
		       if (table.exists(get))
		        b = true;
		     } catch (Exception e) {
		       logger.error(e);
		       throw e;
		     } finally {
		       try {
		         table.close();
		       } catch (IOException e) {
		         logger.error(e);
		         throw e;
		       }
		     }
		     return b;
	}

	public String getCellValue(String tableName, String rowKey, String family, String qualifier) throws Exception {
		   HTable table = getHTable(tableName);
		      try {
		        Get get = new Get(rowKey.getBytes());
		      Result result = table.get(get);
		       byte[] b = result.getValue(family.getBytes(), qualifier.getBytes());
		        if (b == null) {
		          return "";
		        }
		       String str = new String(b);
		        return str;
		      } catch (Exception e) {
		       logger.error(e);
		      throw e;
		      } finally {
		        try {
		        table.close();
		        } catch (IOException e) {
		          logger.error(e);
		        throw e;
		        }
		      }
	}

	/**
	 * 通过rowkey获取指定表的数据
	 */
	public Result getResultByRowKey(String tableName, String rowKey)
			throws Exception {
		Result result = null;
		    HTable table = getHTable(tableName);
		     try {
		       Get get = new Get(rowKey.getBytes());
		      result = table.get(get);
		    } catch (Exception e) {
	     logger.error(e);
		      throw e;
		    } finally {
		      try {
		         table.close();
		      } catch (IOException e) {
		        logger.error(e);
		         throw e;
		      }
		    }
	     return result;
	}

	/**
	 * 使用行过滤器过滤所需的数据
	 */
	public List<Result> getResultByFilter(String tableName, String regexStr) throws Exception {
		 List list = null;
		     HTable table = getHTable(tableName);
		     try {
		      Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStr));
		     Scan scan = new Scan();
		       scan.setFilter(filter);
		       ResultScanner scanner = table.getScanner(scan);
		        if (scanner != null) {
		          list = new ArrayList();
		          for (Result rs : scanner)
		            list.add(rs);
		       }
		     }
		     catch (Exception e) {
		       logger.error(e);
		       throw e;
		     } finally {
		       try {
		         table.close();
		       } catch (IOException e) {
		        logger.error(e);
		         throw e;
		       }
		     }
		     return list;
	}

	/**
	 * 使用行过滤器和值过滤器功能查询所需要的数据
	 * @tableName 表名
	 * @regexStr 正则表达式
	 * @value 要过滤的值
	 */
	public List<Result> getResultByValueFilter(String tableName, String regexStr, String value) throws Exception {
		List list = null;
		     HTable table = getHTable(tableName);
		     try {
		       List filters = new ArrayList();
		       Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStr));
		       filters.add(filter1);
		       Filter filter2 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(value));
		       filters.add(filter2);
		       FilterList fl = new FilterList(filters);
		       Scan scan = new Scan();
		       scan.setFilter(fl);
		       ResultScanner scanner = table.getScanner(scan);
		       if (scanner != null) {
		         list = new ArrayList();
		         for (Result rs : scanner)
		           list.add(rs);
		       }
		     }
		     catch (Exception e) {
		      logger.error(e);
		       throw e;
		     } finally {
		       try {
		     table.close();
		       } catch (IOException e) {
		         logger.error(e);
		         throw e;
		       }
		     }
		     return list;
	}

	/**
	 * 使用行过滤器和列明进行数据过滤
	 */
	public List<Result> getResultByFilterList(String tableName, String regexStr, String qualifier) throws Exception {
		 List list = null;
		     HTable table = getHTable(tableName);
		      try {
		       List filters = new ArrayList();
		       Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStr));
		       filters.add(filter1);
		       //列名过滤器
		        Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(qualifier)));
		        filters.add(filter2);
		        FilterList fl = new FilterList(filters);
		        Scan scan = new Scan();
		        scan.setFilter(fl);
		        ResultScanner scanner = table.getScanner(scan);
		        if (scanner != null) {
		          list = new ArrayList();
		          for (Result rs : scanner)
		            list.add(rs);
		        }
		      }
		      catch (Exception e) {
		        logger.error(e);
		        throw e;
		      } finally {
		        try {
		          table.close();
		        } catch (IOException e) {
		         logger.error(e);
		          throw e;
		        }
		      }
		     return list;
	}

	public List<Result> getResultByFilterList(String tableName, String regexStr, String qualifier, String startRow, String stopRow) throws Exception {
		List list = null;
		     HTable table = getHTable(tableName);
		     try {
		      List filters = new ArrayList();
		       Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStr));
		       filters.add(filter1);
		       if (qualifier != null) {
		         Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(qualifier)));
		         filters.add(filter2);
		       }
		       FilterList fl = new FilterList(filters);
		       Scan scan = new Scan();
		       scan.setFilter(fl);
		       scan.setStartRow(startRow.getBytes());
		       scan.setStopRow(stopRow.getBytes());
		      ResultScanner scanner = table.getScanner(scan);
		       if (scanner != null) {
		         list = new ArrayList();
		         for (Result rs : scanner)
		           list.add(rs);
		       }
		     }
		     catch (Exception e) {
		       logger.error(e);
		       throw e;
		     } finally {
		       try {
		         table.close();
		       } catch (IOException e) {
		         logger.error(e);
		         throw e;
		       }
		     }
		     return list;
	}

	public List<Result> getRangeResultList(String tableName, String regexStr, String startRow, String stopRow)
			throws Exception {
		List list = null;
		     HTable table = getHTable(tableName);
		     try {
		       List filters = new ArrayList();
		      Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStr));
		      filters.add(filter1);
		       FilterList fl = new FilterList(filters);
		      Scan scan = new Scan();
		       scan.setFilter(fl);
		       scan.setStartRow(startRow.getBytes());
		       scan.setStopRow(stopRow.getBytes());
		       ResultScanner scanner = table.getScanner(scan);
		       if (scanner != null) {
		         list = new ArrayList();
		         for (Result rs : scanner)
		          list.add(rs);
		       }
		     }
		     catch (Exception e) {
		       logger.error(e);
		       throw e;
		     } finally {
		       try {
		         table.close();
		       } catch (IOException e) {
		         logger.error(e);
		         throw e;
		       }
		     }
		     return list;
	}

	/**
	 * 列名前缀匹配过滤
	 */
	public List<Result> scaneByPrefixFilter(String tableName, String rowPrifix) throws Exception {
		List list = null;
	     HTable table = getHTable(tableName);
	     try {
	       Scan s = new Scan();
	       s.setFilter(new PrefixFilter(rowPrifix.getBytes()));
	       ResultScanner rs = table.getScanner(s);
	       list = new ArrayList();
	       for (Result r : rs)
	         list.add(r);
	     }
	     catch (IOException e) {
	       logger.error(e);
	       throw e;
	     } finally {
	       try {
	         table.close();
	       } catch (IOException e) {
	         logger.error(e);
	         throw e;
	       }
	     }
	     return list;
	}

	public List<Result> getRangeResultWithFamily(String tableName, String family, String startRow, String stopRow)
			throws Exception {
		List list = null;
	    HTable table = getHTable(tableName);
	    ResultScanner scanner = null;
	    try
	    {
	      Scan scan = new Scan();
	      scan.setStartRow(Bytes.toBytes(startRow));
	      scan.setStopRow(Bytes.toBytes(stopRow));
	      scan.addFamily(Bytes.toBytes(family));

	      scan.setCacheBlocks(false);
	      scanner = table.getScanner(scan);

	      list = new ArrayList();
	      for (Result r : scanner) {
	        list.add(r);
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error("查询hbase异常！ ", e);
	      throw e;
	    }
	    finally
	    {
	      scanner.close();
	      table.close();
	    }
	    return list;
	}

	public List<Result> getRows(String tableName, String startRow, String stopRow, List<String> qualifiers) throws Exception {
		List list = new ArrayList();
	    HTable table = getHTable(tableName);
	    try {
	      Scan scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(stopRow + '\001'));
	      for (String qualifier : qualifiers) {
	        Filter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(qualifier)));
	        scan.setFilter(filter);
	        ResultScanner scanner = table.getScanner(scan);
	        if (scanner != null)
	          for (Result rs : scanner)
	            list.add(rs);
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error(e);
	      throw e;
	    } finally {
	      try {
	        table.close();
	      } catch (IOException e) {
	        logger.error(e);
	        throw e;
	      }
	    }
	    return list;
	}

	public List<Result> getRows(String tableName, String startRow, String stopRow) throws Exception {
		List list = new ArrayList();
	    HTable table = getHTable(tableName);
	    try {
	      Scan scan = new Scan();
	      scan.setStartRow(startRow.getBytes());
	      scan.setStopRow(stopRow.getBytes());
	      ResultScanner scanner = table.getScanner(scan);
	      list = new ArrayList();
	      for (Result rsResult : scanner)
	        list.add(rsResult);
	    }
	    catch (Exception e) {
	      logger.error(e);
	      throw e;
	    } finally {
	      try {
	        table.close();
	      } catch (IOException e) {
	        logger.error(e);
	        throw e;
	      }
	    }
	    return list;
	}

	/**
	 * 删除指定的表，指定rowkey相关数据
	 */
	public boolean deleteByRowKey(String tableName, String rowKey) throws Exception {
		 boolean b = false;
		    HTable table = getHTable(tableName);
		    try {
		      Delete delete = new Delete(Bytes.toBytes(rowKey));
		      table.delete(delete);
		      b = true;
		    } catch (Exception e) {
		      logger.error(e);
		      throw e;
		    } finally {
		      try {
		        table.close();
		      } catch (IOException e) {
		        logger.error(e);
		        throw e;
		      }
		    }
		    return b;
	}

	 private HTableDescriptor createHTDesc(String tableName){
	     return new HTableDescriptor(tableName);
	  }
	 
	 private void addFamily(HTableDescriptor htdesc, String colName, boolean readonly){
	     htdesc.addFamily(createHCDesc(colName));
	     htdesc.setReadOnly(readonly);
	  }
	 
	 private HColumnDescriptor createHCDesc(String colName){
	     String tmp = fixColName(colName);
	      byte[] colNameByte = Bytes.toBytes(tmp);
	     return new HColumnDescriptor(colNameByte);
	    }
	 
	 
	 private String fixColName(String colName){
	     return fixColName(colName, null);
	   }
	 
	 
	 private String fixColName(String colName, String cluster){
	     if ((cluster != null) && (cluster.trim().length() > 0) && 
	       (colName.endsWith(cluster))) {
	       return colName;
	     }
	     String tmp = colName;
	     int index = colName.indexOf(COLENDCHAR);
	
	      if (index == -1) {
	        tmp = tmp + COLENDCHAR;
	      }
	 
	     if ((cluster != null) && (cluster.trim().length() > 0)) {
	       tmp = tmp + cluster;
	     }
	      return tmp;
	   }
	 
	 
	 public static void main(String[] args)throws Exception {
		HbaseDAO dao = new HbaseDAOImp();
		//Result result = dao.getResultByRowKey("spider_task", "c1");
		
		Put put = new Put(Bytes.toBytes("1486451149781-002-ea90e531-a75a-4168-a587-17bcc6905fb1-002"));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("attachment"), Bytes.toBytes("[{\"fileid\":\"E://tender\\\\1486451149781\\\\2bb3a31d-3701-4dec-8fbb-5ee1a40aef05\\\\\\\\常山县政府采购项目申请表210.doc\",\"filename\":\"常山县政府采购项目申请表210.doc\"},{\"fileid\":\"E://tender\\\\1486451149781\\\\2bb3a31d-3701-4dec-8fbb-5ee1a40aef05\\\\\\\\常山县政府采购项目申请表211.doc\",\"filename\":\"常山县政府采购项目申请表211.doc\"}]"));
		dao.save(put, "spider_file_upload");
		
		//System.out.println(result);
	}

}