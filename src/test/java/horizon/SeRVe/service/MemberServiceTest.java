package horizon.SeRVe.service;

import horizon.SeRVe.dto.member.UpdateRoleRequest;
import horizon.SeRVe.entity.*;
import horizon.SeRVe.repository.MemberRepository;
import horizon.SeRVe.repository.TeamRepository;
import horizon.SeRVe.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService; // 테스트 대상

    @Mock private MemberRepository memberRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("보안 검증: 소유자(Owner)가 자신의 권한을 MEMBER로 내리려 하면 예외가 발생해야 한다")
    void updateRole_Fail_OwnerCannotDowngrade() {
        // 1. [상황 설정]
        String teamId = "team-1";
        String ownerId = "owner-user"; // 이 사람이 소유자

        // 팀 생성 (소유자 설정: ownerId)
        Team mockTeam = new Team("My Team", "Desc", ownerId);
        mockTeam.setTeamId(teamId);

        // 유저 생성 (Action을 수행하는 사람도 Owner 본인이라고 가정)
        User ownerUser = User.builder().userId(ownerId).build();

        // 멤버 정보 (현재 Owner는 당연히 ADMIN 상태)
        RepositoryMember ownerMember = RepositoryMember.builder()
                .team(mockTeam)
                .user(ownerUser)
                .role(Role.ADMIN)
                .build();

        // 2. [가짜 DB 동작 정의]
        given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(mockTeam));
        given(userRepository.findById(ownerId)).willReturn(Optional.of(ownerUser));

        // (중요) 요청자(Admin) 확인과 대상(Target) 확인에서 모두 ownerMember가 반환됨
        given(memberRepository.findByTeamAndUser(mockTeam, ownerUser)).willReturn(Optional.of(ownerMember));

        // 3. [실행 및 검증] MEMBER로 권한 변경 요청 -> 예외 발생 기대
        UpdateRoleRequest request = new UpdateRoleRequest("MEMBER");

        SecurityException exception = assertThrows(SecurityException.class, () -> {
            // 소유자(ownerId)가 자기 자신(ownerId)의 권한을 변경 시도
            memberService.updateMemberRole(teamId, ownerId, ownerId, request);
        });

        // 4. [메시지 확인] 우리가 작성한 에러 메시지가 맞는지 확인
        assertEquals("저장소 소유자(Owner)는 권한을 변경할 수 없습니다. (항상 ADMIN 유지)", exception.getMessage());
        System.out.println(">> 테스트 통과: 소유자 권한 변경 방어 성공! (" + exception.getMessage() + ")");
    }

    @Test
    @DisplayName("보안 검증: 누군가 소유자(Owner)를 강퇴하려 하면 예외가 발생해야 한다")
    void kickMember_Fail_CannotKickOwner() {
        // 1. [상황 설정]
        String teamId = "team-1";
        String ownerId = "owner-user"; // 강퇴 대상 (소유자)
        String adminId = "another-admin"; // 강퇴를 시도하는 다른 관리자

        Team mockTeam = new Team("My Team", "Desc", ownerId); // 소유자는 ownerId
        mockTeam.setTeamId(teamId);

        // 2. [가짜 DB 동작 정의]
        given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(mockTeam));

        // 3. [실행 및 검증] 강퇴 시도 -> 예외 발생 기대
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            memberService.kickMember(teamId, ownerId, adminId);
        });

        assertEquals("저장소 소유자(Owner)는 강퇴할 수 없습니다.", exception.getMessage());
        System.out.println(">> 테스트 통과: 소유자 강퇴 방어 성공! (" + exception.getMessage() + ")");
    }
}