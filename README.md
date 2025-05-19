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

If your collision map is out of date, you can regenerate collision-map.zip using the following steps:

### 1. Select Gamepack

From [OpenRS2 Archive](https://archive.openrs2.org/caches), select:

- **Game:** oldschool
- **Env:** live

Once you've found a game cache that you're happy with, click the 'More' button as shown below:
![](collision_map_cache.png)

### 2. Download Cache (.dat2/idx) + Keys (JSON)

1. Download and extract Cache (.dat2/idx)
2. Download Keys (JSON) and move it to the folder created in step 1 (it should be next to the cache folder)
3. Rename *.json file to keys.json
4. Replace all instances of the words 'mapsquare' with 'region' and 'key' with 'keys' inside keys.json

### 3. Configure Directories and Paths

1. Add flag for path of extracted game-pack to CLI arguments of CollisionMapDumper
    - e.g.,
      `--cachedir "C:\Users\Wilson\Downloads\cache-oldschool-live-en-b225-2024-09-11-11-45-05-openrs2#1895\cache" `
2. Add flag for path of keys.json to CLI arguments of CollisionMapDumper
    - e.g.,
      `--xteapath "C:\Users\Wilson\Downloads\cache-oldschool-live-en-b225-2024-09-11-11-45-05-openrs2#1895\keys.json"`
3. Add flag for path to store collision-map.zip after running CollisionMapDumper
    - e.g.,
      `--outputdir "C:\Users\Wilson\Downloads\cache-oldschool-live-en-b225-2024-09-11-11-45-05-openrs2#1895\output"`

This process should result in something that looks like the following:
![](run_configuration.png)

### 4. Execute CollisionMapDumper

Run CollisionMapDumper in debug mode. Upon completion, a collision map should be generated in the output directory
specified in step 3.3
