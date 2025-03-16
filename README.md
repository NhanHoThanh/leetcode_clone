# Requirements

### 1. Docker Desktop
### 2. Java SpringBoot with Maven

# How to run
1. Clone project ve
2. Bat docker desktop
3. cd terminal vao ___/test/demo
4. mvn spring-boot:run


# API for test

#### 1. POST localhost:8080/execute
- **Description**: VD gửi code python về server để chạy
- **Request Body**:

    ```
    {
        "code": "def twoSum(self, nums, target):\n    n = len(nums)\n    for i in range(n - 1):\n        for j in range(i + 1, n):\n            if nums[i] + nums[j] == target:\n                return [i, j]\n    return []",
        "language": "python",
        "problemId": 3
    }
    ```


#### GET localhost:8080/test/helloworld

- **Description**: For the multi-language support, in development
- **Request Body**:
    ```
    none
    ```
- **Response**:
  - `201 Created`: Accepted
  - `400 Bad Request`: If the request body is invalid.
  - `500 Internal Server Error`: If there is an issue with the server.


  #### GET localhost:8080/test/sum

- **Description**: For the multi-language support, in development
- **Request Body**:
    ```
    none
    ```
- **Response**:
  - `201 Created`: Accepted
  - `400 Bad Request`: If the request body is invalid.
  - `500 Internal Server Error`: If there is an issue with the server.