package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionSummaryResponse;
import com.geobudget.geobudget.dto.transaction.TransactionUpdateRequest;
import com.geobudget.geobudget.dto.geoCompany.CountryAndCity;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.dto.fx.FxRateResponse;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.TransactionSummaryProjection;
import com.geobudget.geobudget.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GeoIpService geoIpService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FxRateService fxRateService;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionCreateRequest incomeRequest;

    @BeforeEach
    void setUp() {
        incomeRequest = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("15000.00"))
                .categoryId(2L)
                .description("Salary")
                .occurredAt(LocalDateTime.now())
                .build();

        lenient().when(userRepository.findById(7L)).thenReturn(Optional.of(
                User.builder().id(7L).baseCurrency("RUB").build()
        ));
    }

    @Test
    void createIncome_success() {
        Transaction saved = Transaction.builder()
                .id(1L)
                .userId(7L)
                .type("income")
                .amount(new BigDecimal("15000.00"))
                .category(Category.builder().id(2L).transactionType("income").type("system").build())
                .description("Salary")
                .occurredAt(incomeRequest.getOccurredAt())
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        var response = transactionService.create(7L, incomeRequest);

        assertEquals(1L, response.getId());
        assertEquals("income", response.getType());
        assertEquals(new BigDecimal("15000.00"), response.getAmount());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void createWithoutCategory_failsValidation() {
        TransactionCreateRequest expenseRequest = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .occurredAt(LocalDateTime.now())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, expenseRequest));

        assertEquals("categoryId is required", ex.getMessage());
    }

    @Test
    void getAll_filtersByTypeAndDates() {
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now();

        when(transactionRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));

        transactionService.getAll(7L, "income", from, to, 2L, null, null, PageRequest.of(0, 20));

        verify(transactionRepository).findAll(any(Specification.class), eq(PageRequest.of(0, 20)));
    }

    @Test
    void update_foreignCategoryIsNotAccessible() {
        Transaction transaction = Transaction.builder().id(11L).userId(7L).type("income").amount(BigDecimal.ONE).occurredAt(LocalDateTime.now()).build();
        Category foreignCategory = Category.builder().id(5L).type("user").userId(99L).build();

        TransactionUpdateRequest updateRequest = TransactionUpdateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("10.00"))
                .categoryId(5L)
                .description("Coffee")
                .occurredAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findByIdAndUserIdAndIsDeletedFalse(11L, 7L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(foreignCategory));

        assertThrows(EntityNotFoundException.class, () -> transactionService.update(7L, 11L, updateRequest));
    }

    @Test
    void create_whenTypeMismatchWithCategory_failsValidation() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("expense").type("system").build()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, incomeRequest));

        assertEquals("Transaction type does not match category type", ex.getMessage());
    }

    @Test
    void create_whenOnlyLatitudeProvided_failsValidation() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("5000.00"))
                .categoryId(2L)
                .occurredAt(LocalDateTime.now())
                .latitude(new BigDecimal("55.75"))
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(7L, request));

        assertEquals("latitude and longitude must be provided together", ex.getMessage());
    }

    @Test
    void create_whenLocationMissing_enrichesFromIp() {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr("127.0.0.1");

        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("5000.00"))
                .categoryId(2L)
                .occurredAt(LocalDateTime.now())
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));
        when(geoIpService.getCityByExternalIp(httpRequest)).thenReturn(new CountryAndCity("Poland", "Warsaw", 52.2297, 21.0122));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.create(7L, request, httpRequest);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Warsaw", saved.getCity());
        assertEquals("Poland", saved.getCountry());
        assertEquals(new BigDecimal("52.2297"), saved.getLatitude());
        assertEquals(new BigDecimal("21.0122"), saved.getLongitude());
        assertEquals("ip", saved.getLocationSource());
    }

    @Test
    void create_whenLocationProvided_doesNotOverrideWithIp() {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr("127.0.0.1");

        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("income")
                .amount(new BigDecimal("5000.00"))
                .categoryId(2L)
                .occurredAt(LocalDateTime.now())
                .city("Minsk")
                .country("Belarus")
                .locationSource("manual")
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).transactionType("income").type("system").build()));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.create(7L, request, httpRequest);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("Minsk", saved.getCity());
        assertEquals("Belarus", saved.getCountry());
        assertEquals("manual", saved.getLocationSource());
    }

    @Test
    void create_whenCurrencyDiffersFromUserBase_calculatesBaseAmount() {
        LocalDateTime occurredAt = LocalDateTime.of(2026, 4, 8, 10, 0);
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .categoryId(5L)
                .occurredAt(occurredAt)
                .originalCurrency("USD")
                .build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(
                User.builder().id(7L).baseCurrency("EUR").build()
        ));
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(
                Category.builder().id(5L).transactionType("expense").type("system").build()
        ));
        when(fxRateService.getRate("USD", "EUR", LocalDate.of(2026, 4, 8)))
                .thenReturn(new FxRateResponse("USD", "EUR", LocalDate.of(2026, 4, 8), new BigDecimal("0.920000"), "frankfurter", false));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.create(7L, request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals("USD", saved.getOriginalCurrency());
        assertEquals(new BigDecimal("0.920000"), saved.getRateToBase());
        assertEquals(new BigDecimal("92.00"), saved.getBaseAmount());
    }

    @Test
    void create_whenCurrencyMatchesUserBase_usesUnitRate() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("250.00"))
                .categoryId(5L)
                .occurredAt(LocalDateTime.now())
                .originalCurrency("RUB")
                .build();

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(
                Category.builder().id(5L).transactionType("expense").type("system").build()
        ));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.create(7L, request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertEquals(new BigDecimal("1.000000"), saved.getRateToBase());
        assertEquals(new BigDecimal("250.00"), saved.getBaseAmount());
    }

    @Test
    void create_whenFxUnavailable_usesFallbackClientRate() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .categoryId(5L)
                .occurredAt(LocalDateTime.of(2026, 4, 8, 10, 0))
                .originalCurrency("USD")
                .rateToBase(new BigDecimal("80.500000"))
                .baseAmount(new BigDecimal("8050.00"))
                .build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(
                User.builder().id(7L).baseCurrency("RUB").build()
        ));
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(
                Category.builder().id(5L).transactionType("expense").type("system").build()
        ));
        when(fxRateService.getRate("USD", "RUB", LocalDate.of(2026, 4, 8)))
                .thenThrow(new IllegalStateException("FX unavailable"));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.create(7L, request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertEquals(new BigDecimal("80.500000"), saved.getRateToBase());
        assertEquals(new BigDecimal("8050.00"), saved.getBaseAmount());
    }

    @Test
    void create_afterProfileCurrencyChanged_usesNewBaseCurrencyOnlyForNewTransaction() {
        TransactionCreateRequest oldRequest = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .categoryId(5L)
                .occurredAt(LocalDateTime.of(2026, 4, 8, 10, 0))
                .originalCurrency("USD")
                .build();

        TransactionCreateRequest newRequest = TransactionCreateRequest.builder()
                .type("expense")
                .amount(new BigDecimal("100.00"))
                .categoryId(5L)
                .occurredAt(LocalDateTime.of(2026, 4, 9, 10, 0))
                .originalCurrency("USD")
                .build();

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(
                Category.builder().id(5L).transactionType("expense").type("system").build()
        ));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(7L))
                .thenReturn(Optional.of(User.builder().id(7L).baseCurrency("RUB").build()))
                .thenReturn(Optional.of(User.builder().id(7L).baseCurrency("EUR").build()));
        when(fxRateService.getRate("USD", "RUB", LocalDate.of(2026, 4, 8)))
                .thenReturn(new FxRateResponse("USD", "RUB", LocalDate.of(2026, 4, 8), new BigDecimal("80.000000"), "frankfurter", false));
        when(fxRateService.getRate("USD", "EUR", LocalDate.of(2026, 4, 9)))
                .thenReturn(new FxRateResponse("USD", "EUR", LocalDate.of(2026, 4, 9), new BigDecimal("0.910000"), "frankfurter", false));

        transactionService.create(7L, oldRequest);
        transactionService.create(7L, newRequest);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, org.mockito.Mockito.times(2)).save(captor.capture());

        List<Transaction> saved = captor.getAllValues();
        assertEquals("USD", saved.get(0).getOriginalCurrency());
        assertEquals("USD", saved.get(1).getOriginalCurrency());
        assertEquals(new BigDecimal("80.000000"), saved.get(0).getRateToBase());
        assertEquals(new BigDecimal("0.910000"), saved.get(1).getRateToBase());
        assertEquals(new BigDecimal("8000.00"), saved.get(0).getBaseAmount());
        assertEquals(new BigDecimal("91.00"), saved.get(1).getBaseAmount());
    }

    @Test
    void softDelete_setsIsDeletedTrue() {
        Transaction transaction = Transaction.builder().id(11L).userId(7L).type("income").amount(BigDecimal.ONE).occurredAt(LocalDateTime.now()).isDeleted(false).build();
        when(transactionRepository.findByIdAndUserIdAndIsDeletedFalse(11L, 7L)).thenReturn(Optional.of(transaction));

        transactionService.softDelete(7L, 11L);

        assertEquals(true, transaction.getIsDeleted());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void summary_returnsBalance() {
        TransactionSummaryProjection projection = new TransactionSummaryProjection() {
            @Override
            public BigDecimal getIncome() {
                return new BigDecimal("250000.00");
            }

            @Override
            public BigDecimal getExpense() {
                return new BigDecimal("187540.50");
            }
        };

        when(transactionRepository.getSummary(eq(7L), any(), any())).thenReturn(projection);

        TransactionSummaryResponse response = transactionService.getSummary(7L, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(new BigDecimal("250000.00"), response.getIncome());
        assertEquals(new BigDecimal("187540.50"), response.getExpense());
        assertEquals(new BigDecimal("62459.50"), response.getBalance());
    }
}
