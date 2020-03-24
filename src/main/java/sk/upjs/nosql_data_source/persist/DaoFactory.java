package sk.upjs.nosql_data_source.persist;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {
	INSTANCE;
	
	private JdbcTemplate jdbcTemplate;
	private StudentDao studentDao;
	
	public synchronized JdbcTemplate getJDBCTemplateLocal() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setDatabaseName("ais-like");
			dataSource.setUser("ais-like-user");
			dataSource.setPassword("iceIceBaby");
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

	public synchronized JdbcTemplate getJDBCTemplateServer() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName("nosql.gursky.sk");
			dataSource.setDatabaseName("ais-like");
			dataSource.setUser("student");
			dataSource.setPassword("nosql");
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

	
	public synchronized StudentDao getStudentDao() {
		if (studentDao == null) {
			studentDao = new MysqlStudentDao(getJDBCTemplateServer());
		}
		return studentDao;
	}
}
