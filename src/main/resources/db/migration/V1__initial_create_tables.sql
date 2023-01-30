CREATE TABLE cidade
(
    ibge   INT4         NOT NULL PRIMARY KEY,
    nome   VARCHAR(255) NOT NULL,
    estado VARCHAR(2)   NOT NULL
);

CREATE TABLE cep
(
    cep         INT4        NOT NULL PRIMARY KEY,
    bairro      VARCHAR(255),
    complemento VARCHAR(255),
    logradouro  VARCHAR(255),
    latitude    NUMERIC(17, 14),
    longitude   NUMERIC(17, 14),
    origem      VARCHAR(12) NOT NULL,
    cidade_ibge INT4        NOT NULL REFERENCES cidade
);

CREATE TABLE cep_error
(
    cep           INT4        NOT NULL PRIMARY KEY,
    data_consulta TIMESTAMPTZ NOT NULL
);
