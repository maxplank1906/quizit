-- QuizIt - Sprint 2 Database Script
-- Auto-executed by Spring Boot SQL initialization

CREATE TABLE IF NOT EXISTS questions (
    id             SERIAL PRIMARY KEY,
    quiz_name      VARCHAR(150)  NOT NULL,
    question_text  VARCHAR(1000) NOT NULL,
    option_a       VARCHAR(300)  NOT NULL,
    option_b       VARCHAR(300)  NOT NULL,
    correct_option VARCHAR(5)    NOT NULL CHECK (correct_option IN ('A', 'B'))
);

INSERT INTO questions (quiz_name, question_text, option_a, option_b, correct_option)
SELECT 'Data Structures Mid-Term', 'What is the time complexity of binary search?', 'O(log n)', 'O(n)', 'A'
WHERE NOT EXISTS (
    SELECT 1 FROM questions WHERE question_text = 'What is the time complexity of binary search?'
);

INSERT INTO questions (quiz_name, question_text, option_a, option_b, correct_option)
SELECT 'Data Structures Mid-Term', 'Which data structure uses LIFO order?', 'Queue', 'Stack', 'B'
WHERE NOT EXISTS (
    SELECT 1 FROM questions WHERE question_text = 'Which data structure uses LIFO order?'
);

INSERT INTO questions (quiz_name, question_text, option_a, option_b, correct_option)
SELECT 'Networking Basics', 'Which layer handles routing in the OSI model?', 'Data Link Layer', 'Network Layer', 'B'
WHERE NOT EXISTS (
    SELECT 1 FROM questions WHERE question_text = 'Which layer handles routing in the OSI model?'
);

INSERT INTO questions (quiz_name, question_text, option_a, option_b, correct_option)
SELECT 'Database Systems Q1', 'A primary key must be unique and not null.', 'True', 'False', 'A'
WHERE NOT EXISTS (
    SELECT 1 FROM questions WHERE question_text = 'A primary key must be unique and not null.'
);
