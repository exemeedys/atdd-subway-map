package nextstep.subway.stations.applicaion;

import nextstep.subway.cmmn.exception.EntityNotExistException;
import nextstep.subway.stations.applicaion.dto.StationRequest;
import nextstep.subway.stations.applicaion.dto.StationResponse;
import nextstep.subway.stations.domain.Station;
import nextstep.subway.stations.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    public Station findById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotExistException("지하철역이 존재하지 않습니다. id = " + stationId));
    }
}