# Online Learning Platform Backend

The **Online Learning Platform** is a microservices-based online learning platform, migrated from a previous monolithic application.

The system is built with **Spring Boot 3** and follows **Domain-Driven Design (DDD)** principles.
Services are split by **business domain / bounded context**, not by individual database tables or entities.

The main goal is to keep each service:

* focused on a meaningful business capability
* independently evolvable
* responsible for its own business rules and data
* loosely coupled with other services

---

# Domain Overview

The platform is organized around the following main bounded contexts:

```text
┌───────────────────────────────┐
│ auth-management-service       │
│ Identity & Access Management  │
└───────────────┬───────────────┘
                │
                │
        ┌───────┴────────┐
        │                │
        v                v

┌──────────────────┐   ┌──────────────────┐
│ course-service   │   │ content-service  │
│                  │   │                  │
│ Course           │   │ Curriculum       │
│ Category         │   │ Module           │
│                  │   │ Lesson           │
│                  │   │ Video            │
└────────┬─────────┘   └────────┬─────────┘
         │                      │
         │                      │
         └──────────┬───────────┘
                    │
                    v

          ┌───────────────────────┐
          │ class-service         │
          │                       │
          │ Class / Cohort        │
          │ Class configuration   │
          │ Instructor            │
          │ Access Policy         │
          └───────────┬───────────┘
                      │
                      v
          ┌───────────────────────┐
          │ enrollment-service    │
          │                       │
          │ Enrollment            │
          │ Membership            │
          │ Entitlement           │
          │ Roster                │
          └───────────┬───────────┘
                      │
                      v
          ┌───────────────────────┐
          │ learning-service      │
          │                       │
          │ Progress              │
          │ Completion            │
          │ Watch Progress        │
          └───────────────────────┘


┌──────────────────┐
│ assessment-      │
│ service          │
│                  │
│ Quiz             │
│ Assignment       │
│ Submission       │
│ Attempt          │
│ Grade            │
└──────────────────┘


┌──────────────────┐
│ payment-service  │
│                  │
│ Order            │
│ Payment          │
│ Transaction      │
│ Refund           │
└──────────────────┘


┌──────────────────┐
│ reporting-       │
│ service          │
│                  │
│ Reports          │
│ Aggregation      │
│ Read Models      │
│ PDF / Export     │
└──────────────────┘


┌──────────────────┐
│ community-       │
│ service          │
│                  │
│ Discussion       │
│ Comment          │
│ Question         │
│ Answer           │
│ Moderation       │
└──────────────────┘
```

---

# Core Domain Model

The platform distinguishes between **Course** and **Class**.

## Course

A **Course** represents the learning product / knowledge structure.

For example:

```text
Course
"Java Spring Boot from Zero to Production"
```

A Course answers:

> "What knowledge and learning content do we provide?"

A Course contains or references its learning content, but it does **not** represent a specific group of students.

---

## Class

A **Class** represents a specific learning offering / delivery instance of a Course.

One Course can have multiple Classes.

```text
Course
   |
   +---------------------+
   |                     |
   v                     v
Class A                 Class B
VIP                     Free
```

Example:

```text
Course: Java Spring Boot

    ├── Class: Spring Boot VIP
    │      ├── price = 2,000,000
    │      ├── instructor = A
    │      ├── max_students = 20
    │      └── access_policy = FULL
    │
    ├── Class: Spring Boot Standard
    │      ├── price = 500,000
    │      ├── instructor = B
    │      └── access_policy = STANDARD
    │
    └── Class: Spring Boot Free
           ├── price = 0
           └── access_policy = FREE
```

A Class answers:

> "How is this Course being offered to a particular group of learners?"

The Class can define:

* instructor / mentor
* pricing
* enrollment rules
* maximum number of students
* access policy
* schedule
* class-specific settings

The Class does **not** duplicate the Course content.

---

# Course → Class → Enrollment

The relationship can be visualized as:

```text
Course
   |
   +----------------------------+
   |                            |
   v                            v
Class VIP                    Class Free
   |                            |
   +-- Enrollment               +-- Enrollment
   |                            |
   +-- Access Policy            +-- Access Policy
```

A Course can therefore be reused by many Classes.

This avoids copying the same learning content for every Class.

---

# Example: One Course, Multiple Classes

```text
Course
C001 - Spring Boot
```

Content:

```text
M01 Java Foundation
M02 Spring Core
M03 Spring Boot
M04 Security
M05 Kafka
M06 Microservices
```

The same Course can be offered through different Classes:

```text
Class
CL_FREE
courseId = C001
accessPolicy = FREE
price = 0
```

```text
Class
CL_VIP
courseId = C001
accessPolicy = FULL
price = 2,000,000
```

The content is still stored only once:

```text
C001
 |
 +── M01
 +── M02
 +── M03
 +── M04
 +── M05
 +── M06
```

There is no:

```text
CL_FREE
 └── copy of all lessons

CL_VIP
 └── another copy of all lessons
```

Instead, access is controlled by the Class / Enrollment domain.

---

# Enrollment and Entitlement

An **Enrollment** represents the relationship between a User and a Class.

For example:

```text
Enrollment
userId = U001
classId = CL_FREE
```

Enrollment has its own lifecycle:

```text
ENROLL
   ↓
PENDING
   ↓
CONFIRMED
   ↓
ACTIVE
   ↓
COMPLETED
```

Possible termination / state changes include:

```text
DROP
CANCEL
SUSPEND
TRANSFER
EXPIRE
```

---

## Entitlement

Enrollment alone is not necessarily enough to answer:

> "What content can this user access?"

The platform therefore uses **Entitlement / Access Policy** to determine the actual learning access.

For example:

```text
User A
Enrollment
userId = U001
classId = CL_FREE
```

The Class has:

```text
accessPolicy = FREE
```

Therefore:

```text
M01 ✓
M02 ✓
M03 ✗
M04 ✗
M05 ✗
M06 ✗
```

Another user:

```text
User B
Enrollment
userId = U002
classId = CL_VIP
```

The Class has:

```text
accessPolicy = FULL
```

Therefore:

```text
M01 ✓
M02 ✓
M03 ✓
M04 ✓
M05 ✓
M06 ✓
```

The important point is:

```text
Enrollment != Content Copy
```

Enrollment determines the user's relationship with the Class.

Entitlement / Access Policy determines what the user is allowed to access.

---

# Content Access

The `content-service` owns the learning content itself.

It knows:

> "Lesson L001 exists and belongs to Course C001."

It should **not** own the business decision:

> "User U001 is allowed to access Lesson L001."

That decision belongs to the Class / Enrollment / Access domain.

Conceptually:

```text
CanUserAccess(
    userId,
    classId,
    lessonId
)
```

The access domain evaluates the policy and enrollment state.

The content service remains responsible for the content itself.

---

# Service Responsibilities

## auth-management-service

Responsible for identity and access management.

```text
auth-management-service
├── User identity
├── Authentication
├── Authorization
├── Role
├── Scope
├── Client
└── Token / session related concerns
```

This service owns authentication and platform-level authorization concerns.

---

# course-service

Responsible for the **Course / Catalog domain**.

```text
course-service
├── Course
├── Category
├── Course metadata
├── Course lifecycle
├── Course publishing
└── Course visibility
```

Example:

```text
Course
C001
title = "Spring Boot"
category = "Backend Development"
status = PUBLISHED
```

A Course represents the reusable learning product.

It is not a specific student group.

---

# content-service

Responsible for the **Learning Content domain**.

```text
content-service
├── Curriculum
├── Module
├── Chapter
├── Lesson
├── Video
├── Document
├── Attachment
└── Learning Resource
```

Example:

```text
Course C001
   |
   +── Module 01
   |      |
   |      +── Lesson 01
   |      +── Lesson 02
   |
   +── Module 02
          |
          +── Lesson 03
          +── Lesson 04
```

The content service stores the reusable learning structure.

The same content can be used by multiple Classes.

---

# class-service

Responsible for the **Class / Learning Offering domain**.

```text
class-service
├── Class
├── Class configuration
├── Instructor / Mentor assignment
├── Pricing configuration
├── Enrollment policy
├── Access policy
└── Class lifecycle
```

Example:

```text
Class
CL_VIP
courseId = C001
name = "Spring Boot VIP K01"

price = 2,000,000
maxStudents = 20
accessPolicy = FULL
```

Another Class can reference the same Course:

```text
Class
CL_FREE
courseId = C001
name = "Spring Boot Free"

price = 0
accessPolicy = FREE
```

The Class service does not duplicate Course or Content data.

---

# enrollment-service

Responsible for the **Enrollment / Membership domain**.

```text
enrollment-service
├── Enrollment
├── Membership
├── Entitlement
├── Roster
└── Enrollment lifecycle
```

Example:

```text
Enrollment
userId = U001
classId = CL_FREE
status = ACTIVE
```

This service determines whether a user is enrolled in a Class and manages the enrollment lifecycle.

It can also maintain entitlement information required to determine learning access.

---

# learning-service

Responsible for the **Learning Progress domain**.

```text
learning-service
├── LessonProgress
├── VideoWatchProgress
├── Completion
├── Resume Position
├── Learning Streak
└── Course Progress
```

Example:

```text
Student A
|
+-- Course C001
       |
       +-- Lesson L001 -> COMPLETED
       +-- Lesson L002 -> 65%
       +-- Lesson L003 -> NOT_STARTED
```

The learning service answers:

> "How far has this learner progressed?"

It does not own the lesson or video itself.

---

# assessment-service

Responsible for the **Assessment domain**.

```text
assessment-service
├── Quiz
├── Question
├── Answer
├── Assignment
├── Submission
├── Attempt
├── Grade
└── Evaluation
```

Example:

```text
Quiz
   |
   +── Question
   +── Question
   +── Question
        |
        +── Attempt
               |
               +── Answer
               +── Score
```

This service owns assessment-specific business rules such as:

* creating assessments
* submitting attempts
* evaluating answers
* calculating grades
* tracking submissions

---

# payment-service

Responsible for the **Payment / Billing domain**.

```text
payment-service
├── Order
├── Payment
├── Transaction
├── Refund
├── Payment Method
└── Payment lifecycle
```

Typical flow:

```text
User
   |
   v
Purchase Class
   |
   v
payment-service
   |
   v
PaymentCompleted
   |
   v
enrollment-service
   |
   v
Enrollment Activated
```

The payment service owns payment business rules.

It does not directly create or manage the Enrollment.

Instead, payment-related domain events can trigger actions in other bounded contexts.

---

# reporting-service

Responsible for the **Reporting domain**.

```text
reporting-service
├── Report Definition
├── Report Query
├── Aggregation
├── Reporting Read Model
└── Export
    ├── PDF
    ├── Excel
    └── CSV
```

The service should be called `reporting-service` rather than `pdf-service`.

PDF is an output format, not the domain itself.

Example reports:

```text
Student Progress Report
Class Performance Report
Course Completion Report
Enrollment Report
Revenue Report
```

The reporting service should not directly query the databases of other services.

Instead, it can build its own read models from domain events:

```text
payment-service
      |
      | PaymentCompleted
      v
reporting-service
```

```text
enrollment-service
      |
      | EnrollmentCreated
      v
reporting-service
```

```text
learning-service
      |
      | LessonCompleted
      v
reporting-service
```

This allows reporting to optimize its own data model independently.

---

# community-service

Responsible for the **Discussion / Community domain**.

```text
community-service
├── Discussion
├── Comment
├── Question
├── Answer
├── Like / Reaction
└── Moderation
```

For example:

```text
Course Discussion
   |
   +── Question
   |      |
   |      +── Answer
   |
   +── Comment
   +── Reaction
```

This should remain separate from `content-service` because discussion has its own lifecycle and business rules.

---

# Event-Driven Communication

Services should communicate through APIs and domain events rather than sharing database tables.

Example:

```text
payment-service
      |
      | PaymentCompleted
      v
enrollment-service
      |
      | EnrollmentActivated
      v
learning-service
```

Another example:

```text
learning-service
      |
      | LessonCompleted
      +--------------------+
                           |
                           v
                  reporting-service

                           |
                           v
                  notification-service
                  (future service)
```

The event represents a business fact:

```text
LessonCompleted
EnrollmentActivated
PaymentCompleted
CourseCompleted
```

Consumers react to the event according to their own responsibilities.

---

# Data Ownership

Each service owns its own database and domain model.

```text
course-service
    └── course_db

content-service
    └── content_db

class-service
    └── class_db

enrollment-service
    └── enrollment_db

learning-service
    └── learning_db

assessment-service
    └── assessment_db

payment-service
    └── payment_db

reporting-service
    └── reporting_db

community-service
    └── community_db
```

Services should not directly modify another service's database.

Avoid:

```text
reporting-service
    |
    +── SELECT payment_db
    +── SELECT enrollment_db
    +── SELECT learning_db
```

Prefer:

```text
Domain Events
      |
      v
reporting-service
      |
      v
Reporting Read Model
```

---

# Important Domain Boundaries

The following concepts should remain separate:

```text
Course
    = What is being taught?

Class
    = How is the Course being offered?

Enrollment
    = Which users belong to the Class?

Entitlement / Access Policy
    = What is the user allowed to access?

Content
    = What learning materials exist?

Learning Progress
    = How far has the user progressed?

Assessment
    = How is the user evaluated?

Payment
    = How is the purchase / transaction handled?

Reporting
    = How is business information aggregated and presented?
```

The goal is to avoid creating services such as:

```text
category-service
lesson-service
video-service
member-service
pdf-service
```

simply because separate tables or entities exist.

The service boundary should follow **business capability and domain responsibility**, not database structure.

---

# Target Microservice Structure

```text
online-learning-platform
│
├── auth-management-service
│
├── course-service
│
├── content-service
│
├── class-service
│
├── enrollment-service
│
├── learning-service
│
├── assessment-service
│
├── payment-service
│
├── reporting-service
│
└── community-service
```

Additional services such as `notification-service`, `search-service`, or `analytics-service` can be introduced later when the corresponding business domain becomes large enough to justify an independent bounded context.
