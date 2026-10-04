# Ascending Cloud Staffing - backend (Spring Boot)

## 1. Run it

1. **Database** (MySQL 8)
   - New, empty DB: `mysql -u root -p < database/schema.sql`
   - Your existing DB: back it up, then `mysql -u root -p ascending_cloud_staffing < database/upgrade_existing_db.sql`
     (creates missing tables, adds missing columns, keeps existing jobs visible).
2. **Secrets**: `src/main/resources/application-local.properties` holds your DB password and a JWT secret
   for local development. It is git-ignored. In staging/production use environment variables instead:
   `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET` (32+ chars), `DB_URL`, `DB_USERNAME`, `CORS_ALLOWED_ORIGINS`,
   `STORAGE_ROOT`.
3. `./mvnw spring-boot:run` (the generated `BackendApplicationTests` needs a live DB; use `-DskipTests` when packaging).
4. First admin: register through `POST /auth/register`, then in MySQL:
   `UPDATE users SET role='ADMIN' WHERE email='you@example.com';`

## 2. Auth

`Authorization: Bearer <token>` on every protected call. The logged-in user now comes from the token;
the old `?email=` query parameters are ignored (they were a security hole).

`POST /auth/register`
```json
{ "firstName":"A","lastName":"B","email":"a@b.com","phone":"+91...","password":"min8chars",
  "confirmPassword":"min8chars","role":"CANDIDATE","termsAccepted":true,"recruitmentConsent":true,
  "city":"Delhi","state":"Delhi","country":"India","preferredJobTitle":"Cloud Engineer",
  "workAuthorizationStatus":"Citizen" }
```
Employer: same common fields, `"role":"EMPLOYER"`, plus `companyName`, `jobTitle`, optional `companyWebsite`
(no city/state/country/consents needed). A legacy `"name"` field is accepted instead of firstName/lastName.
`POST /auth/login` -> `{ email, password }`. Both return `{ token, id, name, email, role }`.

## 3. Endpoints (existing paths kept)

| Area | Endpoints | Who |
|---|---|---|
| Jobs (public board) | `GET /jobs`, `/jobs/search?title=&location=`, `/jobs/{id}` | anyone - only **approved + active** jobs |
| Jobs (manage) | `GET /jobs/mine`, `POST /jobs`, `PUT /jobs/{id}`, `PATCH /jobs/{id}/status`, `DELETE /jobs/{id}` | employer (own company) / admin |
| Job approval | `GET /api/admin/jobs[?approvalStatus=PENDING]`, `POST /api/admin/jobs/{id}/approve`, `POST /api/admin/jobs/{id}/reject {"reason":""}` | admin |
| Applications | `POST /api/applications {"jobId":1}`, `GET /api/applications/me`, `GET /api/applications` , `GET /api/applications/job/{jobId}`, `PATCH /api/applications/{id}/status {"status":"Shortlisted"}`, `GET /api/applications/{id}/history`, `GET /api/applications/statuses` | candidate / employer / admin |
| Resume | `POST /api/resumes` (multipart `file`), `GET /api/resumes/me`, `GET /api/resumes/{id}/download`, `DELETE /api/resumes/me` | candidate (+ employer with an application, admin) |
| Company logo | `POST /companies/{id}/logo` (multipart `file`), `GET /companies/{id}/logo` (public) | employer / admin |
| Profiles | `/candidate-profiles/me`, `/employer-profiles/me`, `/companies`, `/candidate-skills`, `/job-skills` | per role |
| Other | `/api/interviews`, `/api/job-alerts`, `/api/saved-jobs/{jobId}`, `/api/conversations`, `/api/messages`, `/api/notifications` | logged-in user, own data only |
| Reference data | `/skills`, `/cloud-platforms`, `/workplace-types`, `/api/employment-types`, `/api/experience-levels`, `/api/job-categories` | read: public, write: admin |
| RBAC | `/api/admin-roles`, `/api/permissions`, `/api/admin-role-permissions`, `/api/user-admin-roles` | admin |

Errors are always `{"error": "message"}` (validation errors add a `fields` map) with proper 400/401/403/404/409 codes.

## 4. Requirements-document rules that are enforced

- Account creation fields, password confirmation, terms and recruitment consent (see `AuthService`).
- 15 application statuses exactly as in the document (`ApplicationStatus`); candidates may only withdraw.
- Job statuses: draft/active/paused/filled/closed/expired; **every job needs admin approval**; employer edits send it back to PENDING.
- Resume: PDF/DOC/DOCX, 5 MB, one per candidate (re-upload replaces), stored under a server-generated name, private.
- Logo: PNG/JPG/JPEG/WebP, 2 MB, public; PNG/JPG are downscaled to 1000 px and re-encoded.
- Only the file key + metadata is in MySQL. Local disk storage is for development; add an S3/Azure `StorageService`
  implementation for production (see `storage/StorageService.java`). Malware scanning, encryption at rest and
  retention/deletion on account delete still need to be added at that stage.

## 5. Things you must still decide / do

- The "Final Data" section of the document is still blank, so no real seed data beyond sample lookups.
- Candidate education / work-experience / certification lists are not modelled yet (only the flat profile fields).
- Email/SMS delivery is not implemented; notifications are in-app only.
