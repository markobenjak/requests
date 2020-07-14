PGDMP     *                     x           requests    12.2    12.2 #    ?           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            @           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            A           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            B           1262    24876    requests    DATABASE     �   CREATE DATABASE requests WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE requests;
                postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                postgres    false            C           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   postgres    false    3            �            1259    24957    customer    TABLE     �   CREATE TABLE public.customer (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    active integer DEFAULT 1 NOT NULL
);
    DROP TABLE public.customer;
       public         heap    postgres    false    3            �            1259    24955    customer_id_seq    SEQUENCE     �   CREATE SEQUENCE public.customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.customer_id_seq;
       public          postgres    false    210    3            D           0    0    customer_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.customer_id_seq OWNED BY public.customer.id;
          public          postgres    false    209            �            1259    24976    hourly_stats    TABLE     �   CREATE TABLE public.hourly_stats (
    customer_id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    request_count bigint DEFAULT 0 NOT NULL,
    invalid_count bigint DEFAULT 0 NOT NULL,
    id integer NOT NULL
);
     DROP TABLE public.hourly_stats;
       public         heap    postgres    false    3            �            1259    25003    hourly_stats_id_seq    SEQUENCE     �   CREATE SEQUENCE public.hourly_stats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.hourly_stats_id_seq;
       public          postgres    false    3    213            E           0    0    hourly_stats_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.hourly_stats_id_seq OWNED BY public.hourly_stats.id;
          public          postgres    false    216            �            1259    24993    incoming_requests    TABLE     �   CREATE TABLE public.incoming_requests (
    id integer NOT NULL,
    tagid integer,
    userid character varying(255),
    remoteip character varying(25),
    customerid integer,
    "timestamp" timestamp without time zone
);
 %   DROP TABLE public.incoming_requests;
       public         heap    postgres    false    3            �            1259    24991    icoming_requests_id_seq    SEQUENCE     �   CREATE SEQUENCE public.icoming_requests_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.icoming_requests_id_seq;
       public          postgres    false    215    3            F           0    0    icoming_requests_id_seq    SEQUENCE OWNED BY     T   ALTER SEQUENCE public.icoming_requests_id_seq OWNED BY public.incoming_requests.id;
          public          postgres    false    214            �            1259    24964    ip_blacklist    TABLE     =   CREATE TABLE public.ip_blacklist (
    ip bigint NOT NULL
);
     DROP TABLE public.ip_blacklist;
       public         heap    postgres    false    3            �            1259    24969    ua_blacklist    TABLE     M   CREATE TABLE public.ua_blacklist (
    ua character varying(255) NOT NULL
);
     DROP TABLE public.ua_blacklist;
       public         heap    postgres    false    3            �
           2604    24960    customer id    DEFAULT     j   ALTER TABLE ONLY public.customer ALTER COLUMN id SET DEFAULT nextval('public.customer_id_seq'::regclass);
 :   ALTER TABLE public.customer ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    210    209    210            �
           2604    25005    hourly_stats id    DEFAULT     r   ALTER TABLE ONLY public.hourly_stats ALTER COLUMN id SET DEFAULT nextval('public.hourly_stats_id_seq'::regclass);
 >   ALTER TABLE public.hourly_stats ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    213            �
           2604    24996    incoming_requests id    DEFAULT     {   ALTER TABLE ONLY public.incoming_requests ALTER COLUMN id SET DEFAULT nextval('public.icoming_requests_id_seq'::regclass);
 C   ALTER TABLE public.incoming_requests ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    214    215            6          0    24957    customer 
   TABLE DATA           4   COPY public.customer (id, name, active) FROM stdin;
    public          postgres    false    210   �%       9          0    24976    hourly_stats 
   TABLE DATA           ]   COPY public.hourly_stats (customer_id, "time", request_count, invalid_count, id) FROM stdin;
    public          postgres    false    213   '&       ;          0    24993    incoming_requests 
   TABLE DATA           a   COPY public.incoming_requests (id, tagid, userid, remoteip, customerid, "timestamp") FROM stdin;
    public          postgres    false    215   a&       7          0    24964    ip_blacklist 
   TABLE DATA           *   COPY public.ip_blacklist (ip) FROM stdin;
    public          postgres    false    211   �'       8          0    24969    ua_blacklist 
   TABLE DATA           *   COPY public.ua_blacklist (ua) FROM stdin;
    public          postgres    false    212   �'       G           0    0    customer_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customer_id_seq', 1, false);
          public          postgres    false    209            H           0    0    hourly_stats_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.hourly_stats_id_seq', 34, true);
          public          postgres    false    216            I           0    0    icoming_requests_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.icoming_requests_id_seq', 147, true);
          public          postgres    false    214            �
           2606    24963    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public            postgres    false    210            �
           2606    25007    hourly_stats hourly_stats_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.hourly_stats
    ADD CONSTRAINT hourly_stats_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.hourly_stats DROP CONSTRAINT hourly_stats_pkey;
       public            postgres    false    213            �
           2606    24998 '   incoming_requests icoming_requests_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.incoming_requests
    ADD CONSTRAINT icoming_requests_pkey PRIMARY KEY (id);
 Q   ALTER TABLE ONLY public.incoming_requests DROP CONSTRAINT icoming_requests_pkey;
       public            postgres    false    215            �
           2606    24968    ip_blacklist ip_blacklist_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.ip_blacklist
    ADD CONSTRAINT ip_blacklist_pkey PRIMARY KEY (ip);
 H   ALTER TABLE ONLY public.ip_blacklist DROP CONSTRAINT ip_blacklist_pkey;
       public            postgres    false    211            �
           2606    24973    ua_blacklist ua_blacklist_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.ua_blacklist
    ADD CONSTRAINT ua_blacklist_pkey PRIMARY KEY (ua);
 H   ALTER TABLE ONLY public.ua_blacklist DROP CONSTRAINT ua_blacklist_pkey;
       public            postgres    false    212            �
           2606    24990 !   hourly_stats unique_customer_time 
   CONSTRAINT     k   ALTER TABLE ONLY public.hourly_stats
    ADD CONSTRAINT unique_customer_time UNIQUE (customer_id, "time");
 K   ALTER TABLE ONLY public.hourly_stats DROP CONSTRAINT unique_customer_time;
       public            postgres    false    213    213            �
           2606    24984 %   hourly_stats hourly_stats_customer_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hourly_stats
    ADD CONSTRAINT hourly_stats_customer_id FOREIGN KEY (customer_id) REFERENCES core.customer(id) ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.hourly_stats DROP CONSTRAINT hourly_stats_customer_id;
       public          postgres    false    213            6   b   x��;@@ �z�s�;�OTVA��0a�ed���h_�"��D��j��\t�chV�V�t����i�������@�)��
u�y��*��/� _�U�      9   *   x�3�4202�50�54R0��20�22�4�4�46����� gf�      ;     x����m�0E�Rn@7k+"�%	RA�D^s` bH�`�x���E��O&J��?��D��jJr�������=�kM�^ShM�I�,
{����F�=��"p��# 3F`���݆1	`C�
"7�6݆̂Y��0vf�n�,�m���0vf�n�,8b�D:L"&�v�t���MŮ�F�֫�55b�F:J#��[P#iĠ�v��[����n�'ש��ۄ;B�`/�.'�w�\Bu�n�Sd2eB�7W
Ǡ�a�3�s�e i      7   %   x�3�2246070316�21�4�437�4����� O      8   (   x�s4���KI�H-�r��O�IM�/��K-/Fp�b���� 'H     