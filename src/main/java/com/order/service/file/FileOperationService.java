package com.order.service.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.order.service.model.Order;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Log4j2
public class FileOperationService {
    private static final String directoryName = "./input/orders";
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    static {
        File file = new File(directoryName);
        file.mkdirs();
        System.out.println(file.getAbsolutePath());
    }

    public static void writeIntoFile(Order order) {
        try (OutputStream outputStream = new FileOutputStream(directoryName + "/order-" + order.getOrderId() + ".json")) {
            outputStream.write(objectMapper.writeValueAsBytes(order));
            outputStream.flush();
            log.info("Order {} write success into file", order.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
