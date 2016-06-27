BEGIN TRANSACTION;
CREATE TABLE user_friend(user_friend_seriseid integer auto_increment,self_uuid varchar(32) not null,friend_uuid varchar(32) unique not null,friend_ip varchar(20) unique,primary key(user_friend_seriseid));
INSERT INTO `user_friend` VALUES (NULL,'4c37987f8e13461dbf0c133af338c039','4d8e938015b34049a21fad31a3e29820','120.0.2.1');
INSERT INTO `user_friend` VALUES (NULL,'4c37987f8e13461dbf0c133af338c039','04fb00752bc94d84b718d9a41e024c7a','241.31.12.53');
INSERT INTO `user_friend` VALUES (NULL,'4c37987f8e13461dbf0c133af338c032','4c37987f8e13461dbf0c133af338c000','121.222.11.2');
INSERT INTO `user_friend` VALUES (NULL,'04fb00752bc94d84b718d9a41e024c7a','04fb00752bc94d84b718d9a41e024c00','121.222.11.23');
COMMIT;
