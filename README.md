# jasyncapicmp

jasyncapicmp is a tool to compare two versions of a [asyncapi](https://www.asyncapi.com/) specification:

```bash
java -jar target/jasyncapicmp-0.0.1-SNAPSHOT-jar-with-dependencies.jar -o old.yaml -n new.yaml
```

It can also be used as a library to parse and compare asyncapi specifications:
```java
AsyncApiParser asyncApiParser = new AsyncApiParser();
AsyncApi oldAsyncApi = asyncApiParser.parse(oldFile, config.getOldPath());
AsyncApi newAsyncApi = asyncApiParser.parse(newFile, config.getNewPath());

AsyncApiComparator comparator = new AsyncApiComparator();
ObjectDiff diff = comparator.compare(oldAsyncApi, newAsyncApi);
```

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
- Maven plugin
- Gradle plugin

## Usage

To use the tool you can clone the repository, build the tool and run it:

```bash
git clone https://github.com/siom79/jasyncapicmp.git
cd jasyncapicmp
mvn package
java -jar target/jasyncapicmp-0.0.1-SNAPSHOT-jar-with-dependencies.jar -o old.yaml -n new.yaml
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

