package gr.georgestaav.accessservice.service;

import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import gr.georgestaav.accessservice.web.dto.AccessCheckDto;

public interface AccessCardService {

    void createAccessCard(String mobileNumber);

    AccessCardDto getAccessCard(String mobileNumber);

    boolean toggleAccessCard(String mobileNumber);

    AccessCheckDto canEnter(String accessCardNumber);

    boolean deleteAccessCard(String mobileNumber);
}
