CREATE TABLE terrain (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    adresse     VARCHAR(255) NOT NULL,
    type        VARCHAR(10)  NOT NULL CHECK (type IN ('FOOT_5', 'FOOT_7'))
);

CREATE TABLE reservation (
    id                BIGSERIAL PRIMARY KEY,
    terrain_id        BIGINT       NOT NULL REFERENCES terrain(id),
    date_reservation  DATE         NOT NULL,
    heure_debut       TIME         NOT NULL,
    nom_client        VARCHAR(100) NOT NULL,
    date_creation     TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT uk_reservation_creneau UNIQUE (terrain_id, date_reservation, heure_debut)
);