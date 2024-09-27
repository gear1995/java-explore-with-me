CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(255)                            NOT NULL,
    pinned BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(250)                            NOT NULL,
    email VARCHAR(254)                            NOT NULL UNIQUE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY                        NOT NULL,
    annotation         VARCHAR(2000)                                                  NOT NULL,
    category_id        BIGINT                                                         NOT NULL,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP                                                      NOT NULL,
    description        varchar(7000)                                                  NOT NULL,
    eventDate          TIMESTAMP                                                      NOT NULL,
    initiator_id       BIGINT,
    location_id        BIGINT                                                         NOT NULL,
    paid               BOOLEAN,
    participant_limit  INT,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    state              TEXT CHECK (events.state IN ('PUBLISH_EVENT', 'REJECT_EVENT')) NOT NULL,
    title              VARCHAR(120)                                                   NOT NULL,
    views              INT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY         NOT NULL,
    requester BIGINT                                          NOT NULL,
    created   TIMESTAMP,
    event     BIGINT                                          NOT NULL,
    status    TEXT CHECK (requests.status IN ('PENDING', 'CONFIRMED',
                                              'CANCELED',
                                              'REJECTED')) NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_to_users FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_events FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE
);