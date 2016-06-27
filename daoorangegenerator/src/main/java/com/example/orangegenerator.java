package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class orangegenerator {

    public static int DBVersion=1;
    public static final String OUTPUTCODEPATH="/work/CSDNBlog4/app/src/main/dbAutoCreateCode";
    public static void main(String[] args){
        Schema schema=new Schema(DBVersion,"com.orange.blog.database.bean");
        schema.setDefaultJavaPackageDao("com.orange.blog.database.dao");


        //create table
//        addUserInfoTable(schema);
        addUserFriendTable(schema);

        try {
            new DaoGenerator().generateAll(schema,OUTPUTCODEPATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addUserFriendTable(Schema schema) {
        Entity entity=schema.addEntity("UserFriends");
        entity.addStringProperty("selfUUID").notNull();
        entity.addStringProperty("friendUUID").notNull().unique();
        entity.addStringProperty("friendIP").notNull();
        entity.addStringProperty("friendNickName").notNull();
    }

    /**
     * 创建用户表
     * @param schema
     */
    private static void addUserInfoTable(Schema schema) {
        Entity entity=schema.addEntity("User");
        entity.addStringProperty("selfUUID").notNull();
    }

}
