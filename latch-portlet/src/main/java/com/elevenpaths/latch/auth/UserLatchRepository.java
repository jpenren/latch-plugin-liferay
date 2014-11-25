package com.elevenpaths.latch.auth;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.elevenpaths.latch.domain.UserLatch;
import com.liferay.portal.kernel.util.InfrastructureUtil;

final class UserLatchRepository {
	
	private static final String INSERT = "INSERT INTO EP_UserLatch (userId, accountId, createDate) VALUES (?,?,?)";
	private static final String FIND_BY_ID = "SELECT * from EP_UserLatch where userId=?";
	private static final String DELETE = "DELETE FROM EP_UserLatch where userId=?";
	private static final String COUNT = "SELECT COUNT(*) FROM EP_UserLatch where userId=?";
	private static final String UPDATE = "UPDATE EP_UserLatch set accountId=? and createDate=? WHERE userId=?";

	UserLatchRepository() {
	}
	
	public void delete(long userId) throws SQLException{
		final QueryRunner run = createRunner();
		run.update(DELETE, userId);
	}
	
	public boolean hasLatch(long userId) throws SQLException{
		final QueryRunner run = createRunner();
		final ScalarHandler<Long> handler = new ScalarHandler<Long>();
		return run.query(COUNT, handler, userId)>0;
	}
	
	public void save(UserLatch userLatch) throws SQLException{
		final QueryRunner run = createRunner();
		final boolean hasLatch = hasLatch(userLatch.getUserId());
		if(hasLatch){
			run.update( UPDATE, userLatch.getAccountId(), userLatch.getCreateDate(), userLatch.getUserId() );
		}else{
			run.update( INSERT, userLatch.getUserId(), userLatch.getAccountId(), userLatch.getCreateDate() );
		}
	}
	
	public UserLatch findByUserId(long userId) throws SQLException{
		final QueryRunner run = createRunner();
		final ResultSetHandler<UserLatch> handler = new BeanHandler<UserLatch>(UserLatch.class);
		
		return run.query( FIND_BY_ID, handler, userId );
	}
	
	private QueryRunner createRunner(){
		final DataSource dataSource = InfrastructureUtil.getDataSource();
		return new QueryRunner(dataSource);
	}
	
}
