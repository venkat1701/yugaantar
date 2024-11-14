package io.github.venkat1701.yugaantar.dtos.sponsors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SponsorDTO {
    private String name;
    private String logoUrl;
    private String websiteUrl;
    private String description;
    private String sponsorshipLevel;
}
