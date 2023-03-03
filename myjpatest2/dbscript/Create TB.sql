create database mymoive;
use mymoive;

CREATE TABLE `tb_movie` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '电影名',
  `director` varchar(100) NOT NULL COMMENT '导演',
  `actor` varchar(100) NOT NULL COMMENT '演员',
  `year` int NOT NULL COMMENT '上映年份',
  `mins` int NOT NULL COMMENT '总时长（分钟）',
  `placard` varchar(255) COMMENT '封面海报' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='电影信息表';

CREATE TABLE `tb_cinema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '电影院名',
  `city` varchar(100) NOT NULL COMMENT '城市',
  `address` varchar(100) NOT NULL COMMENT '地址',
  `hallcount` int NOT NULL COMMENT '放映厅数量' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='院线信息表';

CREATE TABLE `tb_schedul` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL COMMENT '电影ID',
  `cinema_id` bigint NOT NULL COMMENT '院校ID',
  `hall` int NOT NULL COMMENT '放映厅',
  `price` int NOT NULL COMMENT '票价',
  `showtime` TIMESTAMP NOT NULL COMMENT '开始时间' ,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`movie_id`) REFERENCES tb_movie(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY(`cinema_id`) REFERENCES tb_cinema(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='排片信息表';

CREATE TABLE `tb_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `schedul_id` bigint NOT NULL COMMENT '排片ID',
  `phone` varchar(13) NOT NULL COMMENT '订票手机号',
  `bookingtime` TIMESTAMP NOT NULL COMMENT '订票时间' ,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`schedul_id`) REFERENCES tb_schedul(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  COMMENT='订票信息表';

show tables;
drop table `tb_ticket`;
DROP TABLE `tb_schedul`;
drop table `tb_cinema`;
drop table `tb_movie`;
select * from tb_movie;
insert into tb_movie(name,director,actor,placard,year,mins) values('流浪地球 2','郭帆','吴京','https://img1.doubanio.com/view/photo/m/public/p2885955777.webp',2023,173);
insert into tb_movie(name,director,actor,placard,year,mins) values('满江红','张艺谋','易烊千玺','https://img1.doubanio.com/view/photo/m/public/p2886465677.webp',2023,159);
insert into tb_movie(name,director,actor,placard,year,mins) values('流浪地球','郭帆','吴京','https://img2.doubanio.com/view/photo/m/public/p2545472803.webp',2019,125);
insert into tb_movie(name,director,actor,placard,year,mins) values('无名','程耳','梁朝伟','https://img1.doubanio.com/view/photo/m/public/p2886187418.webp',2023,128);
