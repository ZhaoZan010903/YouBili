### 视频模块

### 数据处理方面

将每一条视频的信息存入redis之中,其中redis的key为 Video: ***userID*** : ***videoID***

#### 查询操作

* 直接通过videoID查到相应视频;
* 通过userID查到对应的所有videoID;

### 增删操作

***例如:点赞操作;***

1. 增加视频点赞数

*
    * 将点赞对应的数据添加到likes表之中
*
    * 在将video表之中的点赞数 +1
*
    * 同时删除Redis之中的原本数据
*
    * 在将更新后的数据插入到Redis之中