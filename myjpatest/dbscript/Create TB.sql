create database mymoive;
use mymoive;

CREATE TABLE `movie` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '电影名',
  `director` varchar(100) NOT NULL COMMENT '导演',
  `actor` varchar(100) NOT NULL COMMENT '演员',
  `year` int NOT NULL COMMENT '上映年份',
  `mins` int NOT NULL COMMENT '总时长（分钟）',
  `placard` varchar(255) COMMENT '封面海报' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='电影信息表';

CREATE TABLE `cinema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '电影院名',
  `city` varchar(100) NOT NULL COMMENT '城市',
  `address` varchar(100) NOT NULL COMMENT '地址',
  `hallcount` int NOT NULL COMMENT '放映厅数量' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='院线信息表';

CREATE TABLE `schedul` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL COMMENT '电影ID',
  `cinema_id` bigint NOT NULL COMMENT '院校ID',
  `hall` int NOT NULL COMMENT '放映厅',
  `price` int NOT NULL COMMENT '票价',
  `showtime` TIMESTAMP NOT NULL COMMENT '开始时间' ,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`movie_id`) REFERENCES movie(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY(`cinema_id`) REFERENCES cinema(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='排片信息表';

CREATE TABLE `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `schedul_id` bigint NOT NULL COMMENT '排片ID',
  `phone` varchar(13) NOT NULL COMMENT '订票手机号',
  `bookingtime` TIMESTAMP NOT NULL COMMENT '订票时间' ,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`schedul_id`) REFERENCES schedul(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='订票信息表';

show tables;