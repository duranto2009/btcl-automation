package util;

import java.lang.reflect.*;
import java.sql.Savepoint;

import org.apache.log4j.Logger;

import annotation.Transactional;
import common.RequestFailureException;
import connection.DatabaseConnection;
import javassist.util.proxy.MethodHandler;




public class TransactionalMethodHandler implements MethodHandler{
	
	Logger logger = Logger.getLogger( this.getClass() );
	
	private Object invokeSameConnectionNonTransactional(Object proxyObject, Method actualMethod, Method wrapperMethod
			, Object[] paramArrayOfObject) throws Throwable{

		
		boolean dbOpenedInThisMethod = false;
		
		try{
			if(DatabaseConnectionFactory.isEmpty()){
				DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
				databaseConnection.dbOpen();
				dbOpenedInThisMethod = true;
			}
//			logger.info("Invoking " + proceedMethod.getName() + " in " + proceedMethod.getDeclaringClass().getName() + " [" + TransactionType.READONLY.name() + "]");

			Object resultantObject = wrapperMethod.invoke(proxyObject, paramArrayOfObject);


//			logger.info("Done " + proceedMethod.getName() + " in " + proceedMethod.getDeclaringClass().getName() + " [" + TransactionType.READONLY.name() + "]");
			return resultantObject;
		}catch(Throwable throwable){

			logger.info("[ X ] error executing transactional method handler [" + actualMethod.getName() + "] in: [" + actualMethod.getDeclaringClass().getSimpleName()+"]");

			if(throwable instanceof InvocationTargetException){
				logger.fatal("[ X ] " + ((InvocationTargetException) throwable).getTargetException().getMessage());
				throwable = throwable.getCause();
			}
			
			if(throwable.getCause()!=null ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).debug("fatal",throwable);
			}else if(throwable instanceof RequestFailureException && 
					throwable.getCause()!=null &&   !(throwable.getCause() instanceof RequestFailureException) ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).debug(((RequestFailureException)throwable).getMessage(), throwable);
			}
			
			throw throwable;
		}finally{
			if(dbOpenedInThisMethod){
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbClose();
				DatabaseConnectionFactory.removeLastDatabaseConnection();
			}
		}
		
	}
	
	private Object invokeSameConnectionIndividualTransactional(Object proxyObject, Method actualMethod, Method wrapperMethod
			, Object[] paramArrayOfObject) throws Throwable{

		boolean dbOpenedInThisMethod = false;
		Savepoint savepoint = null;
		try{
			if(DatabaseConnectionFactory.isEmpty()){
				DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
				databaseConnection.dbOpen();
				dbOpenedInThisMethod = true;
			}
			
			savepoint = DatabaseConnectionFactory.getCurrentDatabaseConnection().getSavePoint();
			DatabaseConnectionFactory.getCurrentDatabaseConnection().dbTransationStart();

			Object resultantObject = wrapperMethod.invoke(proxyObject, paramArrayOfObject);
			DatabaseConnectionFactory.getCurrentDatabaseConnection().dbTransationEnd();
			savepoint = null;
			return resultantObject;
		}catch(Throwable throwable){

			logger.info("[ X ] error executing transactional method handler [" + actualMethod.getName() + "] in: [" + actualMethod.getDeclaringClass().getSimpleName()+"]");
			
			if(savepoint != null){
				DatabaseConnectionFactory.getCurrentDatabaseConnection().rollBackSavePoint(savepoint);
			}
			
			if(throwable instanceof InvocationTargetException){
				logger.fatal("[ X ] " + ((InvocationTargetException) throwable).getTargetException().getMessage());
				throwable = throwable.getCause();
			}
			
			if(throwable.getCause()!=null ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).debug("fatal",throwable);
			}else if(throwable instanceof RequestFailureException && 
					throwable.getCause()!=null &&   !(throwable.getCause() instanceof RequestFailureException) ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).debug(((RequestFailureException)throwable).getMessage());
			}
			
			throw throwable;
		}finally{
			if(dbOpenedInThisMethod){
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbClose();
				DatabaseConnectionFactory.removeLastDatabaseConnection();
			}
		}
	}
	
	private Object invokeSameConnectionPreviousTransactional(Object proxyObject, Method actualMethod, Method wrapperMethod
			, Object[] paramArrayOfObject) throws Throwable{
		boolean dbOpenedInThisMethod = false;
		boolean isTransactionStartedInThisMethod = false;
		try{
			if(DatabaseConnectionFactory.isEmpty()){
				DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
				databaseConnection.dbOpen();
				dbOpenedInThisMethod = true;
			}
			
			if(!DatabaseConnectionFactory.getCurrentDatabaseConnection().hasStartedTransaction()){
				isTransactionStartedInThisMethod = true;
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbTransationStart();
			}
			Object resultantObject = wrapperMethod.invoke(proxyObject, paramArrayOfObject);

			if(isTransactionStartedInThisMethod){
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbTransationEnd();
			}
			
			return resultantObject;
		}catch(Throwable throwable){
//			for(StackTraceElement ste : throwable.getStackTrace()) {
//				Logger.getLogger(paramObject.getClass().getSuperclass()).fatal(ste.toString());
//			}
			if (isTransactionStartedInThisMethod) {
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbTransationRollBack();
			}
			if(!wrapperMethod.getDeclaringClass().getSimpleName().contains("LoggedInUserService")) {


				logger.info("[ X ] error executing transactional method handler [" + actualMethod.getName() + "] in: [" + actualMethod.getDeclaringClass().getSimpleName() + "]");


				if (throwable instanceof InvocationTargetException) {
					logger.fatal("[ X ] " + ((InvocationTargetException) throwable).getTargetException().getMessage());
					throwable = throwable.getCause();

				}

				if (throwable.getCause() != null) {
					Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal("fatal", throwable);
				} else if (throwable instanceof RequestFailureException &&
						throwable.getCause() != null && !(throwable.getCause() instanceof RequestFailureException)) {
					Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal(((RequestFailureException) throwable).getMessage());
				} else if (throwable instanceof Exception) {
					Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal(((Exception) throwable).getMessage());
				} else {
					Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal(throwable.getStackTrace());
				}

				throw throwable;
			}else {
				//Please DO NOTHING
				return throwable;
			}
		}finally{
			if(dbOpenedInThisMethod){
				DatabaseConnectionFactory.getCurrentDatabaseConnection().dbClose();
				DatabaseConnectionFactory.removeLastDatabaseConnection();
			}
		}
		
	}
	
	private Object invokeNewConnectionIndividualTransaction(Object proxyObject, Method actualMethod, Method wrapperMethod
			, Object[] paramArrayOfObject) throws Throwable{
		DatabaseConnectionFactory.getNewDatabaseConnection();
		try{
			return invokeSameConnectionIndividualTransactional(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}finally{
			DatabaseConnectionFactory.removeLastDatabaseConnection();
		}
	}
	
	
	private Object invokeReadOnlyTransaction(Object proxyObject, Method actualMethod, Method wrapperMethod, Object[] paramArrayOfObject)
			throws Throwable {
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
		try{
			databaseConnection.dbOpen();
			Object resultantObject = wrapperMethod.invoke(proxyObject, paramArrayOfObject);
			if(databaseConnection.hasStartedTransaction()){
				databaseConnection.dbTransationEnd();
			}
			return resultantObject;
		}catch(Throwable throwable){

			logger.info("[ X ] error executing transactional method handler [" + actualMethod.getName() + "] in: [" + actualMethod.getDeclaringClass().getSimpleName()+"]");
			if(throwable instanceof InvocationTargetException){
				logger.fatal("[ X ] " + ((InvocationTargetException) throwable).getTargetException().getMessage());
				throwable = throwable.getCause();
			}
			
			if(throwable.getCause()!=null ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal("fatal",throwable);
			}else if(throwable instanceof RequestFailureException && 
					throwable.getCause()!=null &&   !(throwable.getCause() instanceof RequestFailureException) ){
				Logger.getLogger(proxyObject.getClass().getSuperclass()).fatal(((RequestFailureException)throwable).getMessage());
			}
			
			
			throw throwable;
		}finally{
			databaseConnection.dbClose();
			DatabaseConnectionFactory.removeLastDatabaseConnection();
		}
	}
	
	
	private TransactionType getTransactionType(Method method) {
		Transactional transactional = method.getAnnotation(Transactional.class);
		return transactional.transactionType();
	}
	
	
	private ConnectionType getConnectionType(Method method){
		Transactional transactional = method.getAnnotation(Transactional.class);
		return transactional.connectionType();
	}
	
	
	@Override
	public Object invoke(Object proxyObject, Method actualMethod, Method wrapperMethod, Object[] paramArrayOfObject)
			throws Throwable {
		TransactionType transactionType = getTransactionType(actualMethod);
		
		ConnectionType connectionType = getConnectionType(actualMethod);

		
		if(connectionType == ConnectionType.OLD_CONNECTION  && transactionType == TransactionType.READONLY ){
			return invokeSameConnectionNonTransactional(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}else if(connectionType == ConnectionType.OLD_CONNECTION  && transactionType == TransactionType.INDIVIDUAL_TRANSACTION ){
			return invokeSameConnectionIndividualTransactional(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}else if(connectionType == ConnectionType.OLD_CONNECTION  && transactionType == TransactionType.PART_OF_PREVIOUS_TRANSACTION ){
			return invokeSameConnectionPreviousTransactional(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}else if(connectionType == ConnectionType.OLD_CONNECTION  && transactionType == TransactionType.PART_OF_PREVIOUS_TRANSACTION_FOR_READONLY ){
			return invokeSameConnectionNonTransactional(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}
		else if(connectionType == ConnectionType.NEW_CONNECTION  && transactionType == TransactionType.INDIVIDUAL_TRANSACTION ){
			return invokeNewConnectionIndividualTransaction(proxyObject, actualMethod, wrapperMethod, paramArrayOfObject);
		}else{
			return wrapperMethod.invoke(proxyObject, paramArrayOfObject);
		}

	}

}
