### 此包仅包含注册功能

* 其中包括发送验证码,以及注册功能
    * 其中发送验证码功能,需要通过客户端携带的cookie来生成session.使用redis存储验证码,使用sessionId +
      email生成key,验证码作为value.设置存储时间为3分钟,若小于120秒时,可覆盖存储重新发送验证码.
    * 注册功能需要参数 username password email nickname
      code,将其封装在[AccountRegDTO.java](pojo%2Fdto%2FAccountRegDTO.java)
      类中,并设置参数格式检查;通过接受到的请求,拿到email以及再次生成session并拼接成key,到redis中查询是否存在key.存在则比较验证码,验证码正确则查看是否有相同的username若没有相同的username则进行插入;