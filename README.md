# Learnzy

## Overview
This is a backend service for a learning platform that allows users to:
- Browse courses, topics, and subtopics
- Search course content (titles, subtopics, content)
- Enroll in courses
- Track progress on subtopics
---

## Entity Relationships

| From       | To                    | Type |
|------------|-----------------------|------|
| Course     | Topic                 | 1:M |
| Topic      | Subtopic              | 1:M |
| User       | Enrollment            | 1:M |
| Enrollment | User & Course         | 1:1 |
| Progress   | Enrollment & SubTopic | 1:1 |

### Database Tables

**Course**
- `ID` (PK), `Course_Code`, `Title`, `Description`

**Topic**
- `ID` (PK), `TopicCode`, `TopicTitle`, `CourseId` (FK)

**Subtopic**
- `ID` (PK), `SubTopicCode`, `SubTopicTitle`, `Content`, `TopicId` (FK)

**User**
- `ID` (PK), `Email`, `Password`

**Enrollment**
- `ID`(PK), `UserId`, `CourseId`, `CreatedDate`, `UpdatedDate`,  `Status`, `CourseId+UserId` (FK)

**Progress**
- `ID`(PK), `EnrollmentId`, `SubtopicId`, `DateCompleted`, `enrollmentId+subTopicId` (FK)

---

## Elasticsearch Document Structure

The backend uses Elasticsearch for fuzzy and partial search. Each indexed document has the following fields:

| Field Name   | Type                      | Description                                                    |
|--------------|---------------------------|----------------------------------------------------------------|
| `id`         | String                    | Unique ID for the document                                     |
| `fieldId`    | Search_As_You_Type        | Identifier of the source field (e.g., subtopicCode, topicCode) |
| `title`      | Search_As_You_Type        | Title of the course, topic, or subtopic                        |
| `content`    | Search_As_You_Type        | Main content                                                   |
| `contentType`| Keyword                   | Enum for content type (e.g., COURSE, TOPIC, SUBTOPIC)          |
| `path`       | Text                      | Path or reference from subTopic -> Topic -> Course             |


## Features

### 1. Authentication & Authorization
- JWT-based authentication
- Handles exceptions thrown via JWT filter

### 2. API Documentation
- Swagger integration

### 3. Search Functionality
- Elasticsearch used for course/topic/subtopic search
- Supports **fuzzy and partial searches** for case-insensitive matching  

---

### 4. Deployment
- Docker integration for containerized deployment
- Backend deployed on **Render**
- PostgreSQL as managed database on **Render**
- Elasticsearch hosted on **Elastic Cloud free tier**

---

### 5. Future Enhancements
- Highlight matched search results
- Semantic search using embeddings

---

### 7. Links
- GitHub repository: `https://github.com/sbhanupriya/Learnzy`
- Deployed application: `https://learnzy-w9nf.onrender.com/swagger-ui/index.html`