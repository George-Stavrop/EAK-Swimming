package gr.georgestaav.accessservice.web.dto;

public record AccessCheckDto(
        boolean allowed,
        String reason
) {}
