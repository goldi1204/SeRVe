package horizon.SeRVe.service;

import horizon.SeRVe.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final TeamRepository teamRepository;

    // 저장소 생성 로직
    @Transactional
    public Long createRepository(String name, String description, String ownerId) {
        // 1. 중복 이름 체크 (선택 사항)
        if (teamRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 저장소 이름입니다.");
        }

        // 2. 저장
        horizon.SeRVe.entity.TeamRepository repo = new horizon.SeRVe.entity.TeamRepository(name, description, ownerId);
        horizon.SeRVe.entity.TeamRepository saved = teamRepository.save(repo);

        return saved.getId();
    }
}