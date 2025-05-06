## Build and Deploy Instructions

Follow these steps to build and deploy the application using AWS SAM:

### 1. Build the Fat JAR
Use Gradle with the Shadow plugin to build the executable fat JAR:

```bash
./gradlew shadowJar
```

### 2. Deploy to AWS Lambda
```bash
aws lambda create-function --function-name Pathfinder --runtime java11 --role arn:aws:iam::956301286619:role/Pathfinder --handler com.pathfinder.lambda.LambdaHandler::handleRequest --memory-size 512 --timeout 15 --zip-file fileb://build/libs/OSRS_Pathfinder.jar --profile user2
```

### 3. Invoke Lambda
```bash
aws lambda invoke --function-name Pathfinder --cli-binary-format raw-in-base64-out --payload '{\"sourceX\": 100, \"sourceY\": 200, \"sourceZ\": 300}' response.json --profile user2
```