package repository;

import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


import databasemanager.DatabaseManager;


/**
 *
 * <p>Title: </p>
 *
 * <p>Description:
 * 1. Call the getInstance() method from main before creating other thread
 * 2. From each repository call RepositoryManager.getInstance().addRepository(this) once at the end of its constructor</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RepositoryManager extends Thread 
{
    private volatile boolean running=false;
    static RepositoryManager repositoryManager = null;
    
    public static long lastModifyTime ;
    private Repository [] registeredRepository;
    private int registeredRepositoryLength;
    public static final int REPOSITORY_LOADING_GRACE_TIME=30000;
    private RepositoryManager()
    {
    	super("Repository Manager");
        registeredRepository = new Repository[40];
        registeredRepositoryLength = 0;
        running=true;
        setDaemon(true);
        start();
    }


    public static RepositoryManager getInstance()
    {
        if (repositoryManager == null)
        {
            CreateRepoManager();
        }
        return repositoryManager;
    }

    public synchronized void addRepository(Repository  p_repository)
    {
        if(p_repository==null)return;
        registeredRepository[registeredRepositoryLength++]=p_repository;
        p_repository.reload(true);
    }
    public synchronized void addRepository(Repository  p_repository, boolean reloadAll)
    {
        if(p_repository==null)return;
        registeredRepository[registeredRepositoryLength++]=p_repository;
        if(reloadAll)
        p_repository.reload(true);
    }
    private synchronized static void CreateRepoManager()
    {
        if (repositoryManager == null)
        {
            repositoryManager = new RepositoryManager();
        }
    }

    public void run()
    {
        Connection connection = null;
        Statement statement = null;

        Set<String> repositoryTableName = new HashSet<String>();
//        Set<String> repositoryName = new HashSet<String>();
        
        while (running) {
            try {
               long tempTime = System.currentTimeMillis()-REPOSITORY_LOADING_GRACE_TIME;
               
               try {
            	   connection = DatabaseManager.getInstance().getConnection();
               } catch (Exception e) {}
               
               try {
            	   statement = connection.createStatement(); //"select tableName, lastModifyTime from vbSequencer where lastModifyTime > ?");
               } catch (Exception e) {}

                String sqlQuery ="select table_name, table_LastModificationTime from vbSequencer where table_LastModificationTime >=" + (lastModifyTime)+" and table_LastModificationTime<="+System.currentTimeMillis();
        //        logger.debug("sqlQuery " + sqlQuery);
                ResultSet resultSet;
                try {
                	resultSet = statement.executeQuery(sqlQuery);
                	while (resultSet.next()) {
                    	repositoryTableName.add(resultSet.getString(1));
//                    	logger.debug("dirty table names "+resultSet.getString(1));
                    	
//                        repositoryName.add(resultSet.getString(3));
                    }
                    resultSet.close();
                } catch (NullPointerException ex){      	
                }
                
                statement.close();
                statement = null;
                DatabaseManager.getInstance().freeConnection(connection);
                connection = null;

               lastModifyTime = tempTime;//TODO put a minus for safety
               
               //logger.debug("repositoryName " + repositoryName + " registeredRepositoryLength " + registeredRepositoryLength);
               
               for(int j=0;j<registeredRepositoryLength;j++) {
            	   Repository r = registeredRepository[j];            	   
            	   //logger.debug("r.getRepoName() " + r.getRepoName());
//            	   if(repositoryName.contains(r.getRepoName()))
            	   if(repositoryTableName.contains(r.getTableName()))	   
            	   {
            		 //  logger.debug("Reloading repository with name :" +r.getTableName() +" at :"+System.currentTimeMillis());
            		   r.reload(false);
            	   }
               }
               
               repositoryTableName.clear();
                
            } catch(Exception ex) { //logger.fatal("Exception in Repository Manager",ex);
            } finally {
                if(statement != null)
                    try{statement.close();}catch(Exception s){}
                if(connection != null)
                    try{DatabaseManager.getInstance().freeConnection(connection);}catch(Exception c){}
            }

            try {
                sleep(1000);
            } catch(InterruptedException ie){}
          
        }
        repositoryTableName.clear();
        repositoryTableName = null;
    }

    public void shutDown() {
        running=false;
    }
}
