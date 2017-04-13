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


INSERT INTO poll(question) VALUES ('Which one is your favourite character?');
INSERT INTO response(poll_id, content) VALUES (1, 'joker');
INSERT INTO response(poll_id, content) VALUES (1, 'batman');
INSERT INTO response(poll_id, content) VALUES (1, 'quinn');
INSERT INTO response(poll_id, content) VALUES (1, 'penguin');

INSERT INTO poll(question) VALUES ('How would you reply a "hi" message?');
INSERT INTO response(poll_id, content) VALUES (2, 'hi back');
INSERT INTO response(poll_id, content) VALUES (2, 'start some topic');
INSERT INTO response(poll_id, content) VALUES (2, 'ignore it');
INSERT INTO response(poll_id, content) VALUES (2, 'delete that guy from contact');

INSERT INTO poll(question) VALUES ('What is the most important?');
INSERT INTO response(poll_id, content) VALUES (3, 'earn money');
INSERT INTO response(poll_id, content) VALUES (3, 'study to earn money');
INSERT INTO response(poll_id, content) VALUES (3, 'take money from parent');
INSERT INTO response(poll_id, content) VALUES (3, 'money');

