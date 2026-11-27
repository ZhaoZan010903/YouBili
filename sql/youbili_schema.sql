-- ============================================================================
-- YouBili 2.0 Database Schema (PostgreSQL 15+)
-- ============================================================================

-- 1. 管理员表
CREATE TABLE IF NOT EXISTS admins (
    admin_id       BIGSERIAL PRIMARY KEY,
    admin_username VARCHAR(100) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,
    nick_name      VARCHAR(60),
    email          VARCHAR(255),
    enable         SMALLINT     NOT NULL DEFAULT 1,
    gmt_create     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. 用户表
CREATE TABLE IF NOT EXISTS users (
    user_id           BIGSERIAL PRIMARY KEY,
    username          VARCHAR(64)  NOT NULL UNIQUE,
    nickname          VARCHAR(64)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    birthday          DATE,
    account_level     INTEGER      NOT NULL DEFAULT 1,
    fans              INTEGER      NOT NULL DEFAULT 0,
    follows           INTEGER      NOT NULL DEFAULT 0,
    video_count       INTEGER      NOT NULL DEFAULT 0,
    total_likes       INTEGER      NOT NULL DEFAULT 0,
    membership_status VARCHAR(32)  NOT NULL DEFAULT 'Inactive'
        CHECK (membership_status IN ('Active', 'Inactive')),
    registration_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_date   TIMESTAMP,
    img_url           VARCHAR(512),
    enable            SMALLINT     NOT NULL DEFAULT 1,
    gmt_create        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. 分类表
CREATE TABLE IF NOT EXISTS categories (
    category_id   BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(64) NOT NULL UNIQUE,
    description   TEXT,
    enable        SMALLINT    NOT NULL DEFAULT 1,
    gmt_create    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. 标签表
CREATE TABLE IF NOT EXISTS tags (
    tag_id       BIGSERIAL PRIMARY KEY,
    tag_name     VARCHAR(64) NOT NULL UNIQUE,
    description  TEXT,
    enable       SMALLINT    NOT NULL DEFAULT 1,
    gmt_create   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. 视频表
CREATE TABLE IF NOT EXISTS videos (
    video_id       BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    title          VARCHAR(200) NOT NULL,
    description    TEXT,
    upload_date    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration       TIME         DEFAULT '00:00:00',
    video_src_url  VARCHAR(512) NOT NULL,
    video_img_url  VARCHAR(512),
    view_count     INTEGER      NOT NULL DEFAULT 0,
    like_count     INTEGER      NOT NULL DEFAULT 0,
    favorite_count INTEGER      NOT NULL DEFAULT 0,
    dislike_count  INTEGER      NOT NULL DEFAULT 0,
    barrage_count  INTEGER      NOT NULL DEFAULT 0,
    comment_count  INTEGER      NOT NULL DEFAULT 0,
    review_status  VARCHAR(32)  NOT NULL DEFAULT 'Approved'
        CHECK (review_status IN ('Pending', 'Approved', 'Rejected')),
    enable         SMALLINT     NOT NULL DEFAULT 1,
    gmt_create     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_videos_user_status ON videos (user_id, review_status, enable);
CREATE INDEX IF NOT EXISTS idx_videos_upload_date ON videos (upload_date DESC);
CREATE INDEX IF NOT EXISTS idx_videos_status_enable ON videos (review_status, enable);

-- 6. 视频分类关联表
CREATE TABLE IF NOT EXISTS video_categories (
    video_id     BIGINT    NOT NULL,
    category_id  BIGINT    NOT NULL,
    enable       SMALLINT  NOT NULL DEFAULT 1,
    gmt_create   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_video_categories UNIQUE (video_id, category_id)
);
CREATE INDEX IF NOT EXISTS idx_vc_category ON video_categories (category_id);

-- 7. 视频标签关联表
CREATE TABLE IF NOT EXISTS video_tags (
    video_id     BIGINT    NOT NULL,
    tag_id       BIGINT    NOT NULL,
    enable       SMALLINT  NOT NULL DEFAULT 1,
    gmt_create   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_video_tags UNIQUE (video_id, tag_id)
);
CREATE INDEX IF NOT EXISTS idx_vt_tag ON video_tags (tag_id);

-- 8. 弹幕表 (PostgreSQL 备份/降级存储，MongoDB 为主存储)
CREATE TABLE IF NOT EXISTS barrages (
    barrage_id   BIGSERIAL PRIMARY KEY,
    user_id      BIGINT,
    video_id     BIGINT       NOT NULL,
    enable       SMALLINT     NOT NULL DEFAULT 1,
    barrage_text VARCHAR(255) NOT NULL,
    barrage_time REAL         NOT NULL DEFAULT 0,
    color        VARCHAR(32)  NOT NULL DEFAULT '#FFFFFF',
    type         VARCHAR(16)  NOT NULL DEFAULT 'scroll',
    gmt_create   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_barrages_video_time ON barrages (video_id, barrage_time);

-- 9. 评论表
CREATE TABLE IF NOT EXISTS comments (
    comment_id    BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    video_id      BIGINT    NOT NULL,
    comment_text  TEXT      NOT NULL,
    comment_date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    like_count    INTEGER   NOT NULL DEFAULT 0,
    dislike_count INTEGER   NOT NULL DEFAULT 0,
    enable        SMALLINT  NOT NULL DEFAULT 1,
    gmt_create    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_comments_video ON comments (video_id, enable, like_count DESC);

-- 10. 评论回复表
CREATE TABLE IF NOT EXISTS comment_replies (
    reply_id        BIGSERIAL PRIMARY KEY,
    comment_id      BIGINT    NOT NULL,
    parent_reply_id BIGINT,
    user_id         BIGINT    NOT NULL,
    reply_text      TEXT      NOT NULL,
    reply_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    like_count      INTEGER   NOT NULL DEFAULT 0,
    dislike_count   INTEGER   NOT NULL DEFAULT 0,
    enable          SMALLINT  NOT NULL DEFAULT 1,
    gmt_create      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_replies_comment ON comment_replies (comment_id, enable, gmt_create ASC);

-- 11. 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    favorite_id   BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    video_id      BIGINT    NOT NULL,
    favorite_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enable        SMALLINT  NOT NULL DEFAULT 1,
    gmt_create    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_favorites_user_video UNIQUE (user_id, video_id)
);
CREATE INDEX IF NOT EXISTS idx_favorites_video ON favorites (video_id);

-- 12. 关注表
CREATE TABLE IF NOT EXISTS follows (
    follow_id    BIGSERIAL PRIMARY KEY,
    follower_id  BIGINT    NOT NULL,
    followed_id  BIGINT    NOT NULL,
    follow_date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enable       SMALLINT  NOT NULL DEFAULT 1,
    gmt_create   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_follows_pair UNIQUE (follower_id, followed_id)
);
CREATE INDEX IF NOT EXISTS idx_follows_followed ON follows (followed_id);

-- 13. 观看历史表
CREATE TABLE IF NOT EXISTS watch_history (
    history_id     BIGSERIAL PRIMARY KEY,
    user_id        BIGINT    NOT NULL,
    video_id       BIGINT    NOT NULL,
    watch_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    watch_duration TIME      DEFAULT '00:00:00',
    enable         SMALLINT  NOT NULL DEFAULT 1,
    gmt_create     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_watch_history_user_video UNIQUE (user_id, video_id)
);
CREATE INDEX IF NOT EXISTS idx_watch_history_user_date ON watch_history (user_id, enable, watch_date DESC);

-- 14. 点赞/点踩表
CREATE TABLE IF NOT EXISTS video_likes (
    like_id      BIGSERIAL PRIMARY KEY,
    user_id      BIGINT    NOT NULL,
    video_id     BIGINT    NOT NULL,
    like_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    enable       SMALLINT  NOT NULL DEFAULT 1,
    type_enable  SMALLINT  NOT NULL,
    gmt_create   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_video_likes_user_video UNIQUE (user_id, video_id)
);
CREATE INDEX IF NOT EXISTS idx_video_likes_video ON video_likes (video_id);

-- 15. 用户协同过滤行为表
CREATE TABLE IF NOT EXISTS user_behaviors (
    behavior_id BIGSERIAL PRIMARY KEY,
    user_id     BIGINT    NOT NULL,
    video_id    BIGINT    NOT NULL,
    type        SMALLINT  NOT NULL,
    gmt_create  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_user_behaviors_pair ON user_behaviors (user_id, video_id);

-- 16. 热度值表
CREATE TABLE IF NOT EXISTS video_heat (
    heat_id    BIGSERIAL PRIMARY KEY,
    video_id   BIGINT           NOT NULL UNIQUE,
    upload     TIMESTAMP,
    heat_value DOUBLE PRECISION NOT NULL DEFAULT 0
);

-- 17. 通知表
CREATE TABLE IF NOT EXISTS notices (
    notice_id    BIGSERIAL PRIMARY KEY,
    user_id      BIGINT,
    system_name  VARCHAR(100) DEFAULT 'YouBili 系统',
    type_enable  SMALLINT     NOT NULL DEFAULT 1,
    is_read      SMALLINT     NOT NULL DEFAULT 0,
    notice_text  TEXT         NOT NULL,
    notice_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_create   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_notices_user_type ON notices (user_id, type_enable, notice_id DESC);

-- 18. 订单表
CREATE TABLE IF NOT EXISTS orders (
    order_id     BIGSERIAL PRIMARY KEY,
    trade_name   VARCHAR(255)   NOT NULL,
    order_number VARCHAR(128)   NOT NULL UNIQUE,
    zfb_number   VARCHAR(128),
    price        NUMERIC(10, 2) NOT NULL,
    payment      VARCHAR(32)    NOT NULL,
    user_id      BIGINT         NOT NULL,
    pay_time     TIMESTAMP,
    status       VARCHAR(32)    NOT NULL DEFAULT '待支付',
    gmt_create   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders (user_id, order_id DESC);

-- 19. 违规举报表
CREATE TABLE IF NOT EXISTS reports (
    report_id    BIGSERIAL PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    target_type  VARCHAR(32)  NOT NULL,
    target_id    BIGINT       NOT NULL,
    category     VARCHAR(64)  NOT NULL,
    reasons      VARCHAR(255),
    description  TEXT         NOT NULL,
    status       VARCHAR(32)  NOT NULL DEFAULT 'Pending',
    gmt_create   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_reports_user ON reports (user_id);
CREATE INDEX IF NOT EXISTS idx_reports_target ON reports (target_type, target_id);

-- ============================================================================
-- Seed Data: 默认分区、标签与系统公告
-- ============================================================================
INSERT INTO categories (category_id, category_name, description) VALUES
    (1, '动画', '原创动画、二次元番剧与MAD剪辑'),
    (2, '电影', '院线大片、独立电影与影评解说'),
    (3, '电视剧', '热播剧集、经典回顾与剧情解析'),
    (4, '综艺', '热门综艺、访谈与娱乐现场'),
    (5, '科技', '数码评测、AI前沿与编程开发'),
    (6, '游戏', '主机/PC实况、电竞赛事与攻略'),
    (7, '音乐', '翻唱、MV、乐器演奏与现场演出'),
    (8, '生活', 'Vlog、美食探店、旅行与日常分享')
ON CONFLICT (category_id) DO NOTHING;

INSERT INTO tags (tag_id, tag_name, description) VALUES
    (1, '原创', 'UP主原创制作'),
    (2, '搞笑', '轻松幽默与整活'),
    (3, '热血', '高燃混剪与励志'),
    (4, '科普', '知识分享与硬核科普'),
    (5, '教程', '实战教学与技能提升'),
    (6, '治愈', '温暖放松与风景'),
    (7, '鬼畜', '鬼畜调教与创意二创'),
    (8, 'Vlog', '生活记录与随拍')
ON CONFLICT (tag_id) DO NOTHING;

INSERT INTO notices (notice_id, user_id, system_name, type_enable, is_read, notice_text) VALUES
    (1, NULL, 'YouBili 官方', 0, 0, '欢迎来到全新重构的 YouBili 2.0！前端全面升级至 Vue 3 + TypeScript + Vite + Element Plus + Tailwind CSS，后端升级至 Spring Boot 3.5 + Java 21 + PostgreSQL + MongoDB + Elasticsearch。')
ON CONFLICT (notice_id) DO NOTHING;

-- 同步自增序列，避免后续插入主键冲突
SELECT setval(pg_get_serial_sequence('categories', 'category_id'), COALESCE((SELECT MAX(category_id) FROM categories), 1));
SELECT setval(pg_get_serial_sequence('tags', 'tag_id'), COALESCE((SELECT MAX(tag_id) FROM tags), 1));
SELECT setval(pg_get_serial_sequence('notices', 'notice_id'), COALESCE((SELECT MAX(notice_id) FROM notices), 1));
