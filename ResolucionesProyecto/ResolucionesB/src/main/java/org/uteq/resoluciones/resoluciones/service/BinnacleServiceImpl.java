package org.uteq.resoluciones.resoluciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.BinnacleResponse;
import org.uteq.resoluciones.resoluciones.entities.Binnacle;
import org.uteq.resoluciones.resoluciones.repository.BinnacleRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinnacleServiceImpl implements BinnacleService {

    private final BinnacleRepository binnacleRepo;

    @Override
    public Page<BinnacleResponse> list(Long resolutionId, Long userId, Pageable pageable) {
        Page<Binnacle> page;
        if (resolutionId != null) {
            page = binnacleRepo.findByResolution3IdOrderByDateDesc(resolutionId, pageable);
        } else if (userId != null) {
            page = binnacleRepo.findByUser2IdOrderByDateDesc(userId, pageable);
        } else {
            page = binnacleRepo.findAllByOrderByDateDesc(pageable);
        }
        return page.map(this::toDto);
    }

    private BinnacleResponse toDto(Binnacle b) {
        return BinnacleResponse.builder()
                .id(b.getId())
                .userId(b.getUser2() != null ? b.getUser2().getId() : null)
                .userName(b.getUser2() != null ? b.getUser2().getUser() : null)
                .resolutionId(b.getResolution3() != null ? b.getResolution3().getId() : null)
                .resolutionNumber(b.getResolution3() != null ? b.getResolution3().getResolutionNumber() : null)
                .action(b.getAction())
                .date(b.getDate())
                .ip(b.getIp())
                .userAgent(b.getUserAgent())
                .build();
    }
}
