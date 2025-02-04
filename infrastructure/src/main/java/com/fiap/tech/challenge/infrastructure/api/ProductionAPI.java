package com.fiap.tech.challenge.infrastructure.api;

import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.infrastructure.production.models.CreateProductionRequest;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionListResponse;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionResponse;
import com.fiap.tech.challenge.infrastructure.production.models.UpdateProductionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequestMapping("productions")
@Tag(name = "Production")
public interface ProductionAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new production")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody CreateProductionRequest aRequest);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a production by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production retrieved"),
            @ApiResponse(responseCode = "404", description = "Production was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })


    ProductionResponse getById(@PathVariable String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a production by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Production updated"),
            @ApiResponse(responseCode = "401", description = "Production was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateProductionRequest aRequest);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "List all productions paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal error was thrown"),
    })
    Pagination<ProductionListResponse> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "sort", required = false, defaultValue = "receivedAt") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir
    );
}
