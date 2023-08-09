create table admin
(
    adminID       bigint auto_increment comment '管理员ID'
        primary key,
    adminUsername varchar(255)      null comment '管理员名称',
    password      varchar(255)      null comment '密码',
    nickName      varchar(30)       null comment '管理员昵称',
    email         varchar(255)      null comment '邮箱',
    enable        tinyint default 1 null comment '1表示启用,0表示未启用',
    gmtCreat      datetime          null comment '创建时间',
    gmtModified   datetime          null comment '更新时间'
)
    comment '管理员表' engine = InnoDB;

create table barrage
(
    barrageID   bigint auto_increment comment '弹幕ID'
        primary key,
    userID      bigint            null comment '用户ID',
    videoID     bigint            null comment '视频ID',
    enable      tinyint default 1 null comment '1表示启用,0表示未启用',
    barrageText varchar(255)      null comment '弹幕内容',
    barrageTime time              null comment '弹幕时间',
    gmtCreat    datetime          null comment '创建时间'
)
    comment '弹幕表' engine = InnoDB;

create table categorie
(
    categoryID   bigint auto_increment comment '分类ID'
        primary key,
    categoryName varchar(255)      null comment '分类名称',
    description  text              null comment '分类描述',
    enable       tinyint default 1 null comment '1表示启用,0表示未启用',
    gmtCreate    datetime          null comment '创建时间',
    gmtModified  datetime          null comment '更新时间'
)
    comment '分类表' engine = InnoDB;

create table comment
(
    commentID    bigint auto_increment comment '评论ID'
        primary key,
    userID       bigint                             null comment '用户ID',
    videoID      bigint                             null comment '视频ID',
    commentText  text                               null comment '评论内容',
    commentDate  datetime default CURRENT_TIMESTAMP null comment '评论日期',
    likeCount    int      default 0                 null comment '点赞数',
    dislikeCount int                                null comment '点踩数',
    enable       tinyint  default 1                 null comment '1表示启用,0表示未启用',
    gmtCreate    datetime                           null comment '创建时间',
    gmtModified  datetime                           null comment '更新时间'
)
    comment '评论表' engine = InnoDB;

create table commentreplie
(
    replyID       bigint auto_increment comment '回复ID'
        primary key,
    commentID     bigint                             null comment '评论ID',
    parentReplyID bigint                             null comment '被回复的回复ID',
    userID        bigint                             null comment '用户ID',
    replyText     text                               null comment '回复内容',
    replyDate     datetime default CURRENT_TIMESTAMP null comment '回复日期',
    likeCount     int      default 0                 null comment '点赞数',
    dislikeCount  int                                null comment '点踩数',
    enable        tinyint  default 1                 null comment '1表示启用,0表示未启用',
    gmtCreate     datetime                           null comment '创建时间',
    gmtModified   datetime                           null comment '更新时间'
)
    comment '评论回复表' engine = InnoDB;

create table favorite
(
    favoriteID   bigint auto_increment comment '收藏ID'
        primary key,
    userID       bigint                   null comment '用户ID',
    videoID      bigint                   null comment '视频ID',
    favoriteDate datetime default (now()) null comment '收藏日期',
    enable       tinyint  default 1       null comment '1表示启用,0表示未启用',
    gmtCreate    datetime                 null comment '创建时间',
    gmtModified  datetime default (now()) null comment '更新时间'
)
    comment '收藏表' engine = InnoDB;

create table follow
(
    followID    bigint auto_increment comment '关注ID'
        primary key,
    followerID  bigint                             null comment '关注者ID',
    followedID  bigint                             null comment '被关注者ID',
    followDate  datetime default CURRENT_TIMESTAMP null comment '关注日期',
    enable      tinyint  default 1                 null comment '1表示启用关注,0表示未启用',
    gmtCreate   datetime                           null comment '创建时间',
    gmtModified datetime                           null comment '更新时间'
)
    comment '关注表' engine = InnoDB;

create table heat
(
    heatID    bigint auto_increment
        primary key,
    videoID   bigint   null comment '视频ID',
    upload    datetime null comment '上传时间',
    heatValue float    null comment '热度值'
)
    comment '热度值表' engine = InnoDB;

create table history
(
    historyID     bigint auto_increment comment '历史记录ID'
        primary key,
    userID        bigint                   null comment '用户ID',
    videoID       bigint                   null comment '视频ID',
    watchDate     datetime default (now()) null comment '观看日期',
    watchDuration time                     null comment '观看时长',
    enable        tinyint  default 1       null comment '1表示启用,0表示未启用',
    gmtCreate     datetime                 null comment '创建时间',
    gmtModified   datetime default (now()) null comment '更新时间'
)
    comment '历史记录表' engine = InnoDB;

create table likes
(
    likeID      bigint auto_increment comment '点赞ID'
        primary key,
    userID      bigint                             null comment '用户ID',
    videoID     bigint                             null comment '视频ID',
    likeDate    datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    enable      tinyint  default 1                 null comment '1表示启用,0表示未启用',
    typeEnable  tinyint                            null comment '1表示点赞,0表示点踩',
    gmtCreate   datetime                           null comment '创建时间',
    gmtModified datetime default (now())           null comment '更新时间'
)
    comment '点赞表' engine = InnoDB;

create table mahout
(
    mahoutID int auto_increment
        primary key,
    userID   bigint  not null comment '用户ID',
    videoID  bigint  null comment '视频ID',
    type     tinyint null comment '0表示点踩,1表示观看,2表示点赞,3表示收藏'
)
    engine = InnoDB;

create table notice
(
    noticeID    bigint auto_increment comment '通知ID'
        primary key,
    userID      bigint            null comment '用户ID',
    systemName  varchar(255)      null comment '通知发布人',
    typeEnable  tinyint default 0 null comment '1表示已读,0表示未读',
    noticeText  text              null comment '通知内容',
    noticeTime  datetime          null comment '通知发布时间',
    gmtCreate   datetime          null comment '创建时间',
    gmtModified datetime          null comment '更新时间'
)
    comment '通知表' engine = InnoDB;

create table orders
(
    orderID     bigint auto_increment comment '订单ID'
        primary key,
    tradeName   varchar(255)                       null comment '商品名',
    orderNumber varchar(256)                       not null comment '订单号',
    zfbNumber   varchar(255)                       null comment '支付宝订单号',
    price       decimal(10, 2)                     not null comment '金额',
    payment     varchar(10)                        not null comment '支付方式',
    userID      bigint                             not null comment '购买人的ID',
    payTime     datetime default CURRENT_TIMESTAMP null,
    enable      varchar(32)                        null
)
    comment '订单表' engine = InnoDB;

create table tag
(
    tagID       bigint auto_increment comment '标签ID'
        primary key,
    tagName     varchar(255)      null comment '标签名称',
    description text              null comment '标签描述',
    enable      tinyint default 1 null comment '1表示启用,0表示禁用',
    gmtCreate   datetime          null comment '创建时间',
    gmtModified datetime          null comment '更新时间'
)
    comment '标签表' engine = InnoDB;

create table user
(
    userID           bigint auto_increment comment '用户ID'
        primary key,
    username         varchar(255)                                          null comment '用户名',
    nickname         varchar(255)                                          null comment '昵称',
    password         varchar(255)                                          null comment '密码',
    email            varchar(255)                                          null comment '邮箱',
    birthday         date                                                  null comment '生日',
    accountLevel     int                         default 0                 null comment '账户等级',
    fans             int                         default 0                 null comment '粉丝数',
    follows          int                         default 0                 null comment '关注数',
    videoCount       int                         default 0                 null comment '视频数',
    totalLikes       int                         default 0                 null comment '用户发布的视频的总点赞数',
    membershipStatus enum ('Active', 'Inactive') default 'Inactive'        null comment '会员状态',
    registrationDate datetime                    default CURRENT_TIMESTAMP null comment '注册日期',
    lastLoginDate    datetime                                              null comment '最后登录日期',
    imgUrl           varchar(255)                                          null comment '头像路径',
    enable           tinyint                     default 1                 null comment '1表示已启用,0表示未启用',
    gmtCreate        datetime                                              null comment '创建时间',
    gmtModified      datetime                                              null comment '更新时间'
)
    comment '用户表' engine = InnoDB;

create table userlikecomment
(
    likeID      bigint auto_increment comment '点赞ID'
        primary key,
    userID      bigint                             null comment '用户ID',
    commentID   bigint                             null comment '评论ID',
    likeDate    datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    enable      tinyint  default 1                 null comment '1表示启用,0表示禁用',
    typeEnable  tinyint                            null comment '1表示点赞,0表示点踩',
    gmtCreate   datetime                           null comment '创建时间',
    gmtModified datetime                           null comment '更新时间'
)
    comment '用户点赞评论表' engine = InnoDB;

create table userlikereplie
(
    likeID      bigint auto_increment comment '点赞ID'
        primary key,
    userID      bigint                             null comment '用户ID',
    replyID     bigint                             null comment '回复ID',
    likeDate    datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    enable      tinyint  default 1                 null comment '1表示启用,0表示禁用',
    typeEnable  tinyint                            null comment '1表示点赞,0表示点踩',
    gmtCreate   datetime                           null comment '创建时间',
    gmtModified datetime                           null comment '更新时间'
)
    comment '用户点赞回复表' engine = InnoDB;

create table video
(
    videoID       bigint auto_increment comment '视频ID'
        primary key,
    userID        bigint                                                             null comment '用户ID',
    title         varchar(255)                                                       null comment '视频标题',
    description   text                                                               null comment '视频描述',
    uploadDate    datetime                                 default CURRENT_TIMESTAMP null comment '上传日期',
    duration      time                                                               null comment '视频时长',
    videoSrcUrl   varchar(255)                                                       null comment '视频地址',
    videoImgUrl   varchar(255)                                                       null comment '封面地址',
    viewCount     int                                      default 0                 null comment '观看次数',
    likeCount     int                                      default 0                 null comment '喜欢次数',
    favoriteCount int                                      default 0                 null comment '收藏次数',
    dislikeCount  int                                      default 0                 null comment '点踩数',
    barrageCount  int                                      default 0                 null comment '弹幕数',
    commentCount  int                                      default 0                 null comment '评论数',
    reviewStatus  enum ('Pending', 'Approved', 'Rejected') default 'Pending'         null comment '审核状态',
    enable        tinyint                                  default 0                 null comment '1表示启用,0表示禁用',
    gmtCreate     datetime                                                           null comment '创建时间',
    gmtModified   datetime                                                           null comment '更新时间'
)
    comment '视频表' engine = InnoDB;

create table videocategory
(
    videoID     bigint            null comment '视频ID',
    categoryID  bigint            null comment '分类ID',
    enable      tinyint default 1 null comment '1表示启用,0表示禁用',
    gmtCreate   datetime          null comment '创建时间',
    gmtModified datetime          null comment '更新时间'
)
    comment '视频分类关联表' engine = InnoDB;

create table videoepisode
(
    episodesID      bigint auto_increment comment '选集ID'
        primary key,
    episodesName    varchar(255)      null comment '选集名称',
    scription       varchar(255)      null comment '选集描述',
    howManyEpisodes int     default 0 null comment '集数',
    enable          tinyint default 1 null comment '1表示启用,0表示禁用',
    gmtCreate       datetime          null comment '创建时间',
    gmtModified     datetime          null comment '更新时间'
)
    comment '视频选集表' engine = InnoDB;

create table videoepisoderelation
(
    videoID     bigint            null,
    episodesID  bigint            null,
    episodesNum int               null comment '第---集',
    enable      tinyint default 1 null comment '1表示启用,0表示禁用',
    gmtCreate   datetime          null comment '创建时间',
    gmtModified datetime          null comment '更新时间'
)
    comment '视频选集关联表' engine = InnoDB;

create table videotag
(
    videoID     bigint            null comment '视频ID',
    tagID       bigint            null comment '标签ID',
    enable      tinyint default 1 null comment '1表示启用,0表示禁用',
    gmtCreate   datetime          null comment '创建时间',
    gmtModified datetime          null comment '更新时间'
)
    comment '视频标签关联表' engine = InnoDB;


