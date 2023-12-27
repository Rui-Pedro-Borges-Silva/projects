DROP TABLE IF EXISTS AgentTeam;
DROP TABLE IF EXISTS Hashtag;
DROP TABLE IF EXISTS Inquirie;
DROP TABLE IF EXISTS Faqs;
DROP TABLE IF EXISTS Team;
DROP TABLE IF EXISTS Department;
DROP TABLE IF EXISTS Ticket;
DROP TABLE IF EXISTS User;

PRAGMA foreign_keys = ON;

CREATE TABLE User (
    userId INTEGER,
    username TEXT CONSTRAINT user_username_null NOT NULL,
    password TEXT CONSTRAINT user_password_null NOT NULL,
    email TEXT CONSTRAINT user_email_null NOT NULL,
    name TEXT CONSTRAINT user_name_null NOT NULL,
    type TEXT CONSTRAINT user_type_null NOT NULL,
    image TEXT,
    PRIMARY KEY(userId)
);

CREATE TABLE Ticket (
    ticketId INTEGER,
    name TEXT CONSTRAINT ticket_name_null NOT NULL,
    type TEXT CONSTRAINT ticket_type_null NOT NULL,
    status TEXT CONSTRAINT ticket_status_null NOT NULL,
    info TEXT CONSTRAINT ticket_info_null NOT NULL,
    priority INTEGER,
    -- file BLOB CONSTRAINT ticket_file_null NOT NULL,
    agentId INTEGER,
    departmentId INTEGER,
    clientId INTEGER,
    PRIMARY KEY(ticketId),
    FOREIGN KEY (agentId) REFERENCES User(userId),
    FOREIGN KEY (clientId) REFERENCES User(userId),
    FOREIGN KEY (departmentId) REFERENCES Department(departmentId)
);

CREATE TABLE Department (
    departmentId INTEGER,
    name TEXT CONSTRAINT department_name_null NOT NULL,
    availableTeams INTEGER CONSTRAINT department_availableTeams_null NOT NULL,
    PRIMARY KEY(departmentId)
);


CREATE TABLE Team (
    teamId INTEGER,
    name TEXT CONSTRAINT team_name_null NOT NULL,
    capacity INTEGER CONSTRAINT team_capacity_null NOT NULL,
    numberOfTickets INTEGER CONSTRAINT team_numberOfTickets_null NOT NULL,
    departmentId INTEGER,
    PRIMARY KEY(teamId),
    FOREIGN KEY(departmentId) REFERENCES Department(departmentId)
);

CREATE TABLE Faqs (
    faqId INTEGER,
    question TEXT CONSTRAINT faqs_question_null NOT NULL,
    answer TEXT CONSTRAINT faqs_answer_null NOT NULL,
    PRIMARY KEY(faqId)
);

CREATE TABLE Inquirie (
    inquirieId INTEGER,
    question TEXT CONSTRAINT inquirie_question_null NOT NULL,
    answer TEXT CONSTRAINT inquirie_answer_null NOT NULL,
    agentId INTEGER,
    clientId INTEGER,
    PRIMARY KEY(inquirieId),
    FOREIGN KEY(agentId) REFERENCES User(userId),
    FOREIGN KEY(clientId) REFERENCES User(userId)
);

CREATE TABLE Hashtag (
    hashtagId INTEGER,
    name TEXT CONSTRAINT hashtag_name_null NOT NULL,
    ticketId INTEGER, 
    PRIMARY KEY(hashtagId),
    FOREIGN KEY(ticketId) REFERENCES Ticket(ticketId)
);

CREATE TABLE AgentTeam (
    agentId INTEGER,
    teamId INTEGER,
    FOREIGN KEY(agentId) REFERENCES User(userId),
    FOREIGN KEY(teamId) REFERENCES Team(teamId),
    PRIMARY KEY(agentId, teamId)
);

/*******************************************************************************
   Populate Tables
********************************************************************************/

-- Insert sample users
INSERT INTO User (userId, username, password, email, name, type, image)
VALUES (1, 'user1', 'password1', 'user1@example.com', 'John Smith', 'client', 'da'),
       (2, 'user2', 'password2', 'user2@example.com', 'Sarah Johnson', 'client', 'da'),
       (3, 'user3', 'password3', 'user3@example.com', 'David Lee', 'agent', 'da'),
       (4, 'user4', 'password4', 'user4@example.com', 'Lisa Chen', 'agent', 'da'),
       (5, 'admin1', 'password5', 'admin1@example.com', 'Mike Johnson', 'admin', 'da'),
       (6, 'admin2', 'password6', 'admin2@example.com', 'Jessica Lee', 'admin', 'da');
       

-- Insert sample departments
INSERT INTO Department (departmentId, name, availableTeams)
VALUES (1, 'Sales', 2),
       (2, 'Support', 3);

-- Insert sample teams
INSERT INTO Team (teamId, name, capacity, numberOfTickets, departmentId)
VALUES (1, 'Sales Team 1', 5, 0, 1),
       (2, 'Sales Team 2', 5, 0, 1),
       (3, 'Support Team 1', 3, 0, 2),
       (4, 'Support Team 2', 3, 0, 2),
       (5, 'Support Team 3', 3, 0, 2);

-- Assign agents to teams
INSERT INTO AgentTeam (agentId, teamId)
VALUES (3, 1),
       (3, 2),
       (4, 3),
       (4, 4),
       (4, 5);

-- Insert sample tickets
INSERT INTO Ticket (ticketId, name, type, status, info, priority, agentId, departmentId, clientId)
VALUES (1, 'Ticket 1', 'Sales', 'Open', 'This is a sample ticket.', 1, 3, 1, 1),
       (2, 'Ticket 2', 'Support', 'Open', 'This is another sample ticket.', 2, 4, 2, 2),
       (3, 'Ticket 3', 'Sales', 'Closed', 'This is a closed ticket.', 3, 3, 1, 1),
       (4, 'Ticket 4', 'Support', 'Closed', 'This is another closed ticket.', 1, 4, 2, 2);

-- Insert sample FAQs
INSERT INTO Faqs (faqId, question, answer)
VALUES (1, 'What is your return policy?', 'Our return policy is...'),
       (2, 'How do I reset my password?', 'To reset your password, go to...'),
       (3, 'How can I contact customer support?', 'You can contact our customer support team by...'),
        (4, 'What payment methods do you accept?', 'We accept credit cards, PayPal, and bank transfers.'),
        (5, 'Can I cancel my order?', 'Yes, you can cancel your order within 24 hours of placing it.'),
        (6, 'Do you offer international shipping?', 'Yes, we offer international shipping to most countries.'),
        (7, 'What is your privacy policy?', 'Our privacy policy outlines how we collect, use, and protect your personal information.'),
        (8, 'How long does shipping usually take?', 'Shipping times vary depending on your location. Typically, it takes 3-5 business days.'),
        (9, 'Do you offer discounts for bulk orders?', 'Yes, we offer discounts for bulk orders. Please contact our sales team for more information.'),
        (10, 'What is your warranty policy?', 'Our products come with a 1-year warranty against manufacturing defects.');

-- Insert sample inquiries
INSERT INTO Inquirie (inquirieId, question, answer, agentId, clientId)
VALUES (1, 'Can you help me with this issue?', 'Sure, I can help you with that.', 3, 1),
       (2, 'I have a question about my order.', 'What is your question?', 4, 2);

-- Insert sample hashtags
INSERT INTO Hashtag (hashtagId, name, ticketId)
VALUES (1, 'CustomerService', 1),
       (2, 'ProductSupport', 2),
       (3, 'Sales', 1),
       (4, 'Billing', 2);
