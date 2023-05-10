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

Development of this tool is not completed, yet. Hence; there are a lot of planned
features:

- Evaluation of changes as breaking or not-breaking
- HTML report
- Maven plugin
- Gradle plugin

## Contributions

Pull requests are welcome, but please follow these rules:

* The basic editor settings (indentation, newline, etc.) are described in the `.editorconfig` file (see [EditorConfig](http://editorconfig.org/)).
* Provide a unit test for every change.
* Name classes/methods/fields expressively.
* Fork the repo and create a pull request (see [GitHub Flow](https://guides.github.com/introduction/flow/index.html)).

