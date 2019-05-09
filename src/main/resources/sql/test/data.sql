INSERT INTO tour_user(USERNAME, NICKNAME, AVATAR, SELF_INTRO, GENDER, BIRTH, REGION_ID, EMAIL, USER_TYPE, PASSWORD,
                      ENABLED, CREATED_AT, UPDATED_AT)
VALUES ('admin', 'Admin', '/static/image/avatar.jpg', 'Hello World', 0, null, null, 'admin@example.com', 1,
        '$2a$10$q8zi3h9DwNJqHsg7fK7D3.ZNYc5Tb8de6XveTMBCKsnli8RZIKeFy', 1,
        1557367889435, 1557367889435);

-- 密码是 admin

INSERT INTO tour_user(USERNAME, NICKNAME, AVATAR, SELF_INTRO, GENDER, BIRTH, REGION_ID, EMAIL, USER_TYPE, PASSWORD,
                      ENABLED, CREATED_AT, UPDATED_AT)
VALUES ('hello', 'Hello', '/static/image/avatar.jpg', '你好世界', 0, null, null, 'hello@example.com', 0,
        '$2a$10$zkwKAl5BjYtnyPGDl1eM3up6hAiAMXv6aCWVnM.OwXYnAdOxEWFhq', 1,
        1557367889435, 1557367889435);

-- 密码是 hello
