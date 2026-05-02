package gr.georgestaav.subscriptionservice.service;

import gr.georgestaav.subscriptionservice.constants.SubscriptionConstants;
import gr.georgestaav.subscriptionservice.entity.Subscription;
import gr.georgestaav.subscriptionservice.exception.ResourceNotFoundException;
import gr.georgestaav.subscriptionservice.exception.SubscriptionAlreadyExistsException;
import gr.georgestaav.subscriptionservice.repository.SubscriptionRepository;
import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;
import gr.georgestaav.subscriptionservice.web.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper mapper;

    @Override
    public void createSubscription(String mobileNumber) {
        subscriptionRepository.findByMobileNumber(mobileNumber)
                .ifPresent(subscription ->  {
                    throw new SubscriptionAlreadyExistsException("Subscription already exists with given mobileNumber "+mobileNumber);
                });
        subscriptionRepository.save(createNewSubscription(mobileNumber));

    }

    private Subscription createNewSubscription(String mobileNumber) {
        Subscription subscription = new Subscription();
        long randomSubscriptionNumber = 100000000000L + new Random().nextInt(900000000);
        subscription.setSubscriptionNumber(Long.toString(randomSubscriptionNumber));
        subscription.setMobileNumber(mobileNumber);
        subscription.setSubscriptionType(SubscriptionConstants.GENERIC_SUBSCRIPTION);
        subscription.setAmountPaid(BigDecimal.valueOf(SubscriptionConstants.MONTHLY_FEE));
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        return subscription;
    }

    @Override
    public SubscriptionDto getSubscription(String mobileNumber) {
        Subscription subscription = subscriptionRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> {
                    log.warn("Subscription NOT FOUND for mobileNumber: {}", mobileNumber);
                    return new ResourceNotFoundException("Subscription", "mobileNumber", mobileNumber);
                 }
        );

        return mapper.toDto(subscription);
    }

    @Override
    public boolean renewSubscription(String mobileNumber) {
        Subscription subscription = subscriptionRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Subscription", "mobileNumber", mobileNumber)
        );

        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setAmountPaid(BigDecimal.valueOf(SubscriptionConstants.MONTHLY_FEE));

        subscriptionRepository.save(subscription);
        return true;
    }

    @Override
    public boolean deleteSubscription(String mobileNumber) {
        Subscription subscription = subscriptionRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Subscription", "mobileNumber", mobileNumber)
        );

        subscriptionRepository.deleteById(subscription.getSubscriptionId());
        return  true;
    }
}

