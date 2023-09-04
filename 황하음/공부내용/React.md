리액트 : 기존의 방식이 상태의 변화에 따라 DOM을 업데이트하는 것이었다면, 리액트는 처음부터 모든 걸 새로 만들어서 보여준다면 어떨까? 하는 발상에서 시작

```jsx
import React from 'react';
// 리액트 컴포넌트를 만든다.

export default 컴포넌트명;
// 해당 컴포넌트를 내보낸다. 최하단에 기입한다.
```

```jsx
import Hello from './hello';
// 이런 식으로, 다른 js파일에서 내보낸 컴포넌트를 사용할 수 있다.

return (
    <div className="App">
      <Hello />
      <Hello />
      <Hello />
    </div>
  );
// 이런 식으로 사용하며, 같은 컴포넌트를 여러 번 사용할 수도 있다!
```

---

## JSX란? : 리액트 컴포넌트에서 xml형식의 값을 반환하는 것

리액트 컴포넌트 파일에서 XML 형태로 코드를 작성하면, babel이 이 JSA를 JS로 변환해준다.

[Babel : 아직 지원되지 않는 최신 문법이나 실험적인 JS 문법들을 정식 JS 형태로 변환한다. 이로써 구형 브라우저에서도 제대로 실행할 수 있게 된다.](https://babeljs.io/repl#?browsers=defaults%2C%20not%20ie%2011%2C%20not%20ie_mob%2011&build=&builtIns=false&corejs=3.21&spec=false&loose=false&code_lz=Q&debug=false&forceAllTransforms=false&modules=false&shippedProposals=false&circleciRepo=&evaluate=false&fileSize=false&timeTravel=false&sourceType=module&lineWrap=true&presets=env%2Creact%2Cstage-2&prettier=false&targets=&version=7.22.10&externalPlugins=&assumptions=%7B%7D)

## JSA가 JS로 제대로 변환되기 위해 지켜야 하는 규칙

### 태그 닫기

HTML에서는 input이나 br 태그를 사용할 때 닫지 않고 사용하기도 한다. 하지만 리액트에서는 무조건 닫아야한다.

<div></div>

만약 br과 같이 태그와 태그 사이에 내용이 들어가지 않을 때에는 열리자마자 바로 닫히는 Self Closing 태그를 사용한다.

<br />

### 두 개 이상의 태그는 무조건 하나의 태그로 감싸기

```jsx
return (
    <Hello />
    <div> 안녕히 계세요. </div>
);
```

위 처럼 사용하면 안된다. 아래와 같이 하나의 태그로 감싸주어야 한다.

```jsx
return (
    <div>
        <Hello />
        <div> 안녕히 계세요. </div>
    </div>
);
```

그러나 이렇게 감싸기만을 위해 불필요한 태그를 사용하는 것은 좋지 않을 수도 있다. 스타일 관련 설정을 하다가 복잡해 질 수도 있고, div로 감싸기엔 애매한 상황이 있기도 한다. 그럴때는 리액트의 Fragment를 사용한다.

**Fargment : 이름 없는 태그. 브라우저 상에서 별도의 엘리먼트로 나타나지 않는다.**

```jsx
return (
    <>
        <Hello />
        <div> 안녕히 계세요. </div>
    </>
);
```

### JSX 안에 자바스크립트 값 사용하기

```jsx
const name = 'react';
return(
    <>
        <Hello />
        <div>{name}</div> // 자바스크립트 변수를 보여주기 위해 { } 를 사용한다.
    </>
    );
```

### style과 className

JSX에서 태그에 CSS를 설정하는 것은 HTML과 다르다. 인라인 스타일은 객체 형태로 작성해야하며, ‘-’로 구분된 이름은 카멜 케이스 형태로 바꾸어줘야한다. (ex. background-color → backgroundColor)

```jsx
function App() {
  const name = 'react';
  const style = {
    backgroundColor: 'black',
    color: 'aqua',
    fontSize: 24, // 기본 단위는 px
    padding: '1rem' // 다른 단위를 사용하면 문자열로 설정.
  }

  return (
    <div className="App">
      <Hello />
      <div style={style}>{name}</div>
    </div>
  );
}
```

CSS 클래스를 설정할 때에는 class= 가 아니라 className= 으로 아래와 같이 설정해야한다.

**App.css**

```css
.gray-box{
    background: gray;
    widgh: 64px;
    height: 64px;
}
```

**App.js**

```jsx
import './App.css';
...
return (
    <div className="App">
      <Hello />
      <div style={style}>{name}</div>
      <div className="gray-box"></div>
    </div>
  );
```

## 주석

반드시 { } 괄호 안에 /* */를 넣어야한다. 열리는 태그 내부에는 // 로도 작성 가능하다.

```jsx
{/* 이렇게 */}

<Hello
    // 이렇게 열린 태그 안에는 슬래시 두 개로 주석 작성 가능
/>
```

## 추가

return ( ) 내부는 JSX이나 그 밖은 JS이다.

---

# Props : properties. 어떤 값을 컴포넌트에 전달해야 할 때 사용한다.

## 기본 사용법

만약 Hello라는 컴포넌트에 name이라는 변수값을 전달하고 싶다면

**App.js**

```jsx
import Hello from './Hello';

return (
    <Hello name="react" />
  );
}
```

name이란 변수값을 전달받는 컴포넌트 입장

**Hello.js**

```jsx
import React from "react";

function Hello(props){
    return <div>안녕하세요 {props.name}</div>
}

export default Hello;
```

Hello 옆에 props 잊지 않기

**Hello.js**

```jsx
function Hello(props){
    return <div style={{color: props.color}} >안녕하세요 {props.name}</div>
}
```

색을 지정할 수도 있다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/db316f8d-0ab2-4263-8520-abb9fd80a553/Untitled.png)

### 비구조화 할당

props를 매번 붙이지 않고 간단하게 작성하고 싶다면 아래 방식을 따라도 좋다.

```jsx
function Hello({color, name}){
    return <div style={{color}} >안녕하세요 {name}</div>
}
```

## defaultProps

props가 없을 때 기본으로 보여줄 값을 지정할 수 있다.

```jsx
Hello.defaultProps = {
    name : '이름 없음'
}
```

위에서는 name을 ‘이름 없음’으로 설정했다!!

이것이 적용되는지 보자

**App.js**

```jsx
return (
    <>
    <Hello name="react" color="red" />
    <Hello color="pink" />
    </>
  );
```

위와 같이 name을 설정하지 않으면 default 값이 나오게 된다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6eed09c7-da8d-42d4-9e2a-2afead349c98/Untitled.png)

## Props.children

아래와 같이 컴포넌트 태그 사이에 들어간 요소를 children이라고 한다.

```jsx
return (
    <Wrapper>
    <Hello name="react" color="red" />
    <Hello color="pink" />
    </Wrapper>
  );
```

Wrapper.js의 자식들인 것이다. 그런데 이 Wrapper.js를 일반적인 방법으로 만들면 이 컴포넌트에 가려 자식 요소들이 보이지 않는다. 때문에 Wrapper.js를 아래와 같이 설정해서 props.children을 렌더링 해야한다.

```jsx
function Wrapper( { children }){
    const style = {
        border: '2px solid black',
        padding: '16px',
    };

    return (
        <div style={style}>
            {children}

        </div>
    )
} 
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/41060bd5-4408-420a-9b56-db9823c3eb59/Untitled.png)

쨘 이렇게 상자 따로 안에 들어가는 글자 따로 컴포넌트를 분리해서 구현할 수 있단 말~!! 우와 재사용성이 올라가겠는걸~~!!!



---

### 조건부 렌더링 : 특정 조건에 따라 다른 결과물 렌더링

**App.js**

```jsx
return (
    <Wrapper>
    <Hello name="react" color="red" isSpecial={true}/>
    <Hello color="pink" />
    </Wrapper>
  );
```

이때 isSpecial 값은 JS값이라서 중괄호로 감싸야한다.

### 삼항 연산자 혹은 단축 평가 논리 계산을 사용하자

Props를 받는 쪽에서

```jsx
{isSpecial ? <b>*</b> : null }
{isSpecial && <b>*</b>}
```

위 두 줄의 코드를 사용해서 isSpecial이 true인가 false인가에 따라 나타나는 값을 다르게 할 수 있다.

위 두 줄은 동일한 결과를 보인다.

- JSX에서 null, false, undefined를 렌더링하면 아무것도 나타나지 않는다.

```jsx
<Wrapper>
      <Hello name="react" color="red" isSpecial />
      <Hello color="pink"/>
</Wrapper>
```

props 값을 설정할 때 이름만 설정하고 값 설정을 생략한다면 true로 간주한다.

---

사용자 인터랙션에 따라 컴포넌트에서 보일 값을 바꾸는 방법 중 하나.

리액트에 Hooks 가 도입되면서 함수형 컴포넌트에서도 상태를 관리할 수 있게 되었다.
