package com.ta.bank.customer.config;

import org.apache.camel.CamelContext;
import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.ta.common.bean.Customer;

@Component
public class CamelPushingData extends RouteBuilder {

	@Autowired
	CamelContext context;

	@Autowired
	DiscoveryClient discoveryClient;
	
	private static Customer currentCustomer = new Customer();

	@DynamicRouter
	@Override
	public void configure() throws Exception {
		JacksonDataFormat customerFormant = new JacksonDataFormat(Customer.class);
		context.setTracing(true);

		restConfiguration().component("netty4-http").host("localhost").port(8000);

		/* POST Calls Start from here */
		rest("/api").consumes("application/json").post("/addcustomer").type(Customer.class)
		.param().name("customerId").type(RestParamType.path).defaultValue("${body.id}").endParam()
		.to("direct:addcustomer");
		
		from("direct:addcustomer").doTry()
				.to("netty4-http:http://localhost:8282/customer/add?bridgeEndpoint=true")
				.log("Customer details added Successfully ${body}").unmarshal(customerFormant).process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						try {
							currentCustomer = exchange.getIn().getBody(Customer.class);
							context.addRoutes(new DynamicRouteBuilder(context, "direct:cancelCustomer","netty4-http:http://localhost:8282/customer/deletecust/"+currentCustomer.getId()));
						}catch (Exception e) {
							e.printStackTrace();
							throw new NullPointerException();
						}
					}
				}).to("direct:addbank").doCatch(Exception.class).endDoTry();
		
		from("direct:addbank").setBody().constant(null).doTry().to("netty4-http:http://localhost:8181/bank/add")
				.log("Bank info added Successfully ${body}").doCatch(Exception.class).to("direct:cancelCustomer")
				.endDoTry();
	}
}
