create table activity
(
    id            varchar(50)                        not null comment '活动ID(支持数字ID和UUID)'
        primary key,
    title         varchar(200)                       not null comment '标题',
    type          varchar(50)                        not null comment '展演/展览/培训/比赛',
    start_time    datetime                           null comment '开始时间',
    end_time      datetime                           null comment '结束时间',
    location      varchar(200)                       null comment '地点',
    description   mediumtext                         null comment '描述',
    status        tinyint  default 0                 not null comment '0草稿 1报名中 2进行中 3已结束',
    cover_file_id bigint                             null comment '封面文件ID',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '活动' charset = utf8mb4
                   row_format = DYNAMIC;

create index idx_status_start
    on activity (status, start_time);

create table activity_signup
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    activity_id varchar(50)                        not null comment '活动ID',
    user_id     bigint                             not null comment '用户ID',
    status      tinyint  default 0                 not null comment '0待审 1通过 2拒绝 3已签到',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint uk_activity_user
        unique (activity_id, user_id)
)
    comment '活动报名' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_user
    on activity_signup (user_id);

create table admin
(
    aid      int auto_increment
        primary key,
    username varchar(20) collate gbk_bin not null,
    name     varchar(20)                 null,
    password varchar(64)                 null,
    email    varchar(255)                null,
    phone    varchar(20)                 null,
    status   int default 1               null,
    lend_num int                         null,
    max_num  int                         null
)
    engine = MyISAM
    charset = gbk
    row_format = DYNAMIC;

create table ai_chat_message
(
    id          bigint auto_increment comment '消息ID'
        primary key,
    session_id  varchar(100)                       not null comment '会话ID',
    role        varchar(20)                        not null comment '角色：user-用户，assistant-AI助手',
    content     text                               not null comment '消息内容',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment 'AI聊天消息记录表' charset = utf8mb4
                               row_format = DYNAMIC;

create index idx_create_time
    on ai_chat_message (create_time);

create index idx_session_id
    on ai_chat_message (session_id);

create table ai_chat_session
(
    id          bigint auto_increment comment '会话ID'
        primary key,
    session_id  varchar(100)                           not null comment '会话唯一标识(UUID)',
    user_id     bigint                                 not null comment '用户ID',
    title       varchar(200) default '新对话'          null comment '会话标题',
    create_time datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint session_id
        unique (session_id)
)
    comment 'AI聊天会话表' charset = utf8mb4
                           row_format = DYNAMIC;

create index idx_create_time
    on ai_chat_session (create_time);

create index idx_session_id
    on ai_chat_session (session_id);

create index idx_user_id
    on ai_chat_session (user_id);

create table b_ad
(
    id          bigint auto_increment
        primary key,
    image       varchar(100) null,
    link        varchar(500) null,
    create_time varchar(30)  null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_classification
(
    id          bigint auto_increment
        primary key,
    title       varchar(100) null,
    create_time varchar(30)  not null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_error_log
(
    id       bigint auto_increment
        primary key,
    ip       varchar(100) null,
    url      varchar(200) null,
    method   varchar(10)  null,
    content  text         null,
    log_time varchar(30)  null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_login_log
(
    id       bigint auto_increment
        primary key,
    username varchar(50)  null,
    ip       varchar(100) null,
    ua       varchar(200) null,
    log_time datetime(6)  null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_notice
(
    id          bigint auto_increment
        primary key,
    title       varchar(100)  null,
    content     varchar(1000) null,
    create_time varchar(30)   null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_op_log
(
    id          bigint auto_increment
        primary key,
    re_ip       varchar(100) null,
    re_time     varchar(30)  null,
    re_ua       varchar(255) null,
    re_url      varchar(200) null,
    re_method   varchar(10)  null,
    re_content  varchar(200) null,
    access_time varchar(30)  null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_tag
(
    id          bigint auto_increment
        primary key,
    title       varchar(100) null,
    create_time varchar(30)  not null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_thing
(
    id                bigint auto_increment
        primary key,
    title             varchar(100)  null,
    cover             varchar(100)  null,
    description       longtext      null,
    price             varchar(50)   null,
    status            varchar(1)    not null,
    score             int default 0 null,
    mobile            varchar(20)   null,
    age               varchar(10)   null,
    sex               varchar(2)    null,
    location          varchar(100)  null,
    create_time       varchar(30)   null,
    pv                int default 0 null,
    recommend_count   int default 0 null,
    wish_count        int default 0 null,
    collect_count     int default 0 null,
    classification_id bigint        null,
    user_id           varchar(20)   null,
    constraint b_thing_ibfk_1
        foreign key (classification_id) references b_classification (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_banner
(
    id          bigint auto_increment
        primary key,
    image       varchar(100) null,
    create_time varchar(30)  null,
    thing_id    bigint       null,
    constraint b_banner_thing_id_3f307d00_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_thing_classification_id_47675ac4_fk_b_classification_id
    on b_thing (classification_id);

create table b_thing_tag
(
    id       bigint auto_increment
        primary key,
    thing_id bigint not null,
    tag_id   bigint not null,
    constraint b_thing_tag_thing_id_tag_id_a5d426b2_uniq
        unique (thing_id, tag_id),
    constraint b_thing_tag_tag_id_d02b28a1_fk_b_tag_id
        foreign key (tag_id) references b_tag (id)
            on update cascade on delete cascade,
    constraint b_thing_tag_thing_id_fb8ecf3f_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_user
(
    id          bigint auto_increment
        primary key,
    username    varchar(50)          null,
    password    varchar(50)          null,
    role        varchar(2)           null,
    status      varchar(1)           not null,
    nickname    varchar(20)          null,
    avatar      varchar(100)         null,
    mobile      varchar(13)          null,
    email       varchar(50)          null,
    gender      varchar(1)           null,
    description longtext             null,
    create_time varchar(30)          null,
    score       int        default 0 null,
    push_email  varchar(40)          null,
    push_switch tinyint(1) default 0 null,
    token       varchar(32)          null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table b_address
(
    id          bigint auto_increment
        primary key,
    name        varchar(100) null,
    mobile      varchar(30)  null,
    description varchar(200) null,
    def         varchar(10)  null,
    create_time varchar(30)  null,
    user_id     bigint       null,
    constraint b_address_ibfk_1
        foreign key (user_id) references b_user (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_address_user_id_a37a8d6a_fk_b_user_id
    on b_address (user_id);

create table b_comment
(
    id           bigint auto_increment
        primary key,
    content      varchar(200)  null,
    comment_time varchar(30)   null,
    like_count   int default 0 not null,
    user_id      bigint        null,
    thing_id     bigint        null,
    constraint b_comment_ibfk_1
        foreign key (user_id) references b_user (id)
            on update cascade on delete cascade,
    constraint b_comment_thing_id_57ab492b_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_comment_user_id_46f0670f_fk_b_user_id
    on b_comment (user_id);

create table b_order
(
    id               bigint auto_increment
        primary key,
    status           varchar(2)    null,
    order_time       varchar(30)   null,
    pay_time         varchar(30)   null,
    thing_id         bigint        null,
    user_id          bigint        null,
    count            int default 0 not null,
    order_number     varchar(13)   null,
    receiver_address varchar(50)   null,
    receiver_name    varchar(20)   null,
    receiver_phone   varchar(20)   null,
    remark           varchar(30)   null,
    constraint b_order_ibfk_1
        foreign key (user_id) references b_user (id)
            on update cascade on delete cascade,
    constraint b_order_thing_id_4e345e2c_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_order_user_id_64854046_fk_b_user_id
    on b_order (user_id);

create table b_thing_collect
(
    id       bigint auto_increment
        primary key,
    thing_id bigint not null,
    user_id  bigint not null,
    constraint b_thing_collect_thing_id_user_id_45b9f252_uniq
        unique (thing_id, user_id),
    constraint b_thing_collect_ibfk_1
        foreign key (user_id) references b_user (id)
            on update cascade on delete cascade,
    constraint b_thing_collect_thing_id_8edce8b3_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_thing_collect_user_id_e5d69968_fk_b_user_id
    on b_thing_collect (user_id);

create table b_thing_wish
(
    id       bigint auto_increment
        primary key,
    thing_id bigint not null,
    user_id  bigint not null,
    constraint b_thing_wish_thing_id_user_id_9d647bbb_uniq
        unique (thing_id, user_id),
    constraint b_thing_wish_ibfk_1
        foreign key (user_id) references b_user (id)
            on update cascade on delete cascade,
    constraint b_thing_wish_thing_id_f0864b16_fk_b_thing_id
        foreign key (thing_id) references b_thing (id)
            on update cascade on delete cascade
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index b_thing_wish_user_id_e2d94f6c_fk_b_user_id
    on b_thing_wish (user_id);

create table book
(
    bid   int auto_increment
        primary key,
    name  varchar(205)                 not null,
    card  varchar(205) charset utf8mb3 not null,
    autho varchar(200)                 null,
    num   int                          not null,
    press varchar(200)                 null,
    type  varchar(255)                 null,
    constraint ISBN
        unique (card)
)
    engine = MyISAM
    charset = gbk
    row_format = DYNAMIC;

create table booktype
(
    tid  int auto_increment
        primary key,
    name varchar(20) not null
)
    engine = MyISAM
    charset = gbk
    row_format = DYNAMIC;

create table course
(
    id            varchar(50)                        not null comment '课程ID(支持数字ID和UUID)'
        primary key,
    title         varchar(200)                       not null comment '标题',
    level         varchar(50)                        null comment '难度等级',
    description   mediumtext                         null comment '描述',
    status        tinyint  default 0                 not null comment '状态',
    cover_file_id bigint                             null comment '封面文件ID',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '课程' charset = utf8mb4
                   row_format = DYNAMIC;

create table course_chapter
(
    id        bigint auto_increment comment '主键ID'
        primary key,
    course_id varchar(50)   not null comment '课程ID',
    title     varchar(200)  not null comment '章节标题',
    content   mediumtext    null comment '内容',
    sort      int default 0 not null comment '排序'
)
    comment '课程章节' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_course_sort
    on course_chapter (course_id, sort);

create table heritage_item
(
    id           varchar(50)                        not null comment '作品ID(支持数字ID和UUID)'
        primary key,
    title        varchar(200)                       not null comment '标题',
    category     varchar(100)                       not null comment '类别',
    region       varchar(100)                       null comment '地区',
    summary      text                               null comment '摘要',
    description  mediumtext                         null comment '描述',
    status       tinyint  default 0                 not null comment '状态 0草稿 1待审 2已发布 3下架',
    creator_id   bigint                             null comment '创建人',
    publish_time datetime                           null comment '发布时间',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '非遗作品' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_category_region_status
    on heritage_item (category, region, status);

create table history
(
    hid       int auto_increment
        primary key,
    aid       int       null,
    bid       int       null,
    card      char(255) null,
    bookname  char(255) null,
    adminname char(255) null,
    username  char(255) null,
    begintime char(255) null,
    endtime   char(255) null,
    status    int       null
)
    engine = MyISAM
    charset = utf8mb3
    row_format = FIXED;

create table inheritor
(
    id             varchar(50)                        not null comment '传承人ID(支持数字ID和UUID)'
        primary key,
    name           varchar(100)                       not null comment '姓名',
    title          varchar(100)                       null comment '称号',
    region         varchar(100)                       null comment '地区',
    bio            mediumtext                         null comment '简介',
    avatar_file_id bigint                             null comment '头像文件',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '传承人' charset = utf8mb4
                     row_format = DYNAMIC;

create index idx_region_name
    on inheritor (region, name);

create table inheritor_item
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    inheritor_id varchar(50) not null comment '传承人ID',
    item_id      varchar(50) not null comment '作品ID',
    constraint uk_inheritor_item
        unique (inheritor_id, item_id)
)
    comment '传承人与作品关联' charset = utf8mb4
                               row_format = DYNAMIC;

create index idx_item
    on inheritor_item (item_id);

create table review_task
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    biz_type      varchar(50)                        not null comment 'ITEM/ACTIVITY/COURSE',
    biz_id        bigint                             not null comment '业务ID',
    title         varchar(200)                       not null comment '标题',
    content       mediumtext                         null comment '内容',
    ai_suggestion mediumtext                         null comment 'AI建议',
    status        tinyint  default 0                 not null comment '0待审 1通过 2驳回',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '内容审核任务' charset = utf8mb4
                           row_format = DYNAMIC;

create index idx_biz
    on review_task (biz_type, biz_id);

create table shop_category
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    parent_id   bigint                             null comment '父类目ID',
    name        varchar(100)                       not null comment '类目名称',
    sort        int      default 0                 not null comment '排序',
    status      tinyint  default 1                 not null comment '状态 0禁用 1启用',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '商城类目' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_parent_sort
    on shop_category (parent_id, sort);

create table shop_order
(
    id                  bigint auto_increment comment '主键ID'
        primary key,
    order_no            varchar(64)                              not null comment '订单号',
    user_id             bigint                                   not null comment '用户ID',
    total_amount        decimal(10, 2) default 0.00              not null comment '订单总金额',
    pay_amount          decimal(10, 2) default 0.00              not null comment '实付金额',
    status              tinyint        default 0                 not null comment '0待支付 1已支付 2已发货 3已完成 4已关闭',
    pay_type            varchar(20)                              null comment 'ALI/WECHAT/OTHER',
    pay_time            datetime                                 null comment '支付时间',
    receiver_address_id bigint                                   null comment '收货地址ID',
    logistics_no        varchar(64)                              null comment '物流单号',
    remark              varchar(200)                             null comment '备注',
    create_time         datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_order_no
        unique (order_no)
)
    comment '订单' charset = utf8mb4
                   row_format = DYNAMIC;

create index idx_user_status_time
    on shop_order (user_id, status, create_time);

create table shop_order_item
(
    id         bigint auto_increment comment '主键ID'
        primary key,
    order_id   bigint         not null comment '订单ID',
    product_id varchar(50)    not null comment 'SPU ID',
    sku_id     varchar(50)    not null comment 'SKU ID',
    title      varchar(200)   not null comment '商品标题',
    sku_title  varchar(200)   null comment 'SKU标题',
    price      decimal(10, 2) not null comment '单价',
    quantity   int            not null comment '数量',
    subtotal   decimal(10, 2) not null comment '小计'
)
    comment '订单明细' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_order
    on shop_order_item (order_id);

create table shop_product
(
    id          varchar(50)                              not null comment '商品ID(支持数字ID和UUID)'
        primary key,
    title       varchar(200)                             not null comment '商品标题',
    subtitle    varchar(255)                             null comment '副标题',
    category_id bigint                                   not null comment '类目ID',
    price       decimal(10, 2) default 0.00              not null comment '商品价格',
    stock       int            default 0                 not null comment '库存数量',
    detail      mediumtext                               null comment '详情',
    status      tinyint        default 1                 not null comment '0下架 1上架',
    create_time datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '商品SPU' charset = utf8mb4
                      row_format = DYNAMIC;

create index idx_category_status
    on shop_product (category_id, status);

create table sys_file_info
(
    id             bigint auto_increment comment '文件ID'
        primary key,
    original_name  varchar(255)                         not null comment '原始文件名',
    file_path      varchar(500)                         not null comment '文件访问路径',
    file_size      bigint                               not null comment '文件大小(字节)',
    file_type      varchar(20)                          not null comment '文件类型(IMG/PDF/TXT/AUDIO/VIDEO)',
    business_type  varchar(50)                          not null comment '业务类型',
    business_id    varchar(50)                          not null comment '业务对象ID(支持数字ID和UUID)',
    business_field varchar(50)                          null comment '业务字段名',
    upload_user_id bigint                               null comment '上传用户ID',
    is_temp        tinyint(1) default 0                 null comment '是否临时文件(0:否 1:是)',
    status         tinyint(1) default 1                 null comment '状态(0:删除 1:正常)',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    expire_time    datetime                             null comment '过期时间(临时文件)'
)
    comment '文件信息表-精简版' charset = utf8mb4
                                row_format = DYNAMIC;

create index idx_business
    on sys_file_info (business_type, business_id);

create index idx_business_field
    on sys_file_info (business_type, business_id, business_field);

create index idx_create_time
    on sys_file_info (create_time);

create index idx_file_path
    on sys_file_info (file_path);

create index idx_is_temp
    on sys_file_info (is_temp);

create index idx_upload_user
    on sys_file_info (upload_user_id);

create table user
(
    id         bigint auto_increment comment '用户ID'
        primary key,
    username   varchar(50)                        not null comment '用户名',
    password   varchar(100)                       not null comment '密码(加密存储)',
    email      varchar(100)                       not null comment '邮箱',
    phone      varchar(20)                        null comment '手机号',
    user_type  varchar(50)                        null comment '角色code',
    name       varchar(50)                        null comment '姓名',
    avatar     varchar(200)                       null comment '头像',
    status     tinyint  default 1                 null comment '状态(0:禁用,1:正常)',
    created_at datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    sex        varchar(255)                       null comment '性别',
    constraint uk_email
        unique (email),
    constraint uk_username
        unique (username)
)
    comment '用户信息表' charset = utf8mb4
                         row_format = DYNAMIC;

create table user_address
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    user_id     bigint                             not null comment '用户ID',
    receiver    varchar(50)                        not null comment '收货人',
    phone       varchar(20)                        not null comment '手机号',
    province    varchar(50)                        not null comment '省',
    city        varchar(50)                        not null comment '市',
    district    varchar(50)                        not null comment '区/县',
    detail      varchar(200)                       not null comment '详细地址',
    is_default  tinyint  default 0                 not null comment '是否默认 0否 1是',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '收货地址' charset = utf8mb4
                       row_format = DYNAMIC;

create index idx_user_default
    on user_address (user_id, is_default);


