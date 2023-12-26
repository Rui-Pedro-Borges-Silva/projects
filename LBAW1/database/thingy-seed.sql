-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

DROP SCHEMA IF EXISTS "2371" CASCADE;
CREATE SCHEMA IF NOT EXISTS "2371";
SET search_path TO "2371";

-- Cleanup: Drop the tables if they exist to ensure a fresh setup.
DROP TABLE IF EXISTS AuthenticatedUser CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS questiontag CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS Tags CASCADE;
DROP TABLE IF EXISTS UserFeed CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_answer_likes CASCADE;
DROP TYPE IF EXISTS VoteValue CASCADE;

-- Users Table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR NOT NULL,
                       email VARCHAR UNIQUE NOT NULL,
                       password VARCHAR NOT NULL,
                       role VARCHAR DEFAULT 'guest'
);

-- UserFeed Table
CREATE TABLE UserFeed (
                          id SERIAL PRIMARY KEY,
                          userid INTEGER REFERENCES users NOT NULL,
                          Content TEXT NOT NULL
);

-- Tags Table
CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      name TEXT NOT NULL CONSTRAINT tag_name_uk UNIQUE
);

-- Question Table
CREATE TABLE question (
                          id SERIAL PRIMARY KEY,
                          title TEXT NOT NULL,
                          description TEXT,
                          authoruserid INTEGER REFERENCES users(id) ON DELETE CASCADE,
                          textsearchable_index_col tsvector,
                          selected_answer_id INTEGER
);

CREATE INDEX question_textsearch_idx ON question USING GIN(textsearchable_index_col);


CREATE TABLE questiontag (
                             questionid INTEGER REFERENCES question(id) ON DELETE CASCADE,
                             tagid INTEGER REFERENCES tags(id) ON DELETE CASCADE,
                             PRIMARY KEY (questionid, tagid)
);

-- Answers Table
CREATE TABLE answers (
                         id SERIAL PRIMARY KEY,
                         text TEXT NOT NULL,
                         authoruserid INTEGER REFERENCES users(id) ON DELETE CASCADE,
                         questionid INTEGER REFERENCES question(id) ON DELETE CASCADE,
                         votes INTEGER DEFAULT 0
);

ALTER TABLE question ADD CONSTRAINT fk_selected_answer_id FOREIGN KEY (selected_answer_id) REFERENCES answers(id);

CREATE TABLE user_answer_likes (
                                   id SERIAL PRIMARY KEY,
                                   user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                                   answer_id INTEGER REFERENCES answers(id) ON DELETE CASCADE,
                                   UNIQUE(user_id, answer_id)
);


-- notifications Table
CREATE TABLE notifications (
                              NotiID SERIAL PRIMARY KEY,
                              Text TEXT NOT NULL,
                              type VARCHAR(255),
                              is_read BOOLEAN DEFAULT FALSE,
                              related_content_id INTEGER,
                              userid INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- AuthenticatedUser Table
CREATE TABLE AuthenticatedUser (
    AuthenticatedUserID SERIAL PRIMARY KEY,
    userid INTEGER NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Functions and Triggers for tsvector
CREATE OR REPLACE FUNCTION question_tsvector_trigger() RETURNS trigger AS $$
BEGIN
    NEW.textsearchable_index_col := to_tsvector('portuguese', coalesce(NEW.title, '') || ' ' || coalesce(NEW.description, ''));
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER tsvectorupdate_question BEFORE INSERT OR UPDATE ON question
FOR EACH ROW EXECUTE FUNCTION question_tsvector_trigger();

-- Ensure we populate the tsvector column for existing rows
UPDATE question SET textsearchable_index_col = to_tsvector('portuguese', coalesce(title, '') || ' ' || coalesce(description, ''));

INSERT INTO users VALUES (DEFAULT, 'Alice Johnson', 'alice@example.com', '$2y$10$K1NvEsj2H3fZIqaBkOjIueLgXdqf9r0nLpG4OjIaBmF2N3uJ4iFOS');
INSERT INTO users VALUES (DEFAULT, 'Carol White', 'carol@example.com', '$2y$10$X2MvDsj4I7fZKqaCkPjIueOgYdrf8t1nQrH6QjIaDnG4O5wJ6kHUT');
INSERT INTO users VALUES (DEFAULT, 'John Doe', 'admin@example.com', '$2y$10$HfzIhGCCaxqyaIdGgjARSuOKAcm1Uy82YfLuNaajn6JrjLWy9Sj/W', 'admin');
INSERT INTO users VALUES (DEFAULT, 'David Brown', 'david@example.com', '$2y$10$W3NwFsj5J8fZLraDlQjIuePnAdrf9u2oUrI7RjIbEmH5P6xK7lIVG');
INSERT INTO users VALUES (DEFAULT, 'Eva Green', 'eva@example.com', '$2y$10$U4PxGsj6K9fZMraElRjIueQnBdrh0v3pVtJ8SjIcFnH6Q7yK8mJWO');
INSERT INTO users VALUES (DEFAULT, 'Lara G.', 'lara@example.com', '$2y$10$U4PxGsj6K9fZMraElRjIueQnBdrh0vdsafsdfewagKJhiuhuyUYBGYTfO');
INSERT INTO users VALUES (DEFAULT, 'Fiona Maple', 'fiona@example.com', '$2y$10$V5TxHsj7L1fZIqaGmSjIueRgWdrf7s8nMrH9RjIaHnI7S8wJ9lKUT');
INSERT INTO users VALUES (DEFAULT, 'George Oak', 'george@example.com', '$2y$10$Y6UzIsj8M2fZJqaHnTjIueSgXdrf8t9oNsK0TjIbIoI8U9xL0mNVP');
INSERT INTO users VALUES (DEFAULT, 'Helen Pine', 'helen@example.com', '$2y$10$Z7V0Jsj9N3fZKqaIoUjIueTnBdrf9u0pOrL1UjIcJpI9Vaxy1oOWQ');
INSERT INTO users VALUES (DEFAULT, 'User1', 'user1@example.com', '$2y$10$HfzIhGCCaxqyaIdGgjARSuOKAcm1Uy82YfLuNaajn6JrjLWy9Sj/W');
INSERT INTO users VALUES (DEFAULT, 'User2', 'user2@example.com', '$2y$10$HfzIhGCCaxqyaIdGgjARSuOKAcm1Uy82YfLuNaajn6JrjLWy9Sj/W');



INSERT INTO UserFeed VALUES (DEFAULT, 1,'Welcome to our community, Harry!');
INSERT INTO UserFeed VALUES (DEFAULT, 2,'Bree posted a new article on tech trends.');
INSERT INTO UserFeed VALUES (DEFAULT, 3,'Tony shared a photo from his recent trip.');
INSERT INTO UserFeed VALUES (DEFAULT, 4,'Harvey commented on your post.');
INSERT INTO UserFeed VALUES (DEFAULT, 5,'Allison updated her profile picture.');
INSERT INTO UserFeed VALUES (DEFAULT, 6,'Benjamin started a new discussion thread.');


INSERT INTO tags VALUES (DEFAULT, 'Technology');
INSERT INTO tags VALUES (DEFAULT, 'Travel');
INSERT INTO tags VALUES (DEFAULT, 'Photography');
INSERT INTO tags VALUES (DEFAULT, 'WebDevelopment');
INSERT INTO tags VALUES (DEFAULT, 'Design');
INSERT INTO tags VALUES (DEFAULT, 'Programming');
INSERT INTO tags VALUES (DEFAULT, 'Art');
INSERT INTO tags VALUES (DEFAULT, 'Science');
INSERT INTO tags VALUES (DEFAULT, 'Education');


INSERT INTO question VALUES (DEFAULT, 'How to improve web design skills?', 'Looking for resources and tips to enhance my web design abilities.', 1,NULL);
INSERT INTO question VALUES (DEFAULT, 'Best programming language for beginners?', 'I am new to programming and wondering which language I should start learning first.', 2,NULL);
INSERT INTO question VALUES (DEFAULT, 'Recommendations for travel destinations in Europe?', 'Planning a trip to Europe next summer. Any suggestions for must-visit places?', 10,NULL);
INSERT INTO question VALUES (DEFAULT, 'How to start a tech blog?', 'Interested in starting a blog about technology and gadgets. Any advice on getting started?', 4,NULL);
INSERT INTO question VALUES (DEFAULT, 'Effective strategies for online learning?', 'What are some effective strategies for self-learning subjects online?', 5,NULL);
INSERT INTO question VALUES (DEFAULT, 'Best practices for mobile app development?', 'I am developing a mobile app and looking for best practices in design and user experience.', 6,NULL);
INSERT INTO question VALUES (DEFAULT, 'How to make healthy eating habits?', 'Looking for advice on how to start and maintain healthy eating habits.', 7,NULL);
INSERT INTO question VALUES (DEFAULT, 'Tips for beginner photographers?', 'I just got my first camera and would like some tips on beginning photography.', 8,NULL);

INSERT INTO questiontag (QuestionID, TagID) VALUES (1, 1);
INSERT INTO questiontag (QuestionID, TagID) VALUES (1, 2);
INSERT INTO questiontag (QuestionID, TagID) VALUES (1, 3);
INSERT INTO questiontag (QuestionID, TagID) VALUES (1, 4);
INSERT INTO questiontag (QuestionID, TagID) VALUES (4, 1);
INSERT INTO questiontag (QuestionID, TagID) VALUES (2, 2);


INSERT INTO answers VALUES (DEFAULT, 'Start with HTML and CSS, then move to JavaScript for a solid foundation in web design.', 1, 1, 0);
INSERT INTO answers VALUES (DEFAULT, 'Python is great for beginners due to its readability and wide range of applications.', 2, 2, 0);
INSERT INTO answers VALUES (DEFAULT, 'Barcelona is a must-visit for its architecture and vibrant cultural scene.', 3, 3, 0);
INSERT INTO answers VALUES (DEFAULT, 'Porto is best city in the world.', 4, 3, 0);
INSERT INTO answers VALUES (DEFAULT, 'Dont go to Lisbon. Worst city ever.', 7, 3, 0);
INSERT INTO answers VALUES (DEFAULT, 'Choose a specific niche in tech to focus your blog content and gain a dedicated audience.', 4, 4, 0);
INSERT INTO answers VALUES (DEFAULT, 'Online courses with interactive exercises can be very helpful.', 5, 5, 0);
INSERT INTO answers VALUES (DEFAULT, 'To learn online you can watch youtube videos.', 2, 5, 0);
INSERT INTO answers VALUES (DEFAULT, 'You can read Ebooks.', 3, 5, 0);
INSERT INTO answers VALUES (DEFAULT, 'Start by searching on Google.', 1, 5, 0);


INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 10);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 1);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 2);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 3);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 4);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 5);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 6);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 7);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 8);
INSERT INTO notifications VALUES (DEFAULT, 'You have a new follower!', 'follow', false, 1, 9);

-- INSERT INTO notifications VALUES (DEFAULT, 'Your post has been upvoted.', 2);
-- INSERT INTO notifications VALUES (DEFAULT, 'New comment on your answer.', 3);
-- INSERT INTO notifications VALUES (DEFAULT, 'Your question received an answer.', 4);
-- INSERT INTO notifications VALUES (DEFAULT, 'Reminder: Event starting soon.', 5);
-- INSERT INTO notifications VALUES (DEFAULT, 'You earned a new badge!', 6);
-- INSERT INTO notifications VALUES (DEFAULT, 'Profile update successful.', 7);


INSERT INTO AuthenticatedUser VALUES (DEFAULT, 1);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 2);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 3);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 4);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 5);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 6);
INSERT INTO AuthenticatedUser VALUES (DEFAULT, 7);