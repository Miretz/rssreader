package com.semerad.rss.schemaexport;

import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

public class MySqlSchemaExport {

	public static void main(final String[] args) {

		final Configuration config = new Configuration();

		final Properties properties = new Properties();

		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/rssreader");
		properties.put("hibernate.connection.username", "root");
		properties.put("hibernate.connection.password", "root");
		properties.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		properties.put("hibernate.show_sql", "true");
		config.setProperties(properties);

		config.addAnnotatedClass(Message.class);
		config.addAnnotatedClass(Feed.class);
		config.addAnnotatedClass(Account.class);

		final SchemaExport schemaExport = new SchemaExport(config);
		schemaExport.setDelimiter(";");

		/** Just dump the schema SQLs to the console , but not execute them ***/
		schemaExport.create(true, false);

	}

}
