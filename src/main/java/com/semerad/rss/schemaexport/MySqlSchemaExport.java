package com.semerad.rss.schemaexport;

import java.util.EnumSet;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

/*
 * Use this class to generate DB Schema
 */

public class MySqlSchemaExport {

	private MySqlSchemaExport() {
		// Empty
	}

	public static void main(final String[] args) {

		final MetadataSources metadata = new MetadataSources(new StandardServiceRegistryBuilder()
				.applySetting("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect").build());

		metadata.addAnnotatedClass(Message.class);
		metadata.addAnnotatedClass(Feed.class);
		metadata.addAnnotatedClass(Account.class);

		new SchemaExport().setOutputFile("schema.sql").create(EnumSet.of(TargetType.SCRIPT), metadata.buildMetadata());

	}

}
