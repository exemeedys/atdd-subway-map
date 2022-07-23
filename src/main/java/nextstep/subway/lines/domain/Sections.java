package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Embeddable
public class Sections implements Iterable<Section> {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    List<Section> sections = new ArrayList<>();

    public boolean addSection(Station upStation, Station downStation, int distance) {

        Section upSection = findUpSection();
        Section downSection = findDownSection();

        if(upSection.isUpStation(downStation) || downSection.isDownStation(upStation)) {
            addSection(new Section(upStation, downStation, distance));
            return true;
        }

        throw new IllegalStateException("");
    }

    protected void addSection(Section section) {
        this.sections.add(section);
    }

    private void throwIfSectionsOne() {
        if(this.sections.size() == 1) {
            throw new IllegalStateException("");
        }
    }

    private Section findFirst() {
        if(!isEmpty()) {
            return this.sections.get(0);
        }

        throw new IllegalStateException("");
    }

    private Section findSectionOnLine(Section firstSection, BiPredicate<Section, Section> findSectionStrategy) {

        // 구간이 비어있을경우 예외를 던짐
        throwIfSectionsEmpty();
        Section findSection = firstSection;

        while(firstSection != null) {
            findSection = firstSection;
            firstSection = correctSection(firstSection, findSectionStrategy).orElse(null);
        }
        return findSection;
    }

    private Optional<Section> correctSection(Section firstSection,BiPredicate<Section, Section> findSectionStrategy) {
        for(Section section : sections) {
            if(findSectionStrategy.test(firstSection, section)) {
                return Optional.ofNullable(section);
            }
        }
        return Optional.empty();
    }

    private Section findUpSection() {
        Section upSection = findFirst();
        return findSectionOnLine(upSection, (findSection, section) -> findSection.correctUpStationFromOtherSectionDownStation(section));
    }

    private Section findDownSection() {
        Section downSection = findFirst();
        return findSectionOnLine(downSection, (findSection, section) -> findSection.correctDownStationFromOtherSectionUpStation(section));
    }

    private void throwIfSectionsEmpty() {
        if(isSectionsEmpty()) {
            throw new IllegalStateException();
        }
    }

    private boolean isSectionsEmpty() {
        return this.sections.isEmpty();
    }

    public boolean removeSection(Station station) {

        Section downSection = findDownSection();
        if(downSection.isDownStation(station)) {
            throwIfSectionsOne();
            return this.sections.remove(downSection);
        }
        return false;
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    @Override
    public Iterator<Section> iterator() {
        return this.sections.iterator();
    }

    public Set<Station> extractStations() {
        Set<Station> stations = new LinkedHashSet<>();

        for(Section section : this.sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return stations;
    }


}
