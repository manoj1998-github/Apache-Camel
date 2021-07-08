package com.camel.microservices.a.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;

	@Autowired
	private SimpleLoginProcessorComponent loggingComponent;

	@Override
	public void configure() throws Exception {
		// timer,transformation,log
		// Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]

		from("timer:first-timer") // instead of queue, null
				.log("${body}")// null
				.transform().constant("My constant message").log("${body}")// My constant message
				// .bean("GetCurrentTimeBean")
				.bean(getCurrentTimeBean).log("${body}").bean(loggingComponent)// times now
				.log("${body}").process(new SimpleLogginProcessor()).to("log:first-timer"); // instead of db
	}

}

@Component
class GetCurrentTimeBean {

	public String getCurrentTime() {
		return "Time now is " + LocalDateTime.now();
	}
}

@Component
class SimpleLoginProcessorComponent {

	private Logger logger = LoggerFactory.getLogger(SimpleLoginProcessorComponent.class);
 
	public void process(String message) {
		logger.info("SimpleLoginProcessorComponent {}", message);
	}
}

class SimpleLogginProcessor implements Processor {

	private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("SimpleLogginProcessor {}", exchange.getMessage().getBody());
	}

}