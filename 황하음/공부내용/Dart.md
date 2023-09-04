flutter와 dart 모두 구글이 만들었기 때문에 flutter를 빠르게 만들기 위해 dart를 조작할 수 있다.

---

### 컴파일 방식.

AOT : ahead of time, 개발이 끝나고 실제로 사용하기 전에 사용한다. 빌드 단계에서 미리 전체코드를 컴파일을 하므로, 실제 사용할 때 렌더가 빠르기 때문이다. 그러나 미리 전체코드를 컴파일하는 방식은 개발 단계에서 변경 상황을 계속해서 모니터링하기에는 불편하고 느리다.

JIT : just in time, 내가 쓴 코드를 바로바로 테스트 해볼 수 있다. dart VM 가상머신에서 돌아간다. 개발할 때 좋지만 실제 배포해서 실행해볼 때는 느리다~~

모든 코드는 main 안에 넣기, ; 은 TS나 JS는 VSCode나 formetter가 달아주지만 dart는 안달아줌! 무슨 cascade라는 역할이 있다고 해

---

# 변수

```dart
var name = '이름';
```

dart 스타일 가이드에서, 함수나 메서드 안에서 지역변수를 사용할 때는 var를 사용하는 것이 권장된다.

처음 저장한 객체의 타입을 이 변수는 그대로 가지고 간다. 위의 예시에서, name의 변수값을 바꿀 때에도 정수나 불린이 아닌 String값으로만 바꿀 수 있다.

```dart
String name = '이름';
```

class에서 변수나 property를 선언할 때에는 위 처럼 타입을 지정한다.

### Dynamic : 여러가지 타입을 가질 수 있는 변수. 권장되진 않지만 사용해야 할 때가 올 수 있다.

```dart
name = '이름';
name = 12;
name = true;

dynamic name = '이름';
```

필요 이유 : 변수가 어떤 타입인지 알 수 없는 경우에 사용한다.

```dart
if(name is String){

}
```

우와 그냥 dynamic이면 dart가 이 변수의 타입이 뭔지 모르기 때문에 사용할 수 있는 함수가 많지 않은데, 위 코드처럼 if문으로 확인해주면 String으로 쓸 수 있는 함수를 쭉 띄워줌~~~

정말 필요할 때만 써야한다!!

---

## Nullable Variables

Null Safety : 개발자가 null 값을 참조할 수 없게 하는 것이다.

개발자가 null값을 참조하면 런타임 에러가 뜬다. 때문에 이상적으론, 컴파일 단계에서 이를 잡아내야한다. null safety가 이것을 해준다.

어떤 변수가 null이 될 수 있음을 명시해야한다.

```dart
String? name = '이름';
name = null;
```

타입 뒤에 ? 를 붙이면 해당 변수가 그 타입이 될 수도, null이 될 수도 있음을 나타낸다.

그런데 얘가 null일 수도 있으니까 아래 로직을 수행해야한다.

```dart
if(name is null){
    name.isNotEmpty; // isNotEmpty 속성을 name에 부여하라.
}

// 이렇게 쓸 수도 있다.
name?.isNotEmpty; // 위에서 if문 쓴 것과 같다.
```

dart는 기본적으로 non-nullable이다. 그렇지만 써야할 상황이 올 수 있다.

---

### Final Variables

한 번 정의된 변수를 수정할 수 없게 만드려면, var 대신 final을 사용하면 된다.

JS나 TS의 const와 같다.

```dart
final name = '이름';
```

구체적으로 해주고싶다면 필수는 아니지만…

```dart
final String name = '이름';
```

컴파일러가 똑똑해서 안 써줘도 되지만! 쓰고 싶다면 써

---

### Late Variables

final이나 var 앞에 붙여서 초기 데이터 없이 변수를 선언할 수 있게 해준다.

```dart
late final String name;
late final age;
```

일단 만들어두고 뭔갈 하는거지 api 요청으로 데이터를 받아서 나중에 변수에 넣을 수 있다.

```dart
late final name;
name = '이름';
```

위 예시는 그래도 final이라서 ‘이름’ 값이 들어간 이후에는 변경할 수 없다는 점 복습

late로 만든 변수에 값을 집어넣어주기 전까지는 변수에 접근하지 말 것. 애초에 dart가 접근 못하게 한다.

flutter와 api 작업할 때 많이 보인다고 해.

null safety같은 것. 데이터를 넣어줘야 사용할 수 있다.

flutter로 data fetching을 할 때 유용하대.

---

### Constant Variable

const. JS나 TS의 const와는 다르다. 거기에서 const는 dart의 final과 비슷하니까.

dart의 const : compile-time constant를 만들어준다.

```dart
const name = '이름';
```

name변수는 수정되지 않는다. final과 비슷함.

그러나 다른 점은? const로 만들어진 변수는 compile-time에 알고 있는 값이어야 한다.

```dart
final API = fetchApi();
```

컴파일러는 API 변수의 값을 모르기 때문에 final을 사용한다.

const는 스토어에 출시 전에 알고있는 값을 다룰 때 사용한다. 사용자가 입력해야 하는 것은 var나 final을 사용한다.

---

## Recap

dart 스타일 가이드에서는 var를 최대한 많이 사용하는 것이 권장된다.

타입으로 변수를 정의할 때는 class의 property정의할 때~~~

컴파일러가 변수 타입을 추론할테니까.
