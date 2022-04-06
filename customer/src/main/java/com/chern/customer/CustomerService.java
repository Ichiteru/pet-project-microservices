package com.chern.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // TODO: 4/6/2022 check if email valid
        // TODO: 4/6/2022 check if email taken
        customerRepository.saveAndFlush(customer);
        // TODO: 4/6/2022 check if fraudster
        FraudCheckResponse fraudCheckResponse = restTemplate
                .getForObject(
                        "http://FRAUD/api/v1/fraud-check/{customerId}",
                        FraudCheckResponse.class,
                        customer.getId());

        if (fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
        // TODO: 4/6/2022 send notification
    }
}
