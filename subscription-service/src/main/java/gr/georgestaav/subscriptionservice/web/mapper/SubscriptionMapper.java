package gr.georgestaav.subscriptionservice.web.mapper;

import gr.georgestaav.subscriptionservice.entity.Subscription;
import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    @Mapping(target = "active", expression = "java(subscription.isActive())")
    SubscriptionDto toDto(Subscription subscription);

    // Subscription toEntity(SubscriptionDto subscriptionDto);

   // void updateEntity(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription);
}
