package com.ta.bank.customer.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.ta.common.bean.Bank;
import com.ta.common.bean.Customer;

public class CustomerAggregationStrategyImpl implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange original, Exchange resource) {
		Object originalBody = original.getIn().getBody();
		Object resourceResponse = resource.getIn().getBody();

		Customer customer = (Customer) originalBody;
		Bank bank = (Bank) resourceResponse;
		customer.setBank(bank);
		return original;
	}

}
