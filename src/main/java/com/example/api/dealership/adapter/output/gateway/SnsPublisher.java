package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SnsPublisher {
    private final SnsTemplate snsTemplate;
    private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:sales-notification";

    public void sendSalesNotification(SalesDtoResponse salesResponse) {
        snsTemplate.convertAndSend(TOPIC_ARN,salesResponse);
    }
}