create table orders (
    id          VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at  TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at  TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at  TIMESTAMP,
    shipper_id  VARCHAR(255)    NOT NULL,
    load_owner  VARCHAR(255)    NOT NULL,
    department  VARCHAR(255)    NOT NULL,
    equipment   VARCHAR(255)    NOT NULL,
    admin_note  VARCHAR(255)
) ;
create        index on orders(shipper_id);

create table loads (
    id              VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at      TIMESTAMP,
    order_id        VARCHAR(255)    NOT NULL REFERENCES orders(id),
    sequence        INTEGER         NOT NULL,
    shipper_notes   VARCHAR(255)
);
create        index on loads(order_id);

create type state_code as enum(
    'AL',
    'AK',
    'AS',
    'AZ',
    'AR',
    'CA',
    'CO',
    'CT',
    'DE',
    'DC',
    'FL',
    'GA',
    'GU',
    'HI',
    'ID',
    'IL',
    'IN',
    'IA',
    'KS',
    'KY',
    'LA',
    'ME',
    'MD',
    'MH',
    'MA',
    'MI',
    'FM',
    'MN',
    'MS',
    'MO',
    'MT',
    'NE',
    'NV',
    'NH',
    'NJ',
    'NM',
    'NY',
    'NC',
    'ND',
    'MP',
    'OH',
    'OK',
    'OR',
    'PW',
    'PA',
    'PR',
    'RI',
    'SC',
    'SD',
    'TN',
    'TX',
    'UT',
    'VT',
    'VA',
    'VI',
    'WA',
    'WV',
    'WI',
    'WY'
);

create table addresses (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at          TIMESTAMP,
    address_line_one    VARCHAR(255)    NOT NULL,
    address_line_two    VARCHAR(255),
    city                VARCHAR(255)    NOT NULL,
    state               state_code      NOT NULL,
    postal_code         VARCHAR(255)    NOT NULL, -- Varchar to support 5 and 5 + 4 format
    phone_number        VARCHAR(255),
    contact_name        VARCHAR(255)
);

create table stops (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at          TIMESTAMP,
    load_id             VARCHAR(255)    NOT NULL REFERENCES loads(id),
    address_id          VARCHAR(255)    NOT NULL REFERENCES addresses(id),
    sequence            INTEGER         NOT NULL
);
create        index on stops(load_id);
create        index on stops(address_id);

create table jobs (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at          TIMESTAMP,
    carrier_account_id  VARCHAR(255)    NOT NULL
);
create        index on jobs(carrier_account_id);

create type action_type as enum(
    'PICKUP',
    'DELIVERY'
);

create type appointment_type as enum(
    'WINDOW',
    'SPECIFIC_TIME'
);

create table appointments (
    id          VARCHAR(255)        NOT NULL PRIMARY KEY,
    created_at  TIMESTAMP           NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at  TIMESTAMP           NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at  TIMESTAMP,
    type        appointment_type    NOT NULL,
    start_time  TIMESTAMP           NOT NULL,
    end_time    TIMESTAMP
);

create table actions(
    id              VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at      TIMESTAMP,
    type            action_type     NOT NULL,
    appointment_id  VARCHAR(255)    NOT NULL REFERENCES appointments(id),
    stop_id         VARCHAR(255)    NOT NULL REFERENCES stops(id),
    sequence        INTEGER         NOT NULL
);

create table cargo (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at          TIMESTAMP,
    is_loaded           BOOLEAN         NOT NULL DEFAULT FALSE,
    container_number    VARCHAR(255)    NOT NULL,
    weight              INTEGER,
    value               NUMERIC(12,2),
    commodity           VARCHAR(255)    NOT NULL,
    tmp_number          VARCHAR(255)    NOT NULL,
    container_type      VARCHAR(255)    NOT NULL,
    container_size      VARCHAR(255)    NOT NULL,
    seal_number         VARCHAR(255),
    chassis_number      VARCHAR(255),
    steamship_line      VARCHAR(255)    NOT NULL
);

create table action_to_cargo_mapping(
    action_id   VARCHAR(255)    NOT NULL REFERENCES actions(id),
    cargo_id    VARCHAR(255)    NOT NULL REFERENCES cargo(id)
);

create table line_item (
    id          VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at  TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at  TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at  TIMESTAMP,
    cargo_id    VARCHAR(255)    NOT NULL REFERENCES cargo(id)
);

create table tnumber (
    id              VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at      TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at      TIMESTAMP,
    load_id         VARCHAR(255)             REFERENCES loads(id),
    job_id          VARCHAR(255)             REFERENCES jobs(id),
    sequence        INTEGER         NOT NULL
);
create index on tnumber(load_id);

create table instructions(
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    created_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    updated_at          TIMESTAMP       NOT NULL DEFAULT (NOW() at time zone 'utc'),
    deleted_at          TIMESTAMP,
    tnumber_id          VARCHAR(255)    NOT NULL REFERENCES tnumber(id),
    location            VARCHAR(255)    NOT NULL,
    appointment_window  VARCHAR(255)    NOT NULL,
    goods               VARCHAR(255)    NOT NULL,
    action_type         action_type     NOT NULL,
    sequence            integer         NOT NULL
);
create        index on instructions(tnumber_id);
create unique index on instructions(tnumber_id, sequence, deleted_at);
