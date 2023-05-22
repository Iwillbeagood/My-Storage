
<h1 align="center">My Storage</h1>
  <br>

<p align="center">
  <img src="https://github.com/Iwillbeagood/My-Storage/assets/106158445/a4559d4b-063f-47c0-8d62-70f6494b9d0a" alt="My Image">
</p>
<br>

>  My Storage는 사용자가 자신이 구매한 생필품이나 물건을 위치별로 관리할 수 있게 하는 애플리케이션입니다. 사용자는 위치로 구분지어진 물건들을 확인할 수 있으며, 물건의 상태를 관리할 수 있습니다.
>  사용자가 물건을 사용 완료 했을시 물건을 '사용 완료'로 옮겨 차후에 다시 구매해야할 물건을 확인할 수 있습니다.
<br>

<h2>목차</h2>
<ul>
  <li><a href="#1-프로젝트-배경">1. 프로젝트 배경</a></li>
  <li><a href="#2-프로젝트-구현-계획">2. 프로젝트 구현 계획</a></li>
  <li><a href="#3-제작-기간">3. 제작 기간</a></li>
  <li><a href="#4-사용-기술">4. 사용 기술</a></li>
  <li><a href="#5-Architecture">5. Architecture</a></li>
  <li><a href="#6-적용-기술">6. 적용 기술</a></li>
  <li><a href="#7-Overview">7. Overview</a></li>
</ul>
<br>

<h2 id="1-프로젝트-배경">1. 프로젝트 배경</h2>

> 물건을 구매하는 과정에서 발생하는 문제를 해결하기 위해 시작된 이 프로젝트는, 필요한 물건을 즉흥적으로 구매하는 습관으로 인해 중복 구매가 빈번히 발생하고, 
> 집에 이미 존재하는 물건을 잘 파악하지 못하는 문제에 대응하고자 합니다. 
> 이를 위해 제작된 애플리케이션은 사용자가 불필요한 물건의 중복 구매를 방지할 수 있도록 도와주며, 
> 구매한 물건을 위치별로 효과적으로 관리할 수 있습니다.
<br>

<h2 id="2-프로젝트-구현-계획">2. 프로젝트 구현 계획</h2>
 <h4 align="center">프로젝트 구현 계획은 아래의 Notion에서 확인할 수 있습니다.</h1>
 <h4 align="center"> 아래의 Notion에는 프로젝트 IA, Flowchart, ERD, wbs 그리고 프로젝트의 전체 진행사항이 있습니다.</h1>
 <br>
 <div align=center> 
   <a href="https://cypress-eris-422.notion.site/My-storage-Project-management-2ec37e57fa0d42a59ed7de78c89d0b52">My Storage Notion 링크</a>
  </div>
<br>

<h2 id="3-제작-기간">3. 제작 기간</h2>
  <ul>
    <li>2023년 3월 6일 ~ 2023년 5월 14일</li>
  </ul>
<br>

<h2 id="4-사용-기술">4. 사용 기술</h2>
<div align=center><h1>📚 STACKS</h1></div>
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=android studio&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/apache-D22128?style=for-the-badge&logo=apache&logoColor=white">   
  <img src="https://img.shields.io/badge/php-777BB4?style=for-the-badge&logo=php&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/visual studio code-007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white">
</div>
<br>

<div align=center><h1>Tech stack & Open-source libraries</h1></div>

- Minimum SDK level 26
- [Kotlin](https://kotlinlang.org/)  기반
- Jetpack
  - Lifecycle: 안드로이드 라이프사이클을 관찰하고 라이프사이클 변경에 따라 UI 상태를 처리합니다.
  - ViewModel: UI 관련 데이터 홀더를 관리하며 라이프사이클을 인식합니다. 화면 회전과 같은 구성 변경에서 데이터를 보존할 수 있습니다. 
  - DataBinding: UI 구성요소를 프로그래밍적으로가 아닌 선언적 형식으로 레이아웃에 데이터 소스와 바인딩합니다.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Bindables: UI 레이어에 데이터 변경을 알리기 위한 Android DataBinding 킷입니다.
- Retrofit2 & OkHttp3: REST API 및 페이징 네트워크 데이터 생성
- ViewPager2: 메인 페이지의 화면 전환에 사용됩니다.
- [Material-Components](https://github.com/material-components/material-components-android): Material Design 컴포넌트들은 메인 페이지의 이동을 정의하는 TabLayout과 아이템 추가를 위한 FloatingActionButton 그리고 사용자에게 정보를 받을 때 사용되는 Chip, ChipGroup이 사용됩니다.
- [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette): 네트워크에서 이미지를 불러옵니다.
- Drawerlayout: Menu를 보여주기 위해 사용됩니다.
- [OpenCV](https://github.com/opencv/opencv): OpenCV는 영상 처리를 위한 오픈 소스 라이브러리로, 영수증의 물품 목록을 출력하기 위한 OCR(광학 문자 인식) 작업에 앞서 이미지 전처리를 수행하는 데 사용됩니다.
<br>

<h2 id="5-Architecture">5. Architecture</h2>

 **My Storage**  는 [Google's official architecture guidance](https://developer.android.com/topic/architecture).
의 MVVM architecture Pattern을 따르고 있습니다.
<br>

<p align="center">
  <img src="https://github.com/Iwillbeagood/My-Storage/assets/106158445/bb1ef6e1-50b6-4e55-89cc-b5b99a1ee57e" alt="My Image">
</p>
<br>

<h2 id="6-적용 기술">6. 적용 기술</h2>
<div align=center><h1>MAD Score</h1></div>
<br>

<div align=center><h1>Open API</h1></div>

1. 핸드폰 인증을 위한 [NAVER SENS SMS](https://api.ncloud-docs.com/docs/ko/ai-application-service-sens-smsv2) 를 사용합니다. NAVER SENS SMS는 


<br>

<h2 id="7-Overview">7. Overview</h2>
<div align=center><h1>Mobile</h1></div>
<br>

<div align=center><h1>Backend</h1></div>
<h4 align="center">My Storage의 Backend 코드와 그에대한 설명은 아래의 Github에서 확인할 수 있습니다.</h1>
<br>
 <div align=center> 
   <a href="https://github.com/Iwillbeagood/My-Storage-PHP">My Storage Backend 링크</a>
  </div>
<br>

