package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.criteria.Order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(new BigDecimal("12.3"));
        item.setDescription("item1 description");

        Cart cart = new Cart();
        cart.addItem(item);
        User user = new User();
        user.setUsername("test");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void order_history() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(new BigDecimal("12.3"));
        item.setDescription("item1 description");

        Cart cart = new Cart();
        cart.addItem(item);
        User user = new User();
        user.setUsername("test");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
