-- Populate TEST_MODELS table
INSERT INTO TEST_MODELS (id, name, age) VALUES (1, 'John Doe', 1);
INSERT INTO TEST_MODELS (id, name, age) VALUES (2, 'Jane Smith', 2);

-- Populate USERS table
INSERT INTO USERS (id, username, password) VALUES (1, 'user', 'password');
INSERT INTO USERS (id, username, password) VALUES (2, 'admin', 'password');

-- Populate PROBLEMS table
INSERT INTO PROBLEMS (id, title, description, function_name) VALUES (1, 'python_helloword', 'print hello world', 'helloWorld');
INSERT INTO PROBLEMS (id, title, description, function_name) VALUES (2, 'sum_of_two_numbers', 'Read two numbers and print their sum', 'sumTwoNumbers');
INSERT INTO PROBLEMS (id, title, description, function_name) VALUES (3, 'Two Sum', 'Given an array of integers, return indices of the two numbers such that they add up to a specific target.', 'twoSum');

-- Populate TEST_CASES table
INSERT INTO TEST_CASES (id, input, expected_output, problem_id) VALUES (1, '', 'hello world', 1);
INSERT INTO TEST_CASES (id, input, expected_output, problem_id) VALUES (2, '2 3', '5', 2);
INSERT INTO TEST_CASES (id, input, expected_output, problem_id) VALUES (3, '[2, 7, 11, 15]\n9', '[0, 1]', 3);
INSERT INTO TEST_CASES (id, input, expected_output, problem_id) VALUES (4, '[3, 2, 4]\n6', '[1, 2]', 3);