package gr.georgestaav.accessservice.service;

import gr.georgestaav.accessservice.constants.AccessConstants;
import gr.georgestaav.accessservice.entity.AccessCard;
import gr.georgestaav.accessservice.exception.AccessCardAlreadyExistsException;
import gr.georgestaav.accessservice.exception.ResourceNotFoundException;
import gr.georgestaav.accessservice.repository.AccessCardRepository;
import gr.georgestaav.accessservice.service.client.SubscriptionFeignClient;
import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import gr.georgestaav.accessservice.web.dto.AccessCheckDto;
import gr.georgestaav.accessservice.web.dto.SubscriptionDto;
import gr.georgestaav.accessservice.web.mapper.AccessCardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessCardServiceImpl implements AccessCardService {

    private final AccessCardRepository accessCardRepository;
    private final AccessCardMapper mapper;
    private final SubscriptionFeignClient subscriptionFeignClient;

    @Override
    public void createAccessCard(String mobileNumber) {
        accessCardRepository.findByMobileNumber(mobileNumber)
                .ifPresent(accessCard ->  {
                    throw new AccessCardAlreadyExistsException("AccessCard already exists with given mobileNumber "+mobileNumber);
                });
        accessCardRepository.save(createNewAccessCard(mobileNumber));
    }

    private AccessCard createNewAccessCard(String mobileNumber) {
        AccessCard accessCard = new AccessCard();
        long randomAccessCardNumber = 100000000000L + new Random().nextInt(900000000);
        accessCard.setAccessCardNumber(Long.toString(randomAccessCardNumber));
        accessCard.setMobileNumber(mobileNumber);
        accessCard.setAccessCardType(AccessConstants.RFID_CARD);
        accessCard.setIsActive(true);
        return accessCard;
    }

    @Override
    public AccessCardDto getAccessCard(String mobileNumber) {
        AccessCard accessCard = accessCardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> {
                    log.warn("Access Card NOT FOUND for mobileNumber: {}", mobileNumber);
                    return new ResourceNotFoundException("AccessCard", "mobileNumber", mobileNumber);
                }
        );

        return mapper.toDto(accessCard);
    }

    @Override
    public AccessCheckDto canEnter(String accessCardNumber) {

        // Does Card exist?
        AccessCard accessCard = accessCardRepository.findByAccessCardNumber(accessCardNumber)
                .orElse(null);

        if (accessCard == null) {
            return new AccessCheckDto(false, AccessConstants.CARD_NOT_FOUND);
        }

        // Is Card active?
        if (!accessCard.getIsActive()) {
            return new AccessCheckDto(false, AccessConstants.CARD_INACTIVE);
        }

        // Does card have active subscription?
        log.info("Fetching subscription for: {}", accessCardNumber);
        SubscriptionDto subscription = subscriptionFeignClient
                .getSubscription(accessCard.getMobileNumber());
        log.info("Subscription fetched for: {}", accessCardNumber);

        if (subscription == null) {
            return new AccessCheckDto(false, AccessConstants.SUBSCRIPTION_SERVICE_UNAVAILABLE);
        }

        if (!subscription.isActive()) {
            return new AccessCheckDto(false, AccessConstants.SUBSCRIPTION_EXPIRED);
        }

        return new AccessCheckDto(true, AccessConstants.ACCESS_GRANTED);
    }


    @Override
    public boolean toggleAccessCard(String mobileNumber) {
        AccessCard accessCard = accessCardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AccessCard", "mobileNumber", mobileNumber));

        accessCard.setIsActive(!accessCard.getIsActive()); // toggle
        accessCardRepository.save(accessCard);
        return true;
    }

    @Override
    public boolean deleteAccessCard(String mobileNumber) {
        AccessCard accessCard = accessCardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Subscription", "mobileNumber", mobileNumber)
        );

        accessCardRepository.deleteById(accessCard.getAccessCardId());
        return  true;
    }

}
