-- ============================================================================
-- YouBili 2.0 Database Schema (MySQL 8.x, utf8mb4)
-- Refactored:
--   - Removed hardcoded schema prefixes (youbili_v1 / youbili_test)
--   - Removed broken root@localhost triggers (counters maintained transactionally)
--   - Added proper UNIQUE constraints, secondary indexes, and report table
--   - Added default category & tag seed data
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `youbili`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `youbili`;

-- 1. 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
    `adminID`       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    `adminUsername` VARCHAR(100) NOT NULL COMMENT '管理员名称',
    `password`      VARCHAR(255) NOT NULL COMMENT '密码(BCrypt)',
    `nickName`      VARCHAR(60)  NULL COMMENT '管理员昵称',
    `email`         VARCHAR(255) NULL COMMENT '邮箱',
    `enable`        TINYINT      NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_admin_username` (`adminUsername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 2. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `userID`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username`         VARCHAR(64)                           NOT NULL COMMENT '用户名',
    `nickname`         VARCHAR(64)                           NOT NULL COMMENT '昵称',
    `password`         VARCHAR(255)                          NOT NULL COMMENT '密码(BCrypt)',
    `email`            VARCHAR(255)                          NOT NULL COMMENT '邮箱',
    `birthday`         DATE                                  NULL COMMENT '生日',
    `accountLevel`     INT                                   NOT NULL DEFAULT 1 COMMENT '账户等级',
    `fans`             INT                                   NOT NULL DEFAULT 0 COMMENT '粉丝数',
    `follows`          INT                                   NOT NULL DEFAULT 0 COMMENT '关注数',
    `videoCount`       INT                                   NOT NULL DEFAULT 0 COMMENT '视频数',
    `totalLikes`       INT                                   NOT NULL DEFAULT 0 COMMENT '获赞总数',
    `membershipStatus` ENUM('Active', 'Inactive')            NOT NULL DEFAULT 'Inactive' COMMENT '会员状态',
    `registrationDate` DATETIME                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',
    `lastLoginDate`    DATETIME                              NULL COMMENT '最后登录日期',
    `imgUrl`           VARCHAR(512)                          NULL COMMENT '头像路径',
    `enable`           TINYINT                               NOT NULL DEFAULT 1 COMMENT '1表示已启用,0表示禁用',
    `gmtCreate`        DATETIME                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`      DATETIME                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_username` (`username`),
    UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 3. 分类表
CREATE TABLE IF NOT EXISTS `categorie` (
    `categoryID`   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    `categoryName` VARCHAR(64) NOT NULL COMMENT '分类名称',
    `description`  TEXT        NULL COMMENT '分类描述',
    `enable`       TINYINT     NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_category_name` (`categoryName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 4. 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `tagID`       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    `tagName`     VARCHAR(64) NOT NULL COMMENT '标签名称',
    `description` TEXT        NULL COMMENT '标签描述',
    `enable`      TINYINT     NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_tag_name` (`tagName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 5. 视频表
CREATE TABLE IF NOT EXISTS `video` (
    `videoID`       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '视频ID',
    `userID`        BIGINT                                     NOT NULL COMMENT '用户ID',
    `title`         VARCHAR(200)                               NOT NULL COMMENT '视频标题',
    `description`   TEXT                                       NULL COMMENT '视频描述',
    `uploadDate`    DATETIME                                   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传日期',
    `duration`      TIME                                       NULL DEFAULT '00:00:00' COMMENT '视频时长',
    `videoSrcUrl`   VARCHAR(512)                               NOT NULL COMMENT '视频地址',
    `videoImgUrl`   VARCHAR(512)                               NULL COMMENT '封面地址',
    `viewCount`     INT                                        NOT NULL DEFAULT 0 COMMENT '观看次数',
    `likeCount`     INT                                        NOT NULL DEFAULT 0 COMMENT '点赞次数',
    `favoriteCount` INT                                        NOT NULL DEFAULT 0 COMMENT '收藏次数',
    `dislikeCount`  INT                                        NOT NULL DEFAULT 0 COMMENT '点踩数',
    `barrageCount`  INT                                        NOT NULL DEFAULT 0 COMMENT '弹幕数',
    `commentCount`  INT                                        NOT NULL DEFAULT 0 COMMENT '评论数',
    `reviewStatus`  ENUM('Pending', 'Approved', 'Rejected')    NOT NULL DEFAULT 'Approved' COMMENT '审核状态',
    `enable`        TINYINT                                    NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`     DATETIME                                   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`   DATETIME                                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_video_user_status` (`userID`, `reviewStatus`, `enable`),
    KEY `idx_video_upload_date` (`uploadDate`),
    KEY `idx_video_status_enable` (`reviewStatus`, `enable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频表';

-- 6. 视频分类关联表
CREATE TABLE IF NOT EXISTS `videocategory` (
    `videoID`     BIGINT   NOT NULL COMMENT '视频ID',
    `categoryID`  BIGINT   NOT NULL COMMENT '分类ID',
    `enable`      TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_video_category` (`videoID`, `categoryID`),
    KEY `idx_vc_category` (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频分类关联表';

-- 7. 视频标签关联表
CREATE TABLE IF NOT EXISTS `videotag` (
    `videoID`     BIGINT   NOT NULL COMMENT '视频ID',
    `tagID`       BIGINT   NOT NULL COMMENT '标签ID',
    `enable`      TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_video_tag` (`videoID`, `tagID`),
    KEY `idx_vt_tag` (`tagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频标签关联表';

-- 8. 弹幕表 (MySQL 备份/降级存储，MongoDB 为主存储)
CREATE TABLE IF NOT EXISTS `barrage` (
    `barrageID`   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '弹幕ID',
    `userID`      BIGINT       NULL COMMENT '用户ID',
    `videoID`     BIGINT       NOT NULL COMMENT '视频ID',
    `enable`      TINYINT      NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `barrageText` VARCHAR(255) NOT NULL COMMENT '弹幕内容',
    `barrageTime` FLOAT        NOT NULL DEFAULT 0 COMMENT '弹幕出现时间(秒)',
    `color`       VARCHAR(32)  NOT NULL DEFAULT '#FFFFFF' COMMENT '弹幕颜色',
    `type`        VARCHAR(16)  NOT NULL DEFAULT 'scroll' COMMENT '弹幕位置: scroll/top/bottom',
    `gmtCreate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_barrage_video_time` (`videoID`, `barrageTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='弹幕表';

-- 9. 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `commentID`    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    `userID`       BIGINT   NOT NULL COMMENT '用户ID',
    `videoID`      BIGINT   NOT NULL COMMENT '视频ID',
    `commentText`  TEXT     NOT NULL COMMENT '评论内容',
    `commentDate`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论日期',
    `likeCount`    INT      NOT NULL DEFAULT 0 COMMENT '点赞数',
    `dislikeCount` INT      NOT NULL DEFAULT 0 COMMENT '点踩数',
    `enable`       TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示删除',
    `gmtCreate`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_comment_video` (`videoID`, `enable`, `likeCount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 10. 评论回复表
CREATE TABLE IF NOT EXISTS `commentreplie` (
    `replyID`       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '回复ID',
    `commentID`     BIGINT   NOT NULL COMMENT '主评论ID',
    `parentReplyID` BIGINT   NULL COMMENT '被回复的回复ID(0或NULL表示回复主评论)',
    `userID`        BIGINT   NOT NULL COMMENT '用户ID',
    `replyText`     TEXT     NOT NULL COMMENT '回复内容',
    `replyDate`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复日期',
    `likeCount`     INT      NOT NULL DEFAULT 0 COMMENT '点赞数',
    `dislikeCount`  INT      NOT NULL DEFAULT 0 COMMENT '点踩数',
    `enable`        TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示删除',
    `gmtCreate`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_reply_comment` (`commentID`, `enable`, `gmtCreate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论回复表';

-- 11. 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `favoriteID`   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    `userID`       BIGINT   NOT NULL COMMENT '用户ID',
    `videoID`      BIGINT   NOT NULL COMMENT '视频ID',
    `favoriteDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏日期',
    `enable`       TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `gmtCreate`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_favorite_user_video` (`userID`, `videoID`),
    KEY `idx_favorite_video` (`videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 12. 关注表
CREATE TABLE IF NOT EXISTS `follow` (
    `followID`    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关注ID',
    `followerID`  BIGINT   NOT NULL COMMENT '关注者ID',
    `followedID`  BIGINT   NOT NULL COMMENT '被关注者ID',
    `followDate`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注日期',
    `enable`      TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用关注,0表示取消',
    `gmtCreate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_follow_pair` (`followerID`, `followedID`),
    KEY `idx_follow_followed` (`followedID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注表';

-- 13. 观看历史表
CREATE TABLE IF NOT EXISTS `history` (
    `historyID`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '历史记录ID',
    `userID`        BIGINT   NOT NULL COMMENT '用户ID',
    `videoID`       BIGINT   NOT NULL COMMENT '视频ID',
    `watchDate`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '观看日期',
    `watchDuration` TIME     NULL DEFAULT '00:00:00' COMMENT '观看进度',
    `enable`        TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示隐藏',
    `gmtCreate`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_history_user_video` (`userID`, `videoID`),
    KEY `idx_history_user_date` (`userID`, `enable`, `watchDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='历史记录表';

-- 14. 点赞/点踩表
CREATE TABLE IF NOT EXISTS `likes` (
    `likeID`      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    `userID`      BIGINT   NOT NULL COMMENT '用户ID',
    `videoID`     BIGINT   NOT NULL COMMENT '视频ID',
    `likeDate`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作日期',
    `enable`      TINYINT  NOT NULL DEFAULT 1 COMMENT '1表示启用,0表示禁用',
    `typeEnable`  TINYINT  NOT NULL COMMENT '1表示点赞,0表示点踩',
    `gmtCreate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_likes_user_video` (`userID`, `videoID`),
    KEY `idx_likes_video` (`videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞点踩表';

-- 15. 协同过滤行为表
CREATE TABLE IF NOT EXISTS `mahout` (
    `mahoutID` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `userID`   BIGINT   NOT NULL COMMENT '用户ID',
    `videoID`  BIGINT   NOT NULL COMMENT '视频ID',
    `type`     TINYINT  NOT NULL COMMENT '0点踩,1观看,2点赞,3收藏',
    `gmtCreate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_mahout_user_video` (`userID`, `videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户行为推荐表';

-- 16. 热度值表
CREATE TABLE IF NOT EXISTS `heat` (
    `heatID`    BIGINT AUTO_INCREMENT PRIMARY KEY,
    `videoID`   BIGINT   NOT NULL COMMENT '视频ID',
    `upload`    DATETIME NULL COMMENT '上传时间',
    `heatValue` DOUBLE   NOT NULL DEFAULT 0 COMMENT '热度值',
    UNIQUE KEY `uk_heat_video` (`videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频热度值表';

-- 17. 通知表
CREATE TABLE IF NOT EXISTS `notice` (
    `noticeID`    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    `userID`      BIGINT       NULL COMMENT '接收用户ID(NULL表示全站系统公告)',
    `systemName`  VARCHAR(100) NULL DEFAULT 'YouBili 系统' COMMENT '通知发布人',
    `typeEnable`  TINYINT      NOT NULL DEFAULT 1 COMMENT '0表示系统公告,1表示个人通知',
    `isRead`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0未读,1已读',
    `noticeText`  TEXT         NOT NULL COMMENT '通知内容',
    `noticeTime`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知发布时间',
    `gmtCreate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_notice_user_type` (`userID`, `typeEnable`, `noticeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 18. 订单表
CREATE TABLE IF NOT EXISTS `orders` (
    `orderID`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    `tradeName`   VARCHAR(255)   NOT NULL COMMENT '商品名',
    `orderNumber` VARCHAR(128)   NOT NULL COMMENT '商户订单号',
    `zfbNumber`   VARCHAR(128)   NULL COMMENT '支付宝交易号',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '金额',
    `payment`     VARCHAR(32)    NOT NULL COMMENT '支付方式',
    `userID`      BIGINT         NOT NULL COMMENT '购买人ID',
    `payTime`     DATETIME       NULL COMMENT '支付时间',
    `enable`      VARCHAR(32)    NOT NULL DEFAULT '待支付' COMMENT '订单状态: 待支付/已支付/已取消',
    `gmtCreate`   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_orders_number` (`orderNumber`),
    KEY `idx_orders_user` (`userID`, `orderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 19. 违规举报表 (新增，对接前端举报功能)
CREATE TABLE IF NOT EXISTS `report` (
    `reportID`    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '举报ID',
    `userID`      BIGINT       NOT NULL COMMENT '举报人ID',
    `targetType`  VARCHAR(32)  NOT NULL COMMENT '目标类型: VIDEO / COMMENT / USER',
    `targetID`    BIGINT       NOT NULL COMMENT '目标ID',
    `category`    VARCHAR(64)  NOT NULL COMMENT '违规性质',
    `reasons`     VARCHAR(255) NULL COMMENT '细分违规标签(逗号分隔)',
    `description` TEXT         NOT NULL COMMENT '详细描述',
    `status`      VARCHAR(32)  NOT NULL DEFAULT 'Pending' COMMENT '处理状态: Pending/Resolved/Rejected',
    `gmtCreate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmtModified` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_report_user` (`userID`),
    KEY `idx_report_target` (`targetType`, `targetID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='违规举报表';

-- ============================================================================
-- Seed Data: 默认分区与标签
-- ============================================================================
INSERT IGNORE INTO `categorie` (`categoryID`, `categoryName`, `description`) VALUES
    (1, '动画', '原创动画、二次元番剧与MAD剪辑'),
    (2, '电影', '院线大片、独立电影与影评解说'),
    (3, '电视剧', '热播剧集、经典回顾与剧情解析'),
    (4, '综艺', '热门综艺、访谈与娱乐现场'),
    (5, '科技', '数码评测、AI前沿与编程开发'),
    (6, '游戏', '主机/PC实况、电竞赛事与攻略'),
    (7, '音乐', '翻唱、MV、乐器演奏与现场演出'),
    (8, '生活', 'Vlog、美食探店、旅行与日常分享');

INSERT IGNORE INTO `tag` (`tagID`, `tagName`, `description`) VALUES
    (1, '原创', 'UP主原创制作'),
    (2, '搞笑', '轻松幽默与整活'),
    (3, '热血', '高燃混剪与励志'),
    (4, '科普', '知识分享与硬核科普'),
    (5, '教程', '实战教学与技能提升'),
    (6, '治愈', '温暖放松与风景'),
    (7, '鬼畜', '鬼畜调教与创意二创'),
    (8, 'Vlog', '生活记录与随拍');

INSERT IGNORE INTO `notice` (`noticeID`, `userID`, `systemName`, `typeEnable`, `isRead`, `noticeText`) VALUES
    (1, NULL, 'YouBili 官方', 0, 0, '欢迎来到全新重构的 YouBili 2.0！前端全面升级至 Vue 3 + TypeScript + Vite + Element Plus + Tailwind CSS，后端升级至 Spring Boot 3.5 + Java 21。');
