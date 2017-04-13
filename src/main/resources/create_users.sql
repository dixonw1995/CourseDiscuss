CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE user_roles (
    user_role_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_role_id),
    FOREIGN KEY (username) REFERENCES users(username)
);

INSERT INTO users (username, password) VALUES ('keith', '$2a$10$ZBEy.THkYX9Y9aHn1pxwv.7FyeGgUPuC5WuEr8v/N97.wewAysN1u');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_USER');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_ADMIN');

INSERT INTO users VALUES ('andrew', '$2a$10$8J1B9BZR0wDnVuMcIh9H2e5IYgiQo2VW/pYRJyEDf3.8v96RfathO');
INSERT INTO user_roles(username, role) VALUES ('andrew', 'ROLE_ADMIN');

INSERT INTO users VALUES ('maria', '$2a$10$uDnJYy.hZtHVNcCjtP5cGOISNBKBVx.hWq1Ji7k2kyu1oN3Cs1IOi');
INSERT INTO user_roles(username, role) VALUES ('maria', 'ROLE_USER');

INSERT INTO users VALUES ('oliver', '$2a$10$G.tp7.mgJQ6jHQTfLy3SlOt5wnLd3GSphcsrGAJi2DcZVLPhszixO');
INSERT INTO user_roles(username, role) VALUES ('oliver', 'ROLE_USER');

INSERT INTO users VALUES ('joker', '$2a$10$BdL/t6iMFyoYpSXaM8oPIedXzCLnIsPJpJQ5SGMtYCk1AaWdBVn1i');
INSERT INTO user_roles(username, role) VALUES ('joker', 'ROLE_BANNED');
INSERT INTO user_roles(username, role) VALUES ('joker', 'ROLE_ADMIN');

INSERT INTO users VALUES ('batman', '$2a$10$BdL/t6iMFyoYpSXaM8oPIedXzCLnIsPJpJQ5SGMtYCk1AaWdBVn1i');
INSERT INTO user_roles(username, role) VALUES ('batman', 'ROLE_BANNED');
INSERT INTO user_roles(username, role) VALUES ('batman', 'ROLE_ADMIN');
INSERT INTO user_roles(username, role) VALUES ('batman', 'ROLE_USER');

INSERT INTO users VALUES ('quinn', '$2a$10$oB/efKFVOW.3piofh/uDguxVcFOmR0i2U7LR/9jOwyoJjTw2piw6C');
INSERT INTO user_roles(username, role) VALUES ('quinn', 'ROLE_BANNED');

INSERT INTO users VALUES ('penguin', '$2a$10$kjRan.eohKegpn7Rln4tgeNVPCEl.Gl/L0vsh7euXtV1x9kvYU3pO');
INSERT INTO user_roles(username, role) VALUES ('penguin', 'ROLE_BANNED');
INSERT INTO user_roles(username, role) VALUES ('penguin', 'ROLE_USER');
