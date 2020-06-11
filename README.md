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

* `http://localhost:8001` 로 실행된다. 
* Database는 `http://localhost:8001/h2-console` 접속해서 확인할 수 있으며 접속정보는 아래와 같다.
	* JDBC URL: jdbc:h2:mem:testdb
	* user name: sa
	* password: 
	
# 3. 기능 요구사항 및 문제해결 전략 
## 필수사항 
* 필수사항의 모든 요청은 헤더에 Authorization에 _3.8 signup 계정생성 API_ 혹은 _3.9 signin 로그인 API_ 에서 발급 받은 유효한 토큰을 가져야 한다. 

* 엔티티는 다음과 같다. 


**COUPON**

| 값 | 자료형 | 의미 |
|---|---|---|
| `coupon` | varchar | 쿠폰번호(PK) |
| `created_at` | timestamp | 생성시간 |
| `issued_at` | timestamp | 발급시간 |
| `expired_at` | timestamp | 만료시간 |
| `is_issued` | boolean | 발급여부 |
| `is_used` | boolean | 사용여부 |
| `user_id` | varchar | 사용자id |

**USER**

| 값 | 자료형 | 의미 |
|---|---|---|
| `user_id` | varchar | 사용자id(PK) |
| `password` | varchar | 비밀번호 |

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
* `MainController`에 `@PostMapping(value = "/coupon/{N}")`를 생성하고 파라미터에 `@PathVariable Long N`을 추가해 N을 input으로 받도록 했다. 
* `com.kakaopay.service.CouponService`의 `generateCoupon(Long N)` 매소드가 로직을 처리한다.
* 쿠폰을 발급하고 DB에 저장되는 로직은 다음과 같다. 
	* N개 만큼 쿠폰을 발급해 `List<Coupont> list`에 담는다. 
	* `List<Coupon> list`을 JPA의 `saveAll(list)`을 통해 DB에 저장한다. 
		* Coupon 번호가 Coupon테이블의 Key로 사용되고 있기 때문에 번호가 같은 경우 DB에 저장되지 않는다. 20자리 난수 String을 발급하기 때문에 경우의 수는 적지만 혹시나 쿠폰번호가 겹쳐 input N개만큼 쿠폰을 발급하지 못하는 경우를 다음과 같이 처리했다. 
			* Coupon의 `create_at`을 확인하여 저장되지 않은 개수를 체크하고 다시 쿠폰을 발급하고 저장한다. 
			* 저장되지 않는 경우, `create_at` 가 null인 점을 활용했다. 
* 쿠폰번호 발급은 다음과 같은 절차로 진행된다 
	* 20자리 대문자, 소문자, 숫자로 구성된 난수 String을 발급한다. 
	* 각 자리의 문자열을 생성할 때, 쿠폰들의 숫자와 문자의 위치가 동일하지 않도록 먼저 1~3사이의 난수를 발급한다. 
	이에따라 0: 소문자 1: 대문자 2:숫자가 각각 랜덤하게 발급되게 한다. 
	* 생성 과정에서는 메모리를 고려해 `StringBuffer`를 활용했다. 

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

#### - 문제해결 전략 
* `MainController`에 `@PutMapping(value = "/coupon/issue")`를 생성한다. 
* `com.kakaopay.service.CouponService`의 `issueCoupon(token)` 매소드가 로직을 처리한다.
* 쿠폰을 발급하는 로직은 아래와 같다.  
	* JPA의 `findTop1ByIsIssued(false)`을 통해 발급되지 않은 쿠폰 중 하나를 찾는다.
	* 해당 Coupon의 `is_issued`를 true로, `issued_at`를 발급시간으로, `user_id`를 토큰의 userId 값으로 값을 변경한다. 
		* JWT 토큰에서 userId를 가져오는 방법은 다음과 같다. 
			* JWT토큰에서 payload를 디코딩한다. 
			* 디코딩한 payload에서 userId 값을 가져온다. 
			* userId도 인코딩되어 있기 때문에 한번 더 디코딩하여 userId 값을 가져온다.
	* 생성되어 있는 쿠폰이 없는 경우에는 다음과 같은 응답을 리턴한다. 
	<pre><code>
	{
    		"code": 94,
    		"message": "No Coupon to Issue"
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

#### - 문제해결 전략 
* `MainController`에 `@GetMapping(value = "/coupon/issue")`를 생성한다. 
* `com.kakaopay.service.CouponService`의 `findIssuedCoupon()` 매소드가 로직을 처리한다.
* 발급된 쿠폰을 찾는 로직은 다음과 같다. 
	* JPA의 `findByIsIssued(true)`를 통해 발급된 쿠폰들을 찾는다. 
	
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

#### - 문제해결 전략 
* `MainController`에 `@PutMapping(value = "/coupon/use")`를 생성하고, `final @Valid @RequestBody RequestCouponDefault coupon`을 추가해 request body의 유효성을 미리 검사한다. 
	* 유효하지 않는 경우 아래와 같이 리턴한다. 
	<pre><code>
	{
    		"code": 98,
    		"message": "Invalid Coupon"
	}
	</pre></code>
* `com.kakaopay.service.CouponService`의 `changeUseCoupon()` 매소드가 로직을 처리한다.
* 발급된 쿠폰을 사용처리하는 로직은 다음과 같다. 
	* JPA의 `findOneByCouponAndIsIssuedAndIsUsed(token, coupon, true, false)`를 통해 발급된 쿠폰들을 찾는다. 
	(입력받은 쿠폰 번호 중 발급이 완료 되었고, 사용은 하지 않은 쿠폰)
	* 해당 조건에 일치하는 쿠폰이 없는 경우에는 위와 같은 `Invalid Coupon`에러를 리턴한다. 
	* 토큰의 소유자와 쿠폰의 소유자가 동일한지 확인한다. 동일하지 않으면 아래와 같이 리턴한다. 
	<pre><code>
	{
    	"code": 90,
    	"message": "UserId is not match"
	}
	</pre></code>
	* 해당 조건에 일치하는 쿠폰이 있는 경우에는 만료기간 검사를 한다. 이미 만료된 경우에는 아래와 같이 리턴한다. 
	<pre><code>
	{
    		"code": 96,
    		"message": "Expired Coupon"
	}
	</pre></code>
	* 모든 조건 검사를 통과하면 Coupon의 `is_used`를 true로 변경한다. 
	
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

#### - 문제해결 전략 
* `MainController`에 `@PutMapping(value = "/coupon/cancel")`를 생성하고, `final @Valid @RequestBody RequestCouponDefault coupon`을 추가해 request body의 유효성을 미리 검사한다. 
	* 유효하지 않는 경우 아래와 같이 리턴한다. 
	<pre><code>
	{
    		"code": 98,
    		"message": "Invalid Coupon"
	}
	</pre></code>
* `com.kakaopay.service.CouponService`의 `changeUseCoupon()` 매소드가 로직을 처리한다.
* 발급된 쿠폰을 사용처리하는 로직은 다음과 같다. 
	* JPA의 `findOneByCouponAndIsIssuedAndIsUsed(token, coupon, true, true)`를 통해 발급된 쿠폰들을 찾는다. 
	(입력받은 쿠폰 번호 중 발급이 완료 되었고, 사용한 쿠폰)
	* 해당 조건에 일치하는 쿠폰이 없는 경우에는 위와 같은 `Invalid Coupon`에러를 리턴한다. 
	* 토큰의 소유자와 쿠폰의 소유자가 동일한지 확인한다. 동일하지 않으면 아래와 같이 리턴한다. 
	<pre><code>
	{
    	"code": 90,
    	"message": "UserId is not match"
	}
	</pre></code>
	* 해당 조건에 일치하는 쿠폰이 있는 경우에는 만료기간 검사를 한다. 이미 만료된 경우에는 아래와 같이 리턴한다.
	<pre><code>
	{
    		"code": 96,
    		"message": "Expired Coupon"
	}
	</pre></code>
* 모든 조건 검사를 통과하면 Coupon의 `is_used`를 false로 변경한다. 

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

#### - 문제해결 전략 
* `MainController`에 `@GetMapping(value = "/coupon/expire")`를 생성한다. 
* `com.kakaopay.service.CouponService`의 `getExpireTodayCoupon()` 매소드가 로직을 처리한다.
* 당일 만료하는 쿠폰을 찾는 로직은 아래와 같다. 
	* JPA의 `findByExpiredAtBetweenAndIsUsed(start, end, false)`를 통해 발급된 쿠폰들을 찾는다. 
	(만료 시간이 당일 00:00:00 ~ 당일 23:59:59 사이에 있고 사용하지 않은 쿠폰) 

## 선택문제

### 3.7 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.)
#### 매일 00시 00분 00초에 발급된 쿠폰  만료 3일전인 쿠폰을 찾 다음과 같이 출력한다. 
#### - 출력화면
<pre><code>
testId567님에게 전송합니다. [jv6ZchIxdXpGRZsB0iTx] 쿠폰이 3일 후 만료됩니다.
</pre></code>

#### - 문제해결 전략 
* 스케쥴러를 등록해 매일 00시 00분 00초에 실행 만료 쿠폰 검사를 실행한다. 
* `com.kakaopay.KakaopayCouponApplication`에 `@EnableScheduling` 어노테이션 추가한다. 
* `com.kakaopay.Scheduler`를 `@Component`로 등록하고 그 안에 `@Scheduled(cron = "0 0 0 * * * ")`를 생성한다. 
* 3일 안에 만료하는 쿠폰을 찾는 로직은 다음과 같다. 
	* JPA의 `findByExpiredAtBetweenAndIsUsed(start, end, false)`를 통해 발급된 쿠폰들을 찾는다. 
	(만료 시간이 당일 00:00:00 ~ 3일후 23:59:59 사이에 있고 사용하지 않은 쿠폰) 


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

#### - 문제해결 전략 
* `UserController`에 `@PostMapping(value = "/user/signup")`를 생성하고, `final @Valid @RequestBody RequestUserDefault user`을 추가해 request body의 유효성을 미리 검사한다. 
* `com.kakaopay.service.UserService`의 `signUpUser()` 매소드가 로직을 처리한다.
* sign up을 처리하는 로직은 다음과 같다. 
	* JPA의 `findOneByUserId(id)`를 통해 아이디 중복 검사를 진행한다. 만약 이미 사용되고 있는 아이디인 경우 아래와 같이 리턴한다.
	<pre><code>
	{
    		"code": 93,
    		"message": "UserId already Used"
	}
	</pre></code>
	* 패스워드를 `SHA256`암호화 방식을 통해 암호화 한다. 
	* 암호화한 패스워드와 입력받은 아이디를 JPA의 `save(user)`를 통해 저장한다. 
	* `com.kakaopay.utils.JWTUtils` 의 `generateToken(id)`를 통해 토큰을 발급한다. 
		* payload를 생성하고, private key를 만들어 서명을 생성한다. 
		* payload 구조
		<pre><code>
		{
  			"exp": 만료시간,
  			"userId": 아이디를 인코딩한 값,
  			"iat": 발급시간
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

* `UserController`에 `@PostMapping(value = "/user/signin")`를 생성하고, `final @Valid @RequestBody RequestUserDefault user`을 추가해 request body의 유효성을 미리 검사한다. 
* `com.kakaopay.service.UserService`의 `signInUser()` 매소드가 로직을 처리한다.
* sign in을 처리하는 로직은 다음과 같다. 
	* JPA의 `findOneByUserId(id)`를 통해 해당 아이디의 사용자가 있는지 먼저 검사한다. 없는 아이디인 경우 다음과 같이 리턴한다. 
	<pre><code>
	{
	    "code": 95,
	    "message": "UserId is not exist"
	}
	</pre></code>
	* 입력받은 아이디의 사용자가 있는 경우, 입력받은 패스워드를 `SHA256`암호화 방식을 통해 암호화 한다. 
	* `findOneByUserId(id)`를 통해 찾은 사용자의 비밀번호와 입력 받은 패스워드의 암호화 값이 동일한지 검사한다. 동일하지 않은 경우 다음과 같이 리턴한다. 
	<pre><code>
	{
	    "code": 92,
	    "message": "Password is incorrect"
	}
	</pre></code>
	* 위의 조건을 모두 만족한 경우, `com.kakaopay.utils.JWTUtils` 의 `generateToken(id)`를 통해 토큰을 발급한다. 
		
### 3.10 그외 문제해결 전략
#### 3.10.1 로깅 
* AOP를 통해 공통적으로 로깅을 처리한다. 
	* `com.kakaopay.aop.Logging`에서 프로젝트 전반적인 로깅을 담당한다. 
	* Request 수신 시 다음과 같이 출력한다.
	<pre><code>
	Request: POST /user/signin < 0:0:0:0:0:0:0:1 
	</pre></code>
	* Request 리턴 시 다음과 같이 출력한다.
	<pre><code>
	Response Code: 200
	Response Body: ReturnDefault [code=200, message=OK, result=TokenDTO(token=eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTE3OTc2MjQsInVzZXJJZCI6IlpFZFdlbVJGYkdzPSIsImlhdCI6MTU5MTc5NDAyNDM1MH0.-SmxSZbBVUAbTEYvdDc7UoHLPFKZm-mQPhmnGYV_SDI)]
	</pre></code>
	* 서비스 클래스 호출시 다음과 같이 출력한다. 
	<pre><code>
	Call Service: com.kakaopay.service.impl.UserServiceImpl.signUpUser
	</pre></code>
	
#### 3.10.2 헤더체크 
* AOP를 통해 공통적으로 헤더체크를 처리한다. 
	* `com.kakaopay.aop.HeaderCheck`에서 모든 Request의 전에 헤더를 검사한다. 
	* 헤더의 Authorization에 토큰이 있는지, 유효한지 검사한다. 헤더가 유효하지 않은 경우 아래와 같이 리턴한다. 
	<pre><code>
	{
		"code": 97,
		"message": "Invalid Token"
	}
	</pre></code>
	* 토큰의 유효성은 `com.kakaopay.utils.validateToken`에서 검사하고 방법은 다음과 같다.
		* 토큰이 null인지 경우는 false(유효하지 않음 처리한다.)
		* jwt 형식(header.payload.signiture)이 아닌 경우는 false 처리한다.
		* payload를 검사한다. payload에 사용자아이디, 만료시간, 발급시간 중 하나라도 없는 경우 false 처리한다. 
		* 토큰의 payload를 기반으로 자체적으로 jwt토큰을 새로 생성한다. 
		* 입력받은 토큰값과 새로 생성한 토큰값을 비교한다. 일치하는 경우 payload의 만료시간을 검사해 만료된 토큰인지 검사하고, 만료된 경우 false 처리한다. 
		* 모든 조건을 통과하면 true(유효한 토큰)로 처리한다. 
#### 3.10.3 예외처리
* `com.kakaopay.exception.MainAdvice` 에서 `@ControllerAdvice`를 추가해 전역 예외처리를 관리한다. 
* 발생할 수 있는 예외 별로 어떤 HTTP Status Code와 메시지를 리턴할지 정의하고, 예외 발생 시 이에 따라 리턴한다. 
* 예외 메시지와 예외 코드는 com.kakaopay.constant.Constant.RES에 공통으로 정의했다. 정의되어 있는 예외 코드는 다음과 같다. 
	<pre><code>
	STATUS_OK(200, "OK"), 
	INVALID_REQUESTBODY(99, "Invalid RequestBody"), 
	INVALID_COUPON(98, "Invalid Coupon"),
	INVALID_TOKEN(97, "Invalid Token"), 
	EXPIRED_COUPON(96, "Expired Coupon"),
	USERID_NOT_EXIST(95, "UserId is not exist"), 
	NO_COUPON(94, "No Coupon to Issue"),
	USERID_ALREADY_USED(93, "UserId already Used"), 
	INCORRECT_PASSWORD(92, "Password is incorrect"),
	FAIL(91, "Something wrong"), 
	USERID_NOT_MATCH(90, "UserId is not match");
	</pre></code>
