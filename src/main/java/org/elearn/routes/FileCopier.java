/**
 * 
 */
package org.elearn.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author kloudone
 * 
 */
@Component
public class FileCopier extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		/**
		 * Move a file from one dirctory to another directory.
		 */
		from("file:data/inbox?noop=true")
			.routeId("FILE-COPIER")
			.choice()
				.when(simple("${body} contains 'Big Data'"))
					.log("this contains big data content. I am moving the file to bigdata folder ${body}")
					.to("file:data/outbox/bigdata")
				.otherwise()
					.log("No data found against BigData ${body}")
			.endChoice().end()
				.to("file:data/outbox")
				.log("Written to the target directory ${body}")
				.split().tokenize("\n")
			.to("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}");
	}

}
