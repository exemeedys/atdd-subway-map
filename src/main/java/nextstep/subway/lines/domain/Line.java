package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Entity
@Table(name = "line", uniqueConstraints = {
        @UniqueConstraint(name = "line_name_unique", columnNames = {"name"})
})
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections;

    protected Line() {

    }

    private Line(final String name, final String color, final Station upStation, final Station downStation, int distance) {
        this.name = name.trim();
        this.color = color.trim();
        this.sections = new Sections();
        this.sections.addSection(new Section(upStation, downStation, distance));
    }

    public static Line makeLine(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        validateName(name);
        validateColor(color);
        return new Line(name, color, upStation, downStation, distance);
    }

    private static void validateName(final String name) {
        if(!StringUtils.hasLength(name.trim())) {
            throw new IllegalArgumentException("지하철 노선명은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    private static void validateColor(final String color) {
        if(!StringUtils.hasLength(color.trim())) {
            throw new IllegalArgumentException("지하철 노선색은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Sections getSections() {
        return this.sections;
    }

    public void changeName(final String name) {
        if(Objects.nonNull(name)) {
            validateName(name);
            this.name = name.trim();
        }
    }

    public void changeColor(final String color) {
        if(Objects.nonNull(color)) {
            validateColor(color);
            this.color = color.trim();
        }
    }

    public boolean addSection(Station upStation, Station downStation, int distance) {
        return this.sections.addSection(upStation, downStation, distance);
    }

    /**
     * 구간 제거
     * @param station
     */
    public boolean removeSection(Station station) {
        return this.sections.removeSection(station);
    }
}
