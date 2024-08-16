package com.example.api.dealership.adapter.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Aspect
@RequiredArgsConstructor
@Component
public class CountMetricAspect {

    private final MeterRegistry meterRegistry;

    @Before("@annotation(countMetricAnnotation)")
    public void countMetric(CountMetricAnnotation countMetricAnnotation){
        Counter.builder(countMetricAnnotation.value())
                .description("Metrica API Dealership")
                .tag("day",String.valueOf(LocalDate.now().getDayOfWeek()))
                .register(meterRegistry)
                .increment();
    }


}
