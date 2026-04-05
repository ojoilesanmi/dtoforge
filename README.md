# DTOForge

Generate Java DTOs from JSON. Supports records, classes, nested objects, and Jackson annotations.

## Quick Start

```bash
./mvnw spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/v1/dto-generator/generate \
  -H "Content-Type: application/json" \
  -d '{
    "json": "{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"London\"}}",
    "rootClassName": "UserDto",
    "outputStyle": "RECORD",
    "useJacksonAnnotations": true
  }'
```

## Output Styles

**Record:**
```java
public record UserDto(
    @JsonProperty("name")
    String name,
    @JsonProperty("age")
    Integer age,
    @JsonProperty("address")
    AddressDto address
) {}

public record AddressDto(
    @JsonProperty("city")
    String city
) {}
```

**Class:**
```java
public class UserDto {
    private String name;
    private Integer age;
    private AddressDto address;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ...
}
```

## Request Body

| Field | Required | Description |
|---|---|---|
| `json` | Yes | Valid JSON object |
| `rootClassName` | Yes | Name for the root DTO class |
| `outputStyle` | Yes | `RECORD` or `CLASS` |
| `useJacksonAnnotations` | No | Add `@JsonProperty` annotations |
| `packageName` | No | Package declaration (defaults to none) |

## Architecture

```
core/          Domain logic — parsing, type inference, code generation
web/           HTTP layer — controller, request/response, error handling
shared/        Exceptions, error codes, constants
```

## Tech Stack

- Java 17
- Spring Boot 3.5
- Jackson
- JUnit 5

## Run Tests

```bash
./mvnw test
```
