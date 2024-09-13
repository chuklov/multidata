DROP ROLE IF EXISTS demo_role;
CREATE ROLE demo_role WITH SUPERUSER;

GRANT demo_role TO admin;

CREATE DATABASE fantasy OWNER admin;
CREATE DATABASE horror OWNER admin;
CREATE DATABASE drama OWNER admin;

GRANT ALL PRIVILEGES ON DATABASE comedy TO admin;
GRANT ALL PRIVILEGES ON DATABASE fantasy TO admin;
GRANT ALL PRIVILEGES ON DATABASE horror TO admin;
GRANT ALL PRIVILEGES ON DATABASE drama TO admin;

-- Connect to each database and create the movies table with corresponding data

-- 1. Comedy Database
\c comedy;

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    info TEXT,
    genre VARCHAR(50),
    length VARCHAR(10)
);

INSERT INTO movies (name, info, genre, length) VALUES
('The Hangover', 'A group of friends wake up with no memory of the previous night''s bachelor party.', 'Comedy', '100 min'),
('Step Brothers', 'Two grown men become step brothers and hilarity ensues.', 'Comedy', '106 min'),
('Superbad', 'Two high school friends try to make the most of their last night before graduation.', 'Comedy', '113 min'),
('Anchorman', 'A 1970s news anchorman struggles with the rise of women in the workplace.', 'Comedy', '94 min'),
('Dumb and Dumber', 'Two dimwitted friends go on a cross-country road trip.', 'Comedy', '107 min'),
('21 Jump Street', 'Two cops go undercover in a high school to bust a drug ring.', 'Comedy', '109 min'),
('The Other Guys', 'Two misfit detectives investigate a case.', 'Comedy', '107 min'),
('Tropic Thunder', 'A group of actors shooting a war movie get caught in a real conflict.', 'Comedy', '107 min'),
('Zombieland', 'A group of survivors navigate a world overrun by zombies.', 'Comedy', '88 min'),
('Shaun of the Dead', 'A man decides to win back his ex during a zombie apocalypse.', 'Comedy', '99 min');

-- 2. Fantasy Database
\c fantasy;

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    info TEXT,
    genre VARCHAR(50),
    length VARCHAR(10)
);

INSERT INTO movies (name, info, genre, length) VALUES
('The Lord of the Rings: The Fellowship of the Ring', 'A young hobbit embarks on a dangerous journey to destroy a powerful ring.', 'Fantasy', '178 min'),
('Harry Potter and the Sorcerer''s Stone', 'A boy discovers he is a wizard and attends a magical school.', 'Fantasy', '152 min'),
('The Chronicles of Narnia', 'Siblings travel through a wardrobe to the magical land of Narnia.', 'Fantasy', '143 min'),
('The Hobbit: An Unexpected Journey', 'A hobbit is recruited to help a group of dwarves reclaim their mountain home.', 'Fantasy', '169 min'),
('The Princess Bride', 'A young woman is rescued from an evil prince by a farmhand who loves her.', 'Fantasy', '98 min'),
('Pan''s Labyrinth', 'A young girl discovers a magical labyrinth during a war.', 'Fantasy', '118 min'),
('Stardust', 'A young man ventures into a magical kingdom to retrieve a fallen star.', 'Fantasy', '127 min'),
('The NeverEnding Story', 'A boy reads a magical book that draws him into the story.', 'Fantasy', '102 min'),
('The Dark Crystal', 'Two gelflings must restore balance to their world by healing a broken crystal.', 'Fantasy', '93 min'),
('Willow', 'A farmer is tasked with protecting a special baby from an evil queen.', 'Fantasy', '126 min');

-- 3. Horror Database
\c horror;

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    info TEXT,
    genre VARCHAR(50),
    length VARCHAR(10)
);

INSERT INTO movies (name, info, genre, length) VALUES
('The Exorcist', 'A priest tries to save a girl possessed by a demon.', 'Horror', '122 min'),
('Halloween', 'A masked killer stalks babysitters on Halloween night.', 'Horror', '91 min'),
('A Nightmare on Elm Street', 'A killer haunts teenagers in their dreams.', 'Horror', '101 min'),
('The Shining', 'A man goes mad while looking after an isolated hotel.', 'Horror', '146 min'),
('Hereditary', 'A family unravels after the death of their grandmother.', 'Horror', '127 min'),
('It', 'A group of children are terrorized by an evil clown.', 'Horror', '135 min'),
('The Texas Chainsaw Massacre', 'A group of friends are hunted by a chainsaw-wielding killer.', 'Horror', '83 min'),
('The Conjuring', 'Paranormal investigators try to help a family experiencing hauntings.', 'Horror', '112 min'),
('Get Out', 'A man uncovers a disturbing secret while visiting his girlfriend''s family.', 'Horror', '104 min'),
('Saw', 'Two men wake up in a room, chained to opposite ends with a deadly game to play.', 'Horror', '103 min');

-- 4. Drama Database
\c drama;

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    info TEXT,
    genre VARCHAR(50),
    length VARCHAR(10)
);

INSERT INTO movies (name, info, genre, length) VALUES
('The Shawshank Redemption', 'A banker is imprisoned for a crime he didn''t commit and forms a life-changing friendship.', 'Drama', '142 min'),
('Forrest Gump', 'The story of a man who unwittingly influences historical events.', 'Drama', '142 min'),
('Fight Club', 'A man forms an underground fight club to escape his mundane life.', 'Drama', '139 min'),
('The Godfather', 'The story of a powerful mafia family.', 'Drama', '175 min'),
('A Beautiful Mind', 'The life of a brilliant mathematician who struggles with schizophrenia.', 'Drama', '135 min'),
('Schindler''s List', 'A businessman saves thousands of Jews during World War II.', 'Drama', '195 min'),
('Gladiator', 'A Roman general seeks revenge after being betrayed by the emperor''s son.', 'Drama', '155 min'),
('The Pursuit of Happyness', 'A man struggles to raise his son and pursue a better life.', 'Drama', '117 min'),
('12 Years a Slave', 'The true story of a free man sold into slavery.', 'Drama', '134 min'),
('The Green Mile', 'A prison guard bonds with a death row inmate who possesses mysterious powers.', 'Drama', '189 min');




