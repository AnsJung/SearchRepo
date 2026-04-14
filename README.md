# 🔍 GitHub Repository Search App

**Jetpack Compose**와 **Paging 3**를 활용하여 GitHub 저장소를 검색하고 상세 정보를 확인할 수 있는 안드로이드 애플리케이션입니다. 

---
### Tech Stack

* **Language & UI**: Kotlin, Jetpack Compose (Material 3)
* **Architecture**: Android App Architecture (MVVM)
* **Async & Network**: Coroutines, Flow, Retrofit2, OkHttp3
* **Paging & DI**: Paging 3, Hilt
  
---
### 효율성을 고려한 설계

* 검색 최적화 
  * `debounce`와 `flatMapLatest` 연산자를 결합하여 불필요한 네트워크 리소스 낭비를 방지하고 검색 반응성을 높였습니다.
* 무한 스크롤 
  * Paging 3 라이브러리를 통해 대량의 데이터를 메모리 효율적으로 관리하며, `cachedIn`을 적용하여 구성 변경 시에도 데이터를 유지합니다.
* 다크 모드 지원 
  * Material 3 기반의 테마 시스템을 적용하여 시스템 설정에 따른 유연한 모드 전환을 지원합니다.
* 실용적인 아키텍처 (Android App Architecture)
  * 프로젝트 규모에 맞춰 불필요한 도메인 레이어의 복잡성을 제거하고, Data와 UI 레이어 간의 직관적인 데이터 흐름을 설계했습니다.

###  Project Structure
```text
├── data (API, Repository, Paging3, Model/DTO)
├── ui (ViewModel, Compose Screens, Components)
└── di (Hilt Modules)
