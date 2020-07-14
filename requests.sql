CREATE SEQUENCE public.customer_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;
	
CREATE SEQUENCE public.hourly_stats_id_seq
    INCREMENT 1
    START 34
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;
	
CREATE SEQUENCE public.icoming_requests_id_seq
    INCREMENT 1
    START 147
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE public.customer
(
    id integer NOT NULL DEFAULT nextval('customer_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    active integer NOT NULL DEFAULT 1,
    CONSTRAINT customer_pkey PRIMARY KEY (id)
)

CREATE TABLE public.hourly_stats
(
    customer_id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    request_count bigint NOT NULL DEFAULT 0,
    invalid_count bigint NOT NULL DEFAULT 0,
    id integer NOT NULL DEFAULT nextval('hourly_stats_id_seq'::regclass),
    CONSTRAINT hourly_stats_pkey PRIMARY KEY (id),
    CONSTRAINT unique_customer_time UNIQUE (customer_id, "time"),
    CONSTRAINT hourly_stats_customer_id FOREIGN KEY (customer_id)
        REFERENCES core.customer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

CREATE TABLE public.incoming_requests
(
    id integer NOT NULL DEFAULT nextval('icoming_requests_id_seq'::regclass),
    tagid integer,
    userid character varying(255) COLLATE pg_catalog."default",
    remoteip character varying(25) COLLATE pg_catalog."default",
    customerid integer,
    "timestamp" timestamp without time zone,
    CONSTRAINT icoming_requests_pkey PRIMARY KEY (id)
)

CREATE TABLE public.ip_blacklist
(
    ip bigint NOT NULL,
    CONSTRAINT ip_blacklist_pkey PRIMARY KEY (ip)
)

CREATE TABLE public.ua_blacklist
(
    ua character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT ua_blacklist_pkey PRIMARY KEY (ua)
)