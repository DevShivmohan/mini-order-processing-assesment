package com.order.service.camel;

import com.order.service.model.Order;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqConsumerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("activemq:queue:USER.CREATED.QUEUE")
                .routeId("activemq-consumer-route")
                .unmarshal().json(JsonLibrary.Jackson, Order.class)
                .log("Order processed | OrderId=${body.orderId} | CustomerId=${body.customerId} | Amount=${body.amount}");
    }
}
