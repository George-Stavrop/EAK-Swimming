package gr.georgestaav.subscriptionservice.service;

import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;

public interface SubscriptionService {

    void createSubscription(String mobileNumber);

    SubscriptionDto getSubscription(String mobileNumber);

    boolean renewSubscription(String mobileNumber);

    boolean deleteSubscription(String mobileNumber);
}
