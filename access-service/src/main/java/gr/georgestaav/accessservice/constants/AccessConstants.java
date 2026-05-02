package gr.georgestaav.accessservice.constants;

public final class AccessConstants {

    private AccessConstants() {
        // restrict instantiation
    }

    public static final String  RFID_CARD = "RFID Card";

    public static final String ACCESS_GRANTED = "ACCESS_GRANTED";
    public static final String CARD_INACTIVE = "CARD_INACTIVE";
    public static final String SUBSCRIPTION_EXPIRED = "SUBSCRIPTION_EXPIRED";
    public static final String CARD_NOT_FOUND = "CARD_NOT_FOUND";
    public static final String SUBSCRIPTION_SERVICE_UNAVAILABLE = "SUBSCRIPTION_SERVICE_UNAVAILABLE";

    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Access Card created successfully";

    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";

    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";


}