package com.semerad.rss.schemaexport;

import java.util.EnumSet;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;
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

		final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySetting(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect").build();

		final MetadataSources metadata = new MetadataSources(serviceRegistry);

		metadata.addAnnotatedClass(Message.class);
		metadata.addAnnotatedClass(Feed.class);
		metadata.addAnnotatedClass(Account.class);

		final SchemaExport schema = new SchemaExport();
		schema.setOutputFile("schema.sql");
		schema.setDelimiter(";");
		schema.setFormat(true);
		schema.create(EnumSet.of(TargetType.SCRIPT), metadata.buildMetadata());

	}

}
