package com.fiap.tech.challenge.infrastructure.production.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tech.challenge.application.production.create.DefaultCreateProductionUseCase;
import com.fiap.tech.challenge.application.production.retrieve.get.DefaultGetProductionByIdUseCase;
import com.fiap.tech.challenge.application.production.retrieve.get.ProductionOutput;
import com.fiap.tech.challenge.application.production.retrieve.list.DefaultListProductionUseCase;
import com.fiap.tech.challenge.application.production.retrieve.list.ProductionListOutput;
import com.fiap.tech.challenge.application.production.update.DefaultUpdateProductionStatusUseCase;
import com.fiap.tech.challenge.application.production.update.UpdateProductionStatusOutput;
import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import com.fiap.tech.challenge.infrastructure.ControllerTest;
import com.fiap.tech.challenge.infrastructure.api.ProductionAPI;
import com.fiap.tech.challenge.infrastructure.production.models.UpdateProductionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = ProductionAPI.class)
public class ProductionAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefaultUpdateProductionStatusUseCase updateProductionStatusUseCase;

    @MockBean
    private DefaultGetProductionByIdUseCase getProductionByIdUseCase;

    @MockBean
    private DefaultListProductionUseCase listProductionUseCase;

    @Test
    void givenAValidCommand_whenCallsUpdateProduction_shouldReturnItsIdentifier() throws Exception {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var expectedStatus = ProductionStatus.IN_PREPARATION;

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var expectedId = aProduction.getId();
        final var command = new UpdateProductionRequest(expectedStatus.name());

        when(updateProductionStatusUseCase.execute(any())).thenReturn(UpdateProductionStatusOutput.from(aProduction));

        final var aRequest = put("/productions/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(command));

        final var response = mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(updateProductionStatusUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedId.getValue(), actualCmd.id()) &&
                        Objects.equals(expectedStatus.name(), actualCmd.status())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsGetById_shouldReturnProduction() throws Exception {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var expectedId = aProduction.getId();

        when(getProductionByIdUseCase.execute(any())).thenReturn(ProductionOutput.from(aProduction));

        final var aRequest = get("/productions/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.order_id", equalTo(aProduction.getOrderId())))
                .andExpect(jsonPath("$.status", equalTo(aProduction.getStatus().name())))
                .andExpect(jsonPath("$.received_at", equalTo(aProduction.getReceivedAt().toString())))
                .andExpect(jsonPath("$.items", hasSize(expectedItems.size())))
                .andExpect(jsonPath("$.items[0].product_id", equalTo(expectedItem.productId())))
                .andExpect(jsonPath("$.items[0].product_name", equalTo(expectedItem.productName())))
                .andExpect(jsonPath("$.items[0].quantity", equalTo(expectedItem.quantity())))
        ;

    }

    @Test
    public void givenValidParams_whenCallsListProduction_shouldReturnIt() throws Exception {
        final var aProduction = Production.createProduction(UUID.randomUUID().toString(), List.of(Item.of(UUID.randomUUID().toString(), "productName", 2)));

        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "";
        final var expectedSort = "receivedAt";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ProductionListOutput.from(aProduction));

        when(listProductionUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var aRequest = get("/productions")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                ;

        final var response = this.mvc.perform(aRequest).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aProduction.getId().getValue())))
                .andExpect(jsonPath("$.items[0].order_id", equalTo(aProduction.getOrderId())))
                .andExpect(jsonPath("$.items[0].status", equalTo(aProduction.getStatus().name())))
                .andExpect(jsonPath("$.items[0].received_at", equalTo(aProduction.getReceivedAt().toString())));


        verify(listProductionUseCase).execute(argThat(aQuery ->
                Objects.equals(expectedPage, aQuery.page())
                        && Objects.equals(expectedPerPage, aQuery.perPage())
                        && Objects.equals(expectedSort, aQuery.sort())
                        && Objects.equals(expectedDirection, aQuery.direction())
                        && Objects.equals(expectedTerms, aQuery.terms())
        ));
    }
}
