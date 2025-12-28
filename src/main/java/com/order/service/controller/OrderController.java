package com.order.service.controller;

import com.order.service.annotations.RequiresPreAuthorizePermission;
import com.order.service.dto.AuthReqDto;
import com.order.service.dto.OrderReqDto;
import com.order.service.dto.OrderResDto;
import com.order.service.exception.GenericException;
import com.order.service.model.Role;
import com.order.service.model.User;
import com.order.service.service.AuthenticationService;
import com.order.service.service.OrderService;
import com.order.service.service.UserService;
import com.order.service.util.RequestContext;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @RequiresPreAuthorizePermission({Role.ADMIN, Role.USER})
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderReqDto orderReqDto) {
        if (RequestContext.getUserFromRequestContextHolder().getRole() != Role.ADMIN) {
            orderReqDto.setCustomerId(RequestContext.getUserFromRequestContextHolder().getUserId());
        } else {
            // just for validate id
            userService.getById(orderReqDto.getCustomerId());
        }
        final var order = orderService.create(orderReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResDto(order.getOrderId(), HttpStatus.CREATED.name()));
    }

    @RequiresPreAuthorizePermission({Role.ADMIN, Role.USER})
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getByOrderId(@PathVariable("orderId") String orderId) {
        return RequestContext.getUserFromRequestContextHolder().getRole() != Role.ADMIN ? ResponseEntity.status(HttpStatus.OK).body(orderService.findByIdAndUserId(orderId, RequestContext.getUserFromRequestContextHolder().getUserId())) : ResponseEntity.status(HttpStatus.OK).body(orderService.findById(orderId));
    }


    @RequiresPreAuthorizePermission({Role.ADMIN, Role.USER})
    @GetMapping
    public ResponseEntity<?> getByCustomerId(@RequestParam("customerId") String customerId) {
        if (RequestContext.getUserFromRequestContextHolder().getRole() != Role.ADMIN && !RequestContext.getUserFromRequestContextHolder().getUserId().equals(customerId)) {
            throw new GenericException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findByCustomerId(customerId));
    }

    @PostConstruct
    public void init() {
        userService.createUser(User.builder().role(Role.ADMIN).name("Shiv").username("shiv").password("Shiv@123").build());
        String customerId = userService.createUser(User.builder().role(Role.USER).name("Mohan").username("mohan").password("Shiv@123").build()).getId();
        System.out.println(authenticationService.login(new AuthReqDto("shiv", "Shiv@123")).getAccessToken());
        var order = orderService.create(new OrderReqDto(customerId, "Pen", 586.));
        System.out.println("Created order " + order);
    }
}
