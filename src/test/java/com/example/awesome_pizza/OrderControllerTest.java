package com.example.awesome_pizza;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.enumz.PizzaType;
import com.example.awesome_pizza.util.JsonUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String orderCode;

    @BeforeEach
    public void init() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.stringify(OrderDto.builder()
                                .pizzaType(PizzaType.MARGHERITA)
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pizzaType").value("MARGHERITA"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andReturn();

        OrderDto order = JsonUtils.asPojo(mvcResult.getResponse().getContentAsByteArray(), OrderDto.class);

        orderCode = order.getOrderCode();
    }

    @Order(1)
    @Test
    void testGetAllOrders() throws Exception {

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pizzaType").value("MARGHERITA"))
                .andExpect(jsonPath("$[0].status").value("NEW"))
                .andReturn();
    }

    @Order(2)
    @Test
    void testTakeOrder() throws Exception {

        mockMvc.perform(put("/api/orders/" + orderCode + "/take"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Order(3)
    @Test
    void testCompleteOrder() throws Exception {

        testTakeOrder();

        mockMvc.perform(put("/api/orders/" + orderCode + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Order(4)
    @Test
    void testCancelOrder() throws Exception {

        testTakeOrder();

        mockMvc.perform(put("/api/orders/" + orderCode + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
