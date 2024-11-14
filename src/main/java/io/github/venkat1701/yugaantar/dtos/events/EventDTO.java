package io.github.venkat1701.yugaantar.dtos.events;

import lombok.*;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    @NotBlank(message = "Event name must not be blank")
    @Size(max = 255, message = "Event name must not exceed 255 characters")
    private String name;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Start date must not be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date must not be null")
    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDateTime endDate;

    @NotBlank(message = "Location must not be blank")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @NotBlank(message = "Venue name must not be blank")
    @Size(max = 255, message = "Venue name must not exceed 255 characters")
    private String venueName;

    @PositiveOrZero(message = "Ticket price must be zero or positive")
    private int ticketPrice;

    private boolean isFeatured;
    private int maxParticipants;
    private int currentParticipants;


}
