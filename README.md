제목테스트 
=============
# 1. 제목
## 1.1 부제목
1. 1단계 
2. 2단계
3. 3단계 
<pre><code>{code}</code></pre>
# 0. 목차
* 개발환경 
* 빌드 및 실행방법
* 기능 요구사항
* 문제해결 전략 

# 1. 개발환경 
* JAVA8 
* SpringBoot 2.3.0
* H2
* MAVEN 
* JUNIT4
* GIT 
* IDE : STS4
* OS : MAC OS High Sierra

# 2. 빌드 및 실행방법
<pre><code>
$ git clone https://github.com/leeheejo/kakaopay.git
$ git cd kakaopay 
$ mvn package 
$ cd target 
$ java -jar kakaopayCoupon-0.0.1-SNAPSHOT.jar
</code></pre>

# 3. 기능 요구사항 
## 필수사항 
#### 필수사항의 모든 요청은 헤더에 <pre><code>Authorization</pre></code>에 (())에서 발급 받 유효한 토큰을 가져야 한다. 
#### 3.1 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.(input : N) 
#### - REQUEST
<pre><code>
POST /coupon/{N}
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK"
}
</pre></code>
