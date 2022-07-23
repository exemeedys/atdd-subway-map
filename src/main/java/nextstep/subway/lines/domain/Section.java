package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;

import javax.persistence.*;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    protected Section() {

    }

    public Section(Station upStation, Station downStation, int distance) {
        validateUpAndDownStation(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    private static void validateUpAndDownStation(Station upStation, Station downStation) {
        if(upStation.equals(downStation)) {
            throw new IllegalArgumentException("상행과 하행이 같습니다.");
        }
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public boolean correctDownStationFromOtherSectionUpStation(Section section) {
        return isDownStation(section.upStation);
    }

    public boolean correctUpStationFromOtherSectionDownStation(Section section) {
        return isUpStation(section.downStation);
    }

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }
}
