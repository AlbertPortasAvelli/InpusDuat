CREATE TABLE "user_media" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" bigint NOT NULL,
  "media_id" bigint NOT NULL,
  "status" varchar NOT NULL DEFAULT 'PENDING',
  "rating" integer,
  "watched_at" timestamp,
  "notes" text
);

CREATE TABLE "users" (
  "id" BIGSERIAL PRIMARY KEY,
  "username" varchar UNIQUE,
  "email" varchar UNIQUE,
  "password_hash" varchar,
  "role" varchar,
  "active" boolean DEFAULT true,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE TABLE "media" (
  "id" BIGSERIAL PRIMARY KEY,
  "title" varchar,
  "original_title" varchar,
  "type" varchar,
  "genre" varchar,
  "language" varchar,
  "release_year" integer,
  "synopsis" text,
  "poster_url" varchar,
  "created_by" bigint NOT NULL,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE TABLE "seasons" (
  "id" BIGSERIAL PRIMARY KEY,
  "media_id" bigint NOT NULL,
  "season_number" integer,
  "title" varchar,
  "episode_count" integer,
  "release_year" integer
);

CREATE TABLE "episodes" (
  "id" BIGSERIAL PRIMARY KEY,
  "season_id" bigint NOT NULL,
  "episode_number" integer,
  "title" varchar,
  "duration_minutes" integer,
  "synopsis" text
);

CREATE TABLE "refresh_tokens" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" bigint NOT NULL,
  "token" text UNIQUE,
  "expires_at" timestamp,
  "revoked" boolean
);

ALTER TABLE "media" ADD CONSTRAINT "user_posts" FOREIGN KEY ("created_by") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_media" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_media" ADD FOREIGN KEY ("media_id") REFERENCES "media" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "seasons" ADD FOREIGN KEY ("media_id") REFERENCES "media" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "episodes" ADD FOREIGN KEY ("season_id") REFERENCES "seasons" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "refresh_tokens" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;
