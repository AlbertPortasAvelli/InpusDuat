package com.inpusduat.inpusduat.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

    @InjectMocks
    private CorrelationIdFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void generatesCorrelationIdWhenHeaderAbsent() throws Exception {
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader(eq("X-Correlation-Id"), argThat(id ->
            id != null && id.length() == 36 // UUID format
        ));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void reusesCorrelationIdFromIncomingHeader() throws Exception {
        String existingId = "my-trace-id-123";
        when(request.getHeader("X-Correlation-Id")).thenReturn(existingId);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader("X-Correlation-Id", existingId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void mdcIsCleanedUpAfterRequest() throws Exception {
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        // MDC must be empty after filter completes — critical for thread pool reuse
        assertThat(MDC.get("correlationId")).isNull();
    }
}