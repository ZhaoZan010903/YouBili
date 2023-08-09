DROP DATABASE youbili_test;

CREATE DATABASE youbili_test;

create table admins
(
    AdminID   int auto_increment comment '管理员ID'
        primary key,
    AdminName varchar(255) null comment '管理员名称',
    Password  varchar(255) null comment '密码',
    Email     varchar(255) null comment '邮箱'
)
    comment '管理员表';

create table categories
(
    CategoryID   int auto_increment comment '分类ID'
        primary key,
    CategoryName varchar(255) null comment '分类名称',
    Description  text         null comment '分类描述'
)
    comment '分类表';

create table tags
(
    TagID       int auto_increment comment '标签ID'
        primary key,
    TagName     varchar(255) null comment '标签名称',
    Description text         null comment '标签描述'
)
    comment '标签表';

create table users
(
    UserID           int auto_increment comment '用户ID'
        primary key,
    Username         varchar(255)                                          null comment '用户名',
    Nickname         varchar(255)                                          null comment '昵称',
    Password         varchar(255)                                          null comment '密码',
    Email            varchar(255)                                          null comment '邮箱',
    Birthday         date                                                  null comment '生日',
    AccountLevel     int                         default 0                 null comment '账户等级',
    Fans             int                         default 0                 null comment '粉丝数',
    Follows          int                         default 0                 null comment '关注数',
    VideoCount       int                         default 0                 null comment '视频数',
    TotalLikes       int                         default 0                 null comment '用户发布的视频的总点赞数',
    MembershipStatus enum ('Active', 'Inactive') default 'Inactive'        null comment '会员状态',
    RegistrationDate datetime                    default CURRENT_TIMESTAMP null comment '注册日期',
    LastLoginDate    datetime                                              null comment '最后登录日期',
    constraint Username
        unique (Username)
)
    comment '用户表';

create table feedbacks
(
    FeedbackID   int auto_increment comment '反馈ID'
        primary key,
    UserID       int                null comment '用户ID',
    FeedbackText text               null comment '反馈内容',
    FeedbackDate datetime           null comment '反馈日期',
    IsResolved   enum ('Yes', 'No') null comment '是否已解决',
    AdminID      int                null comment '管理员ID',
    constraint feedbacks_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint feedbacks_ibfk_2
        foreign key (AdminID) references admins (AdminID)
)
    comment '反馈表';

create index AdminID
    on feedbacks (AdminID);

create index UserID
    on feedbacks (UserID);

create table follows
(
    FollowID   int auto_increment comment '关注ID'
        primary key,
    FollowerID int                                null comment '关注者ID',
    FollowedID int                                null comment '被关注者ID',
    FollowDate datetime default CURRENT_TIMESTAMP null comment '关注日期',
    constraint follows_ibfk_1
        foreign key (FollowerID) references users (UserID),
    constraint follows_ibfk_2
        foreign key (FollowedID) references users (UserID)
)
    comment '关注表';

create index FollowedID
    on follows (FollowedID);

create index FollowerID
    on follows (FollowerID);

create definer = root@localhost trigger decrease_follower_count
    after delete
    on follows
    for each row
BEGIN
    UPDATE Users
    SET Fans = Fans - 1
    WHERE UserID = OLD.FollowedID;
END;

create definer = root@localhost trigger decrease_following_count
    after delete
    on follows
    for each row
BEGIN
    UPDATE Users
    SET Follows = Follows - 1
    WHERE UserID = OLD.FollowerID;
END;

create definer = root@localhost trigger increase_follower_count
    after insert
    on follows
    for each row
BEGIN
    UPDATE Users
    SET Fans = Fans + 1
    WHERE UserID = NEW.FollowedID;
END;

create definer = root@localhost trigger increase_following_count
    after insert
    on follows
    for each row
BEGIN
    UPDATE Users
    SET Follows = Follows + 1
    WHERE UserID = NEW.FollowerID;
END;

create table privatemessages
(
    MessageID   int auto_increment comment '私信ID'
        primary key,
    SenderID    int      null comment '发送者ID',
    RecipientID int      null comment '接收者ID',
    MessageText text     null comment '私信内容',
    SendDate    datetime null comment '发送日期',
    constraint privatemessages_ibfk_1
        foreign key (SenderID) references users (UserID),
    constraint privatemessages_ibfk_2
        foreign key (RecipientID) references users (UserID)
)
    comment '私信表';

create index RecipientID
    on privatemessages (RecipientID);

create index SenderID
    on privatemessages (SenderID);

create table videoepisodes
(
    EpisodesID      int auto_increment comment '选集ID'
        primary key,
    EpisodesName    varchar(255)  null comment '选集名称',
    Scription       varchar(255)  null comment '选集描述',
    HowManyEpisodes int default 0 null comment '集数',
    constraint EpisodesID
        unique (EpisodesID)
)
    comment '视频选集表';

create table videos
(
    VideoID       int auto_increment comment '视频ID'
        primary key,
    UserID        int                                                                null comment '用户ID',
    Title         varchar(255)                                                       null comment '视频标题',
    Description   text                                                               null comment '视频描述',
    UploadDate    datetime                                 default CURRENT_TIMESTAMP null comment '上传日期',
    Duration      time                                                               null comment '视频时长',
    ViewCount     int                                      default 0                 null comment '观看次数',
    LikeCount     int                                      default 0                 null comment '喜欢次数',
    FavoriteCount int                                      default 0                 null comment '收藏次数',
    DislikeCount  int                                                                null comment '点踩数',
    ReportCount   int                                      default 0                 null comment '举报次数',
    BarrageCount  int                                      default 0                 null comment '弹幕数',
    CommentCount  int                                      default 0                 null comment '评论数',
    ReviewStatus  enum ('Pending', 'Approved', 'Rejected') default 'Pending'         null comment '审核状态',
    constraint videos_ibfk_1
        foreign key (UserID) references users (UserID)
)
    comment '视频表';

create table barrages
(
    BarrageID   int auto_increment comment '弹幕ID'
        primary key,
    UserID      int  null comment '用户ID',
    VideoID     int  null comment '视频ID',
    BarrageText text null comment '弹幕内容',
    BarrageTime time null comment '弹幕时间',
    constraint barrages_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint barrages_ibfk_2
        foreign key (VideoID) references videos (VideoID)
)
    comment '弹幕表';

create index UserID
    on barrages (UserID);

create index VideoID
    on barrages (VideoID);

create definer = root@localhost trigger increase_barrage_count
    after insert
    on barrages
    for each row
BEGIN
    UPDATE Videos
    SET BarrageCount = BarrageCount + 1
    WHERE VideoID = NEW.VideoID;
END;

create table comments
(
    CommentID    int auto_increment comment '评论ID'
        primary key,
    UserID       int                                null comment '用户ID',
    VideoID      int                                null comment '视频ID',
    CommentText  text                               null comment '评论内容',
    CommentDate  datetime default CURRENT_TIMESTAMP null comment '评论日期',
    LikeCount    int      default 0                 null comment '点赞数',
    DislikeCount int                                null comment '点踩数',
    ReportCount  int                                null comment '举报数',
    constraint comments_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint comments_ibfk_2
        foreign key (VideoID) references videos (VideoID)
)
    comment '评论表';

create table commentreplies
(
    ReplyID       int auto_increment comment '回
复ID'
        primary key,
    CommentID     int                                null comment '评论ID',
    ParentReplyID int                                null comment '被回复的回复ID',
    UserID        int                                null comment '用户ID',
    ReplyText     text                               null comment '回复内容',
    ReplyDate     datetime default CURRENT_TIMESTAMP null comment '回复日期',
    LikeCount     int      default 0                 null comment '点赞数',
    DislikeCount  int                                null comment '点踩数',
    constraint commentreplies_ibfk_1
        foreign key (CommentID) references comments (CommentID),
    constraint commentreplies_ibfk_2
        foreign key (UserID) references users (UserID)
)
    comment '评论回复表';

create index CommentID
    on commentreplies (CommentID);

create index UserID
    on commentreplies (UserID);

create definer = root@localhost trigger update_comment_count_after_delete_reply
    after delete
    on commentreplies
    for each row
BEGIN
    UPDATE Videos
    SET CommentCount = (SELECT COUNT(*)
                        FROM (SELECT * FROM Comments UNION ALL SELECT * FROM CommentReplies) AS TotalComments
                        WHERE VideoID = (SELECT VideoID FROM Comments WHERE CommentID = OLD.CommentID))
    WHERE VideoID = (SELECT VideoID FROM Comments WHERE CommentID = OLD.CommentID);
END;

create definer = root@localhost trigger update_comment_count_after_insert_reply
    after insert
    on commentreplies
    for each row
BEGIN
    -- 如果是回复评论，则直接通过CommentID找到VideoID
    IF NEW.ParentReplyID IS NULL THEN
        UPDATE Videos
        SET CommentCount = CommentCount + 1
        WHERE VideoID = (SELECT VideoID FROM Comments WHERE CommentID = NEW.CommentID);
        -- 如果是回复回复，则通过ParentReplyID找到CommentID，再找到VideoID
    ELSE
        UPDATE Videos
        SET CommentCount = CommentCount + 1
        WHERE VideoID = (SELECT VideoID
                         FROM Comments
                         WHERE CommentID = (SELECT CommentID FROM CommentReplies WHERE ReplyID = NEW.ParentReplyID));
    END IF;
END;

create index UserID
    on comments (UserID);

create index VideoID
    on comments (VideoID);

create definer = root@localhost trigger update_comment_count_after_delete
    after delete
    on comments
    for each row
BEGIN
    UPDATE Videos
    SET CommentCount = (SELECT COUNT(*)
                        FROM (SELECT * FROM Comments UNION ALL SELECT * FROM CommentReplies) AS TotalComments
                        WHERE VideoID = OLD.VideoID)
    WHERE VideoID = OLD.VideoID;
END;

create definer = root@localhost trigger update_comment_count_after_insert
    after insert
    on comments
    for each row
BEGIN
    UPDATE Videos
    SET CommentCount = (SELECT COUNT(*)
                        FROM (SELECT * FROM Comments UNION ALL SELECT * FROM CommentReplies) AS TotalComments
                        WHERE VideoID = NEW.VideoID)
    WHERE VideoID = NEW.VideoID;
END;

create table favorites
(
    FavoriteID   int auto_increment comment '收藏ID'
        primary key,
    UserID       int                                null comment '用户ID',
    VideoID      int                                null comment '视频ID',
    FavoriteDate datetime default CURRENT_TIMESTAMP null comment '收藏日期',
    constraint UserID
        unique (UserID, VideoID),
    constraint favorites_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint favorites_ibfk_2
        foreign key (VideoID) references videos (VideoID)
)
    comment '收藏表';

create index VideoID
    on favorites (VideoID);

create definer = root@localhost trigger decrease_favorite_count
    after delete
    on favorites
    for each row
BEGIN
    -- 当在Favorites表中删除一行数据后，将对应视频的收藏数减1
    UPDATE Videos
    SET FavoriteCount = FavoriteCount - 1
    WHERE VideoID = OLD.VideoID;
END;

create definer = root@localhost trigger increase_favorite_count
    after insert
    on favorites
    for each row
BEGIN
    UPDATE Videos
    SET FavoriteCount = FavoriteCount + 1
    WHERE VideoID = NEW.VideoID;
END;

create table history
(
    HistoryID     int auto_increment comment '历史记录ID'
        primary key,
    UserID        int      null comment '用户ID',
    VideoID       int      null comment '视频ID',
    WatchDate     datetime null comment '观看日期',
    WatchDuration time     null comment '观看时长',
    constraint UserID
        unique (UserID, VideoID),
    constraint history_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint history_ibfk_2
        foreign key (VideoID) references videos (VideoID)
)
    comment '历史记录表';

create index VideoID
    on history (VideoID);

create definer = root@localhost trigger decrease_view_count
    after delete
    on history
    for each row
BEGIN
    -- 当在History表中删除一行数据后，将对应视频的播放量减1
    UPDATE Videos
    SET ViewCount = ViewCount - 1
    WHERE VideoID = OLD.VideoID;
END;

create definer = root@localhost trigger increase_account_level
    after insert
    on history
    for each row
BEGIN
    DECLARE video_count INT;
    SELECT COUNT(*) INTO video_count FROM History WHERE UserID = NEW.UserID;
    IF video_count % 10 = 0 THEN
        UPDATE Users
        SET AccountLevel = AccountLevel + 1
        WHERE UserID = NEW.UserID;
    END IF;
END;

create definer = root@localhost trigger increase_view_count
    after insert
    on history
    for each row
BEGIN
    UPDATE Videos
    SET ViewCount = ViewCount + 1
    WHERE VideoID = NEW.VideoID;
END;

create table likes
(
    LikeID   int auto_increment comment '点赞ID'
        primary key,
    UserID   int                                null comment '用户ID',
    VideoID  int                                null comment '视频ID',
    LikeDate datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    constraint UserID
        unique (UserID, VideoID),
    constraint likes_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint likes_ibfk_2
        foreign key (VideoID) references videos (VideoID)
)
    comment '点赞表';

create index VideoID
    on likes (VideoID);

create definer = root@localhost trigger decrease_like_count
    after delete
    on likes
    for each row
BEGIN
    -- 当在Likes表中删除一行数据后，将对应视频的点赞数减1
    UPDATE Videos
    SET LikeCount = LikeCount - 1
    WHERE VideoID = OLD.VideoID;
END;

create definer = root@localhost trigger decrease_total_like_count
    after delete
    on likes
    for each row
BEGIN
    -- 当在Likes表中删除一行数据后，将对应用户的总点赞数减去该视频的点赞数
    UPDATE Users
    SET TotalLikes = TotalLikes - (SELECT LikeCount FROM Videos WHERE VideoID = OLD.VideoID)
    WHERE UserID = (SELECT UserID FROM Videos WHERE VideoID = OLD.VideoID);
END;

create definer = root@localhost trigger increase_like_count
    after insert
    on likes
    for each row
BEGIN
    UPDATE Videos
    SET LikeCount = LikeCount + 1
    WHERE VideoID = NEW.VideoID;
END;

create definer = root@localhost trigger increase_total_like_count
    after insert
    on likes
    for each row
BEGIN
    -- 当在Likes表中插入一行数据后，将对应用户的总点赞数加上该视频的点赞数
    UPDATE Users
    SET TotalLikes = TotalLikes + (SELECT LikeCount FROM Videos WHERE VideoID = NEW.VideoID)
    WHERE UserID = (SELECT UserID FROM Videos WHERE VideoID = NEW.VideoID);
END;

create table userlikecomments
(
    LikeID    int auto_increment comment '点赞ID'
        primary key,
    UserID    int                                null comment '用户ID',
    CommentID int                                null comment '评论ID',
    LikeDate  datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    constraint userlikecomments_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint userlikecomments_ibfk_2
        foreign key (CommentID) references comments (CommentID)
)
    comment '用户点赞评论表';

create index CommentID
    on userlikecomments (CommentID);

create index UserID
    on userlikecomments (UserID);

create definer = root@localhost trigger decrease_comment_like_count
    after delete
    on userlikecomments
    for each row
BEGIN
    -- 当在UserLikeComments表中删除一行数据后，将对应评论的点赞数减1
    UPDATE Comments
    SET LikeCount = LikeCount - 1
    WHERE CommentID = OLD.CommentID;
END;

create definer = root@localhost trigger increase_comment_like_count
    after insert
    on userlikecomments
    for each row
BEGIN
    -- 当在UserLikeComments表中插入一行数据后，将对应评论的点赞数加1
    UPDATE Comments
    SET LikeCount = LikeCount + 1
    WHERE CommentID = NEW.CommentID;
END;

create table userlikereplies
(
    LikeID   int auto_increment comment '点赞ID'
        primary key,
    UserID   int                                null comment '用户ID',
    ReplyID  int                                null comment '回复ID',
    LikeDate datetime default CURRENT_TIMESTAMP null comment '点赞日期',
    constraint userlikereplies_ibfk_1
        foreign key (UserID) references users (UserID),
    constraint userlikereplies_ibfk_2
        foreign key (ReplyID) references commentreplies (ReplyID)
)
    comment '用户点赞回复表';

create index ReplyID
    on userlikereplies (ReplyID);

create index UserID
    on userlikereplies (UserID);

create definer = root@localhost trigger decrease_reply_like_count
    after delete
    on userlikereplies
    for each row
BEGIN
    -- 当在UserLikeReplies表中删除一行数据后，将对应回复的点赞数减1
    UPDATE CommentReplies
    SET LikeCount = LikeCount - 1
    WHERE ReplyID = OLD.ReplyID;
END;

create definer = root@localhost trigger increase_reply_like_count
    after insert
    on userlikereplies
    for each row
BEGIN
    -- 当在UserLikeReplies表中插入一行数据后，将对应回复的点赞数加1
    UPDATE CommentReplies
    SET LikeCount = LikeCount + 1
    WHERE ReplyID = NEW.ReplyID;
END;

create table videocategories
(
    VideoID    int null comment '视频ID',
    CategoryID int null comment '分类ID',
    constraint VideoID
        unique (VideoID, CategoryID),
    constraint videocategories_ibfk_1
        foreign key (VideoID) references videos (VideoID),
    constraint videocategories_ibfk_2
        foreign key (CategoryID) references categories (CategoryID)
)
    comment '视频分类关联表';

create index CategoryID
    on videocategories (CategoryID);

create table videoepisoderelations
(
    VideoID     int           not null,
    EpisodesID  int           not null,
    EpisodesNum int default 1 not null comment '第---集',
    constraint videoepisoderelations_videoepisodes_EpisodesID_fk
        foreign key (EpisodesID) references videoepisodes (EpisodesID),
    constraint videoepisoderelations_videos_VideoID_fk
        foreign key (VideoID) references videos (VideoID)
)
    comment '视频选集关联表';

create index UserID
    on videos (UserID);

create definer = root@localhost trigger decrease_video_count
    after delete
    on videos
    for each row
BEGIN
    UPDATE Users
    SET VideoCount = VideoCount - 1
    WHERE UserID = OLD.UserID;
END;

create definer = root@localhost trigger increase_video_count
    after insert
    on videos
    for each row
BEGIN
    -- 增加视频以后用户表视频数+1
    UPDATE Users
    SET VideoCount = VideoCount + 1
    WHERE UserID = NEW.UserID;
END;

create table videotags
(
    VideoID int null comment '视频ID',
    TagID   int null comment '标签ID',
    constraint videotags_ibfk_1
        foreign key (VideoID) references videos (VideoID),
    constraint videotags_ibfk_2
        foreign key (TagID) references tags (TagID)
)
    comment '视频标签关联表';

create index TagID
    on videotags (TagID);

create index VideoID
    on videotags (VideoID);


