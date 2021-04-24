package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setPrice(new BigDecimal("12.3"));
        item1.setDescription("item1 description");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setPrice(new BigDecimal("14.6"));
        item2.setDescription("item2 description");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> fetchedItems = response.getBody();
        assertEquals(2, fetchedItems.size());
        assertEquals("item1", fetchedItems.get(0).getName());
        assertEquals(new BigDecimal("12.3"), fetchedItems.get(0).getPrice());
        assertEquals("item2", fetchedItems.get(1).getName());
        assertEquals(new BigDecimal("14.6"), fetchedItems.get(1).getPrice());
    }

    @Test
    public void get_item_by_id() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(new BigDecimal("12.3"));
        item.setDescription("item1 description");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item fetchedItem = response.getBody();
        assertEquals("item1", fetchedItem.getName());
        assertEquals(new BigDecimal("12.3"), fetchedItem.getPrice());
    }

    @Test
    public void get_item_by_id_do_not_exist() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_name() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item");
        item1.setPrice(new BigDecimal("12.3"));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item");
        item2.setPrice(new BigDecimal("14.6"));

        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> fetchedItems = response.getBody();
        assertEquals(2, fetchedItems.size());
        assertEquals("item", fetchedItems.get(0).getName());
        assertEquals(new BigDecimal("12.3"), fetchedItems.get(0).getPrice());
        assertEquals("item", fetchedItems.get(1).getName());
        assertEquals(new BigDecimal("14.6"), fetchedItems.get(1).getPrice());
    }

}
