# SeRVe

간단 설명

SeRVe는 Spring Boot + Gradle 기반의 Java 웹 애플리케이션입니다.
- Java 17, Gradle Wrapper(`gradlew.bat`) 사용

---

## 빠른 시작

### 사전 준비
- JDK 17 설치
- (선택) Git, GitHub CLI(`gh`) 설치

### 로컬 빌드 및 실행 (PowerShell)
```powershell
cd C:\Users\user\Desktop\SeRVe\SeRVe
.\gradlew.bat clean build
.\gradlew.bat bootRun
```

### 테스트
```powershell
.\gradlew.bat test
```

---

## 프로젝트 구조 (주요 패키지와 역할)

아래는 주요 폴더와 파일의 계층 구조를 보기 쉽게 정리한 목록입니다. DTO는 설계상 포함되어 있으며, 실제 클래스들은 추후 구현될 예정입니다.

- config
  - Security 설정 (JWT), Swagger, CORS 등
- controller  (API 진입점)
  - `AuthController.java`
  - `RepoController.java`        — `/repositories` 관련 (생성, 조회, 이름 변경)
  - `MemberController.java`      — `/repositories/{id}/members` 관련 (초대, 강퇴)
  - `DocumentController.java`    — `/documents` 관련
- service     (비즈니스 로직, 트랜잭션 관리)
  - `AuthService.java`
  - `RepoService.java`           — 저장소 생성 시 Owner를 Member로 등록하는 로직 포함
  - `MemberService.java`         — 초대 시 암호화된 키 처리 로직
  - `DocumentService.java`
- repository  (DB 접근 계층 - Spring Data JPA)
  - `UserRepository.java`
  - `TeamRepoRepository.java`    — 도메인 Repository 엔티티 관리
  - `MemberRepository.java`
  - `DocumentRepository.java`
  - `EncryptedDataRepository.java`
- entity      (DB 테이블 매핑)
  - `User.java`
  - `TeamRepository.java`        — (테이블명: `repositories`)  *중요*
  - `RepositoryMember.java`      — (테이블: `repository_members`, 복합키)
  - `RepositoryMemberId.java`
  - `Document.java`
  - `EncryptedData.java`
- dto         (데이터 전송 객체) — 추후 구현 예정
  - `dto.auth/`
  - `dto.repo/`    — `CreateRepoRequest`, `RepoResponse` 등
  - `dto.member/`  — `InviteMemberRequest` 등
  - `dto.document/`— Document 관련 DTO들

---

## DTO 관련 안내
- `dto` 패키지는 설계상 포함되어 있으며, 실제 DTO 클래스들은 추후 구현됩니다.
- 예시 DTO 목록 (추후 추가 예정):
  - `dto.repo.CreateRepoRequest`, `dto.repo.RepoResponse`
  - `dto.member.InviteMemberRequest`
  - `dto.auth.*` (로그인/회원가입 관련)
  - `dto.document.*` (문서 생성/조회 관련)

---

## 민감정보 및 로컬 설정
- `src/main/resources/application.properties` 또는 `application.yml`에 DB 비밀번호나 API 키 같은 민감정보를 직접 커밋하지 마세요.
- 로컬 설정은 `.gitignore`에 추가되어 있으며, CI/배포 시에는 GitHub Secrets나 환경변수를 사용하세요.

---

## 추가 문서
- 개발 규칙, 도움말 및 스크립트는 `HELP.md` 또는 `CONTRIBUTING.md` (있다면)를 참고하세요.

## 라이선스
- 저장소 루트의 `LICENSE` 파일을 따릅니다.

---

문의
- 프로젝트 관련 문의는 저장소의 이슈 트래커를 이용해주세요.
