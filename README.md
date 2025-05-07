## Development Instructions

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

## Testing Instructions

Follow these instructions to test the application in a local setting using AWS SAM:

### 1. Build Locally Using SAM
```cmd
sam build
```

### 2. Test With Sample Payload
```cmd
sam local invoke MyJavaLambda --event payload.json
```



[//]: # (### 3. Test and Save Results to build/reports/tests/index.html)

[//]: # ()
[//]: # (```bash)

[//]: # (./gradlew testPathfinder)

[//]: # (```)

[//]: # (### 3. Invoke Lambda)

[//]: # (```bash)

[//]: # (aws lambda invoke --function-name Pathfinder --cli-binary-format raw-in-base64-out --payload '{\"sourceX\": 100, \"sourceY\": 200, \"sourceZ\": 300}' response.json --profile user2)

[//]: # (```)