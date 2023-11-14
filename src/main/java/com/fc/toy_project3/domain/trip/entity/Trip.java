package com.fc.toy_project3.domain.trip.entity;

import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.global.common.BaseTimeEntity;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * Trip Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Trip extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_Id")
    @ManyToOne
    private Member member;

    @Column(length = 100)
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Comment("국내 = 1, 국외 = 0")
    private Boolean isDomestic;

    private Long likeCount;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Itinerary> itineraries = new ArrayList<>();

    @Builder
    public Trip(Long id, Member member, String name, LocalDate startDate, LocalDate endDate, Boolean isDomestic,
        Long likeCount, List<Itinerary> itineraries) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.likeCount = likeCount;
        this.itineraries = itineraries;
    }

    public void updateTrip(UpdateTripRequestDTO updateTripRequestDTO) {
        if (updateTripRequestDTO.getTripName() != null) {
            this.name = updateTripRequestDTO.getTripName();
        }
        if (updateTripRequestDTO.getStartDate() != null) {
            this.startDate = DateTypeFormatterUtil.dateFormatter(
                updateTripRequestDTO.getStartDate());
        }
        if (updateTripRequestDTO.getEndDate() != null) {
            this.endDate = DateTypeFormatterUtil.dateFormatter(
                updateTripRequestDTO.getEndDate());
        }
        if (updateTripRequestDTO.getIsDomestic() != null) {
            this.isDomestic = updateTripRequestDTO.getIsDomestic();
        }
    }

    public void updateLikeCount(boolean isIncrease) {
        if (isIncrease) {
            this.likeCount++;
        } else {
            this.likeCount--;
        }
    }

    @Override
    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }
}
