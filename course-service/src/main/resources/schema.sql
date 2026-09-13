-- =========================================================
-- CATALOG SERVICE - INIT DATABASE
-- PostgreSQL
-- =========================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- =========================================================
-- CATEGORY
-- =========================================================

CREATE TABLE IF NOT EXISTS category (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,

    description TEXT,

    parent_id UUID,

    visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_category_slug
    UNIQUE (slug),

    CONSTRAINT fk_category_parent
    FOREIGN KEY (parent_id)
    REFERENCES category(id)
    ON DELETE SET NULL,

    CONSTRAINT ck_category_visibility
    CHECK (
              visibility IN (
              'PUBLIC',
              'PRIVATE'
                            )
    )
    );

CREATE INDEX IF NOT EXISTS idx_category_parent_id
    ON category(parent_id);

CREATE INDEX IF NOT EXISTS idx_category_visibility
    ON category(visibility);

CREATE INDEX IF NOT EXISTS idx_category_active_visibility
    ON category(is_active, visibility);


-- =========================================================
-- COURSE
-- =========================================================

CREATE TABLE IF NOT EXISTS course (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,

    short_description VARCHAR(1000),
    description TEXT,

    level VARCHAR(30),

    language VARCHAR(20),

    thumbnail_url TEXT,

    author_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE',

    published_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_course_slug
    UNIQUE (slug),

    CONSTRAINT ck_course_status
    CHECK (
              status IN (
              'DRAFT',
              'PUBLISHED',
              'ARCHIVED'
                        )
    ),

    CONSTRAINT ck_course_visibility
    CHECK (
              visibility IN (
              'PUBLIC',
              'PRIVATE'
                            )
    ),

    CONSTRAINT ck_course_level
    CHECK (
              level IS NULL
              OR level IN (
              'BEGINNER',
              'INTERMEDIATE',
              'ADVANCED',
              'ALL_LEVELS'
                          )
    )
    );

CREATE INDEX IF NOT EXISTS idx_course_author_id
    ON course(author_id);

CREATE INDEX IF NOT EXISTS idx_course_status
    ON course(status);

CREATE INDEX IF NOT EXISTS idx_course_visibility
    ON course(visibility);

CREATE INDEX IF NOT EXISTS idx_course_status_visibility
    ON course(status, visibility);

CREATE INDEX IF NOT EXISTS idx_course_published
    ON course(status, visibility, published_at);


-- =========================================================
-- COURSE <-> CATEGORY
-- =========================================================

CREATE TABLE IF NOT EXISTS course_category (
                                               course_id UUID NOT NULL,
                                               category_id UUID NOT NULL,

                                               is_primary BOOLEAN NOT NULL DEFAULT FALSE,

                                               PRIMARY KEY (course_id, category_id),

    CONSTRAINT fk_course_category_course
    FOREIGN KEY (course_id)
    REFERENCES course(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_course_category_category
    FOREIGN KEY (category_id)
    REFERENCES category(id)
    ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_course_category_category_id
    ON course_category(category_id);


-- =========================================================
-- SEED DATA - CATEGORY
-- =========================================================

INSERT INTO category (
    name,
    slug,
    description,
    visibility
)
VALUES
    (
        'Backend Development',
        'backend-development',
        'Backend development courses',
        'PUBLIC'
    ),
    (
        'Java',
        'java',
        'Java programming courses',
        'PUBLIC'
    ),
    (
        'Software Engineering',
        'software-engineering',
        'Software engineering courses',
        'PUBLIC'
    )
    ON CONFLICT (slug) DO NOTHING;


-- =========================================================
-- SEED DATA - COURSE
-- =========================================================

INSERT INTO course (
    title,
    slug,
    short_description,
    description,
    level,
    language,
    thumbnail_url,
    author_id,
    status,
    visibility,
    published_at
)
VALUES (
           'Java Spring Boot from Zero to Production',
           'java-spring-boot-from-zero-to-production',
           'Learn Java Spring Boot from fundamentals to production-ready applications.',
           'Complete Spring Boot course covering Spring Core, Spring Boot, Security, Kafka and Microservices.',
           'INTERMEDIATE',
           'vi',
           NULL,
           '00000000-0000-0000-0000-000000000001',
           'PUBLISHED',
           'PUBLIC',
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (slug) DO NOTHING;


-- =========================================================
-- SEED DATA - COURSE CATEGORY
-- =========================================================

INSERT INTO course_category (
    course_id,
    category_id,
    is_primary
)
SELECT
    c.id,
    cat.id,
    CASE
        WHEN cat.slug = 'backend-development' THEN TRUE
        ELSE FALSE
        END
FROM course c
         JOIN category cat
              ON cat.slug IN (
                              'backend-development',
                              'java',
                              'software-engineering'
                  )
WHERE c.slug = 'java-spring-boot-from-zero-to-production'
    ON CONFLICT (course_id, category_id) DO NOTHING;
