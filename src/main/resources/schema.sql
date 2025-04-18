
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS dashboard CASCADE;

CREATE TABLE users (
  user_id       BIGINT     NOT NULL,
  email         VARCHAR    NOT NULL UNIQUE,
  nickname      VARCHAR    NOT NULL,
  created_at    TIMESTAMP  NOT NULL,
  user_rank     BIGINT     DEFAULT 0,
  user_score    BIGINT     DEFAULT 0
);

CREATE TABLE book (
  book_id         BIGINT         NOT NULL,
  title           VARCHAR        NOT NULL,
  author          VARCHAR        NOT NULL,
  description     VARCHAR(255)   NOT NULL,
  publisher       VARCHAR        NOT NULL,
  published_date  TIMESTAMP      NOT NULL,
  isbn            VARCHAR        UNIQUE,
  thumbnailUrl    VARCHAR,
  thumbnailImage  TEXT,
  review_count    BIGINT         DEFAULT 0,
  book_rating     BIGINT         DEFAULT 0,
  created_at      TIMESTAMP      NOT NULL,
  updated_at      TIMESTAMP      NOT NULL,
  book_rank       BIGINT         DEFAULT 0,
  book_score      BIGINT         DEFAULT 0
);

CREATE TABLE review (
  review_id      BIGINT     NOT NULL,
  book_id        BIGINT     NOT NULL,
  user_id        BIGINT     NOT NULL,
  review_content TEXT       NOT NULL,
  like_count     BIGINT     DEFAULT 0,
  comment_count  BIGINT     DEFAULT 0,
  review_rating  BIGINT     NOT NULL,
  created_at     TIMESTAMP  NOT NULL,
  updated_at     TIMESTAMP  NOT NULL,
  review_rank    BIGINT     DEFAULT 0,
  review_score   BIGINT     DEFAULT 0,
  liked          BOOLEAN    DEFAULT false
);

CREATE TABLE comment (
  comment_id     BIGINT     NOT NULL,
  review_id      BIGINT     NOT NULL,
  book_id        BIGINT     NOT NULL,
  user_id        BIGINT     NOT NULL,
  content        TEXT       NOT NULL,
  created_at     TIMESTAMP  NOT NULL,
  updated_at     TIMESTAMP  NOT NULL
);

CREATE TABLE notification (
  notification_id BIGINT     NOT NULL,
  review_id       BIGINT     NOT NULL,
  book_id         BIGINT     NOT NULL,
  user_id         BIGINT     NOT NULL,
  content         TEXT       NOT NULL,
  confirmed       BOOLEAN    DEFAULT false NOT NULL,
  created_at      TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at      TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE dashboard (
  id               BIGINT     NOT NULL,
  type             VARCHAR    NOT NULL,
  period           VARCHAR    NOT NULL,
  user_id          VARCHAR    NULL,
  score            DOUBLE PRECISION NOT NULL,
  rank             BIGINT     NOT NULL,
  like_count       BIGINT     DEFAULT 0,
  comment_count    BIGINT     DEFAULT 0,
  review_count     BIGINT     DEFAULT 0,
  review_rating    DOUBLE PRECISION DEFAULT 0,
  review_score_sum DOUBLE PRECISION DEFAULT 0,
  created_at       TIMESTAMP  NOT NULL,
  updated_at       TIMESTAMP  NOT NULL,
  target_id        VARCHAR    NOT NULL,
  book_id          BIGINT     NULL,
  review_id        BIGINT     NULL
);

-- PRIMARY KEYS
ALTER TABLE users ADD CONSTRAINT pk_users_user_id PRIMARY KEY (user_id);
ALTER TABLE book ADD CONSTRAINT pk_book_book_id PRIMARY KEY (book_id);
ALTER TABLE review ADD CONSTRAINT pk_review_id PRIMARY KEY (review_id, book_id, user_id);
ALTER TABLE comment ADD CONSTRAINT pk_comment_id PRIMARY KEY (comment_id, review_id, book_id, user_id);
ALTER TABLE notification ADD CONSTRAINT pk_notification_id PRIMARY KEY (notification_id, review_id, book_id, user_id);
ALTER TABLE dashboard ADD CONSTRAINT pk_dashboard_id PRIMARY KEY (id);

-- FOREIGN KEYS
ALTER TABLE review
  ADD CONSTRAINT fk_review_book_id FOREIGN KEY (book_id) REFERENCES book(book_id);

ALTER TABLE review
  ADD CONSTRAINT fk_review_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE comment
  ADD CONSTRAINT fk_comment_review_id FOREIGN KEY (review_id) REFERENCES review(review_id);

ALTER TABLE comment
  ADD CONSTRAINT fk_comment_book_id FOREIGN KEY (book_id) REFERENCES review(book_id);

ALTER TABLE comment
  ADD CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES review(user_id);

ALTER TABLE notification
  ADD CONSTRAINT fk_notification_review_id FOREIGN KEY (review_id) REFERENCES review(review_id);

ALTER TABLE notification
  ADD CONSTRAINT fk_notification_book_id FOREIGN KEY (book_id) REFERENCES review(book_id);

ALTER TABLE notification
  ADD CONSTRAINT fk_notification_user_id FOREIGN KEY (user_id) REFERENCES review(user_id);
