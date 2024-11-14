package io.github.venkat1701.yugaantar.models.sponsors;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sponsors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;
    @Column(name="logo_url")
    private String logoUrl;
    @Column(name="website_url")
    private String websiteUrl;
    @Column(name="description")
    private String description;
    @Column(name="sponsorship_level")
    private String sponsorshipLevel;
    @Column(name="created_at")
    private String createdAt;
    @Column(name="updated_at")
    private String updatedAt;

}
