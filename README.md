Rest API 기반 쿠폰시스템
=============
# 0. 목차
* 개발환경 
* 빌드 및 실행방법
* 기능 요구사항 및 문제해결 전략 

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

#### http://localhost:8001 로 실행된다. 
#### Database는 http://localhost:8001/h2-console 접속해서 확인할 수 있으며 접속정보는 아래와 같다.
	* JDBC URL: jdbc:h2:mem:testdb
	* user name: sa
	* password: 
	
# 3. 기능 요구사항 및 문제해결 전략 
## 필수사항 
#### 필수사항의 모든 요청은 헤더에 Authorization에 _3.8 signup 계정생성 API_ 혹은 _3.9 signin 로그인 API_에서 발급 받 유효한 토큰을 가져야 한다. 

### 3.1 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.(input : N) 
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

#### - 문제해결 전략 
* `MainController`에 `@PostMapping(value = "/coupon/{N}")`를 생성하고 파라미터에 `@PathVariable Long N`을 추가해 N을 input으로 받도록 함. 


### 3.2 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요. (output : 쿠폰번호(XXXXX-XXXXXX-XXXXXXXX))
#### - REQUEST
<pre><code>
PUT /coupon/issue
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK",
    "result": {
        "coupon": "eJ9LWNu2f52juGlKOU3v"
    }
}
</pre></code>

### 3.3 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
#### - REQUEST
<pre><code>
GET /coupon/issue
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK",
    "result": {
        "totalCount": 1,
        "coupons": [
            {
                "createdAt": "2020-06-10T19:24:52.035",
                "coupon": "eJ9LWNu2f52juGlKOU3v",
                "issuedAt": "2020-06-10T19:33:29.89",
                "expiredAt": "2020-06-17T23:59:59",
                "used": false,
                "issued": true
            }
        ]
    }
}
</pre></code>

### 3.4 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가) (input : 쿠폰번호)
#### - REQUEST
<pre><code>
PUT /coupon/use
{
	"coupon" :"eJ9LWNu2f52juGlKOU3v"
}
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK"
}
</pre></code>

### 3.5 지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능) (input : 쿠폰번호)
#### - REQUEST
<pre><code>
PUT /coupon/cancel
{
	"coupon" :"eJ9LWNu2f52juGlKOU3v"
}
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK"
}
</pre></code>

### 3.6 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
#### - REQUEST
<pre><code>
GET /coupon/expire
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK",
    "result": {
        "totalCount": 1,
        "coupons": [
            {
                "createdAt": "2020-06-10T19:24:52.035",
                "coupon": "eJ9LWNu2f52juGlKOU3v",
                "issuedAt": "2020-06-10T19:33:29.89",
                "expiredAt": "2020-06-10T23:59:59",
                "used": false,
                "issued": true
            }
        ]
    }
}
</pre></code>


## 선택문제

###3.7 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.)
#### 매일 00시 00분 00초에 발급된 쿠폰  만료 3일전인 쿠폰을 찾 다음과 같이 출력한다. 
#### - 출력화면
<pre><code>
[4945jW73jEFMEed2lX52] 쿠폰이 3일 후 만료됩니다.
</pre></code>

## 제약사항(선택)

### 3.8 signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력한다. 
#### - REQUEST
<pre><code>
POST /user/signup
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK",
    "result": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTE3OTEzMzUsInVzZXJJZCI6IlpFZFdlbVJGYkdzPSIsImlhdCI6MTU5MTc4NzczNTg3OH0.Vhw5L9OHqQmVRSU7oaIERNKOywUt0NeeQIqEqG7PFCw"
    }
}
</pre></code>

### 3.9 signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
#### - REQUEST
<pre><code>
POST /user/signin
</pre></code>

#### - RESPONSE
<pre><code>
{
    "code": 200,
    "message": "OK",
    "result": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTE3OTE0NzIsInVzZXJJZCI6IlpFZFdlbVJGYkdzPSIsImlhdCI6MTU5MTc4Nzg3MjczMn0.iUkYHUcGfT6PEUJzTmkK5CcAu6QClot3FQT_3BQC5uw"
    }
}
</pre></code>

