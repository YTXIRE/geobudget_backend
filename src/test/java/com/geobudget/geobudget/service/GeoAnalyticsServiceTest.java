package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsDetailResponse;
import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsResponse;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoAnalyticsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GeoAnalyticsService geoAnalyticsService;

    @Test
    void getCities_groupsByCityAndAggregatesTotals() {
        Category food = Category.builder().id(1L).name("Еда").transactionType("expense").build();
        Category salary = Category.builder().id(2L).name("Зарплата").transactionType("income").build();

        Transaction warsawExpense = Transaction.builder()
                .id(1L)
                .userId(7L)
                .type("expense")
                .amount(new BigDecimal("150.00"))
                .baseAmount(new BigDecimal("150.00"))
                .category(food)
                .city("Warsaw")
                .country("Poland")
                .latitude(new BigDecimal("52.2297000"))
                .longitude(new BigDecimal("21.0122000"))
                .occurredAt(LocalDateTime.now().minusDays(1))
                .isDeleted(false)
                .build();

        Transaction warsawIncome = Transaction.builder()
                .id(2L)
                .userId(7L)
                .type("income")
                .amount(new BigDecimal("1000.00"))
                .baseAmount(new BigDecimal("1000.00"))
                .category(salary)
                .city("Warsaw")
                .country("Poland")
                .latitude(new BigDecimal("52.2301000"))
                .longitude(new BigDecimal("21.0119000"))
                .occurredAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(warsawExpense, warsawIncome));

        List<GeoCityAnalyticsResponse> cities = geoAnalyticsService.getCities(7L, null, null, null);

        assertEquals(1, cities.size());
        assertEquals("Warsaw, Poland", cities.get(0).label());
        assertEquals(new BigDecimal("1000.00"), cities.get(0).incomeTotal());
        assertEquals(new BigDecimal("150.00"), cities.get(0).expenseTotal());
        assertEquals(new BigDecimal("850.00"), cities.get(0).balance());
        assertNotNull(cities.get(0).latitude());
        assertNotNull(cities.get(0).longitude());
    }

    @Test
    void getCityDetail_buildsCategoryBreakdown() {
        Category food = Category.builder().id(1L).name("Еда").transactionType("expense").build();
        Category transport = Category.builder().id(2L).name("Транспорт").transactionType("expense").build();

        Transaction foodExpense = Transaction.builder()
                .id(1L)
                .userId(7L)
                .type("expense")
                .amount(new BigDecimal("50.00"))
                .baseAmount(new BigDecimal("50.00"))
                .category(food)
                .city("Warsaw")
                .country("Poland")
                .latitude(new BigDecimal("52.2297000"))
                .longitude(new BigDecimal("21.0122000"))
                .occurredAt(LocalDateTime.now().minusDays(1))
                .isDeleted(false)
                .build();

        Transaction transportExpense = Transaction.builder()
                .id(2L)
                .userId(7L)
                .type("expense")
                .amount(new BigDecimal("20.00"))
                .baseAmount(new BigDecimal("20.00"))
                .category(transport)
                .city("Warsaw")
                .country("Poland")
                .latitude(new BigDecimal("52.2301000"))
                .longitude(new BigDecimal("21.0119000"))
                .occurredAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(foodExpense, transportExpense));

        GeoCityAnalyticsDetailResponse detail = geoAnalyticsService.getCityDetail(
                7L,
                "place::poland::warsaw",
                null,
                null,
                null
        );

        assertEquals("Warsaw, Poland", detail.label());
        assertEquals(2, detail.categories().size());
        assertEquals("Еда", detail.categories().get(0).categoryName());
        assertEquals(2, detail.transactions().size());
    }
}
