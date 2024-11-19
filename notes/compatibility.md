# Backward

An API is Backwards Compatible if a program written
against one version of that API will continue to work
the same way, without modification, against future
versions of the API.

The following are Backwards Compatible changes:

- Adding a new method/endpoint to an API
- Adding new fields to request/response messages
- Adding new query parameters

Instead, these are common Backward-incompatible changes:

- Renaming an API method/endpoint
- Renaming fields in request/response
- Changing types for fields in request/response
- Changing the status codes
- Changing headers (”content-format” etc…)

# Forward

An API is Forwards Compatible if a program written
against one version of the API will also work
the same way, without modification, against
previous versions of the API.

To do that the API needs to be is less strict
(more liberal) with the requests received -
Robustness Principle
