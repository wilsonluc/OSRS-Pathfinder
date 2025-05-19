## Build and Deploy Instructions

Follow these steps to build and deploy the application using AWS SAM:

### 1. Build the Fat JAR

Use Gradle with the Shadow plugin to build the executable fat JAR:

```bash
./gradlew shadowJar
```

### 2. Deploy to AWS Lambda

```bash
./gradlew deployPathfinder
```

### 3. Test and Save Results to build/reports/tests/index.html

```bash
./gradlew testPathfinder
```

[//]: # (### 3. Invoke Lambda)

[//]: # (```bash)

[//]: # (aws lambda invoke --function-name Pathfinder --cli-binary-format raw-in-base64-out --payload '{\"sourceX\": 100, \"sourceY\": 200, \"sourceZ\": 300}' response.json --profile user2)

[//]: # (```)

## Collision Map Generation

Open collision-map-generator branch