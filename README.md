# jasyncapicmp

jasyncapicmp is a tool to compare two versions of a [asyncapi](https://www.asyncapi.com/) specification:

```bash
java -jar target/jasyncapicmp-0.0.1-jar-with-dependencies.jar -o old.yaml -n new.yaml
```

It can also be used as a library to parse and compare asyncapi specifications:
```java
AsyncApiParser asyncApiParser = new AsyncApiParser();
AsyncApi oldAsyncApi = asyncApiParser.parse(oldFile, config.getOldPath());
AsyncApi newAsyncApi = asyncApiParser.parse(newFile, config.getNewPath());

AsyncApiComparator comparator = new AsyncApiComparator();
ObjectDiff diff = comparator.compare(oldAsyncApi, newAsyncApi);
```

There is also a maven plugin available.

## Motivation

Every time you release a new version of an API defined by a asyncapi specification,
you need to check if only these things have changed that you wanted to change.
Probably you also have to inform the clients of the API about and changes and want
to document them.

Without the appropriate tooling, this task is tedious and error-prone.
This tool/library helps you to determine the differences between two versions of
an asyncapi specification.

## Features

- Java API to parse async API specifications
- Comparison of two parsed async API specifications
- Differences can be printed out as simple YAML diff format

### Planned features

Development of this tool is not completed, yet. Hence, there are a lot of planned
features:

- Evaluation of more changes as breaking or not-breaking
- HTML report
- Gradle plugin

## Usage

To use the tool you can clone the repository, build the tool and run it:

```bash
git clone https://github.com/siom79/jasyncapicmp.git
cd jasyncapicmp
mvn package
java -jar target/jasyncapicmp-0.0.1-jar-with-dependencies.jar -o old.yaml -n new.yaml
```

A read-to-use jar file with all dependencies can be downloaded from the Maven Central Repository [here](https://repo1.maven.org/maven2/io/github/siom79/jasyncapicmp/jasyncapicmp/0.0.1/jasyncapicmp-0.0.1-jar-with-dependencies.jar).
Be sure to download the file with the extension `-jar-with-dependencies.jar`.

You can also use it as a library:

```xml
<dependency>
  <groupId>io.github.siom79.jasyncapicmp</groupId>
  <artifactId>jasyncapicmp</artifactId>
  <version>0.0.1</version>
</dependency>
```

To integrate the output into your build, you can utilize the maven plugin:

```xml
<build>
	<plugins>
		<plugin>
			<groupId>io.github.siom79.jasyncapicmp</groupId>
			<artifactId>jasyncapicmp-maven-plugin</artifactId>
			<version>0.0.1</version>
			<executions>
				<execution>
					<id>cmp</id>
					<phase>verify</phase>
					<goals>
						<goal>jasyncapicmp</goal>
					</goals>
					<configuration>
						<oldVersion>
							<file>old_2.6.0.yaml</file>
						</oldVersion>
						<newVersion>
							<file>new_2.6.0.yaml</file>
						</newVersion>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

Sample output:

```yaml
asyncapi: 2.6.0 (===)
id: https://github.com/siom79/jasyncapicmp (===)
info: (===)
  description: This is a sample app. (===)
  termsOfService: https://asyncapi.org/terms/ (===)
  title: AsyncAPI Sample App (===)
  version: 1.0.0 (*** old: 0.1.0)
  license: (===)
    name: Apache 2.0 (===)
    url: https://www.apache.org/licenses/LICENSE-2.0.html (===)
  contact: (===)
    name: API Support (===)
    url: https://www.asyncapi.org/support (===)
    email: support@asyncapi.org (*** old: team@asyncapi.org)
servers: (===)
  production: (===)
    protocol: secure-mqtt (===)
    description: The production API server (===)
    url: gigantic-server.com:{port}/path (===)
    variables: (===)
      port: (===)
        defaultValue: 8883 (===)
        enums: (***)
          - 8883 (===)
          - 8884 (+++)
channels: (===)
  userSignedUp: (===)
    subscribe: (===)
      summary: Action to sign a user up. (===)
      operationId: userSignup (===)
      message: (===)
        contentType: application/json (===)
        payload: (===)
          type: object (===)
          properties: (===)
            role: (+++, compatibility change: SCHEMA_PROPERTY_ADDED)
              type: string (+++)
            user: (===)
              type: string (===)
[...]
```

The following incompatibilities ares detected:

| Incompatibility                  | Backward compatible | Forward compatible |
|----------------------------------| ------------------- | ------------------ |
| CHANNEL_ADDED                    |true | true |
| CHANNEL_REMOVED                  |false | false |
| OPERATION_OPERATION_ID_CHANGED   |false | false |
| MESSAGE_MESSAGE_ID_CHANGED       |false | false |
| MESSAGE_CONTENT_TYPE_CHANGED     |false | false |
| MESSAGE_SCHEMA_FORMAT_CHANGED    |false | false |
| SCHEMA_TYPE_CHANGED              |false | false |
| SCHEMA_PROPERTY_ADDED            |true | true |
| SCHEMA_PROPERTY_REMOVED          |true | true |
| SCHEMA_PROPERTY_REQUIRED_ADDED   |false | true |
| SCHEMA_PROPERTY_REQUIRED_REMOVED |true | false |
| SCHEMA_MIN_LENGTH_INCREASED      |false|true|
| SCHEMA_MIN_LENGTH_DECREASED      |true | false |
| SCHEMA_MAX_LENGTH_INCREASED      |true | false |
| SCHEMA_MAX_LENGTH_DECREASED      |false|true|

## Contributions

Pull requests are welcome, but please follow these rules:

* The basic editor settings (indentation, newline, etc.) are described in the `.editorconfig` file (see [EditorConfig](http://editorconfig.org/)).
* Provide a unit test for every change.
* Name classes/methods/fields expressively.
* Fork the repo and create a pull request (see [GitHub Flow](https://guides.github.com/introduction/flow/index.html)).

## Release

This is the current release procedure:

```bash
./release-prepare.sh <last-released-version> <new-version>
```
Run Github Action [Release](https://github.com/siom79/jasyncapicmp/actions/workflows/maven-publish-central.yml)

Login to [OSSRH](https://s01.oss.sonatype.org/#stagingRepositories), close and release staging repository
```bash
mvn versions:set -DnewVersion=<new-SNAPSHOT-version>
mvn versions:commit
git add .
git commit -m "New SNAPSHOT version <new-SNAPSHOT-version>"
```
