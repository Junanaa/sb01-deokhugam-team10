DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS dashboard CASCADE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USERS
CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(60) NOT NULL,
                       nickname VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       user_rank BIGINT DEFAULT 0,
                       user_score BIGINT DEFAULT 0,
                       is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- BOOK
CREATE TABLE book (
                      book_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                      title VARCHAR NOT NULL,
                      author VARCHAR NOT NULL,
                      description VARCHAR(255) NOT NULL,
                      publisher VARCHAR NOT NULL,
                      published_date TIMESTAMP NOT NULL,
                      isbn VARCHAR UNIQUE,
                      thumbnailUrl VARCHAR,
                      thumbnailImage TEXT,
                      review_count BIGINT DEFAULT 0,
                      book_rating BIGINT DEFAULT 0,
                      created_at TIMESTAMP NOT NULL DEFAULT now(),
                      updated_at TIMESTAMP NOT NULL DEFAULT now(),
                      book_rank BIGINT DEFAULT 0,
                      book_score BIGINT DEFAULT 0
);

-- REVIEW
CREATE TABLE review (
                        review_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        book_id UUID NOT NULL,
                        user_id UUID NOT NULL,
                        review_content TEXT NOT NULL,
                        like_count BIGINT DEFAULT 0,
                        comment_count BIGINT DEFAULT 0,
                        review_rating BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT now(),
                        updated_at TIMESTAMP NOT NULL DEFAULT now(),
                        review_rank BIGINT DEFAULT 0,
                        review_score BIGINT DEFAULT 0,
                        liked BOOLEAN DEFAULT false
);

-- COMMENT
CREATE TABLE comment (
                         comment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         review_id UUID NOT NULL,
                         book_id UUID NOT NULL,
                         user_id UUID NOT NULL,
                         content TEXT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- NOTIFICATION
CREATE TABLE notification (
                              notification_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              review_id UUID NOT NULL,
                              book_id UUID NOT NULL,
                              user_id UUID NOT NULL,
                              content TEXT NOT NULL,
                              confirmed BOOLEAN DEFAULT false NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- DASHBOARD
CREATE TABLE dashboard (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           type VARCHAR NOT NULL,
                           period VARCHAR NOT NULL,
                           user_id UUID NULL,
                           score DOUBLE PRECISION NOT NULL,
                           rank BIGINT NOT NULL,
                           like_count BIGINT DEFAULT 0,
                           comment_count BIGINT DEFAULT 0,
                           review_count BIGINT DEFAULT 0,
                           review_rating DOUBLE PRECISION DEFAULT 0,
                           review_score_sum DOUBLE PRECISION DEFAULT 0,
                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                           updated_at TIMESTAMP NOT NULL DEFAULT now(),
                           target_id UUID NOT NULL,
                           book_id UUID NULL,
                           review_id UUID NULL
);

-- FOREIGN KEYS
ALTER TABLE review ADD CONSTRAINT fk_review_book_id FOREIGN KEY (book_id) REFERENCES book(book_id);
ALTER TABLE review ADD CONSTRAINT fk_review_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE comment ADD CONSTRAINT fk_comment_review_id FOREIGN KEY (review_id) REFERENCES review(review_id);
ALTER TABLE comment ADD CONSTRAINT fk_comment_book_id FOREIGN KEY (book_id) REFERENCES book(book_id);
ALTER TABLE comment ADD CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE notification ADD CONSTRAINT fk_notification_review_id FOREIGN KEY (review_id) REFERENCES review(review_id);
ALTER TABLE notification ADD CONSTRAINT fk_notification_book_id FOREIGN KEY (book_id) REFERENCES book(book_id);
ALTER TABLE notification ADD CONSTRAINT fk_notification_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);
