🛑 **This repository is strictly an educational and demonstration project.** It's designed to show how certain functionality is implemented. **Because of this,** configuration files (like .env) are included to simplify running the code locally.

## 1. Build and start

```bash
# build authapi
cd authapi && mvn clean package -DskipTests && cd ..

# build dataapi
cd dataapi && mvn clean package -DskipTests && cd ..

# remove old containers and volumes (it will also reset DB)
docker compose down -v

# rebuild images and start
docker compose up --build
```

---

## 2. Import Postman collections

Open Postman, then import these files from the project folder (you can also drag-and-drop the files into Postman tree):

- `postman/authapi.postman_collection.json`

- `postman/dataapi.postman_collection.json`

---

## 3. Typical request sequence (recommended)

1. **Register** (authapi) — create user

2. **Login** (authapi) — get cookie/JWT stored by Postman

3. **Transform** (authapi) — send text to be processed

You can also call `dataapi` directly **only** if you include the `X-Internal-Token` header (you already have it in Postman). Normal "user communication path" is via `authapi`.

---

## 4. Where to see logs

### Via Docker Desktop (UI)

In Docker Desktop:  
`Docker Desktop App -> Containers → mini-test → authapi → Files → app → logs → authapi.log`

### From terminal

```bash
docker exec -it authapi tail -n 200 /app/logs/authapi.log
```

(Adjust `-n 200` to see more or fewer lines.)

---

## 5. Inspect the database

```bash
docker exec -it mini_test_postgres psql -U postgres -d mini_test_db
```

Then run SQL, for example:

```sql
\dt
SELECT * FROM processing_logs LIMIT 100;
```

To exit `psql`, run:

```sql
\q
```
