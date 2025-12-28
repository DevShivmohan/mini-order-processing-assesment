package com.order.service.camel;

import com.order.service.model.Order;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FileToActiveMqRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class)
                .logStackTrace(true)
                .handled(false)
                .log("File processing failed: ${file:name}");
        from("file:./input/orders?readLock=changed&delete=true")
                .routeId("file-to-activemq-route")
                .log("Reading file: ${file:name}")
                .unmarshal().json(JsonLibrary.Jackson, Order.class)
                .validate((Expression) simple("${body.orderId} != null"))
                .validate((Expression) simple("${body.customerId} != null"))
                .validate((Expression) simple("${body.amount} > 0"))
                .marshal().json(JsonLibrary.Jackson)
                .to("activemq:queue:USER.CREATED.QUEUE")
                .log("Sent to ActiveMQ | USER.CREATED.QUEUE");
    }
}
