### 用户管理模块

* account下是有关于登录内容的包
    * 其中登录接口地址为 "/api/auth/login"
    * 测试类的接口地址为 "/test/auth/abc",此测试类返回值携带当事人信息,其中包含当事人信息的写法如下
  ```java
    @PostMapping("/abc")
    public RestBean<CurrentPrincipal> Test(@AuthenticationPrincipal CurrentPrincipal currentPrincipal){
    return RestBean.success(currentPrincipal);
    }  
  ```