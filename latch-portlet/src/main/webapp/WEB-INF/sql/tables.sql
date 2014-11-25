create table EP_UserLatch (
	userId LONG,
	accountId VARCHAR(64) null,
	createDate DATE null,
	CONSTRAINT PK_UserLatch PRIMARY KEY(userId, accountId)
);