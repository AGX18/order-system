package io.agx.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderProducerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderTestConsumer orderTestConsumer;

    @Test
    void whenOrderPlaced_thenMessagePublishedToKafka() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"product": "Laptop", "quantity": 3}
                        """))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(orderTestConsumer.getLastOrder()).isNotNull()
        );

        assertThat(orderTestConsumer.getLastOrder().getProduct()).isEqualTo("Laptop");
        assertThat(orderTestConsumer.getLastOrder().getQuantity()).isEqualTo(3);
    }
}